package com.example.jordanagreen.washizu;

import android.util.Log;
import android.view.MotionEvent;

import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

/**
 * Created by Jordan on 9/20/2015.
 */
public class HumanPlayer extends Player {

    public static final String TAG = "HumanPlayer";

    public HumanPlayer(Game game, int direction){
        super(game, direction);
    }

    @Override
    public void takeTurn(Game.GameCallback callback){
        Log.d(TAG, "Taking human player's turn");
        Tile tile = game.drawTile();
        Log.d(TAG, "Drew tile " + tile);
        hand.setDrawnTile(tile);
    }

    @Override
    public boolean shouldPon(Tile tile){
        return true;
    }

    //return true if a tile was discarded
    public boolean onTouch(MotionEvent event){

        Tile tileToDiscard = null;
        boolean didDiscardTile = false;

        int x = (int) event.getX();
        int y = (int) event.getY();
        //can't remove tiles while iterating without using an iterator, so just mark it for discard
        for (int i = 0; i < hand.getTiles().size(); i++){
            Tile tile = hand.getTile(i);
            if ((x > tile.x && x < tile.x + TILE_WIDTH) &&
                    y > tile.y && y < tile.y + TILE_HEIGHT){
                tile.onTouch(event);
                tileToDiscard = tile;
            }
        }
        //check if it's the drawn tile
        if (tileToDiscard == null && hand.getDrawnTile() != null){
            Tile drawnTile = hand.getDrawnTile();
            if (x > drawnTile.x && x < drawnTile.x + TILE_HEIGHT &&
                    y > drawnTile.y && y < drawnTile.y + TILE_WIDTH){
                drawnTile.onTouch(event);
                tileToDiscard = drawnTile;
            }
        }
        if (tileToDiscard != null){
            discardTile(tileToDiscard);
            didDiscardTile = true;
        }
        return didDiscardTile;
    }

}
