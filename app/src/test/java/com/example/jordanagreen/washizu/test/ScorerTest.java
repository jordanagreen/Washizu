package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Hand;
import com.example.jordanagreen.washizu.HumanPlayer;
import com.example.jordanagreen.washizu.Player;
import com.example.jordanagreen.washizu.Score;
import com.example.jordanagreen.washizu.Scorer;
import com.example.jordanagreen.washizu.SeatDirection;
import com.example.jordanagreen.washizu.Tile;
import com.example.jordanagreen.washizu.Yaku;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHINROUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_DAISANGEN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_DAISUUSHI;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_HONROUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TAN_YAO;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_TSUUIISOU;
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
import static com.example.jordanagreen.washizu.Constants.PIN_2;
import static com.example.jordanagreen.washizu.Constants.PIN_4;
import static com.example.jordanagreen.washizu.Constants.PIN_5;
import static com.example.jordanagreen.washizu.Constants.PIN_6;
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_3;
import static com.example.jordanagreen.washizu.Constants.SOU_6;
import static com.example.jordanagreen.washizu.Constants.SOU_7;
import static com.example.jordanagreen.washizu.Constants.SOU_8;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Created by Jordan on 1/26/2016.
 */
public class ScorerTest {

    private Score scoreHand(List<Integer> tiles, int drawn){
        Player player = new HumanPlayer(SeatDirection.DOWN);
        Tile drawnTile = new Tile(drawn);
        Hand hand = new Hand(tiles, drawnTile, player);
        Scorer scorer = new Scorer();
        return scorer.scoreHand(hand);
//        return scorer.getScore();
    }

    @Test
    public void testKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, XIA);
        Score score = scoreHand(kokushiTiles, TON);
        assertEquals(score.getHan()[Yaku.KOKUSHI_MUSOU.ordinal()], CLOSED_HAN_KOKUSHI_MUSOU);
    }

    @Test
    public void testDoubleKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON);
        Score score = scoreHand(kokushiTiles, PIN_1);
        assertEquals(score.getHan()[Yaku.KOKUSHI_MUSOU.ordinal()], CLOSED_HAN_KOKUSHI_MUSOU*2);
    }

    @Test
    public void testWrongKokushiMusou(){
        //all kokushi tiles but three xia
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, XIA, XIA, XIA);
        Score score = scoreHand(kokushiTiles, PIN_1);
        assertNull(score);
        //non-kokushi tile
        kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, XIA, XIA, PIN_2);
        score = scoreHand(kokushiTiles, PIN_1);
        assertNull(score);
    }

    @Test
    public void testChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        Score score = scoreHand(chiiToitsuTiles, MAN_7);
        assertEquals(score.getHan()[Yaku.CHII_TOITSU.ordinal()], CLOSED_HAN_CHII_TOITSU);
    }

    @Test
    public void testWrongChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        Score score = scoreHand(chiiToitsuTiles, MAN_8);
        assertNull(score);
    }

    @Test
    public void testFourOfAKindChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_6);
        Score score = scoreHand(chiiToitsuTiles, MAN_6);
        assertEquals(score.getHan()[Yaku.CHII_TOITSU.ordinal()], 0);
    }

    @Test
    public void testChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_8, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_9);
        assertEquals(score.getHan()[Yaku.CHUUREN_POUTOU.ordinal()], CLOSED_HAN_CHUUREN_POUTOU);
    }

    @Test
    public void testDoubleChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_9, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_5);
        assertEquals(score.getHan()[Yaku.CHUUREN_POUTOU.ordinal()], CLOSED_HAN_CHUUREN_POUTOU*2);
    }

    @Test
    public void testTanYao(){
        List<Integer> tiles = Arrays.asList(MAN_2, MAN_3, MAN_4, PIN_4, PIN_5, PIN_6, SOU_6,
                SOU_7, SOU_8, MAN_5, MAN_6, MAN_7, MAN_8);
        Score score = scoreHand(tiles, MAN_8);
        assertEquals(score.getHan()[Yaku.TAN_YAO.ordinal()], CLOSED_HAN_TAN_YAO);
    }

    @Test
    public void testChinRouTou(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_1, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, MAN_9, MAN_9, MAN_9, SOU_1);
        Score score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.CHINROUTOU.ordinal()], CLOSED_HAN_CHINROUTOU);
    }

    @Test
    public void testTsuuIiSou(){
        List<Integer> tiles = Arrays.asList(CHUN, CHUN, CHUN, NAN, NAN, NAN, PEI,
                PEI, PEI, HAKU, HAKU, HAKU, HATSU);
        Score score = scoreHand(tiles, HATSU);
        assertEquals(score.getHan()[Yaku.TSUUIISOU.ordinal()], CLOSED_HAN_TSUUIISOU);
    }

    @Test
    public void testDaiSanGen(){
        List<Integer> tiles = Arrays.asList(CHUN, CHUN, CHUN, HATSU, HATSU, HATSU, MAN_2,
                MAN_3, MAN_4, HAKU, HAKU, HAKU, SOU_3);
        Score score = scoreHand(tiles, SOU_3);
        assertEquals(score.getHan()[Yaku.DAISANGEN.ordinal()], CLOSED_HAN_DAISANGEN);
    }

    @Test
    public void testHonRouTou(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_1, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, PEI, PEI, PEI, SOU_1);
        Score score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.HONROUTOU.ordinal()], CLOSED_HAN_HONROUTOU);
    }

    @Test
    public void testDaiSuuShii(){
        List<Integer> tiles = Arrays.asList(PEI, PEI, PEI, NAN, NAN, NAN, TON,
                TON, TON, XIA, XIA, XIA, SOU_3);
        Score score = scoreHand(tiles, SOU_3);
        assertEquals(score.getHan()[Yaku.DAISUUSHI.ordinal()], CLOSED_HAN_DAISUUSHI);
    }

}
