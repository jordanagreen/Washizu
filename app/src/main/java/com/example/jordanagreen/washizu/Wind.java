package com.example.jordanagreen.washizu;
import static com.example.jordanagreen.washizu.Constants.NAN;
import static com.example.jordanagreen.washizu.Constants.PEI;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;

/**
 * Created by Jordan on 2/1/2016.
 */
public enum Wind {
    EAST(TON, "E"), SOUTH(NAN, "S"), WEST(XIA, "W"), NORTH(PEI, "N");

    private int tileID;
    private String abbreviation;

    Wind(int id, String abbreviation){
        this.tileID = id;
        this.abbreviation = abbreviation;
    }

    int getTileID(){
        return tileID;
    }

    String getAbbreviation(){
        return abbreviation;
    }
}
