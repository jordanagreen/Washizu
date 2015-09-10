package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Hand {

    public static final String TAG = "Hand";

    public Tile[] tiles;

    public Hand(){
        tiles = new Tile[Constants.HAND_SIZE];
        tiles[0] = new Tile(Constants.MAN_1);
        tiles[1] = new Tile(Constants.MAN_2);
        tiles[2] = new Tile(Constants.MAN_3);
        tiles[3] = new Tile(Constants.MAN_4);
        tiles[4] = new Tile(Constants.MAN_5);
        tiles[5] = new Tile(Constants.MAN_6);
        tiles[6] = new Tile(Constants.MAN_7);
        tiles[7] = new Tile(Constants.MAN_8);
        tiles[8] = new Tile(Constants.MAN_9);
        tiles[9] = new Tile(Constants.PIN_1);
        tiles[10] = new Tile(Constants.PIN_2);
        tiles[11] = new Tile(Constants.PIN_3);
        tiles[12] = new Tile(Constants.PIN_4);
    }

    public void draw(Canvas canvas, Bitmap[] tileImages){
        for (int i = 0; i < tiles.length; i++){
            if (tiles[i] != null){
                tiles[i].draw(canvas, tileImages, Constants.TILE_WIDTH * i, 0);
            }
        }
    }
}
