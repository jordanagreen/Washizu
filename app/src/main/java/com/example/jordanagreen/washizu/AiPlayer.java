package com.example.jordanagreen.washizu;

import java.util.Random;

/**
 * Created by Jordan on 9/20/2015.
 */
public class AiPlayer extends Player {

    public static final String TAG = "AiPlayer";

    public AiPlayer(Game game, int direction){
        super(game, direction);
    }

    @Override
    public int takeTurn(int playerIndex){
        int nextPlayerIndex = (playerIndex + 1) % 4;
        Tile tile = game.drawTile();
        hand.setDrawnTile(tile);

        //for now just discard a random tile
        Random rand = new Random();
        int i = rand.nextInt(Constants.HAND_SIZE);
        discardTile(hand.getTile(i));
        return nextPlayerIndex;
    }
}
