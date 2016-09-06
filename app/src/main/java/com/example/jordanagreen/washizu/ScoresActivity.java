package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import static com.example.jordanagreen.washizu.Constants.NUMBER_OF_PLAYERS;

public class ScoresActivity extends Activity {

    private TextView mScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        mScoreView = (TextView) findViewById(R.id.score_view);
        mScoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: have it send back a result for starting the next round
                finish();
            }
        });
    }

    public void setScoreText(List<List<RoundScore>> roundScores){
        StringBuilder sb = new StringBuilder();
        for (List<RoundScore> round: roundScores){
            if (round.size() != NUMBER_OF_PLAYERS){
                throw new IllegalArgumentException(String.format("Round has %d players, not %d: %s",
                        roundScores.size(), NUMBER_OF_PLAYERS, Arrays.toString(round.toArray())));
            }
            for (RoundScore score: round){
                sb.append(score).append('\t');
            }
            sb.append('\n');
        }
        mScoreView.setText(sb.toString());
    }

    public String getScoreText(){
        return mScoreView.getText().toString();
    }

}
