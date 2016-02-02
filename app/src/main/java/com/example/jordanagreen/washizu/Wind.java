package com.example.jordanagreen.washizu;
import static com.example.jordanagreen.washizu.Constants.NAN;
import static com.example.jordanagreen.washizu.Constants.PEI;
import static com.example.jordanagreen.washizu.Constants.TON;
import static com.example.jordanagreen.washizu.Constants.XIA;

/**
 * Created by Jordan on 2/1/2016.
 */
public enum Wind {
    EAST(XIA), SOUTH(NAN), WEST(PEI), NORTH(TON);

    private int tileID;

    Wind(int id){
        this.tileID = id;
    }

    int getTileID(){
        return tileID;
    }
}
