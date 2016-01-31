package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jordan on 1/31/2016.
 */
public class SplitHand {

    private List<Meld> mMelds;
    private Tile[] mPair;

    SplitHand(){
        mMelds = new ArrayList<>(4);
        mPair = new Tile[2];
    }

    void addMeld(Meld meld){
        if (mMelds.size() == 4){
            throw new IllegalStateException("Already have four melds");
        }
        mMelds.add(meld);
    }

    void addAllMelds(Collection<Meld> melds){
        for (Meld meld: melds){
            mMelds.add(meld);
        }
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
    }

    List<Meld> getMelds(){
        return mMelds;
    }

    Tile[] getPair(){
        return mPair;
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
}
