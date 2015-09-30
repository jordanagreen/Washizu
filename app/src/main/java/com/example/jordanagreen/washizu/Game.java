package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

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

    public Game(){
        players = new Player[4];
        pool = new Stack<>();
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
        players[(firstEast+3)%4].setWind(WIND_SOUTH);
        startRound(ROUND_EAST_1);

        mCurrentPlayerIndex = firstEast;
        takeNextTurn();
    }

    private void startRound(int roundNumber){
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
        //if the player is an AI, immediately call onTurnFinished, which will start the next turn
        if (players[mCurrentPlayerIndex] instanceof AiPlayer){
            players[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    Tile discardedTile = players[mCurrentPlayerIndex].getLastDiscardedTile();
                    onTurnFinished(discardedTile);
                }
            });
        }
        //otherwise wait for input and it'll finish the turn when a discard has been made
        else {
            players[mCurrentPlayerIndex].takeTurn(new GameCallback() {
                @Override
                public void callback() {
                    // do nothing
                }
            });
        }
    }

    private void onTurnFinished(final Tile discardedTile){
        players[mCurrentPlayerIndex].setIsMyTurn(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPlayerIndex = getNextPlayerIndex(discardedTile);
                takeNextTurn();
            }
        }, DELAY_BETWEEN_TURNS_MS);
    }

    private int getNextPlayerIndex(Tile discardedTile){
        //TODO: see if anyone wants to call the tile (assuming the game isn't over by tsumo)
        for (int j = mCurrentPlayerIndex + 1; j < mCurrentPlayerIndex + 3; j++){
            int i = j % players.length;
            if (players[i].canPon(discardedTile)){
                if (players[i].shouldPon(discardedTile)){
                    Log.d(TAG, "Player " + i + " calling pon on " + discardedTile + " from " + mCurrentPlayerIndex);
                    players[i].callPon(discardedTile, mCurrentPlayerIndex * 90);
                }
            }
        }


        //for now just go to the player on the right
        Log.d(TAG, "Next player is " + (mCurrentPlayerIndex + 1) % players.length);
        return (mCurrentPlayerIndex + 1) % players.length;
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
            // if it's the player's turn, let him pick a tile to discard
            if (players[mCurrentPlayerIndex] instanceof HumanPlayer
                    && players[mCurrentPlayerIndex].getIsMyTurn()) {
                //once we've actually discarded a tile, start the next turn
                if (((HumanPlayer)(players[mCurrentPlayerIndex])).onTouch(event)){
                    Tile discardedTile = players[mCurrentPlayerIndex].getLastDiscardedTile();
                    onTurnFinished(discardedTile);
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
