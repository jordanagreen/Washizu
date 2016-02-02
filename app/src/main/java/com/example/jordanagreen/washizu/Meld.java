package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_WIDTH;
import static com.example.jordanagreen.washizu.Constants.TILE_WIDTH;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Meld {
    public static final String TAG = "MELD";

    public static final String KEY_TYPE = "type";
    public static final String KEY_ROTATED_INDEX = "rotated_index";
    public static final String KEY_TILES = "tiles";

    private MeldType mType;
    private List<Tile> mTiles;
    private int mRotatedIndex;
    private Hand mHand; // for drawing horizontally - need to know about the other melds

    public Meld(Tile a, Tile b, Tile c, int rotatedIndex, MeldType type, Hand hand){
        mTiles = Arrays.asList(a, b, c);
        this.mType = type;
        this.mRotatedIndex = rotatedIndex;
        this.mHand = hand;
        Log.d(TAG, "Meld made type " + type +  " rotated index " + rotatedIndex);
    }

    public Meld(Tile a, Tile b, Tile c, Tile d, int rotatedIndex, MeldType type){
        if (type != MeldType.KAN && type != MeldType.SHOUMINKAN){
            throw new IllegalArgumentException("Four tiles but not a kan");
        }
        else {
            mTiles = Arrays.asList(a, b, c, d);
            this.mType = type;
            this.mRotatedIndex = rotatedIndex;
        }
    }

    public Meld(JSONObject json, Hand hand) throws JSONException{
        JSONArray jsonTiles = json.getJSONArray(KEY_TILES);
        mTiles = new ArrayList<>();
        for (int i = 0; i < jsonTiles.length(); i++){
            mTiles.add(new Tile(jsonTiles.getJSONObject(i)));
        }
        mRotatedIndex = json.getInt(KEY_ROTATED_INDEX);
        mType = Enum.valueOf(MeldType.class, json.getString(KEY_TYPE));
        this.mHand = hand;
    }

    //for when we don't need to draw anything, i.e. when making melds for scoring
    public Meld(Tile a, Tile b, Tile c, MeldType type){
        mTiles = Arrays.asList(a, b, c);
        this.mType = type;
        Log.d(TAG, "Meld made type " + type);
    }

    //also for making things a bit easier when scoring
    public Meld(List<Tile> tiles){
       this(tiles.get(0), tiles.get(1), tiles.get(2), tiles.get(0).getId() == tiles.get(2).getId() ?
               MeldType.PON : MeldType.CHII);
    }

    public List<Tile> getTiles(){
        return mTiles;
    }

    public MeldType getType(){
        return mType;
    }

    public void ponToKan(Tile tile){
        if (mType != MeldType.PON || tile.compareTo(mTiles.get(0)) != 0 ||
                tile.compareTo(mTiles.get(1)) != 0 || tile.compareTo(mTiles.get(2)) != 0){
            throw new IllegalArgumentException("Illegal kan");
        }
        else {
            mTiles.add(tile);
            mType = MeldType.SHOUMINKAN;
        }
    }

    public void draw(Canvas canvas, SeatDirection seatDirection, int meldNumber){
        switch (seatDirection){
            case UP:
            case DOWN:
                if (mTiles.size() == 3){
                    int x = canvas.getWidth() - (2 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                    int y = canvas.getHeight() - (TILE_SMALL_HEIGHT * (meldNumber+1));
                    for (int i = 0; i < mTiles.size(); i++){
                        if (i == mRotatedIndex){
                            if (seatDirection == SeatDirection.DOWN){
                                mTiles.get(i).drawSmall(canvas, x,
                                        y + (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                        (seatDirection.getAngle() + 90) % 360);
                            }
                            else {
                                mTiles.get(i).drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                        canvas.getHeight() - y - TILE_SMALL_WIDTH -
                                                (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                        (seatDirection.getAngle() + 90) % 360);
                            }
                            x = x + TILE_SMALL_HEIGHT;
                        }
                        else {
                        if (seatDirection == SeatDirection.DOWN){
                            mTiles.get(i).drawSmall(canvas, x, y, seatDirection.getAngle());
                        }
                        else {
                            mTiles.get(i).drawSmall(canvas,
                                    canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                    canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                    seatDirection.getAngle());
                        }

                            x = x + TILE_SMALL_WIDTH;
                        }
                    }
                }
                //kan
                else {
                    if (mType == MeldType.KAN){
                        int x = canvas.getWidth() - (3 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        int y = canvas.getHeight() - (TILE_SMALL_HEIGHT * (meldNumber+1));
                        for (int i = 0; i < mTiles.size(); i++){
                            if (i == mRotatedIndex){
                                if (seatDirection == SeatDirection.DOWN){
                                    mTiles.get(i).drawSmall(canvas, x,
                                            y + (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection.getAngle() + 90) % 360);
                                }
                                else {
                                    mTiles.get(i).drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                            canvas.getHeight() - y - TILE_SMALL_WIDTH -
                                                    (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection.getAngle() + 90) % 360);
                                }
                                x = x + TILE_SMALL_HEIGHT;
                            }
                            else {
                                if (seatDirection == SeatDirection.DOWN){
                                    mTiles.get(i).drawSmall(canvas, x, y, seatDirection.getAngle());
                                }
                                else {
                                    mTiles.get(i).drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                            canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                            seatDirection.getAngle());
                                }

                                x = x + TILE_SMALL_WIDTH;
                            }
                        }
                    }
                    //TODO: shouminkan
                    else {

                    }
                }
                break;
            case LEFT:
            case RIGHT:
                if (mTiles.size() == 3){
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
                    int y = canvas.getHeight() - (2 * TILE_SMALL_WIDTH + TILE_SMALL_HEIGHT)
                    //don't overlap the hand of the player to the right
                        - (TILE_HEIGHT + TILE_WIDTH);
                    // add space for previous melds
                    for (int i = 0; i < meldNumber; i++){
                        Meld meld = mHand.getMelds().get(i);
                        if (meld.getType() == MeldType.KAN){
                            y = y - (3 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        }
                        else {
                            y = y - (2 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT;
                        }
                    }
                    for (int i = 0; i < mTiles.size(); i++){
                        if (i == mRotatedIndex){
                            if (seatDirection == SeatDirection.LEFT){
                                mTiles.get(i).drawSmall(canvas, x, y,
                                        (seatDirection.getAngle() + 90) % 360);
                            }
                            else {
                                mTiles.get(i).drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                        canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                        (seatDirection.getAngle() + 90) % 360);
                            }
                            y = y + TILE_SMALL_HEIGHT;
                        }
                        else {
                            if (seatDirection == SeatDirection.LEFT){
                                mTiles.get(i).drawSmall(canvas, x, y, seatDirection.getAngle());
                            }
                            else {
                                mTiles.get(i).drawSmall(canvas,
                                        canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                        canvas.getHeight() - y - TILE_SMALL_WIDTH,
                                        seatDirection.getAngle());
                            }
                            y = y + TILE_SMALL_WIDTH;
                        }
                    }
                }
                //TODO: side kan
                else {
                    if (mType == MeldType.KAN){
                        int x = 0;
                        int y = canvas.getHeight() - (3 * TILE_SMALL_WIDTH) - TILE_SMALL_HEIGHT
                                - (TILE_HEIGHT + TILE_WIDTH);
                        for (int i = 0; i < mTiles.size(); i++){
                            if (i == mRotatedIndex){
                                if (seatDirection == SeatDirection.LEFT){
                                    mTiles.get(i).drawSmall(canvas, x,
                                            y + (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection.getAngle() + 90) % 360);
                                }
                                else {
                                    mTiles.get(i).drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_HEIGHT,
                                            canvas.getHeight() - y - TILE_SMALL_WIDTH -
                                                    (TILE_SMALL_HEIGHT - TILE_SMALL_WIDTH),
                                            (seatDirection.getAngle() + 90) % 360);
                                }
                                x = x + TILE_SMALL_HEIGHT;
                            }
                            else {
                                if (seatDirection == SeatDirection.LEFT){
                                    mTiles.get(i).drawSmall(canvas, x, y, seatDirection.getAngle());
                                }
                                else {
                                    mTiles.get(i).drawSmall(canvas,
                                            canvas.getWidth() - x - TILE_SMALL_WIDTH,
                                            canvas.getHeight() - y - TILE_SMALL_HEIGHT,
                                            seatDirection.getAngle());
                                }

                                x = x + TILE_SMALL_WIDTH;
                            }
                        }
                    }
                    //TODO: shouminkan
                    else {

                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal direction: " + seatDirection);

        }
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_TYPE, mType.toString());
        json.put(KEY_ROTATED_INDEX, mRotatedIndex);
        JSONArray jsonTiles = new JSONArray();
        for (Tile tile: mTiles){
            jsonTiles.put(tile.toJson());
        }
        json.put(KEY_TILES, jsonTiles);
        return json;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(mType).append(":");
        for (Tile tile: mTiles){
            sb.append(" ").append(tile);
        }
        return sb.toString();
    }
}
