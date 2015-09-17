package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.jordanagreen.washizu.Constants.*;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Hand {

    public static final String TAG = "Hand";

    public ArrayList<Tile> tiles;

    public Hand(){
        tiles = new ArrayList<>(HAND_SIZE);
    }

    public void addTile(Tile tile){
        if (tiles.size() < HAND_SIZE){
            Log.d(TAG, "Tile " + tile + " added");
            tiles.add(tile);
            sortHand();
        }
        else {
            throw new IllegalStateException("Hand is full, cannot add a tile.");
        }
    }

    public void sortHand(){
        Collections.sort(tiles);
    }

    public boolean onTouch(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        for (Tile tile: tiles){
            if ((x > tile.x && x < tile.x + TILE_WIDTH) &&
                 y > tile.y && y < tile.y + TILE_HEIGHT){
                return tile.onTouch(event);
            }
        }
        return false;
    }

    public void draw(Canvas canvas, Bitmap[] tileImages, int seatDirection){
        // draw the ones on the narrow sides of the phone in two rows
        // TODO: don't do this on tablets? - don't do the top row if not enough closed tiles
//      TODO: center the hands
        switch (seatDirection){
            case SEAT_DOWN:
            case SEAT_UP:

                int HOR_CENTER_PADDING = (canvas.getWidth() -
                        (TILE_WIDTH * HAND_BOTTOM_ROW_TILES)) /2;

                // draw the top row
                for (int i = 0; i < HAND_TOP_ROW_TILES; i++){
                    if (tiles.get(i) != null){
                          int x = TILE_WIDTH * i + (TILE_WIDTH/2)
                                  + HOR_CENTER_PADDING;
                          int y = canvas.getHeight() - (TILE_HEIGHT * 2);
                        if (seatDirection == SEAT_UP){
                            x = canvas.getWidth() - TILE_WIDTH - x;
                            y = TILE_HEIGHT;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, tileImages, x, y, seatDirection);
                    }
                }
                // draw the bottom row
                for (int i = HAND_TOP_ROW_TILES; i < HAND_SIZE; i++){
                    if (tiles.get(i) != null){
                        int x = TILE_WIDTH*(i - HAND_TOP_ROW_TILES)
                                + HOR_CENTER_PADDING;
                        int y = canvas.getHeight() - TILE_HEIGHT;
                        if (seatDirection == SEAT_UP){
                            x = canvas.getWidth() - TILE_WIDTH - x;
                            y = 0;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, tileImages, x, y, seatDirection);
                    }
                }
                break;
            case SEAT_LEFT:
            case SEAT_RIGHT:
                break;
            default:
                break;
        }

    }


}
