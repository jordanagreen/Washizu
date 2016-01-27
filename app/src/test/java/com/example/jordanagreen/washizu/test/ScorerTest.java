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
import static com.example.jordanagreen.washizu.Constants.CLOSED_HAN_KOKUSHI_MUSOU;
import static com.example.jordanagreen.washizu.Constants.HAKU;
import static com.example.jordanagreen.washizu.Constants.HATSU;
import static com.example.jordanagreen.washizu.Constants.MAN_1;
import static com.example.jordanagreen.washizu.Constants.MAN_9;
import static com.example.jordanagreen.washizu.Constants.NAN;
import static com.example.jordanagreen.washizu.Constants.PEI;
import static com.example.jordanagreen.washizu.Constants.PIN_1;
import static com.example.jordanagreen.washizu.Constants.PIN_9;
import static com.example.jordanagreen.washizu.Constants.SOU_1;
import static com.example.jordanagreen.washizu.Constants.SOU_9;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;
import static com.example.jordanagreen.washizu.Constants.YAKU_KOKUSHI_MUSOU;
import static org.junit.Assert.assertEquals;


/**
 * Created by Jordan on 1/26/2016.
 */
public class ScorerTest {

    @Test
    public void testKokushiMusou(){
        Player player = new HumanPlayer(Constants.WIND_EAST);
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, XIA);
        Tile drawnTile = new Tile(TON);
        Hand hand = new Hand(kokushiTiles, drawnTile, player);
        Scorer scorer = new Scorer(hand, drawnTile);
        scorer.scoreHand();
        Score score = scorer.getScore();
        int[] han = score.getHan();
        assertEquals(han[YAKU_KOKUSHI_MUSOU], CLOSED_HAN_KOKUSHI_MUSOU);
    }

    @Test
    public void testDoubleKokushiMusou(){
        Player player = new HumanPlayer(Constants.WIND_EAST);
        List<Integer> kokushiTiles = Arrays.asList(MAN_1, MAN_9, PIN_1, PIN_9, SOU_1, SOU_9,
                CHUN, HAKU, HATSU, NAN, PEI, XIA, TON);
        Tile drawnTile = new Tile(PIN_1);
        Hand hand = new Hand(kokushiTiles, drawnTile, player);
        Scorer scorer = new Scorer(hand, drawnTile);
        scorer.scoreHand();
        Score score = scorer.getScore();
        int[] han = score.getHan();
        assertEquals(han[YAKU_KOKUSHI_MUSOU], CLOSED_HAN_KOKUSHI_MUSOU*2);
    }

}
