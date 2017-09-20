package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/18/17.
 */

public class AlgorithmOpt2 extends AlgorithmBase {


    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        int numSamples = 80;
        ArrayList<Entry> data = createDataForChartPositionVsPosition(inputData, Coordinate.y, Coordinate.z, numSamples);

        return normalizeData(data);
    }

    private ArrayList<Entry> normalizeData(ArrayList<Entry> inputData){
        // reverse
        for (Entry entry: inputData){
            entry.setY(-entry.getY());
        }
        for (Entry entry: inputData){
            Log.d("show", entry.toString());
        }

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        for (Entry entry: inputData){
            if (entry.getX() < minX) minX = entry.getX();
            if (entry.getY() < minY) minY = entry.getY();
        }

        // normalize
        for (Entry entry: inputData){
            entry.setX(entry.getX() - minX);
            entry.setY(entry.getY() - minY);
        }


        for (Entry entry: inputData){
            Log.d("show", entry.toString());
        }
        return inputData;
    }


}
