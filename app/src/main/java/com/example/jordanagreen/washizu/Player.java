package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

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

    private Tile tileToDiscard;

    public Player(Game game, int direction){
        this.game = game;
        this.direction = direction;
        this.hand = new Hand();
        this.score = Constants.STARTING_SCORE;
        this.discards = new DiscardPool();
        this.inRiichi = false;
    }

    public abstract void takeTurn();

    public Tile getTileToDiscard() {
        return tileToDiscard;
    }

    //TODO: move this to HumanPlayer, it's just here now to test drawing all four hands
    //return true if a tile was discarded
    public boolean onTouch(MotionEvent event){

        boolean didDiscardTile = false;

        int x = (int) event.getX();
        int y = (int) event.getY();
        //can't remove tiles while iterating without using an iterator, so just mark it for discard
        //doesn't matter because this is just for testing anyway
        tileToDiscard = null;
        //TODO: check if it's the drawn tile too
        for (int i = 0; i < hand.getTiles().size(); i++){
            Tile tile = hand.getTile(i);
            if ((x > tile.x && x < tile.x + TILE_WIDTH) &&
                    y > tile.y && y < tile.y + TILE_HEIGHT){
                tile.onTouch(event);
                tileToDiscard = tile;
            }
        }
        if (tileToDiscard != null){
            discardTile(tileToDiscard);
            tileToDiscard = null;
            didDiscardTile = true;
        }
        return didDiscardTile;
    }

    public void setHand(Hand hand) { this.hand = hand; }

    public void setWind(int wind) { this.wind = wind; }

    public void discardTile(Tile tile){
        Log.d(TAG, "discarding tile " + tile);
        hand.discardTile(tile);
        //TODO: get rid of the try, a normal game won't have it happen
        try {
            discards.addTile(tile, false);
        }
        catch (IllegalStateException e){}
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
        hand.draw(canvas, direction);
        discards.draw(canvas, direction);
    }

}
