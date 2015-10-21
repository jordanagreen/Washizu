package com.example.jordanagreen.washizu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
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
//            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
//            String filename = "game-state-" + df.format(Calendar.getInstance().getTime()) + ".json";
            String filename = "game-state.json";
            writeToFile(filename, gameState);
        }
        super.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//    }

    //just for writing JSON to a file for testing, this can be taken out later

    private void writeToFile(String filename, String text){
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, filename);
        Log.d(TAG, "Wrote JSON to " + file.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(file);

            try {
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
