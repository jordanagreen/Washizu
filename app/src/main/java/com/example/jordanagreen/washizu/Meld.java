package com.example.jordanagreen.washizu;

/**
 * Created by Jordan on 9/17/2015.
 */
public class Meld {
    enum MeldType {CHII, PON, KAN, CLOSED_KAN}

    private MeldType type;
    private Tile[] tiles;
    private int direction;

    public Meld(Tile a, Tile b, Tile c, int direction, MeldType type){
        tiles = new Tile[] {a, b, c};
        this.direction = direction;
        this.type = type;
    }

    public Meld(Tile a, Tile b, Tile c, Tile d, int direction, MeldType type){
        if (type != MeldType.KAN && type != MeldType.CLOSED_KAN){
            throw new IllegalArgumentException("Four tiles but not a kan");
        }
        else {
            tiles = new Tile[] {a, b, c, d};
            this.direction = direction;
            this.type = type;
        }
    }

    public MeldType getType(){
        return type;
    }

    public void ponToKan(Tile tile){
        if (type != MeldType.PON || tile.compareTo(tiles[0]) != 0 ||
                tile.compareTo(tiles[1]) != 0 || tile.compareTo(tiles[2]) != 0){
            throw new IllegalArgumentException("Illegal kan");
        }
        else {
            tiles = new Tile[] {tiles[0], tiles[1], tiles[2], tile};
            type = MeldType.KAN;
        }
    }
}
