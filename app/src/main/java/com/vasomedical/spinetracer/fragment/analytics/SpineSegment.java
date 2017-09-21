package com.vasomedical.spinetracer.fragment.analytics;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/20/17.
 */

public class SpineSegment {

    ArrayList<SpinePiece> pieces;

    public SpineSegment(){
        pieces = new ArrayList<SpinePiece>();
    }

    public boolean isEmpty(){
        return pieces.isEmpty();
    }

    public boolean isNegative(){
        if (pieces==null || pieces.isEmpty()){
            return false;
        }
        return pieces.get(0).isNegative();
    }

    public void addOnePiece(SpinePiece piece){
        this.pieces.add(piece);
    }

    public SpinePiece getInterestedPiece(){
        int i = 0 ;
        float minTan = Float.MAX_VALUE;
        for(int j=0; j < pieces.size(); j++){
            if ( Math.abs(pieces.get(j).getTans()) < minTan){
                minTan = Math.abs(pieces.get(j).getTans());
                i = j;
            }
        }
        return pieces.get(i);
    }
}
