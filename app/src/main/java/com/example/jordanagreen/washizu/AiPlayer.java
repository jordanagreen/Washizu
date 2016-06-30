package com.example.jordanagreen.washizu;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.example.jordanagreen.washizu.Constants.DELAY_BETWEEN_TURNS_MS;

/**
 * Created by Jordan on 9/20/2015.
 */
public class AiPlayer extends Player {

    public static final String TAG = "AiPlayer";

    public AiPlayer(SeatDirection direction){
        super(direction);
    }

    public AiPlayer(JSONObject json) throws JSONException{
        super(json);
    }

    @Override
    public void takeTurn(final GameCallback callback){
        //for now just discard a random tile
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Tile tile = pickTileToDiscard();
                discardTile(tile);
                callback.callback();
            }
        }, DELAY_BETWEEN_TURNS_MS);
    }

    private Tile pickTileToDiscard(){
        Random rand = new Random();
        int i = rand.nextInt(mHand.getTiles().size());
        return mHand.getTile(i);
    }

    public boolean shouldPon(Tile tile){
        return true;
    }

    public boolean shouldChii(Tile tile){
        return true;
    }

    public boolean shouldKan(Tile tile){
        return true;
    }

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
        //TODO: figure out why this sometimes gets thrown
        if (tilea == null || tileb == null){
            throw new IllegalStateException("Couldn't find legal tiles for chii");
        }
        return new Tile[]{tilea, tileb, tile};
    }
}
