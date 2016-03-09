package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jordan on 1/31/2016.
 */
public class SplitHand {

    private List<Meld> mMelds;
    //only really need this for san ankou
    private List<Meld> mClosedMelds;
    private Tile[] mPair;
    private Tile mWinningTile;
//    private Stats mStats;

    SplitHand(){
        mMelds = new ArrayList<>(4);
        mClosedMelds = new ArrayList<>(4);
        mPair = new Tile[2];
//        mStats = new Stats();
    }

    void setWinningTile(Tile tile){
        mWinningTile = tile;
    }

    Tile getWinningTile(){
        return mWinningTile;
    }

//    Stats getStats(){
//        return mStats;
//    }

    void addMeld(Meld meld){
        if (mMelds.size() == 4){
            throw new IllegalStateException("Already have four melds");
        }
        mMelds.add(meld);
//        if (meld.getType() != MeldType.CHII){
//            mStats.pons++;
//            if (meld.getType() == MeldType.KAN || meld.getType() == MeldType.SHOUMINKAN){
//                mStats.kans++;
//            }
//        } else {
//            mStats.chiis++;
//        }
//        for (Tile tile: meld.getTiles()){
//            if (tile.isTerminal()){
//                mStats.terminals++;
//            } else if (tile.getSuit() == Suit.HONOR) {
//                mStats.honors++;
//            } else {
//                mStats.nonTerminals++;
//            }
//        }
    }

    void addAllMelds(Collection<Meld> melds){
//        mMelds.addAll(melds);
        for (Meld meld: melds){
            addMeld(meld);
        }
    }

    void addAllClosedMelds(Collection<Meld> melds){
        mClosedMelds.addAll(melds);
    }

    void setPair(List<Tile> tiles){
        if (tiles.size() != 2){
            throw new IllegalArgumentException("Trying to set pair that isn't two tiles");
        }
        if (tiles.get(0).getId() != tiles.get(1).getId()){
            throw new IllegalArgumentException("Trying to set pair that isn't the same tiles");
        }
        mPair[0] = tiles.get(0);
        mPair[1] = tiles.get(1);
//        for (Tile tile: mPair){
//            if (tile.isTerminal()){
//                mStats.terminals++;
//            } else if (tile.getSuit() == Suit.HONOR) {
//                mStats.honors++;
//            } else {
//                mStats.nonTerminals++;
//            }
//        }
    }

    List<Meld> getMelds(){
        return mMelds;
    }

    List<Meld> getClosedMelds(){
        return mClosedMelds;
    }

    Tile[] getPair(){
        return mPair;
    }


    List<Tile> getFullHand(){
        List<Tile> tiles = new ArrayList<>();
        for (Meld meld: mMelds){
            tiles.addAll(meld.getTiles());
        }
        tiles.addAll(Arrays.asList(mPair));
        return tiles;
    }

    List<Tile> getHandWithoutWinningTile(){
        List<Tile> tiles = getFullHand();
        tiles.remove(mWinningTile);
        return tiles;
    }

    boolean containsPonOf(int tileID){
        for (Meld meld: mMelds){
            if (meld.getType() != MeldType.CHII){
                if (meld.getTiles().get(0).getId() == tileID){
                    return true;
                }
            }
        }
        return false;
    }

    boolean containsChiiStartingWith(int tileID){
        for (Meld meld: mMelds){
            if (meld.getType() == MeldType.CHII){
                if (meld.getTiles().get(0).getId() == tileID){
                    return true;
                }
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Meld meld: mMelds){
            sb.append(meld.getType()).append(":");
            for (Tile tile: meld.getTiles()){
                sb.append(" ").append(tile);
            }
            sb.append('\n');
        }
        sb.append("PAIR: ").append(mPair[0]).append(" ").append(mPair[1]);
        return sb.toString();
    }

    //TODO: this might not be needed, just here in case
//    class Stats {
//        private int pons;
//        private int kans;
//        private int chiis;
//        private int terminals;
//        private int nonTerminals;
//        private int honors;
//
//        public int getPons() {
//            return pons;
//        }
//
//        public int getKans() {
//            return kans;
//        }
//
//        public int getChiis() {
//            return chiis;
//        }
//
//        public int getTerminals() {
//            return terminals;
//        }
//
//        public int getNonTerminals() {
//            return nonTerminals;
//        }
//
//        public int getHonors() {
//            return honors;
//        }
//    }

}
