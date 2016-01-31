package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Hand;
import com.example.jordanagreen.washizu.HandSplitter;
import com.example.jordanagreen.washizu.HumanPlayer;
import com.example.jordanagreen.washizu.Player;
import com.example.jordanagreen.washizu.SplitHand;
import com.example.jordanagreen.washizu.Tile;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_2;
import static com.example.jordanagreen.washizu.Constants.MAN_3;
import static com.example.jordanagreen.washizu.Constants.PIN_5;
import static com.example.jordanagreen.washizu.Constants.PIN_6;
import static com.example.jordanagreen.washizu.Constants.PIN_7;
import static com.example.jordanagreen.washizu.Constants.SOU_4;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.WIND_EAST;

/**
 * Created by Jordan on 1/28/2016.
 */
public class HandSplitterTest extends TestCase {

    public void testGetPossibleSplits() throws Exception {
        HandSplitter hs = new HandSplitter();
        Player player = new HumanPlayer(WIND_EAST);
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, SOU_4, SOU_4, SOU_4, PIN_5, PIN_6,
                PIN_7, CHUN, CHUN, CHUN, TON);
        Tile tile = new Tile(TON);
        Hand hand = new Hand(tiles, tile, player);

        SplitHand splitHand = hs.splitHand(hand);
        System.out.println(splitHand);
    }
}