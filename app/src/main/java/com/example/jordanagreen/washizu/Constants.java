package com.example.jordanagreen.washizu;

import android.content.res.Resources;

/**
 * Created by Jordan on 9/9/2015.
 */
public class Constants {

//    helps for rotating tiles - note that this is the reverse of turn order (i.e. clockwise)
    public static final int SEAT_DOWN = 0;
    public static final int SEAT_LEFT = 90;
    public static final int SEAT_UP = 180;
    public static final int SEAT_RIGHT = 270;

    public static final int WIND_EAST = 0;
    public static final int WIND_SOUTH = 1;
    public static final int WIND_WEST = 2;
    public static final int WIND_NORTH = 3;

    public static final int HAND_SIZE = 13;
    public static final int STARTING_SCORE = 2500;


//    public static final int TILE_WIDTH_PX = 33;
//    public static final int TILE_HEIGHT_PX = 40;

    public static final int TILE_WIDTH_PX = 27;
    public static final int TILE_HEIGHT_PX = 39;

//    public static final int TILE_SMALL_WIDTH_PX = 25;
//    public static final int TILE_SMALL_HEIGHT_PX = 30;

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

    //TODO: see if these can be moved to enums to simplify things - use ordinal() to get the number?
//    public static final int SUIT_MAN = 0;
//    public static final int SUIT_PIN = 1;
//    public static final int SUIT_SOU = 2;
//    public static final int SUIT_HONOR = 3;

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

    public static final int DELAY_BETWEEN_TURNS_MS = 300;

    //would be nice to use an emun for this but json makes that a pain
    public static final int MELD_TYPE_CHII = 0;
    public static final int MELD_TYPE_PON = 1;
    public static final int MELD_TYPE_KAN = 2;
    public static final int MELD_TYPE_SHOUMINKAN = 3;

    public static final int YAKU_RIICHI = 0;
    public static final int YAKU_IPPATSU = 1;
    public static final int YAKU_HAITEI = 2;
    public static final int YAKU_HOUTEI = 3;
    public static final int YAKU_RINSHAN = 4;
    public static final int YAKU_CHANKAN = 5;
    public static final int YAKU_TSUMO = 6;
    public static final int YAKU_PINFU = 7;
    public static final int YAKU_IIPEIKOU = 8;
    public static final int YAKU_SANSHOKU_DOUJUN = 9;
    public static final int YAKU_ITTSUU = 10;
    public static final int YAKU_RYANPEIKOU = 11;
    public static final int YAKU_TOITOI = 12;
    public static final int YAKU_SAN_ANKOU = 13;
    public static final int YAKU_SANSHOUKU_DOUKOU = 14;
    public static final int YAKU_SAN_KANTSU = 15;
    public static final int YAKU_TAN_YAO = 16;
    public static final int YAKU_FANPAI = 17;
    public static final int YAKU_CHANTA = 18;
    public static final int YAKU_JUNCHAN = 19;
    public static final int YAKU_HONROUTOU = 20;
    public static final int YAKU_SHOUSANGEN = 21;
    public static final int YAKU_CHII_TOITSU = 22;
    public static final int YAKU_HON_ITSU = 23;
    public static final int YAKU_CHIN_ITSU = 24;
    public static final int YAKU_KOKUSHI_MUSOU = 25;
    public static final int YAKU_SUU_ANKOU = 26;
    public static final int YAKU_DAISANGEN = 27;
    public static final int YAKU_SHOUSUUSHI = 28;
    public static final int YAKU_DAISUUSHI = 29;
    public static final int YAKU_TSUUIISOU = 30;
    public static final int YAKU_CHINROUTOU = 31;
    public static final int YAKU_RYUUIISOU = 32;
    public static final int YAKU_CHUUREN_POUTOU = 33;
    public static final int YAKU_SUU_KANTSU = 34;
    public static final int YAKU_TENHOU = 35;
    public static final int YAKU_CHIIHOU = 36;
    public static final int YAKU_RENHOU = 37;
    public static final int YAKU_DOUBLE_RIICHI = 38;

    public static final int TOTAL_YAKU = YAKU_DOUBLE_RIICHI;

    //default han for each yaku
    //-1 means you can't get that yaku open
    public static final int OPEN_HAN_RIICHI = -1;
    public static final int OPEN_HAN_IPPATSU = -1;
    public static final int OPEN_HAN_HAITEI = 1;
    public static final int OPEN_HAN_HOUTEI = 1;
    public static final int OPEN_HAN_RINSHAN = 1;
    public static final int OPEN_HAN_CHANKAN = 1;
    public static final int OPEN_HAN_TSUMO = -1;
    public static final int OPEN_HAN_PINFU = -1;
    public static final int OPEN_HAN_IIPEIKOU = -1;
    public static final int OPEN_HAN_SANSHOKU_DOUJUN = 1;
    public static final int OPEN_HAN_ITTSUU = 1;
    public static final int OPEN_HAN_RYANPEIKOU = -1;
    public static final int OPEN_HAN_TOITOI = -1;
    public static final int OPEN_HAN_SAN_ANKOU = 2;
    public static final int OPEN_HAN_SANSHOUKU_DOUKOU = 2;
    public static final int OPEN_HAN_SAN_KANTSU = 2;
    public static final int OPEN_HAN_TAN_YAO = 1;
    public static final int OPEN_HAN_FANPAI = 1;
    public static final int OPEN_HAN_CHANTA = 1;
    public static final int OPEN_HAN_JUNCHAN = 2;
    public static final int OPEN_HAN_HONROUTOU = 2;
    public static final int OPEN_HAN_SHOUSANGEN = 2;
    public static final int OPEN_HAN_CHII_TOITSU = -1;
    public static final int OPEN_HAN_HON_ITSU = 2;
    public static final int OPEN_HAN_CHIN_ITSU = 5;
    public static final int OPEN_HAN_KOKUSHI_MUSOU = -1;
    public static final int OPEN_HAN_SUU_ANKOU = -1;
    public static final int OPEN_HAN_DAISANGEN = 13;
    public static final int OPEN_HAN_SHOUSUUSHI = 13;
    public static final int OPEN_HAN_DAISUUSHI = 13;
    public static final int OPEN_HAN_TSUUIISOU = 13;
    public static final int OPEN_HAN_CHINROUTOU = 13;
    public static final int OPEN_HAN_RYUUIISOU = 13;
    public static final int OPEN_HAN_CHUUREN_POUTOU = -1;
    public static final int OPEN_HAN_SUU_KANTSU = 13;
    public static final int OPEN_HAN_TENHOU = -1;
    public static final int OPEN_HAN_CHIIHOU = -1;
    public static final int OPEN_HAN_RENHOU = -1;
    public static final int OPEN_HAN_DOUBLE_RIICHI = -1;

    public static final int CLOSED_HAN_RIICHI = 1;
    public static final int CLOSED_HAN_IPPATSU = 1;
    public static final int CLOSED_HAN_HAITEI = 1;
    public static final int CLOSED_HAN_HOUTEI = 1;
    public static final int CLOSED_HAN_RINSHAN = 1;
    public static final int CLOSED_HAN_CHANKAN = 1;
    public static final int CLOSED_HAN_TSUMO = 1;
    public static final int CLOSED_HAN_PINFU = 1;
    public static final int CLOSED_HAN_IIPEIKOU = 1;
    public static final int CLOSED_HAN_SANSHOKU_DOUJUN = 2;
    public static final int CLOSED_HAN_ITTSUU = 2;
    public static final int CLOSED_HAN_RYANPEIKOU = 3;
    public static final int CLOSED_HAN_TOITOI = 2;
    public static final int CLOSED_HAN_SAN_ANKOU = 2;
    public static final int CLOSED_HAN_SANSHOUKU_DOUKOU = 2;
    public static final int CLOSED_HAN_SAN_KANTSU = 2;
    public static final int CLOSED_HAN_TAN_YAO = 1;
    public static final int CLOSED_HAN_FANPAI = 1;
    public static final int CLOSED_HAN_CHANTA = 2;
    public static final int CLOSED_HAN_JUNCHAN = 3;
    public static final int CLOSED_HAN_HONROUTOU = 2;
    public static final int CLOSED_HAN_SHOUSANGEN = 2;
    public static final int CLOSED_HAN_CHII_TOITSU = 2;
    public static final int CLOSED_HAN_HON_ITSU = 3;
    public static final int CLOSED_HAN_CHIN_ITSU = 6;
    public static final int CLOSED_HAN_KOKUSHI_MUSOU = 13;
    public static final int CLOSED_HAN_SUU_ANKOU = 13;
    public static final int CLOSED_HAN_DAISANGEN = 13;
    public static final int CLOSED_HAN_SHOUSUUSHI = 13;
    public static final int CLOSED_HAN_DAISUUSHI = 13;
    public static final int CLOSED_HAN_TSUUIISOU = 13;
    public static final int CLOSED_HAN_CHINROUTOU = 13;
    public static final int CLOSED_HAN_RYUUIISOU = 13;
    public static final int CLOSED_HAN_CHUUREN_POUTOU = 13;
    public static final int CLOSED_HAN_SUU_KANTSU = 13;
    public static final int CLOSED_HAN_TENHOU = 13;
    public static final int CLOSED_HAN_CHIIHOU = 13;
    public static final int CLOSED_HAN_RENHOU = 13;
    public static final int CLOSED_HAN_DOUBLE_RIICHI = 2;

    public static int convertToDp(int input) {
        // Get the screen's density scale
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }
}
