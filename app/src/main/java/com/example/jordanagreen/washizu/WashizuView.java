package com.example.jordanagreen.washizu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Jordan on 9/9/2015.
 */
public class WashizuView extends SurfaceView implements SurfaceHolder.Callback {


    public static final String TAG = "WashizuView";

    private Bitmap[] tileImages;
    private Bitmap[] smallTileImages;

    private Game game;

    public WashizuView(Context context, AttributeSet attrs){
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);
        setWillNotDraw(false);
        Tile.setTileImages(loadTileImages());
        Tile.setSmallTileImages(loadSmallTileImages());

        game = new Game();
        game.startRound(Constants.ROUND_EAST_1);
    }

    private Bitmap[] loadTileImages(){
        Bitmap[] bmps = new Bitmap[34];
        bmps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.man_1);
        bmps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.man_2);
        bmps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.man_3);
        bmps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.man_4);
        bmps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.man_5);
        bmps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.man_6);
        bmps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.man_7);
        bmps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.man_8);
        bmps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.man_9);
        bmps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_1);
        bmps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_2);
        bmps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_3);
        bmps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_4);
        bmps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_5);
        bmps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_6);
        bmps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_7);
        bmps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_8);
        bmps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_9);
        bmps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_1);
        bmps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_2);
        bmps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_3);
        bmps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_4);
        bmps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_5);
        bmps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_6);
        bmps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_7);
        bmps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_8);
        bmps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_9);
        bmps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.chun);
        bmps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.haku);
        bmps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.hatsu);
        bmps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.nan);
        bmps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pei);
        bmps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.xia);
        bmps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.ton);
        return bmps;
    }

    private Bitmap[] loadSmallTileImages(){
        Bitmap[] smallBmps = new Bitmap[34];
        smallBmps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.man_1_small);
        smallBmps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.man_2_small);
        smallBmps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.man_3_small);
        smallBmps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.man_4_small);
        smallBmps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.man_5_small);
        smallBmps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.man_6_small);
        smallBmps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.man_7_small);
        smallBmps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.man_8_small);
        smallBmps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.man_9_small);
        smallBmps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_1_small);
        smallBmps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_2_small);
        smallBmps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_3_small);
        smallBmps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_4_small);
        smallBmps[13] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_5_small);
        smallBmps[14] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_6_small);
        smallBmps[15] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_7_small);
        smallBmps[16] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_8_small);
        smallBmps[17] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_9_small);
        smallBmps[18] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_1_small);
        smallBmps[19] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_2_small);
        smallBmps[20] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_3_small);
        smallBmps[21] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_4_small);
        smallBmps[22] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_5_small);
        smallBmps[23] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_6_small);
        smallBmps[24] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_7_small);
        smallBmps[25] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_8_small);
        smallBmps[26] = BitmapFactory.decodeResource(getResources(), R.drawable.sou_9_small);
        smallBmps[27] = BitmapFactory.decodeResource(getResources(), R.drawable.chun_small);
        smallBmps[28] = BitmapFactory.decodeResource(getResources(), R.drawable.haku_small);
        smallBmps[29] = BitmapFactory.decodeResource(getResources(), R.drawable.hatsu_small);
        smallBmps[30] = BitmapFactory.decodeResource(getResources(), R.drawable.nan_small);
        smallBmps[31] = BitmapFactory.decodeResource(getResources(), R.drawable.pei_small);
        smallBmps[32] = BitmapFactory.decodeResource(getResources(), R.drawable.xia_small);
        smallBmps[33] = BitmapFactory.decodeResource(getResources(), R.drawable.ton_small);
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
        //TODO: figure out a way to remove the images from the call
        game.onDraw(canvas);
        postInvalidate();
    }

}
