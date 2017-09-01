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

    int numSamples = 100;
    float cobbAngle = -1;  //暂不计算cobb角

    public ArrayList<Entry> processData(ArrayList<Pose> inputData){

        return createDataForChart(inputData, Coordinate.y, Coordinate.ry, 100);

        /*
        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();

        int totalSamples = inputData.size();
        float minValue = inputData.get(0).getY();
        float maxValue = inputData.get(totalSamples - 1 ).getY();
        float totalValue = maxValue - minValue;
        float eachStepIncrease = totalValue / numSamples;
        for (float i = minValue; i < totalValue; i += eachStepIncrease){
            Pose[] neaestPoints = findNestestTwoPoints(inputData, i);
            float y0 = neaestPoints[0].getY();
            float ry0 = neaestPoints[0].getEuler_y();
            float y1 = neaestPoints[1].getY();
            float ry1 = neaestPoints[1].getEuler_y();
            float calculateRotateY = (i - y0) / (y1- y0) *(ry1 - ry0) + ry0;
            float calculateDegree = Util.radianToDegree(calculateRotateY);

            dataWithProcess.add(new Entry(i,calculateDegree));
        }

        //return null;
        return dataWithProcess;

        */
    }

    private Pose[] findNestestTwoPoints(ArrayList<Pose> inputData, float target){
        Pose[] points = new Pose[2];
        float point0 = 888;
        float point1 = 999;
        for (Pose pose : inputData){
            float diff = Math.abs(pose.getY() - target);
            if ( diff < point0){
                point1 = point0;
                point0 = diff;
                points[1] = points[0];
                points[0] = pose;
            }else if( diff < point1){
                point1 = diff;
                points[1] = pose;
            }
        }
        return points;
    }


}
