package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by Jordan on 9/9/2015.
 */
public class Tile implements Comparable<Tile>{

    public static final String TAG = "tile";

    private int id;

    private static Bitmap[] tileImages;
    private static Bitmap[] smallTileImages;

    boolean isOpaque;
    boolean isRed;

    public int x;
    public int y;

    public Tile(int id, boolean isOpaque){
        if (id >= Constants.TILE_MIN_ID && id <= Constants.TILE_MAX_ID){
            this.id = id;
            this.isOpaque = isOpaque;
        }
        else {
            throw new IllegalArgumentException("Invalid tile id: " + id);
        }
    }

    public static void setTileImages(Bitmap[] images){
        tileImages = images;
    }

    public static void setSmallTileImages(Bitmap[] images){
        smallTileImages = images;
    }

    public int getSuit(){
        return id / 9;
    }

    public void draw(Canvas canvas, int x, int y, int seatDirection){
        Matrix matrix = new Matrix();
        matrix.postRotate(seatDirection);

        // for some reason just applying the rotated matrix to the canvas doesn't work?
        Bitmap result = Bitmap.createBitmap(tileImages[id], 0, 0, tileImages[id].getWidth(),
                tileImages[id].getHeight(), matrix, false);
        canvas.drawBitmap(result, x, y, null);
    }

    public void drawSmall(Canvas canvas, int x, int y, int seatDirection){
        Matrix matrix = new Matrix();
        matrix.postRotate(seatDirection);

        // for some reason just applying the rotated matrix to the canvas doesn't work?
        Bitmap result = Bitmap.createBitmap(smallTileImages[id], 0, 0,
                smallTileImages[id].getWidth(), smallTileImages[id].getHeight(), matrix, false);
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
        return id + " (" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Tile another) {
        return this.id - another.id;
    }
}
