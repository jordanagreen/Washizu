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
//    private Bitmap bmp;

    public boolean isOpaque;
    public boolean isRed;

//  x and y of tile in the hand
    public int x;
    public int y;

    public Tile(int id){
        if (id >= Constants.TILE_MIN_ID && id <= Constants.TILE_MAX_ID){
            this.id = id;
        }
        else {
            throw new IllegalArgumentException("Invalid tile id: " + id);
        }
    }

    // TODO: just get images that are the right size already and remove the scale
    public void draw(Canvas canvas, Bitmap[] tileImages, int x, int y, int seatDirection){
        Matrix matrix = new Matrix();
        matrix.postRotate(seatDirection);

        // for some reason just applying the rotated matrix to the canvas doesn't work?
        Bitmap result = Bitmap.createBitmap(tileImages[id], 0, 0, tileImages[id].getWidth(),
                tileImages[id].getHeight(), matrix, false);
        canvas.drawBitmap(result, x, y, null);
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
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
        return another.id - this.id;
    }
}
