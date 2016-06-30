package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Jordan on 9/13/2015.
 */
public abstract class Player {

    public static final String TAG = "Player";

    public static final String KEY_HAND = "hand";
    public static final String KEY_DISCARDS = "discards";
    public static final String KEY_DIRECTION = "direction";
    public static final String KEY_WIND = "wind";
    public static final String KEY_SCORE = "score";
    public static final String KEY_IN_RIICHI = "in_riichi";
    public static final String KEY_IS_MY_TURN = "is_my_turn";
    public static final String KEY_IS_AI = "is_ai";

    protected Hand mHand;
    private DiscardPool mDiscards;
    protected boolean mInRiichi;
    protected Game mGame;
    int mScore;
    private Wind mWind;
    private SeatDirection mDirection;
    private boolean mIsMyTurn;

    public Player(SeatDirection direction){
        this.mDirection = direction;
        this.mHand = new Hand(this);
        this.mScore = Constants.STARTING_SCORE;
        this.mDiscards = new DiscardPool();
        this.mInRiichi = false;
        mIsMyTurn = false;
    }

    public Player(JSONObject json) throws JSONException {
        this.mHand = new Hand(json.getJSONObject(KEY_HAND), this);
        this.mDiscards = new DiscardPool(json.getJSONObject(KEY_DISCARDS));
        this.mDirection = Enum.valueOf(SeatDirection.class, json.getString(KEY_DIRECTION));
        this.mScore = json.getInt(KEY_SCORE);
        this.mWind = Enum.valueOf(Wind.class, json.getString(KEY_WIND));
        this.mInRiichi = json.getBoolean(KEY_IN_RIICHI);
        this.mIsMyTurn = json.getBoolean(KEY_IS_MY_TURN);
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_HAND, mHand.toJson());
        json.put(KEY_DISCARDS, mDiscards.toJson());
        json.put(KEY_DIRECTION, mDirection.toString());
        json.put(KEY_WIND, mWind.toString());
        json.put(KEY_SCORE, mScore);
        json.put(KEY_IN_RIICHI, mInRiichi);
        json.put(KEY_IS_MY_TURN, mIsMyTurn);
        json.put(KEY_IS_AI, this instanceof AiPlayer);
        return json;
    }

    public void setGame(Game game){
        this.mGame = game;
    }

    //TODO: all the abstract stuff only gets done in AiPlayer, maybe make it non-abstract later

    public abstract void takeTurn(GameCallback callback);

    public void drawTile(){
        Tile tile = mGame.drawTile();
        Log.d(TAG, "Player " + mDirection + " Drew tile " + tile);
        mHand.setDrawnTile(tile);
    }

    public Hand getHand(){
        return mHand;
    }

    public DiscardPool getDiscardPool(){
        return mDiscards;
    }

    public void setWind(Wind wind) {
        this.mWind = wind;
    }

    public Wind getWind(){
        return mWind;
    }

    public void setIsMyTurn(boolean isMyTurn){
        this.mIsMyTurn = isMyTurn;
    }

    public boolean getIsMyTurn(){
        return mIsMyTurn;
    }

    public SeatDirection getDirection(){
        return mDirection;
    }

    public void discardTile(Tile tile){
        Log.d(TAG, "discarding tile " + tile);
        mHand.discardTile(tile);
        tile.isReversed = false;
        mDiscards.addTile(tile, false);
        Log.d(TAG, "Discarded " + tile);
    }

    public void discardTileAndCallRiichi(Tile tile){
        mHand.discardTile(tile);
        mDiscards.addTile(tile, true);
        mInRiichi = true;
        Log.d(TAG, "Discarded " + tile + " for Riichi");
    }

    public Tile getLastDiscardedTile(){
        return mDiscards.getLastTile();
    }

    public void removeLastDiscardedTile(){
        mDiscards.removeLastTile();
    }

    public boolean canPon(Tile tile){
        int inHand = 0;
        for (Tile t: mHand.getTiles()){
            if (tile.compareTo(t) == 0){
                inHand++;
            }
        }
        return inHand >= 2;
    }

    //calledDirection's angle is the player called from * 90 so we know which tile to rotate
    public void callPon(Tile tile, SeatDirection calledDirection){
        //get the two other tiles from the hand
        int ia = 0;
        int ib = 0;
        for (int i = 0; i < mHand.getTiles().size(); i++){
            if (mHand.getTile(i).compareTo(tile) == 0){
                ia = i;
                break;
            }
        }
        for (int i = ia + 1; i < mHand.getTiles().size(); i++){
            if (mHand.getTile(i).compareTo(tile) == 0){
                ib = i;
                break;
            }
        }
        if (ib == 0){
            throw new IllegalStateException("Called pon but couldn't find two similar tiles");
        }
        Tile a = mHand.getTile(ia);
        Tile b = mHand.getTile(ib);
        mHand.makePon(tile, a, b, calledDirection);
    }

    public boolean canChii(Tile tile){
        if (tile.getSuit() == Suit.HONOR){
            return false;
        }
        int id = tile.getId();
        if (mHand.containsTileById(id - 1) && mHand.containsTileById(id - 2)){
            if (Tile.areSameSuit(id, id-1, id-2)){
                return true;
            }
        }
        if (mHand.containsTileById(id - 1) && mHand.containsTileById(id + 1)){
            if (Tile.areSameSuit(id-1, id, id+1)){
                return true;
            }
        }
        if (mHand.containsTileById(id + 1) && mHand.containsTileById(id + 2)){
            if (Tile.areSameSuit(id, id+1, id+2)){
                return true;
            }
        }
        return false;
    }

    abstract Tile[] getTilesForChii(Tile tile);

    public void callChii(Tile tile){
        Tile[] tiles = getTilesForChii(tile);
        mHand.makeChii(tiles[0], tiles[1], tiles[2]);
    }

    public void draw(Canvas canvas){
        //if it's this player's turn, draw the tile they drew too
        mHand.draw(canvas, mDirection, mIsMyTurn);
        mDiscards.draw(canvas, mDirection);
    }

    public boolean canKanOnCall(Tile tile){
        int inHand = 0;
        for (Tile t : mHand.getTiles()){
            if (t.compareTo(tile) == 0){
                inHand++;
            }
        }
        return inHand == 3;
    }

    public boolean canKanOnDraw(Tile tile){
        for (Meld meld: mHand.getMelds()){
            if (meld.getType() == MeldType.PON){
                if (meld.getTiles().get(0).compareTo(tile) == 0){
                    return true;
                }
            }
        }
        int inHand = 0;
        for (Tile t : mHand.getTiles()){
            if (t.compareTo(tile) == 0){
                inHand++;
            }
        }
        return inHand == 3;
    }

    public void callKan(Tile tile, SeatDirection calledDirection){
        for (Meld meld: mHand.getMelds()){
            if (meld.getType() == MeldType.PON && meld.getTiles().get(0).compareTo(tile) == 0){
                mHand.makeShouminkan(tile, meld);
                return;
            }
        }
        List<Tile> tiles = mHand.getAllTilesById(tile.getId());
        if (tiles.size() != 3){
            throw new IllegalArgumentException("Trying to call kan without three tiles in hand");
        }
        mHand.makeKan(tiles.get(0), tiles.get(1), tiles.get(2), tile, calledDirection, false);
    }

    public void callKanOnDraw(Tile tile){
        List<Tile> tiles = mHand.getAllTilesById(tile.getId());
        if (tiles.size() != 3){
            throw new IllegalArgumentException("Trying to call kan without three tiles in hand");
        }
//        hand.makeKan(tiles.get(0), tiles.get(1), tiles.get(2), tile, calledDirection, false);
    }

    public boolean canRon(Tile tile){
        Log.d(TAG, "checking player " + mDirection + " for ron");
        //if the player discarded this tile already, that's furiten
        //TODO: check for furiten on waits other than the tile being called on
        if (mDiscards.containsTile(tile)){
            return false;
        }
        Scorer scorer = new Scorer();
        Score score = scorer.scoreHand(mHand, mGame.getRoundWind(), tile, false);
        //invalid hand
        if (score == null){
            return false;
        }
        for (int yaku: score.getHan()){
            if (yaku > 0){ //hand is worth at least one yaku
                return true;
            }
        }
        return false;
    }

    public void callRon(Tile tile, Player player){
        Log.d(TAG, "Called ron on " + tile);
        Log.d(TAG, mHand.toString());
        Scorer scorer = new Scorer();
        Score score = scorer.scoreHand(mHand, mGame.getRoundWind(), tile, false);
        Log.d(TAG, "Ron: " + score);
    }

    public void reset(){
        mHand.empty();
        mDiscards.empty();
        mInRiichi = false;
        mIsMyTurn = false;
    }

}
