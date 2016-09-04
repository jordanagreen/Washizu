package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ScoresActivity extends Activity {

    private ScoreView mScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        mScoreView = (ScoreView) findViewById(R.id.score_view);
        mScoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: have it send back a result for starting the next round
                finish();
            }
        });
    }


}
