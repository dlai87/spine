package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/28/17.
 */

public abstract class AlgorithmBase  {


    protected enum Coordinate{
        x,
        y,
        z,
        rx,
        ry,
        rz
    }

    public abstract ArrayList<Entry> processData(ArrayList<Pose> inputData);

    /**
     *
     *   create data for Chart with 2 Coordinates
     *
     *   c2 is the function of c1:   c2 = f(c1)
     *
     * */
    protected ArrayList<Entry> createDataForChart(ArrayList<Pose> inputData,
                                                  Coordinate c1 ,
                                                  Coordinate c2,
                                                  int numSamples){

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();


        int totalSamples = inputData.size();
        float minValue = valueOfCoordinate( inputData.get(0), c1);
        float maxValue = valueOfCoordinate( inputData.get(totalSamples - 1 ), c1);
        float totalValue = maxValue - minValue;
        float eachStepIncrease = totalValue / numSamples;
        for (float i = minValue; i < totalValue; i += eachStepIncrease){
            Pose[] neaestPoints = findNestestTwoPoints(inputData, i);
            float y0 = valueOfCoordinate( neaestPoints[0], c1);
            float ry0 = valueOfCoordinate( neaestPoints[0], c2);
            float y1 = valueOfCoordinate( neaestPoints[1], c1);
            float ry1 = valueOfCoordinate( neaestPoints[1], c2);
            float calculateRotateY = (i - y0) / (y1- y0) *(ry1 - ry0) + ry0;
            float calculateDegree = Util.radianToDegree(calculateRotateY);

            if (!Float.isNaN(calculateDegree)){
                dataWithProcess.add(new Entry(i,calculateDegree));
            }else {
                Entry lastEntry = dataWithProcess.get(dataWithProcess.size()-1);
                dataWithProcess.add(new Entry(i, lastEntry.getY()));

                
            }

        }

        return dataWithProcess;
    }


    /**
     *
     *
     *
     * */


    private float valueOfCoordinate(Pose pose, Coordinate c){
        switch (c){
            case x:
                return pose.getX();
            case y:
                return pose.getY();
            case z:
                return pose.getZ();
            case rx:
                return pose.getEuler_x();
            case ry:
                return pose.getEuler_y();
            case rz:
                return pose.getEuler_z();
        }
        return -1;
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
