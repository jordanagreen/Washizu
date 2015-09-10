package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Hand {

    public static final String TAG = "Hand";

    public ArrayList<Tile> tiles;
    int seatDirection;

    public Hand(int seatDirection){
        tiles = new ArrayList<>(Constants.HAND_SIZE);
        this.seatDirection = seatDirection;
    }

    public Hand(int seatDirection, int tileID){
        tiles = new ArrayList<>(Constants.HAND_SIZE);
        this.seatDirection = seatDirection;

        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add(new Tile(tileID));
        tiles.add( new Tile(tileID));
        tiles.add( new Tile(tileID));
        tiles.add( new Tile(tileID));
    }

    public void addTile(Tile tile){
        if (tiles.size() < Constants.HAND_SIZE){
            tiles.add(tile);
        }
        else {
            throw new IllegalStateException("Hand is full, cannot add a tile.");
        }
    }

    public void draw(Canvas canvas, Bitmap[] tileImages){

        // draw the ones on the narrow sides of the phone in two rows
        // TODO: don't do this on tablets? - don't do the top row if not enough closed tiles
//      TODO: center the hands
        switch (seatDirection){
            case Constants.SEAT_DOWN:
            case Constants.SEAT_UP:
                // draw the top row
                for (int i = 0; i < Constants.HAND_TOP_ROW_TILES; i++){
                    if (tiles.get(i) != null){
                          int x = Constants.TILE_WIDTH * i + (Constants.TILE_WIDTH/2);
                          int y = canvas.getHeight() - (Constants.TILE_HEIGHT * 2);
                        if (seatDirection == Constants.SEAT_UP){
                            x = canvas.getWidth() - Constants.TILE_WIDTH - x;
                            y = Constants.TILE_HEIGHT;
                        }
                        tiles.get(i).draw(canvas, tileImages, x, y, seatDirection);
                    }
                }
                // draw the bottom row
                for (int i = Constants.HAND_TOP_ROW_TILES; i < Constants.HAND_SIZE; i++){
                    if (tiles.get(i) != null){
                        int x = Constants.TILE_WIDTH*(i - Constants.HAND_TOP_ROW_TILES);
                        int y = canvas.getHeight() - Constants.TILE_HEIGHT;
                        if (seatDirection == Constants.SEAT_UP){
                            x = canvas.getWidth() - Constants.TILE_WIDTH - x;
                            y = 0;
                        }
                        tiles.get(i).draw(canvas, tileImages, x, y, seatDirection);
                    }
                }
                break;
            case Constants.SEAT_LEFT:
            case Constants.SEAT_RIGHT:
                break;
            default:
                break;
        }


//        for (int i = 0; i < tiles.length/2; i++){
//            if (tiles[i] != null){
//                tiles[i].draw(canvas, tileImages, Constants.TILE_WIDTH * i, 0);
//            }
//        }
    }
}
