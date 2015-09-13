package com.example.jordanagreen.washizu;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Jordan on 9/13/2015.
 */
public class Player {

    public Hand hand;
    public int score;
    public int wind;
    public int direction;

    public Player(int direction){
        this.direction = direction;
        this.hand = new Hand(direction);
        this.score = Constants.STARTING_SCORE;
    }

    public void draw(Canvas canvas, Bitmap[] tileImages){
        hand.draw(canvas, tileImages);
    }

}
