package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/20/17.
 */

public class AnalyticUtil {



    public ArrayList<SpinePiece> findPieceOfInterested(ArrayList<Point> points){
        // build Spine Piece list
        ArrayList<SpinePiece> spinePieceList = new ArrayList<SpinePiece>();
        for(int i = 0 ; i < points.size()-1; i++){
            spinePieceList.add(new SpinePiece(points.get(i), points.get(i+1)));
        }

        // remove singluar point piece
        for (int i = 0 ; i < spinePieceList.size(); i++){
            SpinePiece temp = spinePieceList.get(i);
            if (i == 0 ){
                // first one
                if (temp.getTans() * spinePieceList.get(i+1).getTans() < 0){
                    temp.setSingluarPoint(true);
                }
            }else if (i == spinePieceList.size()-1){
                // last one
                if (temp.getTans() * spinePieceList.get(i-1).getTans() < 0){
                    temp.setSingluarPoint(true);
                }
            }else{
                if (temp.getTans() * spinePieceList.get(i-1).getTans() < 0){
                    if(temp.getTans() * spinePieceList.get(i+1).getTans() < 0){
                        temp.setSingluarPoint(true);
                    }
                }
            }
        }
        for(SpinePiece spinePiece: spinePieceList){
            if (spinePiece.isSingluarPoint()){
                spinePieceList.remove(spinePiece);
            }
        }



        // segmentation
        ArrayList<SpineSegment> segments = new ArrayList<SpineSegment>();

        SpineSegment tempSeg = new SpineSegment();
        for(int i= 0 ; i < spinePieceList.size(); i++ ){
            SpinePiece t = spinePieceList.get(i);
            if(tempSeg.isEmpty()){
                tempSeg.addOnePiece(t);
            }else{
                // same positive or negative
                if (t.isNegative() == tempSeg.isNegative()){
                    tempSeg.addOnePiece(t);
                }else {
                    segments.add(tempSeg);
                    tempSeg = new SpineSegment();
                    tempSeg.addOnePiece(t);
                }
            }
            // add last segment
            if (i == spinePieceList.size()-1){
                segments.add(tempSeg);
            }
        }

        // find interested piece
        ArrayList<SpinePiece> interestedPieces =  new ArrayList<SpinePiece>();

        for(SpineSegment seg : segments){
            interestedPieces.add(seg.getInterestedPiece());
        }

        return interestedPieces;


    }
}
