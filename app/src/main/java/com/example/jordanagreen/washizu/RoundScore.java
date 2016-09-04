package com.example.jordanagreen.washizu;

/**
 * Created by Jordan on 9/3/2016.
 */
public class RoundScore {

    private static final String COLOR_STRING = "<font color='%s'>%s</font>";
    private static final String RED = "#FF0000";
    private static final String BLUE = "#00FF00";

    private int scoreChange;
    private String scoreColor;

    public RoundScore(int scoreChange){
        this.scoreChange = scoreChange;
        if (scoreChange > 0){
            scoreColor = BLUE;
        } else if (scoreChange < 0){
            scoreColor = RED;
        }
    }

    public String toString(){
        if (scoreChange == 0) return scoreChange + "";
        String sign = (scoreChange > 0) ? "+" : "-";
        return String.format(COLOR_STRING, scoreColor, sign + Math.abs(scoreChange));
    }

}
