package com.example.jordanagreen.washizu;

import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHANKAN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHANTA;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHIIHOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHINROUTOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHIN_ITSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_DAISANGEN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_DAISUUSHI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_DOUBLE_RIICHI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_FANPAI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_HAITEI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_HONROUTOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_HON_ITSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_HOUTEI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_IIPEIKOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_IPPATSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_ITTSUU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_JUNCHAN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_PINFU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_RENHOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_RIICHI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_RINSHAN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_RYANPEIKOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_RYUUIISOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SANSHOKU_DOUJUN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SANSHOUKU_DOUKOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SAN_ANKOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SAN_KANTSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SHOUSANGEN;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SHOUSUUSHI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SUU_ANKOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_SUU_KANTSU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_TAN_YAO;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_TENHOU;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_TOITOI;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_TSUMO;
import static com.example.jordanagreen.washizu.Constants.OPEN_HAN_TSUUIISOU;
import static com.example.jordanagreen.washizu.Constants.TOTAL_YAKU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_RIICHI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_IPPATSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_HAITEI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_HOUTEI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_RINSHAN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHANKAN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TSUMO;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_PINFU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_IIPEIKOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SANSHOKU_DOUJUN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_ITTSUU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_RYANPEIKOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TOITOI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SAN_ANKOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SANSHOUKU_DOUKOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SAN_KANTSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TAN_YAO;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_FANPAI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHANTA;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_JUNCHAN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_HONROUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SHOUSANGEN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_HON_ITSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHIN_ITSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SUU_ANKOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_DAISANGEN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SHOUSUUSHI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_DAISUUSHI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TSUUIISOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHINROUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_RYUUIISOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_SUU_KANTSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TENHOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHIIHOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_RENHOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_DOUBLE_RIICHI;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Score {

    //han for each yaku the hand has, indexed by yaku
    private int[] han;
    private int fu;
    //default han values for each yaku
    private static int[] closedHan;
    private static int[] openHan;

    Score(){
        han = new int[TOTAL_YAKU];
        if (openHan == null){
            openHan = makeOpenHan();
        }
        if (closedHan == null){
            closedHan = makeClosedHan();
        }
    }

    private int[] makeOpenHan(){
        return new int[]{
                OPEN_HAN_RIICHI,
                OPEN_HAN_IPPATSU,
                OPEN_HAN_HAITEI,
                OPEN_HAN_HOUTEI,
                OPEN_HAN_RINSHAN,
                OPEN_HAN_CHANKAN,
                OPEN_HAN_TSUMO,
                OPEN_HAN_PINFU,
                OPEN_HAN_IIPEIKOU,
                OPEN_HAN_SANSHOKU_DOUJUN,
                OPEN_HAN_ITTSUU,
                OPEN_HAN_RYANPEIKOU,
                OPEN_HAN_TOITOI,
                OPEN_HAN_SAN_ANKOU,
                OPEN_HAN_SANSHOUKU_DOUKOU,
                OPEN_HAN_SAN_KANTSU,
                OPEN_HAN_TAN_YAO,
                OPEN_HAN_FANPAI,
                OPEN_HAN_CHANTA,
                OPEN_HAN_JUNCHAN,
                OPEN_HAN_HONROUTOU,
                OPEN_HAN_SHOUSANGEN,
                OPEN_HAN_CHII_TOITSU,
                OPEN_HAN_HON_ITSU,
                OPEN_HAN_CHIN_ITSU,
                OPEN_HAN_KOKUSHI_MUSOU,
                OPEN_HAN_SUU_ANKOU,
                OPEN_HAN_DAISANGEN,
                OPEN_HAN_SHOUSUUSHI,
                OPEN_HAN_DAISUUSHI,
                OPEN_HAN_TSUUIISOU,
                OPEN_HAN_CHINROUTOU,
                OPEN_HAN_RYUUIISOU,
                OPEN_HAN_CHUUREN_POUTOU,
                OPEN_HAN_SUU_KANTSU,
                OPEN_HAN_TENHOU,
                OPEN_HAN_CHIIHOU,
                OPEN_HAN_RENHOU,
                OPEN_HAN_DOUBLE_RIICHI
        };
    }

    private int[] makeClosedHan(){
        return new int[]{
                CLOSED_HAN_RIICHI,
                CLOSED_HAN_IPPATSU,
                CLOSED_HAN_HAITEI,
                CLOSED_HAN_HOUTEI,
                CLOSED_HAN_RINSHAN,
                CLOSED_HAN_CHANKAN,
                CLOSED_HAN_TSUMO,
                CLOSED_HAN_PINFU,
                CLOSED_HAN_IIPEIKOU,
                CLOSED_HAN_SANSHOKU_DOUJUN,
                CLOSED_HAN_ITTSUU,
                CLOSED_HAN_RYANPEIKOU,
                CLOSED_HAN_TOITOI,
                CLOSED_HAN_SAN_ANKOU,
                CLOSED_HAN_SANSHOUKU_DOUKOU,
                CLOSED_HAN_SAN_KANTSU,
                CLOSED_HAN_TAN_YAO,
                CLOSED_HAN_FANPAI,
                CLOSED_HAN_CHANTA,
                CLOSED_HAN_JUNCHAN,
                CLOSED_HAN_HONROUTOU,
                CLOSED_HAN_SHOUSANGEN,
                CLOSED_HAN_CHII_TOITSU,
                CLOSED_HAN_HON_ITSU,
                CLOSED_HAN_CHIN_ITSU,
                CLOSED_HAN_KOKUSHI_MUSOU,
                CLOSED_HAN_SUU_ANKOU,
                CLOSED_HAN_DAISANGEN,
                CLOSED_HAN_SHOUSUUSHI,
                CLOSED_HAN_DAISUUSHI,
                CLOSED_HAN_TSUUIISOU,
                CLOSED_HAN_CHINROUTOU,
                CLOSED_HAN_RYUUIISOU,
                CLOSED_HAN_CHUUREN_POUTOU,
                CLOSED_HAN_SUU_KANTSU,
                CLOSED_HAN_TENHOU,
                CLOSED_HAN_CHIIHOU,
                CLOSED_HAN_RENHOU,
                CLOSED_HAN_DOUBLE_RIICHI
        };
    }

    void addClosedYaku(int yaku){
        han[yaku] += closedHan[yaku];
    }

    void addOpenYaku(int yaku){
        han[yaku] += openHan[yaku];
    }

}
