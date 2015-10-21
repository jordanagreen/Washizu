package com.example.jordanagreen.washizu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.HAKU;
import static com.example.jordanagreen.washizu.Constants.HATSU;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_2;
import static com.example.jordanagreen.washizu.Constants.MAN_3;
import static com.example.jordanagreen.washizu.Constants.MAN_4;
import static com.example.jordanagreen.washizu.Constants.MAN_5;
import static com.example.jordanagreen.washizu.Constants.MAN_6;
import static com.example.jordanagreen.washizu.Constants.MAN_7;
import static com.example.jordanagreen.washizu.Constants.MAN_8;
import static com.example.jordanagreen.washizu.Constants.MAN_9;
import static com.example.jordanagreen.washizu.Constants.NAN;
import static com.example.jordanagreen.washizu.Constants.PEI;
import static com.example.jordanagreen.washizu.Constants.PIN_1;
import static com.example.jordanagreen.washizu.Constants.PIN_2;
import static com.example.jordanagreen.washizu.Constants.PIN_3;
import static com.example.jordanagreen.washizu.Constants.PIN_4;
import static com.example.jordanagreen.washizu.Constants.PIN_5;
import static com.example.jordanagreen.washizu.Constants.PIN_6;
import static com.example.jordanagreen.washizu.Constants.PIN_7;
import static com.example.jordanagreen.washizu.Constants.PIN_8;
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_2;
import static com.example.jordanagreen.washizu.Constants.SOU_3;
import static com.example.jordanagreen.washizu.Constants.SOU_4;
import static com.example.jordanagreen.washizu.Constants.SOU_5;
import static com.example.jordanagreen.washizu.Constants.SOU_6;
import static com.example.jordanagreen.washizu.Constants.SOU_7;
import static com.example.jordanagreen.washizu.Constants.SOU_8;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.TOTAL_TILE_IMAGES;
import static com.example.jordanagreen.washizu.Constants.UNKNOWN;
import static com.example.jordanagreen.washizu.Constants.XIA;

/**
 * Created by Jordan on 9/9/2015.
 */
public class WashizuView extends SurfaceView implements SurfaceHolder.Callback {

    //TODO: move game logic into a new thread

    public static final String TAG = "WashizuView";

    private Game game;

    public WashizuView(Context context, AttributeSet attrs){
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        setWillNotDraw(false);
        Tile.setTileImages(loadTileImages());
        Tile.setSmallTileImages(loadSmallTileImages());

        Log.d(TAG, "constructor called");
//        game = new Game();
//        game.startGame();
    }

    private Bitmap[] loadTileImages(){
        Resources resources = getResources();
        Bitmap[] bmps = new Bitmap[TOTAL_TILE_IMAGES];
        bmps[MAN_1] = BitmapFactory.decodeResource(resources, R.drawable.man_1);
        bmps[MAN_2] = BitmapFactory.decodeResource(resources, R.drawable.man_2);
        bmps[MAN_3] = BitmapFactory.decodeResource(resources, R.drawable.man_3);
        bmps[MAN_4] = BitmapFactory.decodeResource(resources, R.drawable.man_4);
        bmps[MAN_5] = BitmapFactory.decodeResource(resources, R.drawable.man_5);
        bmps[MAN_6] = BitmapFactory.decodeResource(resources, R.drawable.man_6);
        bmps[MAN_7] = BitmapFactory.decodeResource(resources, R.drawable.man_7);
        bmps[MAN_8] = BitmapFactory.decodeResource(resources, R.drawable.man_8);
        bmps[MAN_9] = BitmapFactory.decodeResource(resources, R.drawable.man_9);
        bmps[PIN_1] = BitmapFactory.decodeResource(resources, R.drawable.pin_1);
        bmps[PIN_2] = BitmapFactory.decodeResource(resources, R.drawable.pin_2);
        bmps[PIN_3] = BitmapFactory.decodeResource(resources, R.drawable.pin_3);
        bmps[PIN_4] = BitmapFactory.decodeResource(resources, R.drawable.pin_4);
        bmps[PIN_5] = BitmapFactory.decodeResource(resources, R.drawable.pin_5);
        bmps[PIN_6] = BitmapFactory.decodeResource(resources, R.drawable.pin_6);
        bmps[PIN_7] = BitmapFactory.decodeResource(resources, R.drawable.pin_7);
        bmps[PIN_8] = BitmapFactory.decodeResource(resources, R.drawable.pin_8);
        bmps[PIN_9] = BitmapFactory.decodeResource(resources, R.drawable.pin_9);
        bmps[SOU_1] = BitmapFactory.decodeResource(resources, R.drawable.sou_1);
        bmps[SOU_2] = BitmapFactory.decodeResource(resources, R.drawable.sou_2);
        bmps[SOU_3] = BitmapFactory.decodeResource(resources, R.drawable.sou_3);
        bmps[SOU_4] = BitmapFactory.decodeResource(resources, R.drawable.sou_4);
        bmps[SOU_5] = BitmapFactory.decodeResource(resources, R.drawable.sou_5);
        bmps[SOU_6] = BitmapFactory.decodeResource(resources, R.drawable.sou_6);
        bmps[SOU_7] = BitmapFactory.decodeResource(resources, R.drawable.sou_7);
        bmps[SOU_8] = BitmapFactory.decodeResource(resources, R.drawable.sou_8);
        bmps[SOU_9] = BitmapFactory.decodeResource(resources, R.drawable.sou_9);
        bmps[CHUN] = BitmapFactory.decodeResource(resources, R.drawable.chun);
        bmps[HAKU] = BitmapFactory.decodeResource(resources, R.drawable.haku);
        bmps[HATSU] = BitmapFactory.decodeResource(resources, R.drawable.hatsu);
        bmps[NAN] = BitmapFactory.decodeResource(resources, R.drawable.nan);
        bmps[PEI] = BitmapFactory.decodeResource(resources, R.drawable.pei);
        bmps[XIA] = BitmapFactory.decodeResource(resources, R.drawable.xia);
        bmps[TON] = BitmapFactory.decodeResource(resources, R.drawable.ton);
        bmps[UNKNOWN] = BitmapFactory.decodeResource(resources, R.drawable.unknown);
        return bmps;
    }

    private Bitmap[] loadSmallTileImages(){
        Resources resources = getResources();
        Bitmap[] smallBmps = new Bitmap[TOTAL_TILE_IMAGES];
        smallBmps[MAN_1] = BitmapFactory.decodeResource(resources, R.drawable.man_1_small);
        smallBmps[MAN_2] = BitmapFactory.decodeResource(resources, R.drawable.man_2_small);
        smallBmps[MAN_3] = BitmapFactory.decodeResource(resources, R.drawable.man_3_small);
        smallBmps[MAN_4] = BitmapFactory.decodeResource(resources, R.drawable.man_4_small);
        smallBmps[MAN_5] = BitmapFactory.decodeResource(resources, R.drawable.man_5_small);
        smallBmps[MAN_6] = BitmapFactory.decodeResource(resources, R.drawable.man_6_small);
        smallBmps[MAN_7] = BitmapFactory.decodeResource(resources, R.drawable.man_7_small);
        smallBmps[MAN_8] = BitmapFactory.decodeResource(resources, R.drawable.man_8_small);
        smallBmps[MAN_9] = BitmapFactory.decodeResource(resources, R.drawable.man_9_small);
        smallBmps[PIN_1] = BitmapFactory.decodeResource(resources, R.drawable.pin_1_small);
        smallBmps[PIN_2] = BitmapFactory.decodeResource(resources, R.drawable.pin_2_small);
        smallBmps[PIN_3] = BitmapFactory.decodeResource(resources, R.drawable.pin_3_small);
        smallBmps[PIN_4] = BitmapFactory.decodeResource(resources, R.drawable.pin_4_small);
        smallBmps[PIN_5] = BitmapFactory.decodeResource(resources, R.drawable.pin_5_small);
        smallBmps[PIN_6] = BitmapFactory.decodeResource(resources, R.drawable.pin_6_small);
        smallBmps[PIN_7] = BitmapFactory.decodeResource(resources, R.drawable.pin_7_small);
        smallBmps[PIN_8] = BitmapFactory.decodeResource(resources, R.drawable.pin_8_small);
        smallBmps[PIN_9] = BitmapFactory.decodeResource(resources, R.drawable.pin_9_small);
        smallBmps[SOU_1] = BitmapFactory.decodeResource(resources, R.drawable.sou_1_small);
        smallBmps[SOU_2] = BitmapFactory.decodeResource(resources, R.drawable.sou_2_small);
        smallBmps[SOU_3] = BitmapFactory.decodeResource(resources, R.drawable.sou_3_small);
        smallBmps[SOU_4] = BitmapFactory.decodeResource(resources, R.drawable.sou_4_small);
        smallBmps[SOU_5] = BitmapFactory.decodeResource(resources, R.drawable.sou_5_small);
        smallBmps[SOU_6] = BitmapFactory.decodeResource(resources, R.drawable.sou_6_small);
        smallBmps[SOU_7] = BitmapFactory.decodeResource(resources, R.drawable.sou_7_small);
        smallBmps[SOU_8] = BitmapFactory.decodeResource(resources, R.drawable.sou_8_small);
        smallBmps[SOU_9] = BitmapFactory.decodeResource(resources, R.drawable.sou_9_small);
        smallBmps[CHUN] = BitmapFactory.decodeResource(resources, R.drawable.chun_small);
        smallBmps[HAKU] = BitmapFactory.decodeResource(resources, R.drawable.haku_small);
        smallBmps[HATSU] = BitmapFactory.decodeResource(resources, R.drawable.hatsu_small);
        smallBmps[NAN] = BitmapFactory.decodeResource(resources, R.drawable.nan_small);
        smallBmps[PEI] = BitmapFactory.decodeResource(resources, R.drawable.pei_small);
        smallBmps[XIA] = BitmapFactory.decodeResource(resources, R.drawable.xia_small);
        smallBmps[TON] = BitmapFactory.decodeResource(resources, R.drawable.ton_small);
        smallBmps[UNKNOWN] = BitmapFactory.decodeResource(resources, R.drawable.unknown_small);
        return smallBmps;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        thread.setRunning(true);
//        thread.run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
//        boolean retry = true;
//        while (retry){
//            try {
//                thread.join();
//                retry = false;
//            }
//            catch (InterruptedException e){
//                Log.d(TAG, "Shutting down thread");
//                // try again shutting down the thread
//            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        game.onTouch(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        game.onDraw(canvas);
        postInvalidate();
    }

    public String getGameJsonAsString(){
        try{
            return game.toJson().toString();
        }
        catch (JSONException e){
            Log.e(TAG, "error", e);
            return null;
        }
    }

    public void restoreGameFromJsonString(String jsonString){
        Log.d(TAG, "restoring from string");
        Log.d(TAG, jsonString);
        try {
            JSONObject json = new JSONObject(jsonString);
            game = new Game(json);
        }
        catch (JSONException e){
            Log.e(TAG, "error", e);
            startNewGame();
        }

    }

    public void startNewGame(){
        Log.d(TAG, "starting new game");
        game = new Game();
        game.startGame();

    }

}
