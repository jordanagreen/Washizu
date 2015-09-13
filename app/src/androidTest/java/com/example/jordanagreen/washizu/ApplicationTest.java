package com.example.jordanagreen.washizu;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.lang.Exception;
import java.lang.Override;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private MainActivity mainActivity;
    private WashizuView washizuView;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        mainActivity = getActivity();
        washizuView = (WashizuView) mainActivity.findViewById(R.id.game_view);
    }

    public void testHands(){
        Hand hand = new Hand(Constants.SEAT_DOWN, Constants.HAKU);
        Hand upHand = new Hand(Constants.SEAT_UP, Constants.MAN_1);
        washizuView.addPlayer(hand);
        washizuView.addPlayer(upHand);
    }
}