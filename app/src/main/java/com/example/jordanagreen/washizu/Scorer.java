package com.example.jordanagreen.washizu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import static com.example.jordanagreen.washizu.Constants.PIN_4;
import static com.example.jordanagreen.washizu.Constants.PIN_7;
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_4;
import static com.example.jordanagreen.washizu.Constants.SOU_7;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TILE_MAX_ID;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;

/**
 * Created by Jordan on 12/1/2015.
 */
public class Scorer {

    public static final String TAG = "Scorer";

    //return null if the hand couldn't be scored, also check if 0 yaku
    public Score scoreHand(Hand hand, Wind roundWind, Tile winningTile, boolean isTsumo){
//        Tile winningTile = hand.getDrawnTile();
        Score score = new Score();
        //do the two easy ones to check first
        if (isKokushiMusou(hand, winningTile)){
            score.addYaku(Yaku.KOKUSHI_MUSOU);
            if (isDoubleKokushiMusou(hand)){
                score.addYaku(Yaku.KOKUSHI_MUSOU);
            }
            //don't even bother checking anything else
            return score;
        }
        boolean shouldSplit = true;
        if (isChiiToitsu(hand, winningTile)){
            score.addYaku(Yaku.CHII_TOITSU);
            shouldSplit = false;
        }
        //these can go with chii toitsu so do them before splitting
        //TODO: chii toitsu can go with honroutou, tanyao, honitsu, chinitsu

        //TODO: this and chii toitsu together are daichiisei, if allowed it's a double yakuman
        if (isTsuuIiSou(hand, winningTile)){
            score.addYaku(Yaku.TSUUIISOU);
            return score;
        }

        if (isHonRouTou(hand, winningTile)){
            score.addYaku(Yaku.HONROUTOU);
        }
        if (isTanYao(hand, winningTile)){
            score.addYaku(Yaku.TAN_YAO);
        }

        //if we got chii toitsu, don't split the hand
        if (!shouldSplit) return score;

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
            score.addYaku(Yaku.CHUUREN_POUTOU);
            if (isDoubleChuurenPoutou(splitHand)){
                score.addYaku(Yaku.CHUUREN_POUTOU);
            }
            return score;
        }
        if (isDaiSanGen(splitHand)){
            score.addYaku(Yaku.DAISANGEN);
            return score;
        }
        if (isDaiSuuShii(splitHand)){
            score.addYaku(Yaku.DAISUUSHI);
            return score;
        }
        if (isShouSuuShii(splitHand)){
            score.addYaku(Yaku.SHOUSUUSHI);
            return score;
        }
        if (isChinRouTou(hand, winningTile)){
            score.addYaku(Yaku.CHINROUTOU);
            return score;
        }
        if (isSuuKantsu(splitHand)){
            score.addYaku(Yaku.SUU_KANTSU);;
            return score;
        }
        //do yaku which have to be closed here
//        Log.d(TAG, "open? " + hand.getIsOpen());
        if (!hand.getIsOpen()){
            if (isSuuAnKou(splitHand, isTsumo)){
                score.addYaku(Yaku.SUU_ANKOU);
                return score;
            }
            if (isPinfu(splitHand, roundWind, hand.getPlayer().getWind())){
//                Log.d(TAG, "pinfu");
                score.addYaku(Yaku.PINFU);
            }
            if (isIiPeiKou(splitHand)){
                score.addYaku(Yaku.IIPEIKOU);
            }
        }

        //do the rest of the yaku here, can be open
        if (isItsuu(splitHand)){
            score.addYaku(Yaku.ITTSUU, hand.getIsOpen());
        }
        if (isFanpai(splitHand, roundWind, hand.getPlayer().getWind())){
            int totalFanpai = getTotalFanpai(splitHand, roundWind, hand.getPlayer().getWind());
            for (int i = 0; i < totalFanpai; i++){
                score.addYaku(Yaku.FANPAI);
            }
        }
        if (isToiToi(splitHand)){
            score.addYaku(Yaku.TOITOI, true);
        }
        if (!isHonRouTou(hand, winningTile)){
            if (isChanta(splitHand)){
                score.addYaku(Yaku.CHANTA, hand.getIsOpen());
            }
        }
        if (isSanKantsu(splitHand)){
            score.addYaku(Yaku.SAN_KANTSU, hand.getIsOpen());
        }
        if (isSanShokuDouJun(splitHand)){
            score.addYaku(Yaku.SANSHOKU_DOUJUN, hand.getIsOpen());
        }
        if (isSanShokuDouKou(splitHand)){
            score.addYaku(Yaku.SANSHOUKU_DOUKOU, hand.getIsOpen());
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
        // in case of four melds which would make the only two tiles in the hand be a pair
        if (tiles.size() != 14){
            return false;
        }
        int[] tilesInHand = new int[TILE_MAX_ID + 1];
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

    //3 triples of winds and a pair
    private boolean isShouSuuShii(SplitHand hand){
        List<Wind> winds = new ArrayList<>(Arrays.asList(Wind.values()));
        for (Wind wind: Wind.values()){
            if (hand.containsPonOf(wind.getTileID())){
                winds.remove(wind);
            }
        }
        //should have only one left after pons are taken out
        if (winds.size() != 1) return false;
        //pair should be of the last wind
        return hand.getPair()[0].getId() == winds.get(0).getTileID();
    }

    //all honors
    private boolean isTsuuIiSou(Hand hand, Tile winningTile){
        for (Tile tile: getFullHand(hand, winningTile)){
            if (tile.getSuit() != Suit.HONOR) return false;
        }
        return true;
    }

    //all terminals
    private boolean isChinRouTou(Hand hand, Tile winningTile){
        for (Tile tile: getFullHand(hand, winningTile)){
            if (!tile.isTerminal()) return false;
        }
        return true;
    }

    //all terminals or honors
    private boolean isHonRouTou(Hand hand, Tile winningTile){
        for (Tile tile: getFullHand(hand, winningTile)){
            if (!tile.isTerminal() && tile.getSuit() != Suit.HONOR) return false;
        }
        return true;
    }

    //no terminals or honors
    private boolean isTanYao(Hand hand, Tile winningTile){
        for (Tile tile: getFullHand(hand, winningTile)){
            if (tile.isTerminal() || tile.getSuit() == Suit.HONOR) return false;
        }
        return true;
    }

    private boolean isFanpai(SplitHand hand, Wind roundWind, Wind playerWind){
        for (Meld meld: hand.getMelds()){
             if (meld.getType() != MeldType.CHII && isFanpaiTile(meld.getTiles().get(0), roundWind,
                     playerWind)) return true;
        }
        return false;
    }

    private int getTotalFanpai(SplitHand hand, Wind roundWind, Wind playerWind){
        int totalFanpai = 0;
        for (Meld meld: hand.getMelds()){
            if (meld.getType() != MeldType.CHII){
                Tile tile = meld.getTiles().get(0);
                if (tile.getId() == CHUN || tile.getId() == HATSU || tile.getId() == HAKU ||
                        tile.getId() == roundWind.getTileID()){
                    totalFanpai++;
                }
                //do this one separately to handle round and player's winds being the same
                if (tile.getId() == playerWind.getTileID()){
                    totalFanpai++;
                }
            }
        }
        return totalFanpai;
    }

    private boolean isSuuAnKou(SplitHand hand, boolean isTsumo){
        Tile winningTile = hand.getWinningTile();
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.CHII) return false;
            //isn't suu ankou if it was ron and winning tile was part of a pon
            if (!isTsumo){
                if (winningTile.getId() == meld.getTiles().get(0).getId()) return false;
            }
        }
        return true;
    }

    //all chii, two-sided wait, pair isn't dragon or wind that's worth something
    private boolean isPinfu(SplitHand hand, Wind roundWind, Wind playerWind){
        Tile winningTile = hand.getWinningTile();
        for (Meld meld: hand.getMelds()){
            if (meld.getType() != MeldType.CHII) return false;
            if (meld.getTiles().contains(winningTile)){
                if (!isTwoSidedWait(meld, winningTile)) return false;
            }
        }
        Tile pairTile = hand.getPair()[0];
        return !isFanpaiTile(pairTile, roundWind, playerWind);
    }

    private boolean isFanpaiTile(Tile tile, Wind roundWind, Wind playerWind){
        return (tile.getId() == CHUN || tile.getId() == HATSU || tile.getId() == HAKU ||
                tile.getId() == roundWind.getTileID() || tile.getId() == playerWind.getTileID());
    }

    private boolean isTwoSidedWait(Meld meld, Tile winningTile){
        if (meld.getType() != MeldType.CHII) return false;
        if (!meld.getTiles().contains(winningTile))
            throw new IllegalArgumentException();
//        return !(meld.getTiles().get(1).getId() == winningTile.getId()) &&
//                !(meld.getTiles().get(0).getNumericalValue() == 1 ||
//                meld.getTiles().get(2).getNumericalValue() == 9);
        return meld.getTiles().get(1).getId() != winningTile.getId();
    }

    private boolean isToiToi(SplitHand hand){
//        return hand.getStats().getPons() == 4;
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.CHII) return false;
        }
        return true;
    }

    //straight
    private boolean isItsuu(SplitHand hand){
        return ((hand.containsChiiStartingWith(MAN_1) && hand.containsChiiStartingWith(MAN_4) &&
                hand.containsChiiStartingWith(MAN_7)) ||
                (hand.containsChiiStartingWith(PIN_1) && hand.containsChiiStartingWith(PIN_4) &&
                hand.containsChiiStartingWith(PIN_7)) ||
                hand.containsChiiStartingWith(SOU_1) && hand.containsChiiStartingWith(SOU_4) &&
                hand.containsChiiStartingWith(SOU_7));
    }

    //terminals or honors in every set
    private boolean isChanta(SplitHand hand){
        for (Meld meld: hand.getMelds()){
            if (!containsTerminalOrHonor(meld)) return false;
        }
        Tile tile = hand.getPair()[0];
        return (tile.isTerminal() || tile.getSuit() == Suit.HONOR);
    }

    private boolean containsTerminalOrHonor(Meld meld){
        for (Tile tile: meld.getTiles()){
            if (tile.isTerminal() || tile.getSuit() == Suit.HONOR) return true;
        }
        return false;
    }

    private boolean isSanKantsu(SplitHand hand){
//        return hand.getStats().getKans() == 3;
        int kans = 0;
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.KAN || meld.getType() == MeldType.SHOUMINKAN) kans++;
        }
        return kans == 3;
    }

    private boolean isSuuKantsu(SplitHand hand){
//        return hand.getStats().getKans() == 4;
        for (Meld meld: hand.getMelds()){
            if (meld.getType() != MeldType.KAN && meld.getType() != MeldType.SHOUMINKAN)
                return false;
        }
        return true;
    }

    //two identical chiis, closed only
    private boolean isIiPeiKou(SplitHand hand){
        Set<Integer> firstTiles = new HashSet<>();
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.CHII){
                //just check if they have the same first tile
                 if (firstTiles.contains(meld.getTiles().get(0).getId())){
                     return true;
                 }
                firstTiles.add(meld.getTiles().get(0).getId());
            }
        }
        return false;
    }

    //same chii in all three suits
    private boolean isSanShokuDouJun(SplitHand hand){
        SuitCounter[] suitCounters = new SuitCounter[7];
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.CHII){
                Tile tile = meld.getTiles().get(0);
                int val = tile.getNumericalValue() - 1;
                if (suitCounters[val] == null){
                    suitCounters[val] = new SuitCounter();
                }
                suitCounters[val].foundSuit(tile.getSuit());
            }
        }
        for (SuitCounter suitCounter : suitCounters){
            if (suitCounter != null){
                if (suitCounter.allFound()){
                    return true;
                }
            }
        }
        return false;
    }

    //same pon in all three suits
    private boolean isSanShokuDouKou(SplitHand hand){
        SuitCounter[] suitCounters = new SuitCounter[9];
        for (Meld meld: hand.getMelds()){
            if (meld.getType() != MeldType.CHII){
                Tile tile = meld.getTiles().get(0);
                if (tile.getSuit() == Suit.HONOR) continue;
                int val = tile.getNumericalValue() - 1;
                if (suitCounters[val] == null){
                    suitCounters[val] = new SuitCounter();
                }
                suitCounters[val].foundSuit(tile.getSuit());
            }
        }
        for (SuitCounter suitCounter : suitCounters){
            if (suitCounter != null){
                if (suitCounter.allFound()){
                    return true;
                }
            }
        }
        return false;
    }

    //return a list of all tiles in the hand + melds + drawn/called tile
    private List<Tile> getFullHand(Hand hand, Tile winningTile){
        List<Tile> tiles = new ArrayList<>(hand.getTiles());
        for (Meld meld: hand.getMelds()){
            tiles.addAll(meld.getTiles());
        }
        tiles.add(winningTile);
        return tiles;
    }

    //for keeping track of which suits we've found in san shoku
    private class SuitCounter {
        Map<Suit, Boolean> found;
        SuitCounter(){
            found = new HashMap<>();
            found.put(Suit.SOU, false);
            found.put(Suit.MAN, false);
            found.put(Suit.PIN, false);
        }

        void foundSuit(Suit suit){
            found.put(suit, true);
        }

        boolean allFound(){
            for (Suit suit: found.keySet()){
                if (!found.get(suit)) return false;
            }
            return true;
        }
    }

}
