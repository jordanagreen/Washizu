package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameActivity extends Activity {

    //TODO: start with a list of json states to start from, or a new game

    public static final String TAG = "GameActivity";
    public static final String GAME_STATE_KEY = "game_state";
    public static final String EXTRA_FILENAME = "filename";

    private File mRootFolderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //TODO: switch to something like getExternalStorageDirectory once testing is done
        // for now it's easier to just put files in assets from a computer
        mRootFolderPath = Environment.getExternalStorageDirectory();

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
        WashizuView washizuView = (WashizuView) findViewById(R.id.game_view);
        try{
            Log.d(TAG, "Starting new game");
            washizuView.startNewGame();
        }
        catch (Exception e){
            Log.e(TAG, "An exception was thrown.", e);
            //save the state to see what went wrong
            String gameState = washizuView.getGameJsonAsString();
            if (gameState != null){
                String filename = "game-state.json";
                writeToFile(filename, gameState);
            }
        }
    }

    private void resumeGame(String jsonString){
        Log.d(TAG, "Resuming game");
        WashizuView washizuView = (WashizuView) findViewById(R.id.game_view);
        try{
            washizuView.restoreGameFromJsonString(jsonString);
        }
        catch (Exception e){
            Log.e(TAG, "An exception was thrown.", e);
            //save the state to see what went wrong
            String gameState = washizuView.getGameJsonAsString();
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
        WashizuView game = (WashizuView) findViewById(R.id.game_view);
        String gameState = game.getGameJsonAsString();
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
}
