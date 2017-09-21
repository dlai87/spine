package com.vasomedical.spinetracer.algorithm;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/21/17.
 */

public class AlgorithmOptLeftRight extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData) {

        int numSamples = 50;
        return createDataForChart(inputData, Coordinate.y, Coordinate.ry, numSamples);
    }


    /**
     * 可自定义评分规则
     * */
    protected void calScore(ArrayList<Entry> data){
        score = 0 ;
    }
}