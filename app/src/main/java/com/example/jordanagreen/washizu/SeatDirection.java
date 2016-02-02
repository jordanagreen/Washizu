package com.example.jordanagreen.washizu;

/**
 * Created by Jordan on 2/1/2016.
 */
public enum SeatDirection {
    //helps for rotating tiles - note that this is the reverse of turn order (i.e. clockwise)
    DOWN(0), LEFT(90), UP(180), RIGHT(270);

    private int angle;

    SeatDirection(int angle){
        this.angle = angle;
    }

    int getAngle(){
        return angle;
    }

    SeatDirection addOffset(int offset){
        return values()[((angle + offset)%360)/90];
    }
}
