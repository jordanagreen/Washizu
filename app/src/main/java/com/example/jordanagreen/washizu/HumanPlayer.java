package com.example.jordanagreen.washizu;

import android.util.Log;

/**
 * Created by Jordan on 9/20/2015.
 */
public class HumanPlayer extends Player {

    public static final String TAG = "HumanPlayer";

    private Tile tileToDiscard;

    public HumanPlayer(Game game, int direction){
        super(game, direction);
    }

    @Override
    public void takeTurn(){
        Log.d(TAG, "Taking human player's turn");
//        int nextPlayerIndex = (playerIndex + 1) % 4;
        Tile tile = game.drawTile();
        Log.d(TAG, "Drew tile " + tile);
        hand.setDrawnTile(tile);
        //TODO: stall until the user touches a tile

//        return nextPlayerIndex;
    }


}
