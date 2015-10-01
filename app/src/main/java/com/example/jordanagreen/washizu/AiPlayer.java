package com.example.jordanagreen.washizu;

import android.os.Handler;

import java.util.Random;

import static com.example.jordanagreen.washizu.Constants.DELAY_BETWEEN_TURNS_MS;
import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;

/**
 * Created by Jordan on 9/20/2015.
 */
public class AiPlayer extends Player {

    public static final String TAG = "AiPlayer";

    public AiPlayer(Game game, int direction){
        super(game, direction);
    }

    @Override
    public void takeTurn(final Game.GameCallback callback){
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
        int i = rand.nextInt(hand.getTiles().size());
        return hand.getTile(i);
    }

    @Override
    public boolean shouldPon(Tile tile){
        return (direction == SEAT_UP || direction == SEAT_DOWN);
    }
}
