package com.example.jordanagreen.washizu;

import android.content.res.Resources;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Constants {


    enum Wind {EAST, SOUTH, WEST, NORTH};
    enum Direction {DOWN, RIGHT, UP, LEFT};
    enum Suit {MAN, PIN, SOU, HONOR};


//    helps for rotating tiles
    public static final int SEAT_DOWN = 0;
    public static final int SEAT_LEFT = 90;
    public static final int SEAT_UP = 180;
    public static final int SEAT_RIGHT = 270;

    public static final int HAND_SIZE = 13;

    public static final int TILE_WIDTH_PX = 44;
    public static final int TILE_HEIGHT_PX = 53;
    public static final int TILE_WIDTH = convertToDp(TILE_WIDTH_PX);
    public static final int TILE_HEIGHT = convertToDp(TILE_HEIGHT_PX);

    public static final int HAND_TOP_ROW_TILES = 6;
    public static final int HAND_BOTTOM_ROW_TILES = 7;

    public static final int MAN_1 = 0;
    public static final int MAN_2 = 1;
    public static final int MAN_3 = 2;
    public static final int MAN_4 = 3;
    public static final int MAN_5 = 4;
    public static final int MAN_6 = 5;
    public static final int MAN_7 = 6;
    public static final int MAN_8 = 7;
    public static final int MAN_9 = 8;
    public static final int PIN_1 = 9;
    public static final int PIN_2 = 10;
    public static final int PIN_3 = 11;
    public static final int PIN_4 = 12;
    public static final int PIN_5 = 13;
    public static final int PIN_6 = 14;
    public static final int PIN_7 = 15;
    public static final int PIN_8 = 16;
    public static final int PIN_9 = 17;
    public static final int SOU_1 = 18;
    public static final int SOU_2 = 19;
    public static final int SOU_3 = 20;
    public static final int SOU_4 = 21;
    public static final int SOU_5 = 22;
    public static final int SOU_6 = 23;
    public static final int SOU_7 = 24;
    public static final int SOU_8 = 25;
    public static final int SOU_9 = 26;
    public static final int CHUN = 27;
    public static final int HAKU = 28;
    public static final int HATSU = 29;
    public static final int NAN = 30;
    public static final int PEI = 31;
    public static final int XIA = 32;
    public static final int TON = 33;

    public static final int TILE_MIN_ID = MAN_1;
    public static final int TILE_MAX_ID = TON;


    public static int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }

}
