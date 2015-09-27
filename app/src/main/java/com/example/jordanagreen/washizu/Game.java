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

    public int getCurrentPlayerIndex(){
        return mCurrentPlayerIndex;
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
//        int nextPlayerIndex = firstEast;
        startRound(ROUND_EAST_1);

        // get the next player to take a turn (for now just go to the right, no calls
        // if it's an AI, take their turn, else break the loop (i.e. wait for input
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

    //Call when taking the player's turn, starts a loop that goes through all the AI players' turns
    private void takeNextTurn(){
        for (Player player: players){
            player.setIsMyTurn(false);
        }
        if (players[mCurrentPlayerIndex] instanceof AiPlayer){
            final Handler handler = new Handler();
            Log.d(TAG, "AI player's turn " + mCurrentPlayerIndex);
            // wait a little bit just so it doesn't look like everyone's going at once
            //TODO: this really needs to be in another thread
//            Log.d(TAG, "starting handler");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Log.d(TAG, "handler started");
                    players[mCurrentPlayerIndex].takeTurn();
                    players[mCurrentPlayerIndex].setIsMyTurn(true);
                    //for now, just go to the next person on the right
                    mCurrentPlayerIndex = (mCurrentPlayerIndex + 1) % 4;
                    // if the next player is also an AI, do this again
                    if (players[mCurrentPlayerIndex] instanceof AiPlayer){
                        handler.postDelayed(this, DELAY_BETWEEN_TURNS_MS);

                    }
                    else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                players[mCurrentPlayerIndex].takeTurn();
                                players[mCurrentPlayerIndex].setIsMyTurn(true);
                            }
                        }, DELAY_BETWEEN_TURNS_MS);
                    }
                }
            }, DELAY_BETWEEN_TURNS_MS);
        }
        //otherwise it's the player's turn - draw a tile and wait for input
//        else {
//            Log.d(TAG, "Human player's turn");
//            players[mCurrentPlayerIndex].takeTurn();
//            mCurrentPlayerIndex = (mCurrentPlayerIndex + 1) % 4;
//            takeNextTurn();
//        }

        //TODO: see if anyone wants to call the tile (assuming the game isn't over by tsumo)
//        if (players[mCurrentPlayerIndex] instanceof AiPlayer){
////            players[mCurrentPlayerIndex].takeTurn();
////            mCurrentPlayerIndex = mCurrentPlayerIndex + 1;
//            takeNextTurn();
//        }
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
            if (players[mCurrentPlayerIndex] instanceof HumanPlayer) {
                //once we've actually discarded a tile, start the next turn loop
                if (((HumanPlayer)(players[mCurrentPlayerIndex])).onTouch(event)){
                    mCurrentPlayerIndex = mCurrentPlayerIndex + 1;
                    takeNextTurn();
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
