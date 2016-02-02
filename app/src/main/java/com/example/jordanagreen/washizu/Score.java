package com.example.jordanagreen.washizu;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Score {

    //han for each yaku the hand has, indexed by yaku
    private int[] han;
    private int fu;

    Score(){
        han = new int[Yaku.values().length];
    }

    //TODO: actually do this
    public int calculateScore(){
        return 0;
    }

    public int[] getHan(){
        return han;
    }


    void addYaku(Yaku yaku, boolean isOpen){
        if (isOpen){
            addOpenYaku(yaku);
        }
        else {
            addClosedYaku(yaku);
        }
    }

    //default to closed for where it doesn't matter
    void addYaku(Yaku yaku){
        addClosedYaku(yaku);
    }

    private void addClosedYaku(Yaku yaku){
//        System.out.println("added " + closedHan[yaku] + " to " + han[yaku]);
        han[yaku.ordinal()] += yaku.getClosedScore();
    }

    private void addOpenYaku(Yaku yaku){
//        System.out.println("added " + openHan[yaku] + " to " + yaku);
        if (yaku.getOpenScore() == -1){
            throw new IllegalArgumentException("Trying to make an open yaku that can't be open");
        }
        han[yaku.ordinal()] += yaku.getOpenScore();
    }

}
