package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.HAND_BOTTOM_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.HAND_SIZE;
import static com.example.jordanagreen.washizu.Constants.HAND_TOP_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_LEFT;
import static com.example.jordanagreen.washizu.Constants.SEAT_RIGHT;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;
import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Hand {

    public static final String TAG = "Hand";

    private ArrayList<Tile> tiles;
    private ArrayList<Meld> melds;

    public Hand(){
        tiles = new ArrayList<>(HAND_SIZE);
        melds = new ArrayList<>();
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

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Meld> getMelds(){
        return melds;
    }

    public void sortHand(){
        Collections.sort(tiles);
    }

    public void makeChii(Tile a, Tile b, Tile c, int direction){
        if (a.getSuit() != b.getSuit() || b.getSuit() != c.getSuit()){
            throw new IllegalArgumentException("Making chii with tiles of different suits");
        }
        List<Tile> tiles = Arrays.asList(a, b, c);
        Collections.sort(tiles);
        if (tiles.get(0).getId() == tiles.get(1).getId() - 1 &&
                tiles.get(1).getId() == tiles.get(2).getId() - 1){
            Log.d(TAG, "Chii with " + tiles.get(0) + " " + tiles.get(1) + " " + tiles.get(2));
            Meld meld = new Meld(tiles.get(0), tiles.get(1), tiles.get(2),
                    direction, Meld.MeldType.CHII);
            addMeld(meld);
        }
        else {
            throw new IllegalArgumentException("Illegal tiles for chii");
        }
    }

    public void makePon(Tile a, Tile b, Tile c, int direction){
        if (a.compareTo(b) != 0 || b.compareTo(c) != 0){
            throw new IllegalArgumentException("Illegal tiles for pon");
        }
        Log.d(TAG, "Pon with " + a + " " + b + " " + c);
        Meld meld = new Meld(a, b, c, direction, Meld.MeldType.PON);
        addMeld(meld);

    }

    public void makeKan(Tile a, Tile b, Tile c, Tile d, int direction, boolean isClosed){
        if (a.compareTo(b) != 0 || b.compareTo(c) != 0 || c.compareTo(d) != 0){
            throw new IllegalArgumentException("Illegal tiles for kan");
        }
        if (isClosed){
            Log.d(TAG, "Closed kan with " + a + " " + b + " " + c + " " + d);
            Meld meld = new Meld(a, b, c, d, direction, Meld.MeldType.CLOSED_KAN);
            addMeld(meld);
        }
        else {
            Log.d(TAG, "Kan with " + a + " " + b + " " + c + " " + d);
            Meld meld = new Meld(a, b, c, d, direction, Meld.MeldType.KAN);
            addMeld(meld);
        }
    }

    private void addMeld(Meld meld){
        //TODO: remove the tiles from the hand
        if (melds.size() < 4){
            melds.add(meld);
            Log.d(TAG, "Meld added");
        }
        else {
            throw new IllegalStateException("Already have four melds");
        }
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

    public void draw(Canvas canvas, int seatDirection){
        drawHand(canvas, seatDirection);
        drawMelds(canvas, seatDirection);
    }

    private void drawHand(Canvas canvas, int seatDirection){
        // draw the ones on the narrow sides of the phone in two rows
        // TODO: don't do this on tablets? - don't do the top row if not enough closed tiles
        switch (seatDirection){
            case SEAT_DOWN:
            case SEAT_UP:
                int horCenterPadding = (canvas.getWidth() -
                        (TILE_WIDTH * HAND_BOTTOM_ROW_TILES)) /2;
                // draw the top row
                for (int i = 0; i < HAND_TOP_ROW_TILES; i++){
                    if (tiles.get(i) != null){
                          int x = TILE_WIDTH * i + (TILE_WIDTH/2)
                                  + horCenterPadding;
                          int y = canvas.getHeight() - (TILE_HEIGHT * 2);
                        if (seatDirection == SEAT_UP){
                            x = canvas.getWidth() - TILE_WIDTH - x;
                            y = TILE_HEIGHT;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, x, y, seatDirection);
                    }
                }
                // draw the bottom row
                for (int i = HAND_TOP_ROW_TILES; i < HAND_SIZE; i++){
                    if (tiles.get(i) != null){
                        int x = TILE_WIDTH*(i - HAND_TOP_ROW_TILES)
                                + horCenterPadding;
                        int y = canvas.getHeight() - TILE_HEIGHT;
                        if (seatDirection == SEAT_UP){
                            x = canvas.getWidth() - TILE_WIDTH - x;
                            y = 0;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, x, y, seatDirection);
                    }
                }
                break;
            case SEAT_LEFT:
            case SEAT_RIGHT:
                int verCenterPadding = (canvas.getHeight() -
                        (TILE_WIDTH * HAND_SIZE)) /2;
                for (int i = 0; i < HAND_SIZE; i++){
                    if (tiles.get(i) != null){
                        int x = 0;
                        int y = (TILE_WIDTH * i) + verCenterPadding;
                        if (seatDirection == SEAT_RIGHT){
                            x = canvas.getWidth() - TILE_HEIGHT;
                            y = canvas.getHeight() - TILE_WIDTH - y;
                        }
                        Tile tile = tiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, x, y, seatDirection);
                    }
                }
                break;
            default:
                break;
        }

    }

    private void drawMelds(Canvas canvas, int seatDirection){

    }

}
