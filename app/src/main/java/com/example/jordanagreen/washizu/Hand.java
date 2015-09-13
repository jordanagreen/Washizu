package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

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

    public boolean onTouch(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        for (Tile tile: tiles){
            if ((x > tile.x && x < tile.x + Constants.TILE_WIDTH) &&
                 y > tile.y && y < tile.y + Constants.TILE_HEIGHT){
                return tile.onTouch(event);
            }
        }
        return false;
    }

    public void draw(Canvas canvas, Bitmap[] tileImages){

        // draw the ones on the narrow sides of the phone in two rows
        // TODO: don't do this on tablets? - don't do the top row if not enough closed tiles
//      TODO: center the hands
        switch (seatDirection){
            case Constants.SEAT_DOWN:
            case Constants.SEAT_UP:

                int HOR_CENTER_PADDING = (canvas.getWidth() -
                        (Constants.TILE_WIDTH * Constants.HAND_BOTTOM_ROW_TILES)) /2;

                // draw the top row
                for (int i = 0; i < Constants.HAND_TOP_ROW_TILES; i++){
                    if (tiles.get(i) != null){
                          int x = Constants.TILE_WIDTH * i + (Constants.TILE_WIDTH/2)
                                  + HOR_CENTER_PADDING;
                          int y = canvas.getHeight() - (Constants.TILE_HEIGHT * 2);
                        if (seatDirection == Constants.SEAT_UP){
                            x = canvas.getWidth() - Constants.TILE_WIDTH - x;
                            y = Constants.TILE_HEIGHT;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, tileImages, x, y, seatDirection);

                    }
                }
                // draw the bottom row
                for (int i = Constants.HAND_TOP_ROW_TILES; i < Constants.HAND_SIZE; i++){
                    if (tiles.get(i) != null){
                        int x = Constants.TILE_WIDTH*(i - Constants.HAND_TOP_ROW_TILES)
                                + HOR_CENTER_PADDING;
                        int y = canvas.getHeight() - Constants.TILE_HEIGHT;
                        if (seatDirection == Constants.SEAT_UP){
                            x = canvas.getWidth() - Constants.TILE_WIDTH - x;
                            y = 0;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, tileImages, x, y, seatDirection);
                    }
                }
                break;
            case Constants.SEAT_LEFT:
            case Constants.SEAT_RIGHT:
                break;
            default:
                break;
        }

    }
}
