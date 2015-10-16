package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import static com.example.jordanagreen.washizu.Constants.SEAT_DOWN;
import static com.example.jordanagreen.washizu.Constants.SEAT_LEFT;
import static com.example.jordanagreen.washizu.Constants.SEAT_RIGHT;
import static com.example.jordanagreen.washizu.Constants.SEAT_UP;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_WIDTH;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Meld {
    public static final String TAG = "MELD";

    enum MeldType {CHII, PON, KAN, SHOUMINKAN}

    private MeldType type;
    private Tile[] tiles;
    private int rotatedIndex;
    private Hand hand; // for drawing horizontally - need to know about the other melds

    public Meld(Tile a, Tile b, Tile c, int rotatedIndex, MeldType type, Hand hand){
        tiles = new Tile[] {a, b, c};
        this.type = type;
        this.rotatedIndex = rotatedIndex;
        this.hand = hand;
        Log.d(TAG, "Meld made type " + type +  " rotated index " + rotatedIndex);
    }

    public Meld(Tile a, Tile b, Tile c, Tile d, int directionCalled, MeldType type){
        if (type != MeldType.KAN && type != MeldType.SHOUMINKAN){
            throw new IllegalArgumentException("Four tiles but not a kan");
        }
        else {
            tiles = new Tile[] {a, b, c, d};
            this.type = type;
        }
    }

    public Tile[] getTiles(){
        return tiles;
    }

    public MeldType getType(){
        return type;
    }

    public void ponToKan(Tile tile){
        if (type != MeldType.PON || tile.compareTo(tiles[0]) != 0 ||
                tile.compareTo(tiles[1]) != 0 || tile.compareTo(tiles[2]) != 0){
            throw new IllegalArgumentException("Illegal kan");
        }
        else {
            tiles = new Tile[] {tiles[0], tiles[1], tiles[2], tile};
            type = MeldType.SHOUMINKAN;
        }
    }

    public void draw(Canvas canvas, int seatDirection, int meldNumber){
        switch (seatDirection){
            case SEAT_UP:
            case SEAT_DOWN:
                //TODO: draw kans too
                if (tiles.length == 3){
                    int x = canvas.getWidth() - (2 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                    int y = canvas.getHeight() - (TILE_SMALL_HEIGHT * (meldNumber+1));
                    for (int i = 0; i < tiles.length; i++){
                        if (i == rotatedIndex){
                            if (seatDirection == SEAT_DOWN){
                                tiles[i].drawSmall(canvas, x,
                                        y + (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                        (seatDirection + 90) % 360);
                            }
                            else {
                                tiles[i].drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                        canvas.getHeight() - y - TILE_SMALL_WIDTH -
                                                (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                        (seatDirection + 90) % 360);
                            }
                            x = x + TILE_SMALL_HEIGHT;
                        }
                        else {
                        if (seatDirection == SEAT_DOWN){
                            tiles[i].drawSmall(canvas, x, y, seatDirection);
                        }
                        else {
                            tiles[i].drawSmall(canvas,
                                    canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                    canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                    seatDirection);
                        }

                            x = x + TILE_SMALL_WIDTH;
                        }
                    }
                }
                //kan
                else {
                    if (type == MeldType.KAN){
                        int x = canvas.getWidth() - (3 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        int y = canvas.getHeight() - (TILE_SMALL_HEIGHT * (meldNumber+1));
                        for (int i = 0; i < tiles.length; i++){
                            if (i == rotatedIndex){
                                if (seatDirection == SEAT_DOWN){
                                    tiles[i].drawSmall(canvas, x,
                                            y + (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection + 90) % 360);
                                }
                                else {
                                    tiles[i].drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                            canvas.getHeight() - y - TILE_SMALL_WIDTH -
                                                    (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection + 90) % 360);
                                }
                                x = x + TILE_SMALL_HEIGHT;
                            }
                            else {
                                if (seatDirection == SEAT_DOWN){
                                    tiles[i].drawSmall(canvas, x, y, seatDirection);
                                }
                                else {
                                    tiles[i].drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                            canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                            seatDirection);
                                }

                                x = x + TILE_SMALL_WIDTH;
                            }
                        }
                    }
                    //shouminkan
                    else {

                    }
                }
                break;
            case SEAT_LEFT:
            case SEAT_RIGHT:
                if (tiles.length == 3){
                    //not enough screen space for this, but would probably work on a tablet
                    /*
                    int x = TILE_SMALL_HEIGHT * meldNumber;
                    int y = canvas.getHeight() - (2 * TILE_SMALL_WIDTH + TILE_SMALL_HEIGHT);
                    for (int i = 0; i < tiles.length; i++){
                        if (i == rotatedIndex){
                            if (seatDirection == SEAT_LEFT){
                                tiles[i].drawSmall(canvas, x,
                                        y,
                                        (seatDirection + 90) % 360);
                            }
                            else {
                                tiles[i].drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                        canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                        (seatDirection + 90) % 360);
                            }
                            y = y + TILE_SMALL_HEIGHT;
                        }
                        else {
                            if (seatDirection == SEAT_LEFT){
                                tiles[i].drawSmall(canvas, x, y, seatDirection);
                            }
                            else {
                                tiles[i].drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                        canvas.getHeight() - y - TILE_SMALL_WIDTH,
                                        seatDirection);
                            }

                            y = y + TILE_SMALL_WIDTH;
                        }
                    }
                    */

                    // put the melds horizontally to take up the space lost by the hand
                    int x = 0;
                    int y = canvas.getHeight() - (2 * TILE_SMALL_WIDTH + TILE_SMALL_HEIGHT);
                    // add space for previous melds
                    for (int i = 0; i < meldNumber; i++){
                        Meld meld = hand.getMelds().get(i);
                        if (meld.getType() == MeldType.KAN){
                            y = y - (3 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        }
                        else {
                            y = y - (2 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        }
                    }
                    for (int i = 0; i < tiles.length; i++){
                        if (i == rotatedIndex){
                            if (seatDirection == SEAT_LEFT){
                                tiles[i].drawSmall(canvas, x, y, (seatDirection + 90) % 360);
                            }
                            else {
                                tiles[i].drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                        canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                        (seatDirection + 90) % 360);
                            }
                            y = y + TILE_SMALL_HEIGHT;
                        }
                        else {
                            if (seatDirection == SEAT_LEFT){
                                tiles[i].drawSmall(canvas, x, y, seatDirection);
                            }
                            else {
                                tiles[i].drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                        canvas.getHeight() - y - TILE_SMALL_WIDTH,
                                        seatDirection);
                            }
                            y = y + TILE_SMALL_WIDTH;
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal direction: " + seatDirection);

        }
    }
}
