package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jordanagreen.washizu.Constants.UNKNOWN;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Tile implements Comparable<Tile>{

    public static final String TAG = "tile";

    public static final String KEY_ID = "id";
    public static final String KEY_IS_OPAQUE = "is_opaque";
    public static final String KEY_IS_REVERSED = "is_reversed";

    //just for printing things out - using an emum makes using id as an index too long to write
    public static final String[] NAMES = new String[]{
            "MAN_1", "MAN_2", "MAN_3", "MAN_4", "MAN_5", "MAN_6", "MAN_7", "MAN_8", "MAN_9",
            "PIN_1", "PIN_2", "PIN_3", "PIN_4", "PIN_5", "PIN_6", "PIN_7", "PIN_8", "PIN_9",
            "SOU_1", "SOU_2", "SOU_3", "SOU_4", "SOU_5", "SOU_6", "SOU_7", "SOU_8", "SOU_9",
            "CHUN", "HAKU", "HATSU",
            "NAN", "PEI", "XIA", "TON" };

    boolean isOpaque;

    public static final String KEY_IS_DISCARDABLE = "is_discardable";

    private int id;
    private static Bitmap[] tileImages;

    private static Bitmap[] smallTileImages;
    boolean isReversed;
    boolean isDiscardable;

    public int x;
    public int y;

    public Tile(int id, boolean isOpaque){
        if (id >= Constants.TILE_MIN_ID && id <= Constants.TILE_MAX_ID){
            this.id = id;
            this.isOpaque = isOpaque;
            this.isDiscardable = true;
        }
        else {
            throw new IllegalArgumentException("Invalid tile id: " + id);
        }
    }

    public Tile(int id){
        this(id, true);
    }

    public Tile(JSONObject json) throws JSONException{
        this.id = json.getInt(KEY_ID);
        this.isOpaque = json.getBoolean(KEY_IS_OPAQUE);
        this.isReversed = json.getBoolean(KEY_IS_REVERSED);
        this.isDiscardable = json.getBoolean(KEY_IS_DISCARDABLE);
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_ID, id);
        json.put(KEY_IS_OPAQUE, isOpaque);
        json.put(KEY_IS_REVERSED, isReversed);
        json.put(KEY_IS_DISCARDABLE, isDiscardable);
        return json;
    }

    public static void setTileImages(Bitmap[] images){
        tileImages = images;
    }

    public static void setSmallTileImages(Bitmap[] images){
        smallTileImages = images;
    }

//    public int getSuit(){
//        return id / 9;
//    }

    public Suit getSuit(){
        return Suit.values()[id / 9];
    }

    public int getNumericalValue(){
        return getSuit() == Suit.HONOR ? -1 : (id % 9) + 1;
    }

    public static boolean areSameSuit(int a, int b){
        return (a/9) == (b/9);
    }

    public static boolean areSameSuit(int a, int b, int c){
        return (a/9) == (b/9) && (b/9) == (c/9);
    }

    public boolean isTerminal(){
        return getNumericalValue() == 1 || getNumericalValue() == 9;
    }

    public void draw(Canvas canvas, int x, int y, int angle){
        int drawId = isReversed ? UNKNOWN : id;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // for some reason just applying the rotated matrix to the canvas doesn't work?
        Bitmap result = Bitmap.createBitmap(tileImages[drawId], 0, 0, tileImages[drawId].getWidth(),
                tileImages[drawId].getHeight(), matrix, false);
        canvas.drawBitmap(result, x, y, null);
    }

    public void drawSmall(Canvas canvas, int x, int y, int angle){
        int drawId = isReversed ? UNKNOWN : id;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // for some reason just applying the rotated matrix to the canvas doesn't work?
        Bitmap result = Bitmap.createBitmap(smallTileImages[drawId], 0, 0,
                smallTileImages[drawId].getWidth(), smallTileImages[drawId].getHeight(), matrix,
                false);
        canvas.drawBitmap(result, x, y, null);
    }

    //is this necessary?
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public boolean onTouch(MotionEvent event){
        Log.d(TAG, this + " touched");
        return true;
    }

    public String toString(){
//        return id + " (" + x + ", " + y + ")";
        return NAMES[id];
    }

    @Override
    public int compareTo(Tile another) {
        return this.id - another.id;
    }
}
