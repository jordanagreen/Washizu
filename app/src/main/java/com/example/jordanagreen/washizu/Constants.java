package com.example.jordanagreen.washizu;

import android.content.res.Resources;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Constants {

    public static final int NUMBER_OF_PLAYERS = 4;

    public static final int HAND_SIZE = 13;
    public static final int STARTING_SCORE = 2500;

    public static final int TILE_WIDTH_PX = 27;
    public static final int TILE_HEIGHT_PX = 39;

    public static final int TILE_SMALL_WIDTH_PX = 20;
    public static final int TILE_SMALL_HEIGHT_PX = 29;

    public static final int TILE_WIDTH = convertToDp(TILE_WIDTH_PX);
    public static final int TILE_HEIGHT = convertToDp(TILE_HEIGHT_PX);
    public static final int TILE_SMALL_WIDTH = convertToDp(TILE_SMALL_WIDTH_PX);
    public static final int TILE_SMALL_HEIGHT = convertToDp(TILE_SMALL_HEIGHT_PX);

    public static final int HAND_TOP_ROW_TILES = 6;
    public static final int HAND_BOTTOM_ROW_TILES = 7;

    public static final int DISCARD_ROW_TILES = 6;
    public static final int DISCARD_NUM_ROWS = 4;
    public static final int DISCARD_MAX_TILES = DISCARD_ROW_TILES * DISCARD_NUM_ROWS;
    public static final int DISCARD_SIDE_ROW_TILES = 12;
    public static final int DISCARD_SIDE_NUM_ROWS = 2;
    public static final int DISCARD_SIDE_MAX_TILES = DISCARD_SIDE_ROW_TILES * DISCARD_SIDE_NUM_ROWS;

    //not worth putting in an enum, too many cases where the number is needed and ordinal is a pain
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
    public static final int NAN = 30; //south
    public static final int PEI = 31; //north
    public static final int XIA = 32; //west
    public static final int TON = 33; //east

    public static final int TILE_MIN_ID = MAN_1;
    public static final int TILE_MAX_ID = TON;
    public static final int UNKNOWN = TON + 1;
    public static final int TOTAL_TILE_IMAGES = UNKNOWN + 1;

    public static final int ROUND_EAST_1 = 0;
    public static final int ROUND_EAST_2 = 1;
    public static final int ROUND_EAST_3 = 2;
    public static final int ROUND_EAST_4 = 3;
    public static final int ROUND_SOUTH_1 = 4;
    public static final int ROUND_SOUTH_2 = 5;
    public static final int ROUND_SOUTH_3 = 6;
    public static final int ROUND_SOUTH_4 = 7;

    public static final int NUM_ROUNDS = ROUND_SOUTH_4;
    public static final int ROUNDS_PER_WIND = 4;

    public static final int DELAY_BETWEEN_TURNS_MS = 300;

    public static int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }
}
