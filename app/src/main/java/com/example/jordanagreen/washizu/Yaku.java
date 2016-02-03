package com.example.jordanagreen.washizu;

/**
 * Created by Jordan on 2/1/2016.
 */
public enum Yaku {
    RIICHI(-1, 1),
    IPPATSU(-1, 1),
    HAITEI(1, 1),
    HOUTEI(1, 1),
    RINSHAN(1, 1),
    CHANKAN(1, 1),
    TSUMO(-1, 1),
    PINFU(-1, 1),
    IIPEIKOU(-1, 1),
    SANSHOKU_DOUJUN(1, 2),
    ITTSUU(1, 2),
    RYANPEIKOU(-1, 3),
    TOITOI(2, -1),
    SAN_ANKOU(2, 2),
    SANSHOUKU_DOUKOU(2, 2),
    SAN_KANTSU(2, 2),
    TAN_YAO(1, 1),
    FANPAI(1, 1),
    CHANTA(1, 2),
    JUNCHAN(2, 3),
    HONROUTOU(2, 2),
    SHOUSANGEN(2, 2),
    CHII_TOITSU(-1, 2),
    HON_ITSU(2, 3),
    CHIN_ITSU(5, 6),
    KOKUSHI_MUSOU(-1, 13),
    SUU_ANKOU(-1, 13),
    DAISANGEN(13, 13),
    SHOUSUUSHI(13, 13),
    DAISUUSHI(13, 13),
    TSUUIISOU(13, 13),
    CHINROUTOU(13, 13),
    RYUUIISOU(13, 13),
    CHUUREN_POUTOU(-1, 13),
    SUU_KANTSU(13, 13),
    TENHOU(-1, 13),
    CHIIHOU(-1, 13),
    RENHOU(-1, 13),
    DOUBLE_RIICHI(-1, 2);

    private int openHan, closedHan;

    Yaku(int openHan, int closedHan){
        this.openHan = openHan;
        this.closedHan = closedHan;
    }

    public int getOpenHan(){
        return openHan;
    }

    public int getClosedHan(){
        return closedHan;
    }
}
