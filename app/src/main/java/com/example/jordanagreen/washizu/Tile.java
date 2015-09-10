package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;




/**
 * Created by Jordan on 9/9/2015.
 */
public class Tile {

    public static final String TAG = "tile";

    private int id;
    private Bitmap bmp;

    public Tile(int id){
        this.id = id;
    }

    public void draw(Canvas canvas, Bitmap[] tileImages, int x, int y){
        Log.d(TAG, "tile draw: id = " + id);
        canvas.drawBitmap(tileImages[id], x, y, null);
    }
}
