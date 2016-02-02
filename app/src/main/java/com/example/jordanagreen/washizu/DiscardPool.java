package com.example.jordanagreen.washizu;

import android.graphics.Canvas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.jordanagreen.washizu.Constants.DISCARD_MAX_TILES;
import static com.example.jordanagreen.washizu.Constants.DISCARD_NUM_ROWS;
import static com.example.jordanagreen.washizu.Constants.DISCARD_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.DISCARD_SIDE_ROW_TILES;
import static com.example.jordanagreen.washizu.Constants.TILE_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_HEIGHT;
import static com.example.jordanagreen.washizu.Constants.TILE_SMALL_WIDTH;

/**
 * Created by Jordan on 9/17/2015.
 */
public class DiscardPool {

    public static final String TAG = "DiscardPool";

    public static final String KEY_TILES = "tiles";
    public static final String KEY_RIICHI_INDEX = "riichi_index";

    private ArrayList<Tile> tiles;
    private int riichiIndex;

    public DiscardPool(){
        this.tiles = new ArrayList<>();
        this.riichiIndex = -1;
    }

    public DiscardPool(JSONObject json) throws JSONException {
        this.tiles = new ArrayList<>();
        JSONArray jsonTiles = json.getJSONArray(KEY_TILES);
        for (int i = 0; i < jsonTiles.length(); i++){
            tiles.add(new Tile(jsonTiles.getJSONObject(i)));
        }
        this.riichiIndex = json.getInt(KEY_RIICHI_INDEX);
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        JSONArray jsonTiles = new JSONArray();
        for (int i = 0; i < tiles.size(); i++){
            jsonTiles.put(tiles.get(i).toJson());
        }
        json.put(KEY_TILES, jsonTiles);
        json.put(KEY_RIICHI_INDEX, riichiIndex);
        return json;
    }

    public ArrayList<Tile> getTiles(){
        return tiles;
    }

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

    public Tile getLastTile() {
        return tiles.get(tiles.size() - 1);
    }

    public Tile removeLastTile(){
        return tiles.remove(tiles.size() - 1);
    }

    public int getSize(){ return tiles.size(); }

    public void draw(Canvas canvas, SeatDirection direction){
        switch (direction){
            case DOWN:
            case UP:

                int horCenterPadding = (canvas.getWidth() -
                        (TILE_SMALL_WIDTH * DISCARD_ROW_TILES)) /2;

                for (int i = 0; i < tiles.size(); i++){
                    //TODO: handle riichi sideways tile
                    int x = (i % DISCARD_ROW_TILES) * TILE_SMALL_WIDTH + horCenterPadding;
                    //TODO: make it a bit smaller if it gets to a fourth row?
                    int y = canvas.getHeight() - ((TILE_HEIGHT * 2) + (TILE_SMALL_HEIGHT *
                        (DISCARD_NUM_ROWS - (int)(Math.floor(i/DISCARD_ROW_TILES))))
                        + TILE_SMALL_HEIGHT/2);
                    if (direction == SeatDirection.UP){
                        x = canvas.getWidth() - TILE_SMALL_WIDTH - x;
                        y = canvas.getHeight() - TILE_SMALL_HEIGHT - y;
                    }
                    tiles.get(i).drawSmall(canvas, x, y, direction.getAngle());
//                    Log.d(TAG, "Drawing discarded tile " + tiles.get(i) + "at " + x + ", " + y);
                }
                break;
            case LEFT:
            case RIGHT:
                int verCenterPadding = (canvas.getHeight() -
                        (TILE_SMALL_WIDTH * DISCARD_SIDE_ROW_TILES)) / 2;
                for (int i = 0; i < tiles.size(); i++){
                        int x = TILE_HEIGHT + (TILE_SMALL_HEIGHT *
                            (1 - (int) (Math.ceil(i/DISCARD_SIDE_ROW_TILES))));
                        int y = (i % DISCARD_SIDE_ROW_TILES) * TILE_SMALL_WIDTH + verCenterPadding;
                    if (direction == SeatDirection.RIGHT){
                        x = canvas.getWidth() - TILE_SMALL_HEIGHT - x;
                        y = canvas.getHeight() - TILE_SMALL_WIDTH - y;
                    }
                        tiles.get(i).drawSmall(canvas, x, y, direction.getAngle());
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal direction: " + direction);
        }
    }

}
