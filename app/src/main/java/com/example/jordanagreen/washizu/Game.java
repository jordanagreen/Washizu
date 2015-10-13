package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.example.jordanagreen.washizu.Meld.MeldType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

import static com.example.jordanagreen.washizu.Constants.DELAY_BETWEEN_TURNS_MS;
import static com.example.jordanagreen.washizu.Constants.HAND_SIZE;
import static com.example.jordanagreen.washizu.Constants.ROUND_EAST_1;
import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_LEFT;
import static com.example.jordanagreen.washizu.Constants.SEAT_RIGHT;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TILE_MIN_ID;
import static com.example.jordanagreen.washizu.Constants.WIND_EAST;
import static com.example.jordanagreen.washizu.Constants.WIND_NORTH;
import static com.example.jordanagreen.washizu.Constants.WIND_SOUTH;
import static com.example.jordanagreen.washizu.Constants.WIND_WEST;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Game {

    public static final String TAG = "Game";

    private Player[] players;
    private int roundNumber;
    private Stack<Tile> pool;
    private int mCurrentPlayerIndex;
    //TODO: there should be a better way to implement this
    private boolean mCallMade;
    private boolean mWaitingForDecisionOnCall;

    public Game(){
        players = new Player[4];
        pool = new Stack<>();
        mCallMade = false;
        mWaitingForDecisionOnCall = false;
    }

    public void startGame(){
        players[0] = new HumanPlayer(this, SEAT_DOWN);
        players[1] = new AiPlayer(this, SEAT_RIGHT);
        players[2] = new AiPlayer(this, SEAT_UP);
        players[3] = new AiPlayer(this, SEAT_LEFT);

        Random rand = new Random();
        int firstEast = rand.nextInt(4);
        players[firstEast].setWind(WIND_EAST);
        players[(firstEast+1)%4].setWind(WIND_NORTH);
        players[(firstEast+2)%4].setWind(WIND_WEST);
        players[(firstEast +3)%4].setWind(WIND_SOUTH);
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

    interface GameCallback {
        void callback();
    }

    private void takeNextTurn(){
        players[mCurrentPlayerIndex].setIsMyTurn(true);

        //if they're getting their turn from calling a tile, they don't get to draw
        if (!mCallMade){
            Log.d(TAG, "drawing a tile");
            players[mCurrentPlayerIndex].drawTile();
        }
        else {
            Log.d(TAG, "tile called, no draw");
        }
        mCallMade = false;
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
        //TODO: check for kan and ron

        mWaitingForDecisionOnCall = false;

        //check all other players for pon
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + players.length; j++){
            int i = j % players.length;
            if (players[i].canPon(discardedTile)){
                if (players[i].shouldPon(discardedTile)){
                    if (players[i] instanceof AiPlayer){
                        onCallMade(i, MeldType.PON);
                    }
                    else {
                        mWaitingForDecisionOnCall = true;
                        Log.d(TAG, "Waiting for touch on pon");
                    }
                }
            }
        }
        //check next player for chii
        if (!mCallMade) {
            if (players[(mCurrentPlayerIndex + 1) % players.length].canChii(discardedTile)) {
                if (players[(mCurrentPlayerIndex + 1) % players.length].shouldChii(discardedTile)) {

                    if (players[(mCurrentPlayerIndex + 1) % players.length] instanceof AiPlayer){
                        onCallMade((mCurrentPlayerIndex + 1) % players.length, MeldType.CHII);
                    }
                    else {
                        mWaitingForDecisionOnCall = true;
                        Log.d(TAG, "Waiting for touch on chii");
                    }

                }
            }
        }
        //if no calls were made, just go to the player on the right
        if (!mCallMade && !mWaitingForDecisionOnCall){
            onTurnFinished((mCurrentPlayerIndex + 1) % players.length);
        }
        //otherwise, wait for onCallMade to be triggered when the user presses a button
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
            case PON:
                Log.d(TAG, "Player " + playerIndex + " calling pon on " + discardedTile + " from "
                        + mCurrentPlayerIndex);
                players[playerIndex].callPon(discardedTile, mCurrentPlayerIndex * 90);
                break;
            case CHII:
                Log.d(TAG, "Player " + (mCurrentPlayerIndex + 1) % players.length +
                        " calling chii on " + discardedTile + " from " + mCurrentPlayerIndex);
                players[playerIndex].callChii(discardedTile, mCurrentPlayerIndex * 90);
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
        pool.empty();
        pool.addAll(list);
    }

    private void dealHands(){
        for (Player player: players){
            Hand hand = new Hand();
            for (int i = 0; i < HAND_SIZE; i++){
                hand.addTile(drawTile());
            }
            player.setHand(hand);
        }
    }

    public Tile drawTile(){
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

            // if we're waiting for the user to decide whether to call
            if (mWaitingForDecisionOnCall){
                Log.d(TAG, "touched while waiting for call");
                //TODO: make this better
                //for now just do it because no actual buttons yet
                if (players[0] instanceof HumanPlayer){
                    if (getLastDiscardedTile() != null){
                        if (players[0].canPon(getLastDiscardedTile())){
                            onCallMade(0, MeldType.PON);
                        }
                        else if ((players[0].canChii(getLastDiscardedTile()))){
                            onCallMade(0, MeldType.CHII);
                        }
                    }

                }
            }

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

    protected void onDraw(Canvas canvas){
        for (Player player: players){
            if (player != null) {
                player.draw(canvas);
            }
        }
    }
}
