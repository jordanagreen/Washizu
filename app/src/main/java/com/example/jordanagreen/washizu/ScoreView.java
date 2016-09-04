package com.example.jordanagreen.washizu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

import static com.example.jordanagreen.washizu.Constants.NUMBER_OF_PLAYERS;

/**
 * Created by Jordan on 8/31/2016.
 */
public class ScoreView extends TextView {

    private String text;

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //TODO: actually show the scores here

    public void addScore(List<RoundScore> roundScores){
        if (roundScores.size() != NUMBER_OF_PLAYERS){
            throw new IllegalArgumentException(String.format("Round has %d players, not %d",
                    roundScores.size(), NUMBER_OF_PLAYERS));
        }
        StringBuilder sb = new StringBuilder();
        for (RoundScore score: roundScores){
            sb.append(score).append('\t');
        }
        sb.append('\n');
        text = text + sb.toString();
        setText(text);
    }
}
