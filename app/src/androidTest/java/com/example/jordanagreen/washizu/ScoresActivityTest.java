package com.example.jordanagreen.washizu;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordan on 9/4/2016.
 */
public class ScoresActivityTest extends ActivityInstrumentationTestCase2<ScoresActivity> {

    private ScoresActivity mScoresActivity;

    public ScoresActivityTest() {
        super(ScoresActivity.class);
    }

    @Override
    protected void setUp(){
        mScoresActivity = getActivity();
    }

    public void testBlankScore(){
        mScoresActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScoresActivity.setScoreText(new ArrayList<List<RoundScore>>());
                assertEquals(mScoresActivity.getScoreText(), "");
            }
        });

    }

    public void testSingleRoundScore(){
        mScoresActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] scoreChanges = new int[]{500, 100, 0, -250};
                List<RoundScore> scores = new ArrayList<>();
                for (int scoreChange: scoreChanges){
                    scores.add(new RoundScore(scoreChange));
                }
                List<List<RoundScore>> roundScores = new ArrayList<>();
                roundScores.add(scores);
                mScoresActivity.setScoreText(roundScores);
                for (int scoreChange: scoreChanges){
                    assertTrue(mScoresActivity.getScoreText().contains(Integer.toString(scoreChange)));
                }
            }
        });
    }

    public void testTwoRoundsScore(){
        mScoresActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] scoreChanges = new int[]{0,0,0,0};
                List<RoundScore> scores = new ArrayList<>();
                for (int scoreChange: scoreChanges){
                    scores.add(new RoundScore(scoreChange));
                }
                List<List<RoundScore>> roundScores = new ArrayList<>();
                roundScores.add(scores);
                roundScores.add(scores);
                mScoresActivity.setScoreText(roundScores);
                assertEquals(mScoresActivity.getScoreText(), "0\t0\t0\t0\t\n0\t0\t0\t0\t\n");
            }
        });
    }

    public void testScoreColors(){
        mScoresActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int[] increases = new int[]{100, 0, 0, 0};
                List<RoundScore> scores = new ArrayList<>();
                for (int scoreChange: increases){
                    scores.add(new RoundScore(scoreChange));
                }
                List<List<RoundScore>> roundScores = new ArrayList<>();
                roundScores.add(scores);
                mScoresActivity.setScoreText(roundScores);
                assertTrue(mScoresActivity.getScoreText().contains("#00FF00"));
                int[] decreases = new int[]{-100, 0, 0, 0};
                scores = new ArrayList<>();
                for (int scoreChange: decreases){
                    scores.add(new RoundScore(scoreChange));
                }
                roundScores = new ArrayList<>();
                roundScores.add(scores);
                mScoresActivity.setScoreText(roundScores);
                assertTrue(mScoresActivity.getScoreText().contains("#FF0000"));
                assertFalse(mScoresActivity.getScoreText().contains("00FF00"));
            }
        });
    }


}
