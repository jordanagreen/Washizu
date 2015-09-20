package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

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
        int nextPlayerIndex = firstEast;
        startRound(ROUND_EAST_1);

//        for (int i = 0; i < NUM_ROUNDS; i++){
//            roundNumber = i;
//            boolean roundFinished = false;
            // TODO: figure out the dealer after each round
//            startRound(i);
//            while (!roundFinished){
//                nextPlayerIndex = players[nextPlayerIndex].takeTurn(nextPlayerIndex);
//                if (nextPlayerIndex == -1){
//                    roundFinished = true;
//                }
//            }
//        }
    }

    private void startRound(int roundNumber){
        Log.d(TAG, "Starting round " + roundNumber);
        // should this be part of the call or auto-increment?
        this.roundNumber = roundNumber;
        shufflePool();
        dealHands();
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
            // TODO: check boundaries to see if it's actually touching the hand (or anything else)
            // and if so return to consume the event
            // also only need to check the human player and while it's their turn
            // but for now let it touch anyone for testing drawing

            for(Player player: players){
                player.onTouch(event);
            }
//            HumanPlayer humanPlayer = (HumanPlayer) players[0];
//                Log.d(TAG, "Touched on human player's turn");
//                humanPlayer.onTouch(event);

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
