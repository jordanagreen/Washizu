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

    protected Hand hand;
    private DiscardPool discards;
    protected boolean inRiichi;
    protected Game game;
    int score;
    private Wind wind;
    private SeatDirection direction;
    private boolean isMyTurn;

    public Player(SeatDirection direction){
        this.direction = direction;
        this.hand = new Hand(this);
        this.score = Constants.STARTING_SCORE;
        this.discards = new DiscardPool();
        this.inRiichi = false;
        isMyTurn = false;
    }

    public Player(JSONObject json) throws JSONException {
        this.hand = new Hand(json.getJSONObject(KEY_HAND), this);
        this.discards = new DiscardPool(json.getJSONObject(KEY_DISCARDS));
        this.direction = Enum.valueOf(SeatDirection.class, json.getString(KEY_DIRECTION));
        this.score = json.getInt(KEY_SCORE);
        this.wind = Enum.valueOf(Wind.class, json.getString(KEY_WIND));
        this.inRiichi = json.getBoolean(KEY_IN_RIICHI);
        this.isMyTurn = json.getBoolean(KEY_IS_MY_TURN);
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(KEY_HAND, hand.toJson());
        json.put(KEY_DISCARDS, discards.toJson());
        json.put(KEY_DIRECTION, direction.toString());
        json.put(KEY_WIND, wind.toString());
        json.put(KEY_SCORE, score);
        json.put(KEY_IN_RIICHI, inRiichi);
        json.put(KEY_IS_MY_TURN, isMyTurn);
        json.put(KEY_IS_AI, this instanceof AiPlayer);
        return json;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public abstract void takeTurn(Game.GameCallback callback);

    public void drawTile(){
        Tile tile = game.drawTile();
        Log.d(TAG, "Player " + direction + " Drew tile " + tile);
        hand.setDrawnTile(tile);
    }

    public Hand getHand(){
        return hand;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind(){
        return wind;
    }

    public void setIsMyTurn(boolean isMyTurn){
        this.isMyTurn = isMyTurn;
    }

    public boolean getIsMyTurn(){
        return isMyTurn;
    }

    public SeatDirection getDirection(){
        return direction;
    }

    public void discardTile(Tile tile){
        Log.d(TAG, "discarding tile " + tile);
        hand.discardTile(tile);
        tile.isReversed = false;
        discards.addTile(tile, false);
        Log.d(TAG, "Discarded " + tile);
    }

    public void discardTileAndCallRiichi(Tile tile){
        hand.discardTile(tile);
        discards.addTile(tile, true);
        inRiichi = true;
        Log.d(TAG, "Discarded " + tile + " for Riichi");
    }

    public Tile getLastDiscardedTile(){
        return discards.getLastTile();
    }

    public void removeLastDiscardedTile(){
        discards.removeLastTile();
    }

    public boolean canPon(Tile tile){
        int inHand = 0;
        for (Tile t: hand.getTiles()){
            if (tile.compareTo(t) == 0){
                inHand++;
            }
        }
        return inHand >= 2;
    }

    //might be better to take out the abstractness eventually
    public abstract boolean shouldPon(Tile tile);
    public abstract boolean shouldChii(Tile tile);
    public abstract boolean shouldKan(Tile tile);

    //calledDirection's angle is the player called from * 90 so we know which tile to rotate
    public void callPon(Tile tile, SeatDirection calledDirection){
        //get the two other tiles from the hand
        int ia = 0;
        int ib = 0;
        for (int i = 0; i < hand.getTiles().size(); i++){
            if (hand.getTile(i).compareTo(tile) == 0){
                ia = i;
                break;
            }
        }
        for (int i = ia + 1; i < hand.getTiles().size(); i++){
            if (hand.getTile(i).compareTo(tile) == 0){
                ib = i;
                break;
            }
        }
        if (ib == 0){
            throw new IllegalStateException("Called pon but couldn't find two similar tiles");
        }
        Tile a = hand.getTile(ia);
        Tile b = hand.getTile(ib);
        hand.makePon(tile, a, b, calledDirection);
    }

    public boolean canChii(Tile tile){
        if (tile.getSuit() == Suit.HONOR){
            return false;
        }
        int id = tile.getId();
        if (hand.containsTileById(id - 1) && hand.containsTileById(id - 2)){
            if (Tile.areSameSuit(id, id-1, id-2)){
                return true;
            }
        }
        if (hand.containsTileById(id - 1) && hand.containsTileById(id + 1)){
            if (Tile.areSameSuit(id-1, id, id+1)){
                return true;
            }
        }
        if (hand.containsTileById(id + 1) && hand.containsTileById(id + 2)){
            if (Tile.areSameSuit(id, id+1, id+2)){
                return true;
            }
        }
        return false;
    }

    abstract Tile[] getTilesForChii(Tile tile);

    public void callChii(Tile tile){
        Tile[] tiles = getTilesForChii(tile);
        hand.makeChii(tiles[0], tiles[1], tiles[2]);
    }

    public void draw(Canvas canvas){
        //if it's this player's turn, draw the tile they drew too
        hand.draw(canvas, direction, isMyTurn);
        discards.draw(canvas, direction);
    }

    public boolean canKanOnCall(Tile tile){
        int inHand = 0;
        for (Tile t : hand.getTiles()){
            if (t.compareTo(tile) == 0){
                inHand++;
            }
        }
        return inHand == 3;
    }

    public boolean canKanOnDraw(Tile tile){
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.PON){
                if (meld.getTiles()[0].compareTo(tile) == 0){
                    return true;
                }
            }
        }
        int inHand = 0;
        for (Tile t : hand.getTiles()){
            if (t.compareTo(tile) == 0){
                inHand++;
            }
        }
        return inHand == 3;
    }

    public void callKan(Tile tile, SeatDirection calledDirection){
        for (Meld meld: hand.getMelds()){
            if (meld.getType() == MeldType.PON && meld.getTiles()[0].compareTo(tile) == 0){
                hand.makeShouminkan(tile, meld);
                return;
            }
        }
        List<Tile> tiles = hand.getAllTilesById(tile.getId());
        if (tiles.size() != 3){
            throw new IllegalArgumentException("Trying to call kan without three tiles in hand");
        }
        hand.makeKan(tiles.get(0), tiles.get(1), tiles.get(2), tile, calledDirection, false);
    }

    public void callKanOnDraw(Tile tile){
        List<Tile> tiles = hand.getAllTilesById(tile.getId());
        if (tiles.size() != 3){
            throw new IllegalArgumentException("Trying to call kan without three tiles in hand");
        }
//        hand.makeKan(tiles.get(0), tiles.get(1), tiles.get(2), tile, calledDirection, false);
    }

}
