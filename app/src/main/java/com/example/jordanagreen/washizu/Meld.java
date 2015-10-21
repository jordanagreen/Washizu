package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_KAN;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_PON;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_SHOUMINKAN;
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

    public static final String KEY_TYPE = "type";
    public static final String KEY_ROTATED_INDEX = "rotated_index";
    public static final String KEY_TILES = "tiles";

    private int type;
    private Tile[] tiles;
    private int rotatedIndex;
    private Hand hand; // for drawing horizontally - need to know about the other melds

    public Meld(Tile a, Tile b, Tile c, int rotatedIndex, int type, Hand hand){
        tiles = new Tile[] {a, b, c};
        this.type = type;
        this.rotatedIndex = rotatedIndex;
        this.hand = hand;
        Log.d(TAG, "Meld made type " + type +  " rotated index " + rotatedIndex);
    }

    public Meld(JSONObject json, Hand hand) throws JSONException{
        JSONArray jsonTiles = json.getJSONArray(KEY_TILES);
        tiles = new Tile[jsonTiles.length()];
        for (int i = 0; i < jsonTiles.length(); i++){
            tiles[i] = new Tile(jsonTiles.getJSONObject(i));
        }
        rotatedIndex = json.getInt(KEY_ROTATED_INDEX);
        type = json.getInt(KEY_TYPE);
        this.hand = hand;
    }

    public Meld(Tile a, Tile b, Tile c, Tile d, int directionCalled, int type){
        if (type != MELD_TYPE_KAN && type != MELD_TYPE_SHOUMINKAN){
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

    public int getType(){
        return type;
    }

    public void ponToKan(Tile tile){
        if (type != MELD_TYPE_PON || tile.compareTo(tiles[0]) != 0 ||
                tile.compareTo(tiles[1]) != 0 || tile.compareTo(tiles[2]) != 0){
            throw new IllegalArgumentException("Illegal kan");
        }
        else {
            tiles = new Tile[] {tiles[0], tiles[1], tiles[2], tile};
            type = MELD_TYPE_SHOUMINKAN;
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
                    if (type ==MELD_TYPE_KAN){
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
                        if (meld.getType() == MELD_TYPE_KAN){
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

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_TYPE, type);
        json.put(KEY_ROTATED_INDEX, rotatedIndex);
        JSONArray jsonTiles = new JSONArray();
        for (int i = 0; i < tiles.length; i++){
            jsonTiles.put(tiles[i].toJson());
        }
        json.put(KEY_TILES, jsonTiles);
        return json;
    }
}
