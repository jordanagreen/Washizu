package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends Activity {

    public static final String GAME_STATE_KEY = "game_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
            String jsonString;
            if ((jsonString = savedInstanceState.getString(GAME_STATE_KEY)) != null){
                WashizuView washizuView = (WashizuView) findViewById(R.id.game_view);
                washizuView.restoreGameFromJsonString(jsonString);
            }
        }
        else {
            WashizuView washizuView = (WashizuView) findViewById(R.id.game_view);
            washizuView.startNewGame();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        WashizuView game = (WashizuView) findViewById(R.id.game_view);
        String gameState = game.getGameJsonAsString();
        if (gameState != null){
            outState.putString(GAME_STATE_KEY, gameState);
        }
        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//    }
}
