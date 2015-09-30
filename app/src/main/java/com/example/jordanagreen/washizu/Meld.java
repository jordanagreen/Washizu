package com.example.jordanagreen.washizu;

import android.graphics.Canvas;

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
    enum MeldType {CHII, PON, KAN, CLOSED_KAN}

    private MeldType type;
    private Tile[] tiles;
    private int directionCalled;

    public Meld(Tile a, Tile b, Tile c, int directionCalled, MeldType type){
        tiles = new Tile[] {a, b, c};
        this.directionCalled = directionCalled;
        this.type = type;
    }

    public Meld(Tile a, Tile b, Tile c, Tile d, int directionCalled, MeldType type){
        if (type != MeldType.KAN && type != MeldType.CLOSED_KAN){
            throw new IllegalArgumentException("Four tiles but not a kan");
        }
        else {
            tiles = new Tile[] {a, b, c, d};
            this.directionCalled = directionCalled;
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
            type = MeldType.KAN;
        }
    }

    public void draw(Canvas canvas, int seatDirection, int meldNumber){
        switch (seatDirection){
            case SEAT_UP:
            case SEAT_DOWN:
                //TODO: draw kans too
                if (tiles.length == 3){
                    int rotatedIndex = 0;
                    if (directionCalled == (seatDirection + 180) % 360){
                        rotatedIndex = 1;
                    }
                    else if (directionCalled == (seatDirection + 270) % 360){
                        rotatedIndex = 2;
                    }
                    int x = canvas.getWidth() - (2 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                    int y = canvas.getHeight() - (TILE_SMALL_HEIGHT * (meldNumber+1));
                    for (int i = 0; i < tiles.length; i++){
//                        if (seatDirection == SEAT_UP){
//                            x = canvas.getWidth() - x;
//                            y = canvas.getHeight() - y;
//                        }
//                        if (i == rotatedIndex){
//                            tiles[i].drawSmall(canvas, x, y, (seatDirection + 90) % 360);
//                            x = x + TILE_SMALL_HEIGHT;
//                        }
//                        else {
                        if (seatDirection == SEAT_DOWN){
                            tiles[i].drawSmall(canvas, x, y, seatDirection);
                        }
                        else {
                            tiles[i].drawSmall(canvas, canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                    canvas.getHeight() - y - TILE_SMALL_HEIGHT, seatDirection);
                        }

                            x = x + TILE_SMALL_WIDTH;
//                        }
                    }
                }
                break;
            case SEAT_LEFT:
            case SEAT_RIGHT:
                break;
            default:
                throw new IllegalArgumentException("Illegal directionCalled: " + seatDirection);

        }
    }
}
