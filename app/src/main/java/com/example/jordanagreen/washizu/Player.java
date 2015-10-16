package com.example.jordanagreen.washizu;

import android.graphics.Canvas;
import android.util.Log;

import static com.example.jordanagreen.washizu.Constants.SUIT_HONOR;

/**
 * Created by Jordan on 9/13/2015.
 */
public abstract class Player {

    public static final String TAG = "Player";

    protected Hand hand;
    private DiscardPool discards;
    protected boolean inRiichi;
    protected Game game;
    int score;
    int wind;
    private int direction;
    private boolean isMyTurn;

    public Player(int direction){
        this.direction = direction;
        this.hand = new Hand(this);
        this.score = Constants.STARTING_SCORE;
        this.discards = new DiscardPool();
        this.inRiichi = false;
        isMyTurn = false;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public abstract void takeTurn(Game.GameCallback callback);

    public void drawTile(){
        Tile tile = game.drawTile();
        Log.d(TAG, "Player + " + direction + " Drew tile " + tile);
        hand.setDrawnTile(tile);
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public void setIsMyTurn(boolean isMyTurn){
        this.isMyTurn = isMyTurn;
    }

    public boolean getIsMyTurn(){
        return isMyTurn;
    }

    public int getDirection(){
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

    public abstract boolean shouldPon(Tile tile);
    public abstract boolean shouldChii(Tile tile);

    public void callPon(Tile tile, int calledDirection){
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
        if (tile.getSuit() == SUIT_HONOR){
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

    public boolean canKan(Tile tile){
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
            if (meld.getType() == Meld.MeldType.PON){
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

}
