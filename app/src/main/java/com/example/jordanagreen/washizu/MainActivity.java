package com.example.jordanagreen.washizu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    //TODO: start with a list of json states to start from, or a new game

    public static final String TAG = "MainActivity";

    public static final String EXTRA_FILENAME = "filename";

    public static final String NEW_GAME = "New Game";

    private List<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listItems = getListOfSavedGames();
        listItems.add(0, NEW_GAME);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout, listItems);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "List item " + position + " clicked");
        //new game
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        if (position != 0) {
            String filename = (String) getListView().getItemAtPosition(position);
            intent.putExtra(EXTRA_FILENAME, filename);
        }
        MainActivity.this.startActivity(intent);
    }

    private List<String> getListOfSavedGames(){
        List<String> games = new ArrayList<>();
        //TODO: switch to a different folder, for now just using Assets for test cases
//        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        Log.d(TAG, "looking for files in " + root.getAbsolutePath());
//        File[] files = root.listFiles();
        try {
            String assetFiles[] = getResources().getAssets().list("");

//        for (File file: files){
//            if (file.isFile()){
//                String filename = file.getName();
            for (String filename: assetFiles){
                Log.d(TAG, "checking file " + filename);
                int i = filename.lastIndexOf('.');
                if (i < 0){
                    continue;
                }
                String ext = filename.substring(i);
                if (ext.equals(".json")){
                    games.add(filename);
                }
//            }
            }
        }
        catch (IOException e){
            return new ArrayList<>();
        }
        return games;
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

}
