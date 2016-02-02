package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Hand;
import com.example.jordanagreen.washizu.HumanPlayer;
import com.example.jordanagreen.washizu.Player;
import com.example.jordanagreen.washizu.Score;
import com.example.jordanagreen.washizu.Scorer;
import com.example.jordanagreen.washizu.SeatDirection;
import com.example.jordanagreen.washizu.Tile;
import com.example.jordanagreen.washizu.Wind;
import com.example.jordanagreen.washizu.Yaku;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
import static com.example.jordanagreen.washizu.Constants.PIN_2;
import static com.example.jordanagreen.washizu.Constants.PIN_4;
import static com.example.jordanagreen.washizu.Constants.PIN_5;
import static com.example.jordanagreen.washizu.Constants.PIN_6;
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_3;
import static com.example.jordanagreen.washizu.Constants.SOU_4;
import static com.example.jordanagreen.washizu.Constants.SOU_5;
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

    private Score scoreHand(List<Integer> tiles, int drawn, boolean isTsumo){
        Player player = new HumanPlayer(SeatDirection.DOWN);
        player.setWind(Wind.EAST);
        Tile drawnTile = new Tile(drawn);
        Hand hand = new Hand(tiles, drawnTile, player);
        Scorer scorer = new Scorer();
        return scorer.scoreHand(hand, Wind.EAST, isTsumo);
    }

    private Score scoreHand(List<Integer> tiles, int drawn){
        return scoreHand(tiles, drawn, true);
    }

    @Test
    public void testKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, XIA);
        Score score = scoreHand(kokushiTiles, TON);
        assertEquals(score.getHan()[Yaku.KOKUSHI_MUSOU.ordinal()], Yaku.KOKUSHI_MUSOU.getClosedHan());
    }

    @Test
    public void testDoubleKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON);
        Score score = scoreHand(kokushiTiles, PIN_1);
        assertEquals(score.getHan()[Yaku.KOKUSHI_MUSOU.ordinal()], Yaku.KOKUSHI_MUSOU.getClosedHan()*2);
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
        assertEquals(score.getHan()[Yaku.CHII_TOITSU.ordinal()], Yaku.CHII_TOITSU.getClosedHan());
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
        assertEquals(score.getHan()[Yaku.CHUUREN_POUTOU.ordinal()], Yaku.CHUUREN_POUTOU.getClosedHan());
    }

    @Test
    public void testDoubleChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_9, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_5);
        assertEquals(score.getHan()[Yaku.CHUUREN_POUTOU.ordinal()], Yaku.CHUUREN_POUTOU.getClosedHan()*2);
    }

    @Test
    public void testTanYao(){
        List<Integer> tiles = Arrays.asList(MAN_2, MAN_3, MAN_4, PIN_4, PIN_5, PIN_6, SOU_6,
                SOU_7, SOU_8, MAN_5, MAN_6, MAN_7, MAN_8);
        Score score = scoreHand(tiles, MAN_8);
        assertEquals(score.getHan()[Yaku.TAN_YAO.ordinal()], Yaku.TAN_YAO.getClosedHan());
    }

    @Test
    public void testChinRouTou(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_1, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, MAN_9, MAN_9, MAN_9, SOU_1);
        Score score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.CHINROUTOU.ordinal()], Yaku.CHINROUTOU.getClosedHan());
    }

    @Test
    public void testTsuuIiSou(){
        List<Integer> tiles = Arrays.asList(CHUN, CHUN, CHUN, NAN, NAN, NAN, PEI,
                PEI, PEI, HAKU, HAKU, HAKU, HATSU);
        Score score = scoreHand(tiles, HATSU);
        assertEquals(score.getHan()[Yaku.TSUUIISOU.ordinal()], Yaku.TSUUIISOU.getClosedHan());
    }

    @Test
    public void testDaiSanGen(){
        List<Integer> tiles = Arrays.asList(CHUN, CHUN, CHUN, HATSU, HATSU, HATSU, MAN_2,
                MAN_3, MAN_4, HAKU, HAKU, HAKU, SOU_3);
        Score score = scoreHand(tiles, SOU_3);
        assertEquals(score.getHan()[Yaku.DAISANGEN.ordinal()], Yaku.DAISANGEN.getClosedHan());
    }

    @Test
    public void testHonRouTou(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_9, MAN_9, PIN_1, PIN_1, SOU_9,
                SOU_9, XIA, XIA, PEI, PEI, SOU_1);
        Score score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.HONROUTOU.ordinal()], Yaku.HONROUTOU.getClosedHan());
    }

    @Test
    public void testDaiSuuShii(){
        List<Integer> tiles = Arrays.asList(PEI, PEI, PEI, NAN, NAN, NAN, TON,
                TON, TON, XIA, XIA, XIA, SOU_3);
        Score score = scoreHand(tiles, SOU_3);
        assertEquals(score.getHan()[Yaku.DAISUUSHI.ordinal()], Yaku.DAISUUSHI.getClosedHan());
    }

    @Test
    public void testFanpai(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, PEI, PEI, PEI, SOU_1);
        Score score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.FANPAI.ordinal()], 0);
        tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, CHUN, CHUN, CHUN, SOU_1);
        score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.FANPAI.ordinal()], Yaku.FANPAI.getClosedHan());
        tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, PIN_1, PIN_1, PIN_1, SOU_9,
                SOU_9, SOU_9, XIA, XIA, XIA, SOU_1);
        score = scoreHand(tiles, SOU_1);
        assertEquals(score.getHan()[Yaku.FANPAI.ordinal()], Yaku.FANPAI.getClosedHan() * 2);
    }

    @Test
    public void testShouSuuShii(){
        List<Integer> tiles = Arrays.asList(PEI, PEI, PEI, NAN, NAN, NAN, TON,
                TON, TON, SOU_3, SOU_3, SOU_3, XIA);
        Score score = scoreHand(tiles, XIA);
        assertEquals(score.getHan()[Yaku.SHOUSUUSHI.ordinal()], Yaku.SHOUSUUSHI.getClosedHan());
    }

    @Test
    public void testSuuAnkou(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_2, MAN_2, TON,
                TON, TON, SOU_3, SOU_3, SOU_3, XIA);
        Score score = scoreHand(tiles, XIA);
        assertEquals(score.getHan()[Yaku.SUU_ANKOU.ordinal()], Yaku.SUU_ANKOU.getClosedHan());
        //try with calling ron to make a pon, should not be suu ankou
        tiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_2, MAN_2, TON,
                TON, TON, SOU_3, SOU_3, XIA, XIA);
        Player player = new HumanPlayer(SeatDirection.DOWN);
        player.setWind(Wind.EAST);
        Tile drawnTile = new Tile(SOU_3);
        Hand hand = new Hand(tiles, drawnTile, player);
        Scorer scorer = new Scorer();
        score = scorer.scoreHand(hand, Wind.EAST, false);
        assertEquals(score.getHan()[Yaku.SUU_ANKOU.ordinal()], 0);
    }

    @Test
    public void testPinFu(){
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, SOU_3, SOU_3);
        Score score = scoreHand(tiles, MAN_6);
        assertEquals(score.getHan()[Yaku.PINFU.ordinal()], Yaku.PINFU.getClosedHan());
    }

    @Test
    public void testWrongPinFu(){
        //pair of xia
        List<Integer> tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_5, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, XIA, XIA);
        Score score = scoreHand(tiles, MAN_6);
        assertEquals(score.getHan()[Yaku.PINFU.ordinal()], 0);
        //one sided wait
        tiles = Arrays.asList(MAN_1, MAN_2, MAN_3, MAN_4, MAN_6, SOU_3, SOU_4,
                SOU_5, MAN_2, MAN_3, MAN_4, XIA, XIA);
        score = scoreHand(tiles, MAN_5);
        assertEquals(score.getHan()[Yaku.PINFU.ordinal()], 0);
    }

}
