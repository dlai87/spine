package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/19/17.
 */

public class AlgorithmOpt3 extends AlgorithmBase {

    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        int numSamples = 80;
        ArrayList<Entry> data = createDataForChartPositionVsPosition(inputData, Coordinate.x, Coordinate.z, numSamples);

        return data;
    }


}
