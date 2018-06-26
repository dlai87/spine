package com.vasomedical.spinetracer.algorithm;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/26/18.
 */

public class AlgorithmOptForwardBack extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData) {

        int numSamples = 50;
        return createDataForChart(inputData, Coordinate.y, Coordinate.rx, numSamples);
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


        if ( Math.abs(minDegree) < 20 || Math.abs(maxDegree) < 70){
            score = 5;
        }else if(Math.abs(minDegree) < 22 || Math.abs(maxDegree) < 72){
            score = 4;
        }else if(Math.abs(minDegree) < 25 || Math.abs(maxDegree) < 75){
            score = 3;
        }else if(Math.abs(minDegree) < 28 || Math.abs(maxDegree) < 78){
            score = 2;
        }else if(Math.abs(minDegree) < 30 || Math.abs(maxDegree) < 90){
            score = 1;
        }else{
            score = 0;
        }
    }
}