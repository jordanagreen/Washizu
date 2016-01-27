package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.HAND_SIZE;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_CHII;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_KAN;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_PON;
import static com.example.jordanagreen.washizu.Constants.MELD_TYPE_SHOUMINKAN;
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

    public static final String KEY_MELDS = "mMelds";
    public static final String KEY_TILES = "mTiles";
//    public static final String KEY_DRAWN_TILE = "drawn_tile";

    private List<Tile> mTiles;
    private List<Meld> mMelds;
    //TODO: make sure this is null when it's not the player's turn
    private Tile mDrawnTile;
    private Player mPlayer;
    private boolean mIsOpen;

    public Hand(Player player){
        mTiles = new ArrayList<>(HAND_SIZE);
        mMelds = new ArrayList<>();
        mDrawnTile = null;
        mIsOpen = false;
        this.mPlayer = player;
    }

    public Hand(JSONObject json, Player player) throws JSONException{
        JSONArray jsonMelds = json.getJSONArray(KEY_MELDS);
        mMelds = new ArrayList<>();
        for (int i = 0; i < jsonMelds.length(); i++){
            mMelds.add(new Meld(jsonMelds.getJSONObject(i), this));
        }
        JSONArray jsonTiles = json.getJSONArray(KEY_TILES);
        mTiles = new ArrayList<>();
        for (int i = 0; i < jsonTiles.length(); i++){
            mTiles.add(new Tile(jsonTiles.getJSONObject(i)));
        }
//        if (!json.isNull(KEY_DRAWN_TILE)){
//            mDrawnTile = new Tile(json.getJSONObject(KEY_DRAWN_TILE));
//        }
//        else {
//            mDrawnTile = null;
//        }
        this.mPlayer = player;
    }

    public Hand(List<Integer> ids, Tile drawnTile, Player player){
        if (ids.size() != 13){
            throw new IllegalArgumentException("Trying to make a hand with " + ids.size() + " mTiles");
        }
        mTiles = new ArrayList<>();
        for (Integer id: ids){
            mTiles.add(new Tile(id));
        }
        mDrawnTile = drawnTile;
        mPlayer = player;
        mMelds = new ArrayList<>();
        mIsOpen = false;
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        JSONArray jsonMelds = new JSONArray();
        for (int i = 0; i < mMelds.size(); i++){
            jsonMelds.put(mMelds.get(i).toJson());
        }
        json.put(KEY_MELDS, jsonMelds);
        JSONArray jsonTiles = new JSONArray();
        for (int i = 0; i < mTiles.size(); i++){
            jsonTiles.put(mTiles.get(i).toJson());
        }
        json.put(KEY_TILES, jsonTiles);
//        json.put(KEY_DRAWN_TILE, mDrawnTile == null ? null : mDrawnTile.toJson());
        return json;
    }

    public void addTile(Tile tile){
        if (mTiles.size() < HAND_SIZE){
            Log.d(TAG, "Tile " + tile + " added");
            mTiles.add(tile);
            sortHand();
        }
        else {
            throw new IllegalStateException("Hand is full, cannot add a tile.");
        }
    }

    public void setTilesVisibility(){
        if (mPlayer instanceof AiPlayer){
            for (Tile tile: mTiles){
                tile.isReversed = tile.isOpaque;
            }
        }
    }

    public boolean getIsOpen(){
        return mIsOpen;
    }

    public Tile getTile(int i){
        return mTiles.get(i);
    }

    public Tile getTileById(int id){
        for (Tile tile: mTiles){
            if (tile.getId() == id){
                return tile;
            }
        }
        throw new IllegalArgumentException("Trying to get tile that isn't in hand");
    }

    public ArrayList<Tile> getAllTilesById(int id){
        ArrayList<Tile> t = new ArrayList<>();
        for (Tile tile: mTiles){
            if (tile.getId() == id){
                t.add(tile);
            }
        }
        return t;
    }

    public boolean containsTileById(int id){
        for (Tile tile: mTiles){
            if (tile.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void discardTile(Tile tile){
        if (mTiles.contains(tile)){
            mTiles.remove(tile);
            if (mDrawnTile != null){
                addTile(mDrawnTile);
            }
        }
        else if (tile == mDrawnTile) {
            Log.d(TAG, "discarded drawn tile");
        }
        else {
            throw new IllegalArgumentException("Trying to discard tile you don't have");
        }
        mDrawnTile = null;
    }

    public List<Tile> getTiles() {
        return mTiles;
    }

    public List<Meld> getmMelds(){
        return mMelds;
    }

    public void setDrawnTile(Tile tile){
        Log.d(TAG, "drawn tile set to " + tile);
        mDrawnTile = tile;
    }

    public Tile getDrawnTile(){
        return mDrawnTile;
    }

    public void sortHand(){
        Collections.sort(mTiles);
    }

    public void makeChii(Tile a, Tile b, Tile c){
        if (a.getSuit() != b.getSuit() || b.getSuit() != c.getSuit()){
            throw new IllegalArgumentException("Making chii with mTiles of different suits");
        }
        List<Tile> tiles = Arrays.asList(a, b, c);
        Collections.sort(tiles);
        if (tiles.get(0).getId() == tiles.get(1).getId() - 1 &&
                tiles.get(1).getId() == tiles.get(2).getId() - 1){
            Log.d(TAG, "Chii with " + tiles.get(0) + " " + tiles.get(1) + " " + tiles.get(2));
            Meld meld = new Meld(tiles.get(0), tiles.get(1), tiles.get(2), 0,
                    MELD_TYPE_CHII, this);
            addMeld(meld);
        }
        else {
            throw new IllegalArgumentException("Illegal mTiles for chii");
        }
    }

    public void makePon(Tile a, Tile b, Tile c, int calledDirection){
        if (a.compareTo(b) != 0 || b.compareTo(c) != 0){
            throw new IllegalArgumentException("Illegal mTiles for pon");
        }
        Log.d(TAG, "Pon with " + a + " " + b + " " + c + " by " + mPlayer.getDirection()
                + " from " + calledDirection);
        int rotatedIndex = 0;
        if (calledDirection == (mPlayer.getDirection() + 180) % 360){
           rotatedIndex = 1;
        }
        else if (calledDirection == (mPlayer.getDirection() + 90) % 360){
           rotatedIndex = 2;
        }
        Meld meld = new Meld(a, b, c, rotatedIndex, MELD_TYPE_PON, this);
        addMeld(meld);

    }

    //TODO: maybe split open and closed kans into their own methods
    public void makeKan(Tile a, Tile b, Tile c, Tile d, int calledDirection, boolean isClosed){
        if (a.compareTo(b) != 0 || b.compareTo(c) != 0 || c.compareTo(d) != 0){
            throw new IllegalArgumentException("Illegal mTiles for kan");
        }
        if (isClosed){
            Log.d(TAG, "Closed kan with " + a + " " + b + " " + c + " " + d);
            Meld meld = new Meld(a, b, c, d, calledDirection, MELD_TYPE_SHOUMINKAN);
            addMeld(meld);
        }
        else {
            Log.d(TAG, "Kan with " + a + " " + b + " " + c + " " + d);
            int rotatedIndex = 0;
            if (calledDirection == (mPlayer.getDirection() + 180) % 360){
                rotatedIndex = 1;
            }
            else if (calledDirection == (mPlayer.getDirection() + 90) % 360){
                rotatedIndex = 3;
            }
            Log.d(TAG, "Kan rotated index is " + rotatedIndex);
            Meld meld = new Meld(a, b, c, d, rotatedIndex, MELD_TYPE_KAN);
            addMeld(meld);
        }
    }

    public void makeShouminkan(Tile tile, Meld meld){
        meld.ponToKan(tile);
    }

    public void makeClosedKan(Tile tile){
        ArrayList<Tile> kanTiles = getAllTilesById(tile.getId());
        if (kanTiles.size() != 3){
            throw new IllegalArgumentException("Trying to call closed kan without three mTiles");
        }
        kanTiles.add(tile);
        for (Tile t: kanTiles){
            t.isDiscardable = false;
        }
        kanTiles.get(0).isReversed = true;
        kanTiles.get(3).isReversed = true;
    }

    private void addMeld(Meld meld){
        if (mMelds.size() < 4){
            mMelds.add(meld);
            for (Tile tile: meld.getTiles()){
                mTiles.remove(tile);
            }
            Log.d(TAG, "Meld added");
        }
        else {
            throw new IllegalStateException("Already have four mMelds");
        }
    }

    public void draw(Canvas canvas, int seatDirection, boolean drawDrawnTile){
        drawHand(canvas, seatDirection, drawDrawnTile);
        drawMelds(canvas, seatDirection);
    }

    private void drawHand(Canvas canvas, int seatDirection, boolean drawDrawnTile){
        // draw the ones on the narrow sides of the phone in two rows
        switch (seatDirection){
            case SEAT_DOWN:
            case SEAT_UP:
                //this code draws it in two rows - didn't work that well but might be worth keeping

//                int horCenterPadding = (canvas.getWidth() - (TILE_WIDTH * HAND_BOTTOM_ROW_TILES))/2;
//                if (mTiles.size() > HAND_BOTTOM_ROW_TILES) {
//                    // draw the top row
//                    int topRowTiles = Math.max(HAND_TOP_ROW_TILES - (HAND_SIZE - mTiles.size()), 0);
//                    for (int i = 0; i < topRowTiles; i++) {
//                        if (mTiles.get(i) != null) {
//                            int x = TILE_WIDTH * i + (TILE_WIDTH / 2)
//                                    + horCenterPadding;
//                            int y = canvas.getHeight() - (TILE_HEIGHT * 2);
//                            if (seatDirection == SEAT_UP) {
//                                x = canvas.getWidth() - TILE_WIDTH - x;
//                                y = TILE_HEIGHT;
//                            }
//                            Tile tile = mTiles.get(i);
//                            tile.setLocation(x, y);
//                            tile.draw(canvas, x, y, seatDirection);
//                        }
//                    }
//                    // draw the bottom row
//                    for (int i = topRowTiles; i < mTiles.size(); i++) {
//                        if (mTiles.get(i) != null) {
//                            int x = TILE_WIDTH * (i - topRowTiles)
//                                    + horCenterPadding;
//                            int y = canvas.getHeight() - TILE_HEIGHT;
//                            if (seatDirection == SEAT_UP) {
//                                x = canvas.getWidth() - TILE_WIDTH - x;
//                                y = 0;
//                            }
//                            Tile tile = mTiles.get(i);
//                            tile.setLocation(x, y);
//                            tile.draw(canvas, x, y, seatDirection);
//                        }
//                    }
//                }
//                else {
                    // only draw the bottom row
                int horCenterPadding = (canvas.getWidth() - (TILE_WIDTH * HAND_SIZE)) / 2;
                //TODO: fix bottom and top rows overlapping the first call (shrink mTiles?)
                for (int i = 0; i < mTiles.size(); i++) {
                    if (mTiles.get(i) != null) {
                        int x = TILE_WIDTH * i
                                + horCenterPadding;
                        int y = canvas.getHeight() - TILE_HEIGHT;
                        if (seatDirection == SEAT_UP) {
                            x = canvas.getWidth() - TILE_WIDTH - x;
                            y = 0;
                        }
                        Tile tile = mTiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, x, y, seatDirection);
                    }
                }
//                }

                if (drawDrawnTile && mDrawnTile != null){
//                    int x = TILE_WIDTH * 4 + (TILE_WIDTH / 2)
                    int x = TILE_WIDTH * Math.max((mTiles.size() - 2), 0) + horCenterPadding;
//                    int y = canvas.getHeight() - (TILE_HEIGHT * 2  + TILE_WIDTH);
                    int y = canvas.getHeight() - (TILE_WIDTH + TILE_HEIGHT);
                    if (seatDirection == SEAT_UP){
                        x = canvas.getWidth() - x - TILE_HEIGHT;
                        y = canvas.getHeight() - y - TILE_WIDTH;
                    }
                    mDrawnTile.setLocation(x, y);
                    mDrawnTile.draw(canvas, x, y, (seatDirection + 90) % 360);
                }
                break;
            case SEAT_LEFT:
            case SEAT_RIGHT:
                int verCenterPadding = (canvas.getHeight() - (TILE_WIDTH * HAND_SIZE)) /2;
                for (int i = 0; i < mTiles.size(); i++){
                    if (mTiles.get(i) != null){
                        int x = 0;
                        int y = (TILE_WIDTH * i) + verCenterPadding;
                        if (seatDirection == SEAT_RIGHT){
                            x = canvas.getWidth() - TILE_HEIGHT;
                            y = canvas.getHeight() - TILE_WIDTH - y;
                        }
                        Tile tile = mTiles.get(i);
                        tile.setLocation(x, y);
                        tile.draw(canvas, x, y, seatDirection);
                    }
                }
                if (drawDrawnTile && mDrawnTile != null){
                    int x = TILE_HEIGHT;
                    int y = (TILE_WIDTH * 11) + verCenterPadding;
                    if (seatDirection == SEAT_RIGHT){
                        x = canvas.getWidth() - x - TILE_HEIGHT;
                        y = canvas.getHeight() - y - TILE_WIDTH;
                    }
                    mDrawnTile.draw(canvas, x, y, (seatDirection + 90) % 360);
                }
                break;
            default:
                break;
        }

    }

    private void drawMelds(Canvas canvas, int seatDirection){
        for (int i = 0; i < mMelds.size(); i++){
            mMelds.get(i).draw(canvas, seatDirection, i);
        }
    }

}
