package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ListIterator;

import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

/**
 * Created by Jordan on 9/13/2015.
 */
public class Player {

    public static final String TAG = "Player";

    //TODO: split into human and AI

    private Hand hand;
    private DiscardPool discards;
    public int score;
    public int wind;
    public int direction;
    private boolean inRiichi;

    public Player(int direction){
        this.direction = direction;
        this.hand = new Hand();
        this.score = Constants.STARTING_SCORE;
        this.discards = new DiscardPool();
        this.inRiichi = false;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public boolean onTouch(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        //need an iterator so we can remove tiles while iterating over the list
        ListIterator<Tile> it = hand.getTiles().listIterator();
        while (it.hasNext()){
            Tile tile = it.next();
            if ((x > tile.x && x < tile.x + TILE_WIDTH) &&
                    y > tile.y && y < tile.y + TILE_HEIGHT){
                tile.onTouch(event);
                discardTile(tile, it);
            }
        }
        return false;
    }

    public void discardTile(Tile tile, ListIterator<Tile> it){
        hand.removeTile(tile, it);
        //TODO: get rid of the try, a normal game won't have it happen
        try {
            discards.addTile(tile, false);
        }
        catch (IllegalStateException e){}
        Log.d(TAG, "Discarded " + tile);
    }

    public void discardTileAndCallRiichi(Tile tile, ListIterator<Tile> it){
        hand.removeTile(tile, it);
        discards.addTile(tile, true);
        inRiichi = true;
        Log.d(TAG, "Discarded " + tile + " for Riichi");
    }

    public void draw(Canvas canvas){
        hand.draw(canvas, direction);
        discards.draw(canvas, direction);
    }

}
