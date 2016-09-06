package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.jordanagreen.washizu.Constants.DELAY_BETWEEN_TURNS_MS;
import static com.example.jordanagreen.washizu.Constants.HAND_SIZE;
import static com.example.jordanagreen.washizu.Constants.NUMBER_OF_PLAYERS;
import static com.example.jordanagreen.washizu.Constants.NUM_ROUNDS;
import static com.example.jordanagreen.washizu.Constants.ROUND_EAST_1;
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TILE_MIN_ID;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Game {

    public static final String TAG = "Game";

    public static final String KEY_PLAYERS = "players";
    public static final String KEY_ROUND_NUMBER = "round_number";
    public static final String KEY_CURRENT_PLAYER_INDEX = "current_player_index";
    public static final String KEY_CURRENT_DEALER_INDEX = "current_dealer_index";
    public static final String KEY_CALL_MADE = "call_made";
    public static final String KEY_WAITING_FOR_DECISION_CALL = "waiting_for_decision_on_call";
    public static final String KEY_WAITING_FOR_DECISION_TSUMO = "waiting_for_decision_on_tsumo";
    public static final String KEY_DRAW_POOL = "draw_pool";
    public static final String KEY_ROUND_WIND = "round_wind";

    private Player[] mPlayers;
    private int mRoundNumber;
    private ArrayDeque<Tile> mDrawPool;
    private int mCurrentPlayerIndex;
    private int mCurrentDealerIndex;
    private boolean mCallMade;
    private boolean mKanMade; //needs to be separate so you can draw after kan
    private boolean mWaitingForDecisionOnCall;
    // separate since if you don't take tsumo it's your turn, not the next person's
    private boolean mWaitingForDecisionOnTsumo;
    private Wind mRoundWind;
    private GameActivity mGameActivity;

    public Game(GameActivity gameActivity){
        Log.d(TAG, "default game constructor called");
        mPlayers = new Player[NUMBER_OF_PLAYERS];
        mDrawPool = new ArrayDeque<>();
        mCallMade = false;
        mKanMade = false;
        mWaitingForDecisionOnCall = false;
        mWaitingForDecisionOnTsumo = false;
        mRoundWind = Wind.EAST;
        mRoundNumber = ROUND_EAST_1;
        mGameActivity = gameActivity;
    }

    //TODO: find out when this doesn't work right, seems to be good enough for now
    public Game(GameActivity gameActivity, JSONObject json) throws JSONException{
        mGameActivity = gameActivity;
        Log.d(TAG, "Starting recreation");
        mPlayers = new Player[NUMBER_OF_PLAYERS];
        JSONArray jsonPlayers = json.getJSONArray(KEY_PLAYERS);
        for (int i = 0; i < jsonPlayers.length(); i++){
            JSONObject jsonPlayer = jsonPlayers.getJSONObject(i);
            boolean isAi = jsonPlayer.getBoolean(Player.KEY_IS_AI);
            if (isAi){
                mPlayers[i] = new AiPlayer(jsonPlayer);
            }
            else {
                mPlayers[i] = new HumanPlayer(jsonPlayer);
            }
            mPlayers[i].setGame(this);
        }
        mDrawPool = new ArrayDeque<>();
        JSONArray jsonPool = json.getJSONArray(KEY_DRAW_POOL);
        for (int i = jsonPool.length() - 1; i >= 0; i--){
            Tile tile = new Tile(jsonPool.getJSONObject(i));
            mDrawPool.push(tile);
            Log.d(TAG, "added " + tile + " to draw pool");
        }
        mRoundNumber = json.getInt(KEY_ROUND_NUMBER);
        mCurrentPlayerIndex = json.getInt(KEY_CURRENT_PLAYER_INDEX);
        mCurrentDealerIndex = json.getInt(KEY_CURRENT_DEALER_INDEX);
        mCallMade = json.getBoolean(KEY_CALL_MADE);
        mWaitingForDecisionOnCall = json.getBoolean(KEY_WAITING_FOR_DECISION_CALL);
        mWaitingForDecisionOnTsumo = json.getBoolean(KEY_WAITING_FOR_DECISION_TSUMO);
        mRoundWind = Enum.valueOf(Wind.class, json.getString(KEY_ROUND_WIND));
        Log.d(TAG, "Finishing recreation");
        //update the display stuff that would normally be set at the start of the round
        updateRoundNumberText(mRoundNumber);
        setPlayerWinds(mCurrentDealerIndex);
        updatePlayerWindText(mCurrentDealerIndex);
        //not entirely sure this will work, but looks like it does for now
        if (!(mWaitingForDecisionOnCall || mWaitingForDecisionOnTsumo)){
            takeNextTurn();
        }
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        JSONArray jsonPlayers = new JSONArray();
        for (int i = 0; i < mPlayers.length; i++){
            jsonPlayers.put(mPlayers[i].toJson());
        }
        json.put(KEY_PLAYERS, jsonPlayers);
        JSONArray jsonPool = new JSONArray();
        //get the last tile that was drawn and put it back in the pool, then just restart the turn
        //need to copy the pool or we might try drawing from it after it's already empty
        ArrayDeque<Tile> poolCopy = new ArrayDeque<>(mDrawPool);
        while (!poolCopy.isEmpty()){
            jsonPool.put(poolCopy.pop().toJson());
        }
        if (!mWaitingForDecisionOnCall) {
            Tile lastDrawnTile = mPlayers[mCurrentPlayerIndex].getHand().getDrawnTile();
            //might be null for calls, etc.
            if (lastDrawnTile != null) {
                jsonPool.put(lastDrawnTile.toJson());
            }
        }
        json.put(KEY_ROUND_NUMBER, mRoundNumber);
        json.put(KEY_CURRENT_PLAYER_INDEX, mCurrentPlayerIndex);
        json.put(KEY_CURRENT_DEALER_INDEX, mCurrentDealerIndex);
        json.put(KEY_CALL_MADE, mCallMade);
        json.put(KEY_WAITING_FOR_DECISION_CALL, mWaitingForDecisionOnCall);
        json.put(KEY_WAITING_FOR_DECISION_TSUMO, mWaitingForDecisionOnTsumo);
        json.put(KEY_DRAW_POOL, jsonPool);
        json.put(KEY_ROUND_WIND, mRoundWind.toString());
        return json;
    }

    public Wind getRoundWind(){
        return mRoundWind;
    }

    public void startGame(){
        Log.d(TAG, "startGame");
        mPlayers[0] = new HumanPlayer(SeatDirection.DOWN);
        mPlayers[1] = new AiPlayer(SeatDirection.RIGHT);
        mPlayers[2] = new AiPlayer(SeatDirection.UP);
        mPlayers[3] = new AiPlayer(SeatDirection.LEFT);
        for (Player player: mPlayers){
            player.setGame(this);
        }
        Random rand = new Random();
        startRound(ROUND_EAST_1, rand.nextInt(mPlayers.length));
    }

    private void startRound(int roundNumber, int dealerIndex) {
        Log.d(TAG, "Starting round " + roundNumber);
        Log.d(TAG, "Dealer is " + dealerIndex);
        // should this be part of the call or auto-increment?
        mRoundNumber = roundNumber;
        mCurrentPlayerIndex = dealerIndex;
        updateRoundNumberText(roundNumber);
        setPlayerWinds(dealerIndex);
        updatePlayerWindText(dealerIndex);
        shufflePool();
        dealHands();
        takeNextTurn();
    }

    private void updateRoundNumberText(int roundNumber){
        // activity might be null during testing
        if (mGameActivity != null){
            mGameActivity.updateRoundNumberText(roundNumber);
        }
    }

    private void setPlayerWinds(int dealerIndex){
        mPlayers[dealerIndex].setWind(Wind.EAST);
        mPlayers[(dealerIndex+1)% mPlayers.length].setWind(Wind.SOUTH);
        mPlayers[(dealerIndex+2)% mPlayers.length].setWind(Wind.WEST);
        mPlayers[(dealerIndex +3)% mPlayers.length].setWind(Wind.NORTH);
    }

    //TODO: either move wind letters to be near the players' hands or just show the dealer sign
    private void updatePlayerWindText(int eastPlayerIndex){
        if (mGameActivity != null){
            mGameActivity.updatePlayerWindText(eastPlayerIndex);
        }
    }

    private void endRound(){
        //TODO: stuff like calculating scores
        Log.d(TAG, "Ending round " + mRoundNumber);
        //clear the canvas
        mGameActivity.showScores();
        //wait for the player to click through the scores screen before starting the next round
    }

    public void startNextRound(){
        //empty everyone's hands, discards, etc
        for (Player player: mPlayers){
            player.reset();
        }
        if (mRoundNumber < NUM_ROUNDS){
            // rotate the seat winds
            mCurrentDealerIndex = (mCurrentDealerIndex + 1) % mPlayers.length;
            setPlayerWinds(mCurrentDealerIndex);
            startRound(mRoundNumber + 1, mCurrentDealerIndex);
        }
        // else end the game
    }

    private void takeNextTurn(){
        mPlayers[mCurrentPlayerIndex].setIsMyTurn(true);
        Log.d(TAG, "Starting turn for player " + mPlayers[mCurrentPlayerIndex]);
        //if they're getting their turn from calling a tile, they don't get to draw, unless it's kan
        if (!mCallMade || mKanMade){
            Log.d(TAG, "player " + mPlayers[mCurrentPlayerIndex] + " drawing a tile");
            mPlayers[mCurrentPlayerIndex].drawTile();
            //check for tsumo
            if (mPlayers[mCurrentPlayerIndex].canTsumo()){
                Log.d(TAG, "player " + mPlayers[mCurrentPlayerIndex] + " can tsumo");
                if (mPlayers[mCurrentPlayerIndex] instanceof AiPlayer) {
                    if (((AiPlayer) mPlayers[mCurrentPlayerIndex]).shouldTsumo()) {
                        onCallMade(mCurrentPlayerIndex, MeldType.TSUMO);
                    }
                    else {
                        continueTakingTurn();
                    }
                }
                //otherwise wait for player input
                else {
                    mGameActivity.makeButtonClickable(MeldType.TSUMO);
                    mWaitingForDecisionOnTsumo = true;
                    continueTakingTurn();
                }
            }
            else {
                continueTakingTurn();
            }
        }
        else {
            Log.d(TAG, "tile called, no draw");
            continueTakingTurn();
        }

    }

    // still part of starting the turn, but in another method to make changing the control flow
    // possible when there's a tsumo
    private void continueTakingTurn(){
        mCallMade = false;
        mKanMade = false;
        //if the player is an AI, go to the next step (calls) without waiting for input
        if (mPlayers[mCurrentPlayerIndex] instanceof AiPlayer){
            mPlayers[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    Tile discardedTile = mPlayers[mCurrentPlayerIndex].getLastDiscardedTile();
                    checkForCalls(discardedTile);
                }
            });
        }
        //otherwise wait for input and it'll finish the turn when a discard/call has been made
        else {
            mPlayers[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    // do nothing
                }
            });
        }
    }

    private void onTurnFinished(int nextPlayerIndex){
        mPlayers[mCurrentPlayerIndex].setIsMyTurn(false);
        Log.d(TAG, "player " + mPlayers[mCurrentPlayerIndex] + " finished, next is " +
                mPlayers[nextPlayerIndex]);
        mCurrentPlayerIndex = nextPlayerIndex;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeNextTurn();
            }
        }, DELAY_BETWEEN_TURNS_MS);
    }

    private void checkForCalls(Tile discardedTile){
        mWaitingForDecisionOnCall = false;
        Log.d(TAG, "Checking tile " + discardedTile + " for calls");
        checkForRon(discardedTile);
        if (!mCallMade && !mWaitingForDecisionOnCall){
            Log.d(TAG, "No ron called, checking for pon and kan");
            checkForPonAndKan(discardedTile);
        }
        if (!mCallMade && !mWaitingForDecisionOnCall){
            Log.d(TAG, "No pon or kan called, checking for chii");
            checkForChii(discardedTile);
        }
        //if no calls were made, just go to the player on the right
        if (!mCallMade && !mWaitingForDecisionOnCall){
            Log.d(TAG, "No calls made, going to next turn");
            onTurnFinished((mCurrentPlayerIndex + 1) % mPlayers.length);
        }
    }

    private void checkForRon(Tile discardedTile){
        //check all other players for ron
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + mPlayers.length; j++) {
            int i = j % mPlayers.length;
            if (mPlayers[i].canRon(discardedTile)) {
                Log.d(TAG, "Player " + i + " can ron");
                if (mPlayers[i] instanceof AiPlayer) {
                    if (((AiPlayer) mPlayers[i]).shouldRon(discardedTile)) {
                        onCallMade(i, MeldType.RON);
                    }
                }
                else {
                    mWaitingForDecisionOnCall = true;
                    mGameActivity.makeButtonClickable(MeldType.RON);
                    Log.d(TAG, "Waiting for touch on ron");
                }
            }
        }
    }

    private void checkForPonAndKan(Tile discardedTile){
        //check all other players for pon and kan
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + mPlayers.length; j++) {
            int i = j % mPlayers.length;
            if (mPlayers[i].canPon(discardedTile)) {
                if (mPlayers[i].canKanOnCall(discardedTile)) {
                    if (mPlayers[i] instanceof AiPlayer) {
                        if (((AiPlayer) mPlayers[i]).shouldKan(discardedTile)){
                            onCallMade(i, MeldType.KAN);
                        }
                    } else {
                        mWaitingForDecisionOnCall = true;
                        mGameActivity.makeButtonClickable(MeldType.KAN);
                        Log.d(TAG, "Waiting for touch on kan");
                    }
                }
                // allow picking either kan or pon
                if (mPlayers[i] instanceof AiPlayer) {
                    if (!mCallMade && ((AiPlayer) mPlayers[i]).shouldPon(discardedTile)){
                        onCallMade(i, MeldType.PON);
                    }
                }
                else {
                    mWaitingForDecisionOnCall = true;
                    mGameActivity.makeButtonClickable(MeldType.PON);
                    Log.d(TAG, "Waiting for touch on pon");
                }
            }
        }
    }

    private void checkForChii(Tile discardedTile){
        //check next player for chii
        if (!mCallMade) {
            Player nextPlayer = mPlayers[(mCurrentPlayerIndex + 1) % mPlayers.length];
            if (nextPlayer.canChii(discardedTile)) {
                if (nextPlayer instanceof AiPlayer){
                    if (((AiPlayer) nextPlayer).shouldChii(discardedTile)){
                        onCallMade((mCurrentPlayerIndex + 1) % mPlayers.length, MeldType.CHII);
                    }
                }
                else {
                    mWaitingForDecisionOnCall = true;
                    mGameActivity.makeButtonClickable(MeldType.CHII);
                    Log.d(TAG, "Waiting for touch on chii");
                }
            }
        }
    }

    private Tile getLastDiscardedTile(){
        try{
            return mPlayers[mCurrentPlayerIndex].getLastDiscardedTile();
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }

    }

    private void onCallMade(int playerIndex, MeldType callType){
        // PlayerIndex is the one making the call from mCurrentPlayerIndex.
        // If it's tsumo, they'll both be mCurrentPlayerIndex so it doesn't really matter

        //do tsumo first since that doesn't rely on the tile discarded
        if (callType == MeldType.TSUMO){
            Log.d(TAG, "Player " + playerIndex + " calling tsumo");
            mPlayers[playerIndex].callTsumo();
            endRound();
            return;
        }
        //call the tile, remove it from the discard, and that player gets the next turn
        Log.d(TAG, "Player " + playerIndex + " making a call from player " + mCurrentPlayerIndex);

        Tile discardedTile = getLastDiscardedTile();
        mCallMade = true;
        mWaitingForDecisionOnCall = false;
        switch (callType){
            case RON:
                Log.d(TAG, "Player " + playerIndex + " calling ron on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                mPlayers[playerIndex].callRon(discardedTile, mPlayers[mCurrentPlayerIndex]);
                endRound();
                // don't break and go to the next turn since the round is over
                return;
            case PON:
                Log.d(TAG, "Player " + playerIndex + " calling pon on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                //TODO: this shouldn't default to 90
                mPlayers[playerIndex].callPon(discardedTile,
                        SeatDirection.values()[mCurrentPlayerIndex]);
                break;
            case CHII:
                Log.d(TAG, "Player " + (mCurrentPlayerIndex + 1) % mPlayers.length +
                        " calling chii on " + discardedTile + " from " + mCurrentPlayerIndex);
                mPlayers[playerIndex].callChii(discardedTile);
                break;
            case KAN:
                Log.d(TAG, "Player " + playerIndex + " calling kan on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                mPlayers[playerIndex].callKan(discardedTile,
                        SeatDirection.values()[mCurrentPlayerIndex].addOffset(90));
                mKanMade = true;
                //TODO: when dora are added, flip another one here
                break;
            case SHOUMINKAN:
                break;
            default:
                throw new IllegalArgumentException("Illegal call type");
        }
        mPlayers[mCurrentPlayerIndex].removeLastDiscardedTile();
        Log.d(TAG, "call finished");
        onTurnFinished(playerIndex);
    }

    private void shufflePool(){
        //one opaque and three see-through for each tile
        ArrayList<Tile> list = new ArrayList<>();
        for (int i = TILE_MIN_ID; i <= TILE_MAX_ID; i++){
            list.add(new Tile(i, true));
            for (int j = 0; j < 3; j++){
                list.add(new Tile(i, false));
            }
        }
        Collections.shuffle(list);
        mDrawPool.clear();
        mDrawPool.addAll(list);
    }

    private void dealHands(){
        for (Player player: mPlayers){
            for (int i = 0; i < HAND_SIZE; i++){
                player.getHand().addTile(drawTile());
            }
//            Leave this off for debugging right now
//            player.hand.setTilesVisibility();
        }
    }

    public Tile drawTile(){
        Log.d(TAG, "mDrawPool has " + mDrawPool.size() + " tiles, drawing");
        if (mDrawPool.isEmpty()){
            throw new IllegalStateException("Pool is empty, can't draw");
        }
        else {
            return mDrawPool.pop();
        }
    }

    public boolean onTouch(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "Touch down at " + event.getX() + ", " + event.getY());

            // if waiting for decision on a call, touching out of the buttons should not call.
            // choosing to not take tsumo should not trigger this, or it would finish the player's
            // turn before they actually discard a tile
            if (mWaitingForDecisionOnCall){
                Log.d(TAG, "touch while waiting for decision on call");
                mWaitingForDecisionOnCall = false;
                mGameActivity.makeAllButtonsUnclickable();
                Log.d(TAG, "Player didn't call, going to next turn");
                onTurnFinished((mCurrentPlayerIndex + 1) % mPlayers.length);
            }


            // if it's the player's turn, let him pick a tile to discard
            else if (mPlayers[mCurrentPlayerIndex] instanceof HumanPlayer
                    && mPlayers[mCurrentPlayerIndex].getIsMyTurn()) {
                // player chose not to take tsumo
                if (mWaitingForDecisionOnTsumo){
                    mWaitingForDecisionOnTsumo = false;
                    mGameActivity.makeAllButtonsUnclickable();
                }
                //once we've actually discarded a tile, start the next turn
                if (((HumanPlayer)(mPlayers[mCurrentPlayerIndex])).onTouch(event)){
                    Tile discardedTile = mPlayers[mCurrentPlayerIndex].getLastDiscardedTile();
                    checkForCalls(discardedTile);
                }
            }
        }
        return true;
    }

    public void onButtonPressed(MeldType buttonType){
        // this shouldn't happen but just in case
        // update: actually this breaks tsumo when there's no discards yet
//        if (!mWaitingForDecisionOnCall || getLastDiscardedTile() == null){
        if (!(mWaitingForDecisionOnCall || mWaitingForDecisionOnTsumo)){
            return;
        }
        // if we're waiting for the user to decide whether to call
        Log.d(TAG, "touched while waiting for call");
        onCallMade(0, buttonType);
    }

    protected void onDraw(Canvas canvas){
        for (Player player: mPlayers){
            if (player != null) {
                player.draw(canvas);
            }
        }
    }
}
