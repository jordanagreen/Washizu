package com.example.jordanagreen.washizu;

import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

/**
 * Created by Jordan on 9/20/2015.
 */
public class HumanPlayer extends Player {

    public static final String TAG = "HumanPlayer";

    public HumanPlayer(SeatDirection direction){
        super(direction);
    }

    public HumanPlayer(JSONObject json) throws JSONException{
        super(json);
    }

    @Override
    public void takeTurn(GameCallback callback){
    }

    //TODO: make buttons appear on the screen

    @Override
    public boolean shouldPon(Tile tile){
        return true;
    }

    @Override
    public boolean shouldChii(Tile tile){
        return true;
    }

    @Override
    public boolean shouldKan(Tile tile){
        return true;
    }

    @Override
    public boolean shouldRon(Tile tile){
        return true;
    }

    @Override
    protected Tile[] getTilesForChii(Tile tile){
        //TODO: get some better logic for this, for now it's just picking an option randomly
        int id = tile.getId();
        Tile tilea = null;
        Tile tileb = null;
        if (mHand.containsTileById(id - 1) && mHand.containsTileById(id - 2)){
            if (Tile.areSameSuit(id, id-1, id-2)){
                tilea = mHand.getTileById(id-2);
                tileb = mHand.getTileById(id-1);
            }
        }
        else if (mHand.containsTileById(id - 1) && mHand.containsTileById(id + 1)){
            if (Tile.areSameSuit(id-1, id, id+1)){
                tilea = mHand.getTileById(id-1);
                tileb = mHand.getTileById(id+1);
            }
        }
        else if (mHand.containsTileById(id + 1) && mHand.containsTileById(id + 2)){
            if (Tile.areSameSuit(id, id+1, id+2)){
                tilea = mHand.getTileById(id+1);
                tileb = mHand.getTileById(id+2);
            }
        }
        if (tilea == null || tileb == null){
            throw new IllegalStateException("Couldn't find legal tiles for chii");
        }
        return new Tile[]{tilea, tileb, tile};
    }

    //return true if a tile was discarded
    public boolean onTouch(MotionEvent event){

        Tile tileToDiscard = null;
        boolean didDiscardTile = false;

        int x = (int) event.getX();
        int y = (int) event.getY();
        //can't remove tiles while iterating without using an iterator, so just mark it for discard
        for (int i = 0; i < mHand.getTiles().size(); i++){
            Tile tile = mHand.getTile(i);
            if ((x > tile.x && x < tile.x + TILE_WIDTH) &&
                    y > tile.y && y < tile.y + TILE_HEIGHT){
                tile.onTouch(event);
                tileToDiscard = tile;
            }
        }
        //check if it's the drawn tile
        if (tileToDiscard == null && mHand.getDrawnTile() != null){
            Tile drawnTile = mHand.getDrawnTile();
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
