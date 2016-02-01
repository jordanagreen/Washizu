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
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Scorer {

//    private Hand mHand;
    //might not be in the hand already if it's from ron
//    private Tile mWinningTile;
//    private Score mScore;

//    public Scorer(Hand hand, Tile winningTile){
//        this.mHand = hand;
//        this.mWinningTile = winningTile;
////        this.mScore = new Score();
//    }

//    public Scorer(){
//        this.mScore = new Score();
//    }
//
//    public Score getScore(){
//        return mScore;
//    }

//    public void setHand(Hand hand){
//        this.mHand = hand;
//    }

//    public void setWinningTile(Tile tile){
//        this.mWinningTile = tile;
//    }

    //all the tiles including the winning one for when which one was drawn doesn't matter
//    private List<Tile> getFullHand(Hand hand, Tile winningTile){
//        List<Tile> tiles = new ArrayList<>();
//        for (Tile tile: hand.getTiles()){
//            tiles.add(tile);
//        }
//        tiles.add(winningTile);
//        return tiles;
//    }
//
//    private List<Tile> getHandWithoutWinningTile(){
//        List<Tile> tiles = new ArrayList<>();
//        for (Tile tile: mHand.getTiles()){
//            tiles.add(tile);
//        }
//        return tiles;
//    }

//    void addRiichi(){
//        addYaku(Yaku.RIICHI);
//    }
//
//    void addIppatsu(){
//        addYaku(Yaku.IPPATSU);
//    }

    //return null if the hand couldn't be scored
    public Score scoreHand(Hand hand){
        Tile winningTile = hand.getDrawnTile();
        Score score = new Score();
        //do the two easy ones to check first
        if (isKokushiMusou(hand, winningTile)){
            score.addYaku(Yaku.KOKUSHI_MUSOU, hand.getIsOpen());
            if (isDoubleKokushiMusou(hand)){
                score.addYaku(Yaku.KOKUSHI_MUSOU, hand.getIsOpen());
            }
            //don't even bother checking anything else
            return score;
        }
        if (isChiiToitsu(hand, winningTile)){
            score.addYaku(Yaku.CHII_TOITSU, hand.getIsOpen());
        }

        //if we get to this point, it needs to be four melds and a pair
        HandSplitter handSplitter = new HandSplitter();
        SplitHand splitHand;
        try {
            splitHand = handSplitter.splitHand(hand, winningTile);
        }
        catch (IllegalStateException | IllegalArgumentException e){
            return null;
        }

        //yakuman, can stop after we get one of them for obvious reasons
        if (isChuurenPoutou(splitHand)){
            score.addYaku(Yaku.CHUUREN_POUTOU, hand.getIsOpen());
            if (isDoubleChuurenPoutou(splitHand)){
                score.addYaku(Yaku.CHUUREN_POUTOU, hand.getIsOpen());
            }
            return score;
        }
        if (isDaiSanGen(splitHand)){
            score.addYaku(Yaku.DAISANGEN, hand.getIsOpen());
            return score;
        }
        if (isDaiSuuShii(splitHand)){
            score.addYaku(Yaku.DAISUUSHI, hand.getIsOpen());
            return score;
        }
        if (isTsuuIiSou(splitHand)){
            score.addYaku(Yaku.TSUUIISOU, hand.getIsOpen());
            return score;
        }
        if (isChinRouTou(splitHand)){
            score.addYaku(Yaku.CHINROUTOU, hand.getIsOpen());
            return score;
        }
        if (isHonRouTou(splitHand)){
            score.addYaku(Yaku.HONROUTOU, hand.getIsOpen());
        }
        if (isTanYao(splitHand)){
            score.addYaku(Yaku.TAN_YAO, hand.getIsOpen());
        }


        return score;
    }

    private boolean isKokushiMusou(Hand hand, Tile winningTile){
        int[] kokushiTiles = new int[]{MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON};
        int[] numberInHand = new int[kokushiTiles.length];
        List<Tile> tiles = new ArrayList<>(hand.getTiles());
        tiles.add(winningTile);
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

    private boolean isDoubleKokushiMusou(Hand hand){
        //one of each with 13-sided wait
        List<Tile> tiles = hand.getTiles();
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

    private boolean isChiiToitsu(Hand hand, Tile winningTile){
        //seven pairs
        List<Tile> tiles = new ArrayList<>(hand.getTiles());
        tiles.add(winningTile);
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

    private boolean isChuurenPoutou(SplitHand hand){
        // 1-1-1-2-3-4-5-6-7-8-9-9-9 and one more in the same suit
        List<Tile> tiles = hand.getFullHand();
        //make sure they're all the same suit and count them up
        Suit suit = tiles.get(0).getSuit();
        if (suit == Suit.HONOR) return false;
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

    private boolean isDoubleChuurenPoutou(SplitHand hand){
        List<Tile> tiles = hand.getHandWithoutWinningTile();
        int[] expectedIDs = new int[]{MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, MAN_6, MAN_7,
            MAN_8, MAN_9, MAN_9, MAN_9};
        //change tiles we're comparing to to the right suit by adding 9 to get from man->pin->sou
        final Suit suit = tiles.get(0).getSuit();
        if (suit == Suit.HONOR) return false;
        if (hand.getWinningTile().getSuit() != suit) return false;
        for (int i = 0; i < expectedIDs.length; i++){
            expectedIDs[i] = expectedIDs[i] + (suit.ordinal() * 9);
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
    private boolean isDaiSanGen(SplitHand hand){
        return hand.containsPonOf(CHUN) && hand.containsPonOf(HAKU) && hand.containsPonOf(HATSU);
    }

    //4 triples of winds
    private boolean isDaiSuuShii(SplitHand hand){
        return hand.containsPonOf(XIA) && hand.containsPonOf(TON) && hand.containsPonOf(NAN) &&
                hand.containsPonOf(PEI);
    }

    //all honors
    private boolean isTsuuIiSou(SplitHand hand){
        for (Tile tile: hand.getFullHand()){
            if (tile.getSuit() != Suit.HONOR) return false;
        }
        return true;
    }

    //all terminals
    private boolean isChinRouTou(SplitHand hand){
        for (Tile tile: hand.getFullHand()){
            if (!tile.isTerminal()) return false;
        }
        return true;
    }

    //all terminals or honors
    private boolean isHonRouTou(SplitHand hand){
        for (Tile tile: hand.getFullHand()){
            if (!tile.isTerminal() && tile.getSuit() != Suit.HONOR) return false;
        }
        return true;
    }

    //no terminals or honors
    private boolean isTanYao(SplitHand hand){
        for (Tile tile: hand.getFullHand()){
            if (tile.isTerminal() || tile.getSuit() == Suit.HONOR) return false;
        }
        return true;
    }


}
