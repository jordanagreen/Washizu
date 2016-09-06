package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.jordanagreen.washizu.Constants.ROUNDS_PER_WIND;

public class GameActivity extends Activity {

    public static final String TAG = "GameActivity";
    public static final String GAME_STATE_KEY = "game_state";
    public static final String EXTRA_FILENAME = "filename";

    private File mRootFolderPath;
    private WashizuView mWashizuView;
    private Button chiiButton;
    private Button ponButton;
    private Button kanButton;
    private Button ronButton;
    //TODO: make buttons wider so width doesn't change for tsumo button having more text
    private Button tsumoButton;
    private Button[] mButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mRootFolderPath = Environment.getExternalStorageDirectory();

        mWashizuView = (WashizuView) findViewById(R.id.game_view);
        chiiButton = (Button) findViewById(R.id.chii_button);
        ponButton = (Button) findViewById(R.id.pon_button);
        kanButton = (Button) findViewById(R.id.kan_button);
        ronButton = (Button) findViewById(R.id.ron_button);
        tsumoButton = (Button) findViewById(R.id.tsumo_button);
        mButtons = new Button[]{chiiButton, ponButton, kanButton, ronButton, tsumoButton};
        makeAllButtonsUnclickable();

        if (savedInstanceState != null){
            String jsonString;
            if ((jsonString = savedInstanceState.getString(GAME_STATE_KEY)) != null){
                resumeGame(jsonString);
            }
        }
        else if (getIntent().hasExtra(EXTRA_FILENAME)){
            String filename = getIntent().getStringExtra(EXTRA_FILENAME);
            //TODO: this should be uncommented for loading saved games, right now just working
            //on test cases in assets
//            File file = new File(mRootFolderPath, filename);
//            if (!file.exists()){
//                Log.d(TAG, "File " + file.getAbsolutePath() + " didn't exist");
//                startNewGame();
//            }
            try{
//                BufferedReader br = new BufferedReader(new FileReader(file));
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(getAssets().open(filename)));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null){
                    sb.append(s);
                }
                Log.d(TAG, "Resuming game " + filename);
                resumeGame(sb.toString());
            }
            catch (IOException e){
                startNewGame();
            }
        }
        else {
            startNewGame();
        }
    }

    private void startNewGame(){
        try{
            Log.d(TAG, "Starting new game");
            mWashizuView.startNewGame();
        }
        catch (Exception e){
            Log.e(TAG, "An exception was thrown.", e);
            //save the state to see what went wrong
            String gameState = mWashizuView.getGameJsonAsString();
            if (gameState != null){
                String filename = "game-state.json";
                writeToFile(filename, gameState);
            }
        }
    }

    private void resumeGame(String jsonString){
        Log.d(TAG, "Resuming game");
        try{
            mWashizuView.restoreGameFromJsonString(jsonString);
        }
        catch (Exception e){
            Log.e(TAG, "An exception was thrown.", e);
            //save the state to see what went wrong
            String gameState = mWashizuView.getGameJsonAsString();
            if (gameState != null){
                String filename = "game-state.json";
                writeToFile(filename, gameState);
            }
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
        String gameState = mWashizuView.getGameJsonAsString();
        if (gameState != null){
            outState.putString(GAME_STATE_KEY, gameState);
//            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
//            String filename = "game-state-" + df.format(Calendar.getInstance().getTime()) + ".json";
//            String filename = "game-state.json";
//            writeToFile(filename, gameState);
        }
        super.onSaveInstanceState(outState);
    }

    //use for reporting errors, maybe for saving games later
    private void writeToFile(String filename, String text){
        File file = new File(mRootFolderPath, filename);
        Log.d(TAG, "Wrote JSON to " + file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try  {
                fos.write(text.getBytes());
            }
            catch (IOException e){
                Log.e(TAG, "Error writing file", e);
            }
            finally {
                fos.close();
            }
        }
        catch (IOException e){
            Log.e(TAG, "Error writing file", e);
        }
    }

    public void onChiiButtonPressed(View v){
        makeAllButtonsUnclickable();
        mWashizuView.onButtonPressed(MeldType.CHII);
    }

    public void onPonButtonPressed(View v){
        makeAllButtonsUnclickable();
        mWashizuView.onButtonPressed(MeldType.PON);
    }

    public void onKanButtonPressed(View v){
        makeAllButtonsUnclickable();
        mWashizuView.onButtonPressed(MeldType.KAN);
    }

    public void onRonButtonPressed(View v){
        makeAllButtonsUnclickable();
        mWashizuView.onButtonPressed(MeldType.RON);
    }

    public void onTsumoButtonPressed(View v){
        makeAllButtonsUnclickable();
        mWashizuView.onButtonPressed(MeldType.TSUMO);
    }

    public void makeAllButtonsUnclickable(){
        for (Button button: mButtons){
            button.setAlpha(.3f);
            button.setClickable(false);
        }
    }

    public void makeButtonClickable(MeldType buttonType){
        Button button;
        switch (buttonType){
            case CHII:
                button = chiiButton;
                break;
            case PON:
                button = ponButton;
                break;
            case KAN:
                button = kanButton;
                break;
            case RON:
                button = ronButton;
                button.setVisibility(View.VISIBLE);
                tsumoButton.setVisibility(View.GONE);
                break;
            case TSUMO:
                button = tsumoButton;
                button.setVisibility(View.VISIBLE);
                ronButton.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalArgumentException();
        }
        button.setAlpha(1f);
        button.setClickable(true);
    }

    public void showScores(){
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: use different result codes for going to next round, ending game, etc.
        mWashizuView.startNextRound();
//        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateRoundNumberText(int roundNumber){
        TextView t = (TextView) findViewById(R.id.round_text);
        String s = (roundNumber <= ROUNDS_PER_WIND ?
                getResources().getString(R.string.round_east) :
                getResources().getString(R.string.round_south)) +
                " " + ((roundNumber % ROUNDS_PER_WIND) + 1);
        t.setText(s);
    }

    public void updatePlayerWindText(int dealerIndex){
        TextView down = (TextView) findViewById(R.id.wind_down);
        TextView right = (TextView) findViewById(R.id.wind_right);
        TextView up = (TextView) findViewById(R.id.wind_up);
        TextView left = (TextView) findViewById(R.id.wind_left);
        TextView[] seats = new TextView[]{down, right, up, left};
        Wind[] winds = Wind.values();
        for (int i = 0; i < winds.length; i++){
            int seat = (dealerIndex + i) % seats.length;
            seats[seat].setText(winds[i].getAbbreviation());
        }
    }

}
