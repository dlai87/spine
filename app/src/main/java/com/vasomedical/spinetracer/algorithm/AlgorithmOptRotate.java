package com.vasomedical.spinetracer.algorithm;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/26/18.
 */

public class AlgorithmOptRotate extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData) {

        int numSamples = 50;
        return createDataForChart(inputData, Coordinate.y, Coordinate.rz, numSamples);
    }


    /**
     * 可自定义评分规则
     * */
    protected void calScore(ArrayList<Entry> data){
        score = 0 ;
    }
}