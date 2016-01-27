package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.HAKU;
import static com.example.jordanagreen.washizu.Constants.HATSU;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
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
import static com.example.jordanagreen.washizu.Constants.YAKU_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_IPPATSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_RIICHI;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Scorer {

    private Hand hand;
    //might not be in the hand already if it's from ron
    private Tile winningTile;
    private Score score;

    public Scorer(Hand hand, Tile winningTile){
        this.hand = hand;
        this.winningTile = winningTile;
        this.score = new Score();
    }

    public Scorer(){
        this.score = new Score();
    }

    public Score getScore(){
        return score;
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public void setWinningTile(Tile tile){
        this.winningTile = tile;
    }

    //all the tiles including the winning one for when which one was drawn doesn't matter
    //we only need the tile IDs so don't bother returning the whole tiles to simplify things
    private List<Integer> getFullHand(){
        List<Integer> ids = new ArrayList<>();
        for (Tile tile: hand.getTiles()){
            ids.add(tile.getId());
        }
        ids.add(winningTile.getId());
        return ids;
    }

    private List<Integer> getHandWithoutWinningTile(){
        List<Integer> ids = new ArrayList<>();
        for (Tile tile: hand.getTiles()){
            ids.add(tile.getId());
        }
        return ids;
    }

    private void addYaku(int yaku){
        if (hand.getIsOpen()){
            score.addOpenYaku(yaku);
        } else {
//            System.out.println("added " + yaku);
            score.addClosedYaku(yaku);
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

   }

    private boolean isKokushiMusou(){
        int[] kokushiTiles = new int[]{MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON};
        int[] numberInHand = new int[kokushiTiles.length];
        List<Integer> tiles = getFullHand();
        for (int tile: tiles){
            for (int i = 0; i < kokushiTiles.length; i++){
                if (tile == kokushiTiles[i]){
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
        List<Integer> tiles = getHandWithoutWinningTile();
        int[] kokushiTiles = new int[]{MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON};
        Integer[] handTiles = tiles.toArray(new Integer[tiles.size()]);
        Arrays.sort(handTiles);
        //check if it's equal to the actual double kokushi
        for (int i = 0; i < handTiles.length; i++){
            if (handTiles[i] != kokushiTiles[i]){
                return false;
            }
        }
        return true;
    }

    private boolean isChiiToitsu(){
        //seven pairs
        List<Integer> tiles = getFullHand();
        int[] tilesInHand = new int[TILE_MAX_ID];
        for (int tile: tiles){
            tilesInHand[tile]++;
            //four of a kind doesn't count as two pairs
            if (tilesInHand[tile] >= 3){
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



}
