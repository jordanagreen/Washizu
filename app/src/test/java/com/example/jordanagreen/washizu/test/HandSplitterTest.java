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

import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_2;
import static com.example.jordanagreen.washizu.Constants.MAN_3;
import static com.example.jordanagreen.washizu.Constants.MAN_4;
import static com.example.jordanagreen.washizu.Constants.MAN_5;
import static com.example.jordanagreen.washizu.Constants.MAN_6;
import static com.example.jordanagreen.washizu.Constants.MAN_7;
import static com.example.jordanagreen.washizu.Constants.MAN_9;
import static com.example.jordanagreen.washizu.Constants.WIND_EAST;

/**
 * Created by Jordan on 1/28/2016.
 */
public class HandSplitterTest extends TestCase {

    public void testGetPossibleSplits() throws Exception {
        HandSplitter hs = new HandSplitter();
        Player player = new HumanPlayer(WIND_EAST);
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        Tile tile = new Tile(MAN_9);
        Hand hand = new Hand(tiles, tile, player);
        SplitHand splitHand = hs.splitHand(hand);
        System.out.println(splitHand);
    }
}