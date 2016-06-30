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
    public static final String KEY_CALL_MADE = "call_made";
    public static final String KEY_WAITING_FOR_DECISION_CALL = "waiting_for_decision_on_call";
    public static final String KEY_POOL = "pool";
    public static final String KEY_ROUND_WIND = "round_wind";

    private Player[] players;
    private int roundNumber;
    private ArrayDeque<Tile> pool;
    private int mCurrentPlayerIndex;
    private boolean mCallMade;
    private boolean mKanMade; //needs to be separate so you can draw after kan
    private boolean mWaitingForDecisionOnCall;
    private Wind mRoundWind;
    private WashizuView mWashizuView;

    public Game(WashizuView washizuView){
        Log.d(TAG, "default game constructor called");
        players = new Player[4];
        pool = new ArrayDeque<>();
        mCallMade = false;
        mKanMade = false;
        mWaitingForDecisionOnCall = false;
        mRoundWind = Wind.EAST;
        this.mWashizuView = washizuView;
    }

    //TODO: find out when this doesn't work right, seems to be good enough for now
    public Game(WashizuView washizuView, JSONObject json) throws JSONException{
        this.mWashizuView = washizuView;
        Log.d(TAG, "Starting recreation");
        players = new Player[4];
        JSONArray jsonPlayers = json.getJSONArray(KEY_PLAYERS);
        for (int i = 0; i < jsonPlayers.length(); i++){
            JSONObject jsonPlayer = jsonPlayers.getJSONObject(i);
            boolean isAi = jsonPlayer.getBoolean(Player.KEY_IS_AI);
            if (isAi){
                players[i] = new AiPlayer(jsonPlayer);
            }
            else {
                players[i] = new HumanPlayer(jsonPlayer);
            }
            players[i].setGame(this);
        }
        pool = new ArrayDeque<>();
        JSONArray jsonPool = json.getJSONArray(KEY_POOL);
        for (int i = 0; i < jsonPool.length(); i++){
            pool.push(new Tile(jsonPool.getJSONObject(i)));
        }
        roundNumber = json.getInt(KEY_ROUND_NUMBER);
        mCurrentPlayerIndex = json.getInt(KEY_CURRENT_PLAYER_INDEX);
        mCallMade = json.getBoolean(KEY_CALL_MADE);
        mWaitingForDecisionOnCall = json.getBoolean(KEY_WAITING_FOR_DECISION_CALL);
        mRoundWind = Enum.valueOf(Wind.class, json.getString(KEY_ROUND_WIND));
        Log.d(TAG, "Finishing recreation");
        //not entirely sure this will work, but looks like it does for now
        if (!mWaitingForDecisionOnCall){
            takeNextTurn();
        }
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        JSONArray jsonPlayers = new JSONArray();
        for (int i = 0; i < players.length; i++){
            jsonPlayers.put(players[i].toJson());
        }
        json.put(KEY_PLAYERS, jsonPlayers);
        JSONArray jsonPool = new JSONArray();
        //get the last tile that was drawn and put it back in the pool, then just restart the turn
        //need to copy the pool or we might try drawing from it after it's already empty
        ArrayDeque<Tile> poolCopy = new ArrayDeque<>(pool);
        while (!poolCopy.isEmpty()){
            jsonPool.put(poolCopy.pop().toJson());
        }
        if (!mWaitingForDecisionOnCall) {
            Tile lastDrawnTile = players[mCurrentPlayerIndex].getHand().getDrawnTile();
            //might be null for calls, etc.
            if (lastDrawnTile != null) {
                jsonPool.put(lastDrawnTile.toJson());
            }
        }
        json.put(KEY_ROUND_NUMBER, roundNumber);
        json.put(KEY_CURRENT_PLAYER_INDEX, mCurrentPlayerIndex);
        json.put(KEY_CALL_MADE, mCallMade);
        json.put(KEY_WAITING_FOR_DECISION_CALL, mWaitingForDecisionOnCall);
        json.put(KEY_POOL, jsonPool);
        json.put(KEY_ROUND_WIND, mRoundWind.toString());
        return json;
    }

    public Wind getRoundWind(){
        return mRoundWind;
    }

    public void startGame(){
        Log.d(TAG, "startGame");
        players[0] = new HumanPlayer(SeatDirection.DOWN);
        players[1] = new AiPlayer(SeatDirection.RIGHT);
        players[2] = new AiPlayer(SeatDirection.UP);
        players[3] = new AiPlayer(SeatDirection.LEFT);
        for (Player player: players){
            player.setGame(this);
        }

        Random rand = new Random();
        int firstEast = rand.nextInt(4);
        players[firstEast].setWind(Wind.EAST);
        players[(firstEast+1)%4].setWind(Wind.SOUTH);
        players[(firstEast+2)%4].setWind(Wind.WEST);
        players[(firstEast +3)%4].setWind(Wind.NORTH);
        startRound(ROUND_EAST_1);

        mCurrentPlayerIndex = firstEast;
        takeNextTurn();
    }

    private void startRound(int roundNumber) {
        Log.d(TAG, "Starting round " + roundNumber);
        // should this be part of the call or auto-increment?
        this.roundNumber = roundNumber;
        shufflePool();
        dealHands();
    }

    private void takeNextTurn(){
        players[mCurrentPlayerIndex].setIsMyTurn(true);
        Log.d(TAG, "Starting turn for player " + mCurrentPlayerIndex);
        //if they're getting their turn from calling a tile, they don't get to draw, unless it's kan
        if (!mCallMade || mKanMade){
            Log.d(TAG, "player " + mCurrentPlayerIndex + " drawing a tile");
            players[mCurrentPlayerIndex].drawTile();
        }
        else {
            Log.d(TAG, "tile called, no draw");
        }
        mCallMade = false;
        mKanMade = false;
        //if the player is an AI, go to the next step (calls) without waiting for input
        if (players[mCurrentPlayerIndex] instanceof AiPlayer){
            players[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    Tile discardedTile = players[mCurrentPlayerIndex].getLastDiscardedTile();
                    checkForCalls(discardedTile);
                }
            });
        }
        //otherwise wait for input and it'll finish the turn when a discard/call has been made
        else {
            players[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    // do nothing
                }
            });
        }
    }

    private void onTurnFinished(int nextPlayerIndex){
        players[mCurrentPlayerIndex].setIsMyTurn(false);
        Log.d(TAG, "player " + mCurrentPlayerIndex + " finished, next is " + nextPlayerIndex);
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
            onTurnFinished((mCurrentPlayerIndex + 1) % players.length);
        }
    }

    private void checkForRon(Tile discardedTile){
        //check all other players for ron
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + players.length; j++) {
            int i = j % players.length;
            if (players[i].canRon(discardedTile)) {
                Log.d(TAG, "Player " + i + " can ron");
                if (players[i].shouldRon(discardedTile)) {
                    if (players[i] instanceof AiPlayer) {
                        onCallMade(i, MeldType.RON);
                    } else {
                        mWaitingForDecisionOnCall = true;
                        mWashizuView.makeButtonClickable(MeldType.RON);
                        Log.d(TAG, "Waiting for touch on ron");
                    }
                }
            }
        }
    }

    private void checkForPonAndKan(Tile discardedTile){
        //check all other players for pon and kan
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + players.length; j++) {
            int i = j % players.length;
            if (players[i].canPon(discardedTile)) {
                if (players[i].canKanOnCall(discardedTile)) {
                    if (players[i].shouldKan(discardedTile)) {
                        if (players[i] instanceof AiPlayer) {
                            onCallMade(i, MeldType.KAN);
                        } else {
                            mWaitingForDecisionOnCall = true;
                            mWashizuView.makeButtonClickable(MeldType.KAN);
                            Log.d(TAG, "Waiting for touch on kan");
                        }
                    }
                }
                //TODO: when buttons are added, allow calling either kan or pon
                else if (players[i].shouldPon(discardedTile)) {
                    if (players[i] instanceof AiPlayer) {
                        onCallMade(i, MeldType.PON);
                    } else {
                        mWaitingForDecisionOnCall = true;
                        mWashizuView.makeButtonClickable(MeldType.PON);
                        Log.d(TAG, "Waiting for touch on pon");
                    }
                }
            }
        }
    }

    private void checkForChii(Tile discardedTile){
        //check next player for chii
        if (!mCallMade) {
            if (players[(mCurrentPlayerIndex + 1) % players.length].canChii(discardedTile)) {
                if (players[(mCurrentPlayerIndex + 1) % players.length].shouldChii(discardedTile)) {

                    if (players[(mCurrentPlayerIndex + 1) % players.length] instanceof AiPlayer){
                        onCallMade((mCurrentPlayerIndex + 1) % players.length, MeldType.CHII);
                    }
                    else {
                        mWaitingForDecisionOnCall = true;
                        mWashizuView.makeButtonClickable(MeldType.CHII);
                        Log.d(TAG, "Waiting for touch on chii");
                    }

                }
            }
        }
    }

    private Tile getLastDiscardedTile(){
        try{
            return players[mCurrentPlayerIndex].getLastDiscardedTile();
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }

    }

    private void onCallMade(int playerIndex, MeldType callType){
        //call the tile, remove it from the discard, and that player gets the next turn
        Log.d(TAG, "Player " + playerIndex + " making a call from player " + mCurrentPlayerIndex);
        Tile discardedTile = getLastDiscardedTile();
        switch (callType){
            case RON:
                Log.d(TAG, "Player " + playerIndex + " calling ron on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                players[playerIndex].callRon(discardedTile, players[mCurrentPlayerIndex]);
                break;
            case PON:
                Log.d(TAG, "Player " + playerIndex + " calling pon on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                //TODO: this shouldn't default to 90
                players[playerIndex].callPon(discardedTile,
                        SeatDirection.values()[mCurrentPlayerIndex]);
                break;
            case CHII:
                Log.d(TAG, "Player " + (mCurrentPlayerIndex + 1) % players.length +
                        " calling chii on " + discardedTile + " from " + mCurrentPlayerIndex);
                players[playerIndex].callChii(discardedTile);
                break;
            case KAN:
                Log.d(TAG, "Player " + playerIndex + " calling kan on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                players[playerIndex].callKan(discardedTile,
                        SeatDirection.values()[mCurrentPlayerIndex].addOffset(90));
                mKanMade = true;
                //TODO: when dora are added, flip another one here
                break;
            case SHOUMINKAN:
                break;
            default:
                throw new IllegalArgumentException("Illegal call type");
        }
        players[mCurrentPlayerIndex].removeLastDiscardedTile();
        mCallMade = true;
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
        pool.clear();
        pool.addAll(list);
    }

    private void dealHands(){
        for (Player player: players){
            for (int i = 0; i < HAND_SIZE; i++){
                player.mHand.addTile(drawTile());
            }
//            Leave this off for debugging right now
//            player.hand.setTilesVisibility();
        }
    }

    public Tile drawTile(){
        Log.d(TAG, "pool has " + pool.size() + " tiles, drawing");
        if (pool.isEmpty()){
            throw new IllegalStateException("Pool is empty, can't draw");
        }
        else {
            return pool.pop();
        }
    }

    public boolean onTouch(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d(TAG, "Touch down at " + event.getX() + ", " + event.getY());
            // if it's the player's turn, let him pick a tile to discard
            if (players[mCurrentPlayerIndex] instanceof HumanPlayer
                    && players[mCurrentPlayerIndex].getIsMyTurn()) {
                //once we've actually discarded a tile, start the next turn
                if (((HumanPlayer)(players[mCurrentPlayerIndex])).onTouch(event)){
                    Tile discardedTile = players[mCurrentPlayerIndex].getLastDiscardedTile();
                    checkForCalls(discardedTile);
                }
            }
        }
        return true;
    }

    public void onButtonPressed(MeldType buttonType){
        // this shouldn't happen but just in case
        if (!mWaitingForDecisionOnCall || getLastDiscardedTile() == null){
            return;
        }
        // if we're waiting for the user to decide whether to call
        Log.d(TAG, "touched while waiting for call");
        onCallMade(0, buttonType);
    }

    protected void onDraw(Canvas canvas){
        for (Player player: players){
            if (player != null) {
                player.draw(canvas);
            }
        }
    }
}
