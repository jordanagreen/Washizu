package com.example.jordanagreen.washizu;

import android.test.ActivityInstrumentationTestCase2;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2 {

    private MainActivity mainActivity;
    private WashizuView washizuView;

    public ApplicationTest() {
        super("com.example.jordanagreen.washizu", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        mainActivity = (MainActivity) getActivity();
        washizuView = (WashizuView) mainActivity.findViewById(R.id.game_view);
    }

    public void testPreconditions(){
        assertNotNull(washizuView);
    }

//    public void testPon(){
//        Game game = new Game();
//        ArrayList<Tile> tiles = new ArrayList<>();
//        for (int i = 0; i < 2; i++){
//            tiles.add(new Tile(CHUN, false));
//        }
//        for (int i = 0; i < 11; i++){
//            tiles.add(new Tile(i, false));
//        }
//        Player p1 = new HumanPlayer(game, SEAT_DOWN, new Hand(tiles));
//        ArrayList<Tile> tiles2 = new ArrayList<>();
//        tiles2.add(new Tile(CHUN, false));
//        for (int i = 0; i < 12; i++){
//            tiles2.add(new Tile(i, false));
//        }
//        Player p2 = new AiPlayer(game, SEAT_UP, new Hand(tiles2));
//
//        game.setPlayers(new Player[]{p1, p2});
//
//        washizuView.setTestGame(game);
//    }
}