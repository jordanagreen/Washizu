package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Game;
import com.example.jordanagreen.washizu.HumanPlayer;
import com.example.jordanagreen.washizu.Player;
import com.example.jordanagreen.washizu.SeatDirection;
import com.example.jordanagreen.washizu.Tile;
import com.example.jordanagreen.washizu.Wind;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_2;
import static com.example.jordanagreen.washizu.Constants.MAN_3;
import static com.example.jordanagreen.washizu.Constants.MAN_4;
import static com.example.jordanagreen.washizu.Constants.MAN_5;
import static com.example.jordanagreen.washizu.Constants.MAN_6;
import static com.example.jordanagreen.washizu.Constants.MAN_7;
import static com.example.jordanagreen.washizu.Constants.MAN_9;
import static com.example.jordanagreen.washizu.Constants.NAN;
import static com.example.jordanagreen.washizu.Constants.PEI;
import static com.example.jordanagreen.washizu.Constants.PIN_1;
import static com.example.jordanagreen.washizu.Constants.PIN_2;
import static com.example.jordanagreen.washizu.Constants.PIN_3;
import static com.example.jordanagreen.washizu.Constants.PIN_4;
import static com.example.jordanagreen.washizu.Constants.SOU_3;
import static com.example.jordanagreen.washizu.Constants.SOU_4;
import static com.example.jordanagreen.washizu.Constants.SOU_5;
import static com.example.jordanagreen.washizu.Constants.SOU_6;
import static com.example.jordanagreen.washizu.Constants.SOU_7;
import static com.example.jordanagreen.washizu.Constants.SOU_8;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TON;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Jordan on 6/28/2016.
 */
public class PlayerTest {

    private Game mGame;
    private Player mPlayer;

    @Before
    public void setUp(){
        mGame = new Game(null);
        mPlayer = new HumanPlayer(SeatDirection.DOWN);
        mPlayer.setGame(mGame);
        mPlayer.setWind(Wind.NORTH);
    }

    private void setHand(List<Integer> tiles){
        for (Integer id: tiles){
            mPlayer.getHand().addTile(new Tile(id));
        }
    }

    @Test
    public void testCanRon_Valid(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, MAN_6, PIN_2,
                PIN_3, SOU_6, SOU_7, SOU_8, SOU_9, SOU_9);
        setHand(tiles);
        assertTrue(mPlayer.canRon(new Tile(PIN_4)));
        assertTrue(mPlayer.canRon(new Tile(PIN_1)));
    }

    @Test
    public void testCanRon_ValidChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_9, MAN_9, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        setHand(chiiToitsuTiles);
        Tile winningTile = new Tile(MAN_7);
        assertTrue(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_Invalid(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        setHand(tiles);
        Tile winningTile = new Tile(CHUN);
        assertFalse(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_RoundWind(){
        List<Integer> tiles = Arrays.asList(TON, TON, TON, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        setHand(tiles);
        Tile winningTile = new Tile(MAN_6);
        assertTrue(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_PlayerWind(){
        List<Integer> tiles = Arrays.asList(PEI, PEI, PEI, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        setHand(tiles);
        Tile winningTile = new Tile(MAN_6);
        assertTrue(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_WrongWind(){
        List<Integer> tiles = Arrays.asList(NAN, NAN, NAN, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        setHand(tiles);
        Tile winningTile = new Tile(MAN_6);
        assertFalse(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_Furiten(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        setHand(tiles);
        Tile winningTile = new Tile(MAN_6);
        mPlayer.getDiscardPool().addTile(new Tile(MAN_6), false);
        assertFalse(mPlayer.canRon(winningTile));
    }

    @Test
    public void testCanRon_FuritenOnOtherWait(){
        //TODO: calculate other waits and check them for furiten
    }

    @Test
    public void testCanTsumo_Valid(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, MAN_6, PIN_2,
                PIN_3, SOU_6, SOU_7, SOU_8, SOU_9, SOU_9);
        setHand(tiles);
        mPlayer.getHand().setDrawnTile(new Tile(PIN_4));
        assertTrue(mPlayer.canTsumo());
        mPlayer.getHand().setDrawnTile(new Tile(PIN_1));
        assertTrue(mPlayer.canTsumo());
    }

    @Test
    public void testCanTsumo_Invalid(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, MAN_6, PIN_2,
                PIN_3, SOU_6, SOU_7, SOU_8, SOU_9, SOU_9);
        setHand(tiles);
        mPlayer.getHand().setDrawnTile(new Tile(MAN_2));
        assertFalse(mPlayer.canTsumo());
    }


}
