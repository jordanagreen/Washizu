package com.example.jordanagreen.washizu;

import android.os.Handler;

import java.util.Random;

import static com.example.jordanagreen.washizu.Constants.DELAY_BETWEEN_TURNS_MS;

/**
 * Created by Jordan on 9/20/2015.
 */
public class AiPlayer extends Player {

    public static final String TAG = "AiPlayer";

    public AiPlayer(Game game, int direction){
        super(game, direction);
    }

    @Override
    //return true if game is finished (tsumo)
    //if returned false, should be a discarded tile
    public void takeTurn(final Game.GameCallback callback){
        Tile tile = game.drawTile();
        hand.setDrawnTile(tile);

        //TODO: make it delay so it doesn't discard before even showing what the draw was
        //for now just discard a random tile
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();
                int i = rand.nextInt(hand.getTiles().size());
                discardTile(hand.getTile(i));
                callback.callback();
            }
        }, DELAY_BETWEEN_TURNS_MS);


    }
}
