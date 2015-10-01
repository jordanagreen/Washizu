package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Tile;

import org.junit.Test;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_2;
import static com.example.jordanagreen.washizu.Constants.MAN_3;
import static com.example.jordanagreen.washizu.Constants.PIN_1;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SUIT_HONOR;
import static com.example.jordanagreen.washizu.Constants.SUIT_MAN;
import static com.example.jordanagreen.washizu.Constants.SUIT_PIN;
import static com.example.jordanagreen.washizu.Constants.SUIT_SOU;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


/**
 * Created by Jordan on 9/17/2015.
 */
public class TileTest  {


    @Test
    public void testGetSuit() throws Exception {
        Tile tilea = new Tile(MAN_1, false);
        Tile tileb = new Tile(PIN_1, false);
        Tile tilec = new Tile(SOU_1, false);
        Tile tiled = new Tile(CHUN, false);
        assertEquals(tilea.getSuit(), SUIT_MAN);
        assertEquals(tileb.getSuit(), SUIT_PIN);
        assertEquals(tilec.getSuit(), SUIT_SOU);
        assertEquals(tiled.getSuit(), SUIT_HONOR);
    }

    @Test
    public void testCompareTo() throws Exception {
        Tile tilea = new Tile(MAN_1, false);
        Tile tileb = new Tile(MAN_2, false);
        Tile tilec = new Tile(MAN_2, false);
        Tile tiled = new Tile(MAN_3, false);
        assertEquals(tileb.compareTo(tilec), 0);
        assertTrue(tilea.compareTo(tilec) < 0);
        assertTrue(tiled.compareTo(tilec) > 0);
    }
}