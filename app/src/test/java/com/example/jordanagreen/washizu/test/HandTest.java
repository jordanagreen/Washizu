package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.AiPlayer;
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
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        assertEquals(hand.getTiles().size(), 0);
        hand.addTile(new Tile(Constants.CHUN, false));
        assertEquals(hand.getTiles().size(), 1);
    }

    @Test
    public void testAddTile_FullHand(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Hand is full");
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        assertEquals(hand.getTiles().size(), 0);
        for (int i = 0; i < Constants.HAND_SIZE; i++){
            hand.addTile(new Tile(Constants.CHUN, false));
        }
        assertEquals(hand.getTiles().size(), Constants.HAND_SIZE);
        hand.addTile(new Tile(Constants.CHUN, false));
    }

    @Test
    public void testMakePon(){
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        assertEquals(hand.getMelds().size(), 0);
        Tile a = new Tile(Constants.CHUN, false);
        Tile b = new Tile(Constants.CHUN, false);
        Tile c = new Tile(Constants.CHUN, false);
        hand.makePon(a, b, c, Constants.SEAT_DOWN);
        assertEquals(hand.getMelds().size(), 1);
    }

    @Test
    public void testMakePon_WrongTiles(){
        thrown.expect(IllegalArgumentException.class);
        Tile a = new Tile(Constants.CHUN, false);
        Tile b = new Tile(Constants.CHUN, false);
        Tile c = new Tile(Constants.HAKU, false);
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        hand.makePon(a, b, c, Constants.SEAT_DOWN);
    }

    @Test
    public void testMakeChii(){
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        assertEquals(hand.getMelds().size(), 0);
        Tile a = new Tile(Constants.MAN_1, false);
        Tile b = new Tile(Constants.MAN_2, false);
        Tile c = new Tile(Constants.MAN_3, false);
        hand.makeChii(a, b, c);
        assertEquals(hand.getMelds().size(), 1);
    }

    @Test
    public void testMakeKan(){
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        assertEquals(hand.getMelds().size(), 0);
        Tile a = new Tile(Constants.CHUN, false);
        Tile b = new Tile(Constants.CHUN, false);
        Tile c = new Tile(Constants.CHUN, false);
        Tile d = new Tile(Constants.CHUN, false);
        hand.makeKan(a, b, c, d, Constants.SEAT_DOWN, false);
        assertEquals(hand.getMelds().size(), 1);
    }

    @Test
    public void testMakeKan_WrongTiles(){
        thrown.expect(IllegalArgumentException.class);
        Tile a = new Tile(Constants.CHUN, false);
        Tile b = new Tile(Constants.CHUN, false);
        Tile c = new Tile(Constants.HAKU, false);
        Tile d = new Tile(Constants.HATSU, false);
        Hand hand = new Hand(new AiPlayer(Constants.SEAT_DOWN));
        hand.makeKan(a, b, c, d, Constants.SEAT_DOWN, false);
    }
}