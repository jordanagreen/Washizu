package com.example.jordanagreen.washizu;

import android.graphics.Canvas;

import java.util.ArrayList;

import static com.example.jordanagreen.washizu.Constants.DISCARD_MAX_TILES;
import static com.example.jordanagreen.washizu.Constants.DISCARD_NUM_ROWS;
import static com.example.jordanagreen.washizu.Constants.DISCARD_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.DISCARD_SIDE_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_LEFT;
import static com.example.jordanagreen.washizu.Constants.SEAT_RIGHT;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;
import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_WIDTH;

/**
 * Created by Jordan on 9/17/2015.
 */
public class DiscardPool {

    public static final String TAG = "DiscardPool";

    private ArrayList<Tile> tiles;
    private int riichiIndex;

    public DiscardPool(){
        this.tiles = new ArrayList<>();
        this.riichiIndex = -1;
    }

    public ArrayList<Tile> getTiles(){
        return tiles;
    }

    public Tile getLastTile() { return tiles.get(tiles.size()); }

    public void addTile(Tile tile, boolean calledRiichi){
        if (tiles.size() < DISCARD_MAX_TILES) {
            tiles.add(tile);
            if (calledRiichi) {
                riichiIndex = tiles.size();
            }
        }
        // it should never get this full in an actual game
        else {
            throw new IllegalStateException("Discard pool is full, can't discard more");
        }
    }

    public int getSize(){ return tiles.size(); }

    public void draw(Canvas canvas, int direction){
        switch (direction){
            case SEAT_DOWN:
            case SEAT_UP:

                int horCenterPadding = (canvas.getWidth() -
                        (TILE_SMALL_WIDTH * DISCARD_ROW_TILES)) /2;

                for (int i = 0; i < tiles.size(); i++){
                    //TODO: handle riichi sideways tile
                    int x = (i % DISCARD_ROW_TILES) * TILE_SMALL_WIDTH + horCenterPadding;
                    //TODO: move it up a bit if it gets to a fourth row
                    int y = canvas.getHeight() - ((TILE_HEIGHT * 2) + (TILE_SMALL_HEIGHT *
                        (DISCARD_NUM_ROWS - (int)(Math.floor(i/DISCARD_ROW_TILES))))
                        + TILE_SMALL_HEIGHT/2);
                    if (direction == SEAT_UP){
                        x = canvas.getWidth() - TILE_SMALL_WIDTH - x;
                        y = canvas.getHeight() - TILE_SMALL_HEIGHT - y;
                    }
                    tiles.get(i).drawSmall(canvas, x, y, direction);
//                    Log.d(TAG, "Drawing discarded tile " + tiles.get(i) + "at " + x + ", " + y);
                }
                break;
            //TODO: draw right and left discard pools
            case SEAT_LEFT:
            case SEAT_RIGHT:
                int verCenterPadding = (canvas.getHeight() -
                        (TILE_SMALL_WIDTH * DISCARD_SIDE_ROW_TILES)) / 2;
                for (int i = 0; i < tiles.size(); i++){
                        int x = TILE_HEIGHT + (TILE_SMALL_HEIGHT *
                            (1 - (int) (Math.ceil(i/DISCARD_SIDE_ROW_TILES))));
                        int y = (i % DISCARD_SIDE_ROW_TILES) * TILE_SMALL_WIDTH + verCenterPadding;
                    if (direction == SEAT_RIGHT){
                        x = canvas.getWidth() - TILE_SMALL_WIDTH - x;
                        y = canvas.getHeight() - TILE_SMALL_HEIGHT - y;
                    }
                        tiles.get(i).drawSmall(canvas, x, y, direction);
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal direction: " + direction);
        }
    }
}
