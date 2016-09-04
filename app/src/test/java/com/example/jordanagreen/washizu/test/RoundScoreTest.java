package com.example.jordanagreen.washizu.test;


import com.example.jordanagreen.washizu.RoundScore;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jordan on 9/3/2016.
 */
public class RoundScoreTest {

    private RoundScore roundScore;

    @Test
    public void testNoChange(){
        roundScore = new RoundScore(0);
        assertEquals(roundScore.toString(), "0");
    }

    @Test
    public void testIncrease(){
        roundScore = new RoundScore(500);
        assertEquals(roundScore.toString(), "<font color='#00FF00'>+500</font>");
    }

    @Test
    public void testDecrease(){
        roundScore = new RoundScore(-500);
        assertEquals(roundScore.toString(), "<font color='#FF0000'>-500</font>");
    }
}
