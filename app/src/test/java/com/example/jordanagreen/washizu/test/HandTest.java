package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Constants;
import com.example.jordanagreen.washizu.Hand;
import com.example.jordanagreen.washizu.Tile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Jordan on 9/10/2015.
 */
public class HandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAddTile(){
        Hand hand = new Hand(Constants.SEAT_DOWN);
        assertEquals(hand.tiles.size(), 0);
        hand.addTile(new Tile(Constants.CHUN));
        assertEquals(hand.tiles.size(), 1);
    }

    @Test
    public void testAddTile_FullHand(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Hand is full");
        Hand hand = new Hand(Constants.SEAT_DOWN);
        assertEquals(hand.tiles.size(), 0);
        for (int i = 0; i < Constants.HAND_SIZE; i++){
            hand.addTile(new Tile(Constants.CHUN));
        }
        assertEquals(hand.tiles.size(), Constants.HAND_SIZE);
        hand.addTile(new Tile(Constants.CHUN));
    }

}