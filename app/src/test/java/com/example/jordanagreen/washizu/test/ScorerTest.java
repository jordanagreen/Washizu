package com.example.jordanagreen.washizu.test;

import com.example.jordanagreen.washizu.Constants;
import com.example.jordanagreen.washizu.Hand;
import com.example.jordanagreen.washizu.HumanPlayer;
import com.example.jordanagreen.washizu.Player;
import com.example.jordanagreen.washizu.Score;
import com.example.jordanagreen.washizu.Scorer;
import com.example.jordanagreen.washizu.Tile;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.CHUN;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_KOKUSHI_MUSOU;
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
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;
import static com.example.jordanagreen.washizu.Constants.YAKU_CHII_TOITSU;
import static com.example.jordanagreen.washizu.Constants.YAKU_CHUUREN_POUTOU;
import static com.example.jordanagreen.washizu.Constants.YAKU_KOKUSHI_MUSOU;
import static org.junit.Assert.assertEquals;


/**
 * Created by Jordan on 1/26/2016.
 */
public class ScorerTest {

    private Score scoreHand(List<Integer> tiles, int drawn){
        Player player = new HumanPlayer(Constants.WIND_EAST);
        Tile drawnTile = new Tile(drawn);
        Hand hand = new Hand(tiles, drawnTile, player);
        Scorer scorer = new Scorer(hand, drawnTile);
        scorer.scoreHand();
        return scorer.getScore();
    }

    @Test
    public void testKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, XIA);
        Score score = scoreHand(kokushiTiles, TON);
        assertEquals(score.getHan()[YAKU_KOKUSHI_MUSOU], CLOSED_HAN_KOKUSHI_MUSOU);
    }

    @Test
    public void testDoubleKokushiMusou(){
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON);
        Score score = scoreHand(kokushiTiles, PIN_1);
        assertEquals(score.getHan()[YAKU_KOKUSHI_MUSOU], CLOSED_HAN_KOKUSHI_MUSOU*2);
    }

    @Test
    public void testWrongKokushiMusou(){
        //all kokushi tiles but three xia
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, XIA, XIA, XIA);
        Score score = scoreHand(kokushiTiles, PIN_1);
        assertEquals(score.getHan()[YAKU_KOKUSHI_MUSOU], 0);
        //non-kokushi tile
        kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, XIA, XIA, PIN_2);
        score = scoreHand(kokushiTiles, PIN_1);
        assertEquals(score.getHan()[YAKU_KOKUSHI_MUSOU], 0);
    }

    @Test
    public void testChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        Score score = scoreHand(chiiToitsuTiles, MAN_7);
        assertEquals(score.getHan()[YAKU_CHII_TOITSU], CLOSED_HAN_CHII_TOITSU);
    }

    @Test
    public void testWrongChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_7);
        Score score = scoreHand(chiiToitsuTiles, MAN_8);
        assertEquals(score.getHan()[YAKU_CHII_TOITSU], 0);
    }

    @Test
    public void testFourOfAKindChiiToitsu(){
        List<Integer> chiiToitsuTiles = Arrays.asList(MAN_1, MAN_1, MAN_2, MAN_2, MAN_3, MAN_3,
                MAN_4, MAN_4, MAN_5, MAN_5, MAN_6, MAN_6, MAN_6);
        Score score = scoreHand(chiiToitsuTiles, MAN_6);
        assertEquals(score.getHan()[YAKU_CHII_TOITSU], 0);
    }

    @Test
    public void testChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_8, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_9);
        assertEquals(score.getHan()[YAKU_CHUUREN_POUTOU], CLOSED_HAN_CHUUREN_POUTOU);
    }

    @Test
       public void testDoubleChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_9, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_5);
        assertEquals(score.getHan()[YAKU_CHUUREN_POUTOU], CLOSED_HAN_CHUUREN_POUTOU*2);
    }

    @Test
    public void testWrongChuurenPoutou(){
        List<Integer> chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, MAN_1, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_9, MAN_9, MAN_9);
        Score score = scoreHand(chuurenPoutouTiles, MAN_5);
        assertEquals(score.getHan()[YAKU_CHUUREN_POUTOU], 0);
        chuurenPoutouTiles = Arrays.asList(MAN_1, MAN_1, MAN_1, PIN_2, MAN_3, MAN_4,
                MAN_5, MAN_6, MAN_7, MAN_8, MAN_9, MAN_9, MAN_9);
        score = scoreHand(chuurenPoutouTiles, MAN_5);
        assertEquals(score.getHan()[YAKU_CHUUREN_POUTOU], 0);
    }

}
