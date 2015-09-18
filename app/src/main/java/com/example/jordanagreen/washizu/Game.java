package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import static com.example.jordanagreen.washizu.Constants.HAND_SIZE;
import static com.example.jordanagreen.washizu.Constants.ROUND_EAST_1;
import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_LEFT;
import static com.example.jordanagreen.washizu.Constants.SEAT_RIGHT;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TILE_MIN_ID;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Game {

    public static final String TAG = "Game";

    private Player[] players;
    private int roundNumber;
    private Stack<Tile> pool;

    public Game(){
        players = new Player[4];

        roundNumber = ROUND_EAST_1;

        players[0] = new Player(SEAT_DOWN);
        players[1] = new Player(SEAT_RIGHT);
        players[2] = new Player(SEAT_UP);
        players[3] = new Player(SEAT_LEFT);

        pool = new Stack<>();
    }

    public void startRound(int roundNumber){
        Log.d(TAG, "Starting round " + roundNumber);
        // should this be part of the call or auto-increment?
        this.roundNumber = roundNumber;
        pool = shufflePool();
        dealHands();
    }

    private Stack<Tile> shufflePool(){
        //one opaque and three see-through for each tile
        ArrayList<Tile> list = new ArrayList<>();
        for (int i = TILE_MIN_ID; i <= TILE_MAX_ID; i++){
            list.add(new Tile(i, true));
            for (int j = 0; j < 3; j++){
                list.add(new Tile(i, false));
            }
        }
        Collections.shuffle(list);
        pool = new Stack<>();
        pool.addAll(list);
        return pool;
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

    private Tile drawTile(){
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
            // TODO: check boundaries to see if it's actually touching the hand (or anything else)
            // and if so return to consume the event
            // also only need to check the human player
            for (Player player: players){
                player.onTouch(event);
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
