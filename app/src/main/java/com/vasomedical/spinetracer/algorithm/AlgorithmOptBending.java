package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.algorithm.filters.BasicOperation;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Coor;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/24/18.
 */

public class AlgorithmOptBending extends AlgorithmBase {


    private float[] segmentRatio = {
            0.03092783505f,
            0.06872852234f,
            0.116838488f,
            0.1649484536f,
            0.2164948454f,
            0.2680412371f,
            0.3195876289f,
            0.3711340206f,
            0.4226804124f,
            0.4742268041f,
            0.5257731959f,
            0.587628866f,
            0.6563573883f,
            0.7319587629f,
            0.8075601375f,
            0.9037800687f,
            1.0f
    };

    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        BasicOperation basicOperation = new BasicOperation();
        ArrayList<ArrayList<Pose>> processedData = basicOperation.chop(inputData, segmentRatio, Coor.py);

        ArrayList<Entry> data = createDataForChartSpineSegment2(processedData, Coordinate.x, Coordinate.y);
        int numSamples = 80;
        //   ArrayList<Entry> data = createDataForChartPositionVsPosition(inputData, Coordinate.x, Coordinate.z, numSamples);
        //   ArrayList<Entry> data = createDataForChartSpineSegment(inputData, Coordinate.x, Coordinate.y );


        return normalizeData(data);
    }


    private ArrayList<Entry> normalizeData(ArrayList<Entry> inputData){
        // reverse
        /*
        for (Entry entry: inputData){
            entry.setY(-entry.getY());
        }
        for (Entry entry: inputData){
            Log.d("show", entry.toString());
        }
        */

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
            Log.d("show", "entry ====== " + entry.toString());
        }
        return inputData;
    }


    /**
     * 可自定义评分规则
     * */
    protected void calScore(ArrayList<Entry> data){
        score = 1; // temp
    }
}
