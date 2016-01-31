package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jordan on 1/28/2016.
 */
public class HandSplitter {

    public static final String TAG = "HandSplitter";

    private static int[][] mPossiblePairValues;

    public HandSplitter(){
        if (mPossiblePairValues == null){
            mPossiblePairValues = new int[3][3];
            mPossiblePairValues[0] = new int[]{3, 6, 9};
            mPossiblePairValues[1] = new int[]{2, 5, 8};
            mPossiblePairValues[2] = new int[]{1, 4, 7};
        }
    }

    //TODO: if a hand can be split two ways, return both (or see if we always know which is better)
    public SplitHand splitHand(Hand hand){
        return splitHand(hand, hand.getDrawnTile());
    }

    /**
     * @param hand The hand to be split
     * @param winningTile The hand's winning tile
     * @return A SplitHand representing the way the hand can be split
     */
   public SplitHand splitHand(Hand hand, Tile winningTile){
       SplitHand split = new SplitHand();
       //any open melds are obviously used
       for (Meld meld: hand.getMelds()){
           split.addMeld(meld);
       }
       //make a copy of all the tiles still left in the hand
       List<Tile> tiles = new ArrayList<>();
       for (Tile tile: hand.getTiles()){
           tiles.add(tile);
       }
       tiles.add(winningTile);
       Collections.sort(tiles);

       /*
       The tiles in both a chii and a pon will add up to 3n: n+n+n or n-1 + n + n+1
       So the full hand, not counting honors, is 4n + 2m where m is the pair and total 5 3
       shows us what number the pair can be (unless it's an honor)
       */

       int[] counts = hand.getTileCounts();
       List<Tile> pair = null;
       List<Tile> toDelete = new ArrayList<>();

       //take out honors first
       for (int i = 0; i < tiles.size(); i++){
           Tile tile = tiles.get(i);
           if (tile.getSuit() == Suit.HONOR){
               //if there's any honors, they should be pons
               List<Tile> honors = getAllTilesById(tiles, tile.getId());
               if (honors.size() == 2){
                   //already found a pair, can't have another pair of honors
                   if (pair != null){
                       throw new IllegalStateException("Unmatched pair of honors");
                   }
                   pair = honors;
                   split.setPair(pair);
                   toDelete.addAll(pair);
                   i++; //skip the next one so we don't check for a second pair of the same tile
               }
               else if (honors.size() == 3){
                   //make a pon of them and take them out of the hand
                   split.addMeld(new Meld(honors));
                   toDelete.addAll(honors);
                   i += 2; //skip the next 2
               }
               else {
                   throw new IllegalStateException("Unmatched honor tile");
               }
           }
       }
       //can't remove other elements during iteration so do that now
       tiles.removeAll(toDelete);
       toDelete.clear();

       //if we don't have the pair yet, see what it could be and try all of the possible values
       if (pair == null){
           int total = 0;
           for (Tile tile: tiles){
               //should only be number tiles now
               if (tile.getSuit() == Suit.HONOR){
                   throw new IllegalStateException("Unmatched honor left in hand");
               }
               total += tile.getNumericalValue();
           }
           int[] possiblePairs = mPossiblePairValues[total % 3];
           for (int pairValue: possiblePairs){
               int inHand = counts[pairValue];
               if (inHand >= 2){
                   //take out the pair, try splitting the rest
                   pair = getAllTilesById(tiles, pairValue).subList(0, 2);
                   split.setPair(pair);
                   tiles.removeAll(pair);
                   List<Meld> possibleMelds = splitTiles(tiles);
                   //if it worked, we can return the melds we have now
                   if (possibleMelds != null){
                       split.addAllMelds(possibleMelds);
                       return split;
                   }
                   //if it didn't work, put the pair back and try the next pair
                   tiles.addAll(pair);
               }
           }
           //if we've reached this point, it isn't going to work
           return null;
       }
       //already found a pair of honors, try splitting this hand
       else {
           List<Meld> possibleMelds = splitTiles(tiles);
           split.addAllMelds(possibleMelds);
           return split;
       }
   }

    /**
     * @param tiles The tiles to try splitting into melds
     * @return A list of melds these tiles can be split into
     */
    private List<Meld> splitTiles(List<Tile> tiles){
        List<Meld> melds = new ArrayList<>();
        //make a copy so it doesn't change the original list when we take tiles out
        List<Tile> t = new ArrayList<>(tiles);
        Collections.sort(tiles);
        while (t.size() > 0){
            Tile tile = t.get(0);
            Tile tile2, tile3;
            MeldType type;
            //pon
            List<Tile> allSameTiles = getAllTilesById(tiles, tile.getId());
            if (allSameTiles.size() >= 3){
                tile = allSameTiles.get(0);
                tile2 = allSameTiles.get(1);
                tile3 = allSameTiles.get(2);
                type = MeldType.PON;
            }
            //chii
            else {
                tile2 = getTileById(tiles, tile.getId() + 1);
                tile3 = getTileById(tiles, tile.getId() + 2);
                if (!(tile.getSuit() == tile2.getSuit() && tile2.getSuit() == tile3.getSuit())){
                    throw new IllegalStateException("Tiles for chii aren't the same suit: " +
                            tile2 + " " + tile3);
                }
                type = MeldType.CHII;
            }
            Meld meld = new Meld(tile, tile2, tile3, type);
            melds.add(meld);
            t.removeAll(Arrays.asList(tile, tile2, tile3));
        }
        return melds;
    }

    private List<Tile> getAllTilesById(List<Tile> tiles, int id){
        List<Tile> found = new ArrayList<>();
        for (Tile t: tiles){
            if (t.getId() == id){
                found.add(t);
            }
        }
        return found;
    }

    private Tile getTileById(List<Tile> tiles, int id){
        for (Tile tile: tiles){
            if (tile.getId() == id){
                return tile;
            }
        }
        throw new IllegalArgumentException("Trying to get tile that isn't in hand");
    }

}
