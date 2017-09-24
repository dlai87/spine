package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/28/17.
 */

public class AlgorithmOptSlant extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        int numSamples = 50;
        return createDataForChart(inputData, Coordinate.y, Coordinate.ry, numSamples);
    }

    /**
     * 可自定义评分规则
     * */
    protected void calScore(ArrayList<Entry> data){
        float minDegree = Float.MAX_VALUE;
        float maxDegree = Float.MIN_VALUE;
        for(Entry entry : data){
            if (entry.getY() < minDegree){
                minDegree = entry.getY();
            }
            if(entry.getY() > maxDegree){
                maxDegree = entry.getY();
            }
        }
        float delta = Math.abs(maxDegree - minDegree);
        Log.e("temp", "delta " + delta);
        float THRESH1 = 2;
        float THRESH2 = 4;
        float THRESH3 = 6;
        float THRESH4 = 8;
        float THRESH5 = 10;

        if (delta <= THRESH1){
            score = 0;
        }else if(delta <= THRESH2){
            score = 1;
        }else if(delta <= THRESH3){
            score = 2;
        }else if(delta <= THRESH4){
            score = 3;
        }else if(delta <= THRESH5){
            score = 4;
        }else{
            score = 5;
        }
    }

}
