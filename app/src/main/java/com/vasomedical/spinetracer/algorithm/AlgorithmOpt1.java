package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/28/17.
 */

public class AlgorithmOpt1 extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        int numSamples = 50;
        return createDataForChart(inputData, Coordinate.y, Coordinate.ry, numSamples);
    }


}
