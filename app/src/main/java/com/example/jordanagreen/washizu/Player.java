package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Jordan on 9/13/2015.
 */
public class Player {

    //TODO: split into human and AI

    private Hand hand;
    public int score;
    public int wind;
    public int direction;

    public Player(int direction){
        this.direction = direction;
        this.hand = new Hand();
        this.score = Constants.STARTING_SCORE;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public boolean onTouch(MotionEvent event){
        return hand.onTouch(event);
    }

    public void draw(Canvas canvas){
        if (hand != null){
            hand.draw(canvas, direction);
        }
    }

}
