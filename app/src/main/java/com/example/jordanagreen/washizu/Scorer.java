package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.SUIT_HONOR;
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;
import static com.example.jordanagreen.washizu.Constants.YAKU_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_CHINROUTOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_DAISANGEN;
import static com.example.jordanagreen.washizu.Constants.YAKU_DAISUUSHI;
import static com.example.jordanagreen.washizu.Constants.YAKU_HONROUTOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_IPPATSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_RIICHI;
import static com.example.jordanagreen.washizu.Constants.YAKU_TAN_YAO;
import static com.example.jordanagreen.washizu.Constants.YAKU_TSUUIISOU;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Scorer {

    private Hand mHand;
    //might not be in the hand already if it's from ron
    private Tile mWinningTile;
    private Score mScore;

    public Scorer(Hand hand, Tile winningTile){
        this.mHand = hand;
        this.mWinningTile = winningTile;
        this.mScore = new Score();
    }

    public Scorer(){
        this.mScore = new Score();
    }

    public Score getScore(){
        return mScore;
    }

    public void setHand(Hand hand){
        this.mHand = hand;
    }

    public void setWinningTile(Tile tile){
        this.mWinningTile = tile;
    }

    //all the tiles including the winning one for when which one was drawn doesn't matter
    private List<Tile> getFullHand(){
        List<Tile> tiles = new ArrayList<>();
        for (Tile tile: mHand.getTiles()){
            tiles.add(tile);
        }
        tiles.add(mWinningTile);
        return tiles;
    }

    private List<Tile> getHandWithoutWinningTile(){
        List<Tile> tiles = new ArrayList<>();
        for (Tile tile: mHand.getTiles()){
            tiles.add(tile);
        }
        return tiles;
    }

    private void addYaku(int yaku){
        if (mHand.getIsOpen()){
            mScore.addOpenYaku(yaku);
        } else {
            mScore.addClosedYaku(yaku);
        }
    }

    void addRiichi(){
        addYaku(YAKU_RIICHI);
    }

    void addIppatsu(){
        addYaku(YAKU_IPPATSU);
    }

   public void scoreHand(){
       //do the two easy ones to check first
       if (isKokushiMusou()){
           addYaku(YAKU_KOKUSHI_MUSOU);
           if (isDoubleKokushiMusou()){
               addYaku(YAKU_KOKUSHI_MUSOU);
           }
           //don't even bother checking anything else
           return;
       }
       if (isChiiToitsu()){
           addYaku(YAKU_CHII_TOITSU);
       }
       //TODO: figure out how to actually make sure it's four melds and a pair, and split it
        //yakuman, can stop after we get one of them for obvious reasons
       if (isChuurenPoutou()){
           addYaku(YAKU_CHUUREN_POUTOU);
           if (isDoubleChuurenPoutou()){
               addYaku(YAKU_CHUUREN_POUTOU);
           }
           return;
       }
       if (isDaiSanGen()){
           addYaku(YAKU_DAISANGEN);
           return;
       }
       if (isDaiSuuShii()){
           addYaku(YAKU_DAISUUSHI);
           return;
       }
       if (isTsuuIiSou()){
           addYaku(YAKU_TSUUIISOU);
           return;
       }
       if (isChinRouTou()){
           addYaku(YAKU_CHINROUTOU);
           return;
       }
       if (isHonRouTou()){
           addYaku(YAKU_HONROUTOU);
       }
       if (isTanYao()){
           addYaku(YAKU_TAN_YAO);
       }

   }

    private boolean isKokushiMusou(){
        int[] kokushiTiles = new int[]{MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON};
        int[] numberInHand = new int[kokushiTiles.length];
        List<Tile> tiles = getFullHand();
        for (Tile tile: tiles){
            for (int i = 0; i < kokushiTiles.length; i++){
                if (tile.getId() == kokushiTiles[i]){
                    numberInHand[i]++;
                    break;
                }
            }
        }
        // need to have 1 of all, except one which has 2
        boolean foundPair = false;
        for (int num: numberInHand){
            //missing a tile or three or more of it
            if (num == 0 || num > 2) return false;
            //two of a tile, need one pair
            if (num == 2){
                //two pairs
                if (foundPair) return false;
                foundPair = true;
            }
            //otherwise it's 1
        }
        //if all are 1 and there's a pair, return true
        return foundPair;
    }

    private boolean isDoubleKokushiMusou(){
        //one of each with 13-sided wait
        List<Tile> tiles = getHandWithoutWinningTile();
        int[] kokushiTiles = new int[]{MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON};
        int[] tileIDs = new int[tiles.size()];
        for (int i = 0; i < tiles.size(); i++){
            tileIDs[i] = tiles.get(i).getId();
        }
        Arrays.sort(tileIDs);
        //check if it's equal to the actual double kokushi
        for (int i = 0; i < tileIDs.length; i++){
            if (tileIDs[i] != kokushiTiles[i]){
                return false;
            }
        }
        return true;
    }

    private boolean isChiiToitsu(){
        //seven pairs
        List<Tile> tiles = getFullHand();
        int[] tilesInHand = new int[TILE_MAX_ID];
        for (Tile tile: tiles){
            tilesInHand[tile.getId()]++;
            //four of a kind doesn't count as two pairs
            if (tilesInHand[tile.getId()] >= 3){
                return false;
            }
        }
        for (int i = 0; i < tilesInHand.length; i++){
            if (tilesInHand[i] != 0 && tilesInHand[i] != 2){
                return false;
            }
        }
        return true;
    }

    private boolean isChuurenPoutou(){
        // 1-1-1-2-3-4-5-6-7-8-9-9-9 and one more in the same suit
        List<Tile> tiles = getFullHand();
        //make sure they're all the same suit and count them up
        int suit = tiles.get(0).getSuit();
        if (suit == SUIT_HONOR) return false;
        int[] tileCount = new int[9];
        for (Tile tile: tiles){
            if (tile.getSuit() != suit) return false;
            int val = tile.getNumericalValue();
            tileCount[val-1]++;
        }
        //make sure we have the right amount of each one, with one pair allowed
        if (tileCount[0] != 3 || tileCount[8] != 3) return false;
        boolean pairFound = false;
        for (int i = 1; i < tileCount.length-1; i++){
            if (tileCount[i] == 2){
                if (pairFound) return false;
                pairFound = true;
                continue;
            }
            if (tileCount[i] != 1) return false;
        }
        return true;
    }

    private boolean isDoubleChuurenPoutou(){
        List<Tile> tiles = getHandWithoutWinningTile();
        int[] expectedIDs = new int[]{MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, MAN_6, MAN_7,
            MAN_8, MAN_9, MAN_9, MAN_9};
        //change to the right suit
        final int suit = tiles.get(0).getSuit();
        if (suit == SUIT_HONOR) return false;
        if (mWinningTile.getSuit() != suit) return false;
        for (int i = 0; i < expectedIDs.length; i++){
            expectedIDs[i] = expectedIDs[i] + (suit * 9);
        }
        int[] handIDs = new int[tiles.size()];
        for (int i = 0; i < tiles.size(); i++){
            if (tiles.get(i).getSuit() != suit) return false;
            handIDs[i] = tiles.get(i).getId();
        }
        Arrays.sort(handIDs);
        for (int i = 0; i < handIDs.length; i++){
            if (handIDs[i] != expectedIDs[i]) return false;
        }
        return true;
    }

    //3 triples of dragons
    private boolean isDaiSanGen(){
        int[] tileCounts = mHand.getTileCounts();
        return (tileCounts[CHUN] >= 3 && tileCounts[HAKU] >= 3 && tileCounts[HATSU] >= 3);
    }

    //4 triples of winds
    private boolean isDaiSuuShii(){
        int[] tileCounts = mHand.getTileCounts();
        return (tileCounts[XIA] >= 3 && tileCounts[TON] >= 3 && tileCounts[PEI] >= 3
            && tileCounts[NAN] >= 3);
    }

    //all honors
    private boolean isTsuuIiSou(){
        for (Tile tile: getFullHand()){
            if (tile.getSuit() != SUIT_HONOR) return false;
        }
        return true;
    }

    //all terminals
    private boolean isChinRouTou(){
        for (Tile tile: getFullHand()){
            if (!tile.isTerminal()) return false;
        }
        return true;
    }

    //all terminals or honors
    private boolean isHonRouTou(){
        for (Tile tile: getFullHand()){
            if (!tile.isTerminal() && tile.getSuit() != SUIT_HONOR) return false;
        }
        return true;
    }

    //no terminals or honors
    private boolean isTanYao(){
        for (Tile tile: getFullHand()){
            if (tile.isTerminal() || tile.getSuit() == SUIT_HONOR) return false;
        }
        return true;
    }

}
