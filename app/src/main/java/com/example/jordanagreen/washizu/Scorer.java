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
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;
import static com.example.jordanagreen.washizu.Constants.YAKU_IPPATSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_RIICHI;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Scorer {

    Hand hand;
    Tile winningTile;
    Score score;

    Scorer(Hand hand, Tile winningTile){
        this.hand = hand;
        this.winningTile = winningTile;
        this.score = new Score();
    }

    //all the tiles including the winning one for when which one was drawn doesn't matter
    //we only need the tile IDs so don't bother returning the whole tiles
    private List<Integer> getFullHand(){
        List<Integer> ids = new ArrayList<>();
        for (Tile tile: hand.getTiles()){
            ids.add(tile.getId());
        }
        ids.add(winningTile.getId());
        return ids;
    }

    private List<Integer> getHandWIthoutWinningTile(){
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
            score.addClosedYaku(yaku);
        }
    }

    void addRiichi(){
        addYaku(YAKU_RIICHI);
    }

    void addIppatsu(){
        addYaku(YAKU_IPPATSU);
    }

   Score scoreHand(){
       if (isKokushiMusou()){
           addYaku(YAKU_KOKUSHI_MUSOU);
           if (isDoubleKokushiMusou()){
               addYaku(YAKU_KOKUSHI_MUSOU);
           }
       }

       return score;
   }

    private boolean isKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON);
        List<Integer> tiles = getFullHand();
        for (int tile: tiles){
            if (!kokushiTiles.contains(tile)){
                return false;
            }
        }
        return true;
    }

    private boolean isDoubleKokushiMusou(){
        //one of each with 13-sided wait
        List<Integer> tiles = getHandWIthoutWinningTile();
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



}
