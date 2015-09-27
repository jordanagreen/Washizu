package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Jordan on 9/13/2015.
 */
public abstract class Player {

    public static final String TAG = "Player";

    //TODO: split into human and AI

    protected Hand hand;
    protected DiscardPool discards;
    protected boolean inRiichi;
    protected Game game;
    int score;
    int wind;
    private int direction;
    private boolean isMyTurn;



    public Player(Game game, int direction){
        this.game = game;
        this.direction = direction;
        this.hand = new Hand();
        this.score = Constants.STARTING_SCORE;
        this.discards = new DiscardPool();
        this.inRiichi = false;
        isMyTurn = false;
    }

    public abstract void takeTurn();

    public void setHand(Hand hand) { this.hand = hand; }

    public void setWind(int wind) { this.wind = wind; }

    public void setIsMyTurn(boolean isMyTurn){
        this.isMyTurn = isMyTurn;
    }

    public void discardTile(Tile tile){
        Log.d(TAG, "discarding tile " + tile);
        hand.discardTile(tile);
        discards.addTile(tile, false);
        Log.d(TAG, "Discarded " + tile);
    }

    public void discardTileAndCallRiichi(Tile tile){
        hand.discardTile(tile);
        discards.addTile(tile, true);
        inRiichi = true;
        Log.d(TAG, "Discarded " + tile + " for Riichi");
    }

    public Tile getLastDiscardedTile(){
        return discards.getLastTile();
    }

    public void draw(Canvas canvas){
        //if it's this player's turn, draw the tile they drew too
        hand.draw(canvas, direction, isMyTurn);
        discards.draw(canvas, direction);
    }

}
