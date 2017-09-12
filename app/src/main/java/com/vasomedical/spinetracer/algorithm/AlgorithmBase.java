package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Util;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.PolynomialFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

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


    public enum DATA_RESULT{
        illegal,
        warning,
        success
    }

    public abstract ArrayList<Entry> processData(ArrayList<Pose> inputData);


    /**
     *
     *   create data for Chart with 2 Coordinates -- continuous method
     *
     *   c2 is the function of c1:   c2 = f(c1)
     *
     * */
    protected ArrayList<Entry> createDataForChart(ArrayList<Pose> inputData,
                                                  Coordinate c1 ,
                                                  Coordinate c2,
                                                  int numSamples){

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();


        for (Pose pose : inputData){
            Log.d("dataprocess", "raw " + pose );
        }

        int totalSamples = inputData.size();
        float minValue = valueOfCoordinate( inputData.get(0), c1);
        float maxValue = valueOfCoordinate( inputData.get(totalSamples - 1 ), c1);
        float totalValue = maxValue - minValue;
        float eachStepIncrease = totalValue / numSamples;

        for (float i = minValue; i < totalValue; i += eachStepIncrease){
            Pose[] neaestPoints = findNestestTwoPoints(inputData, i, c1);
            float y0 = valueOfCoordinate( neaestPoints[0], c1);
            float ry0 = valueOfCoordinate( neaestPoints[0], c2);
            float y1 = valueOfCoordinate( neaestPoints[1], c1);
            float ry1 = valueOfCoordinate( neaestPoints[1], c2);
            float calculateRotateY = (i - y0) / (y1- y0) *(ry1 - ry0) + ry0;
            float calculateDegree = Util.radianToDegree(calculateRotateY);


            Log.d("dataprocess", "y0 " + y0 + " ry0 " + ry0 + " y1 " + y1 + " ry1 " + ry1
                    + " calculateRotateY " + calculateRotateY
                    + " calculateDegree " + calculateDegree);
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
     *   create data for Chart with 2 Coordinates -- discrete method ; using raw data
     *
     *   c2 is the function of c1:   c2 = f(c1)
     *
     * */
    protected ArrayList<Entry> createDataForChart(ArrayList<Pose> inputData,
                                                  int numSamples){

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();
        int step = inputData.size() / numSamples;
        Log.e("show", "++++ step length ++++" + step);
        for (int i = 0 ; i < inputData.size(); i+=step ){
            Pose temp = inputData.get(i);
            float eulurDegree =  Util.radianToDegree(temp.getEuler_y());
            Log.e("show", "++++ y ++++" + temp.getY() + " " + temp.getEuler_y() + " degree " + eulurDegree);
            dataWithProcess.add(new Entry(temp.getY(), eulurDegree));
        }

        for (Entry entry : dataWithProcess){
            Log.e("show", "entry " + entry.toString());
        }

        if (preAnalyseData(dataWithProcess) == DATA_RESULT.illegal ){
            // remove first element and try again
            Log.e("temp", "Try again 1st time" );
            dataWithProcess.remove(0);
            if (preAnalyseData(dataWithProcess) == DATA_RESULT.illegal ){
                // remove first element and try again
                Log.e("temp", "Try again 2nd time" );
                dataWithProcess.remove(0);
                if (preAnalyseData(dataWithProcess) == DATA_RESULT.illegal ){
                    // give up trying, still illegal , clean data
                    dataWithProcess.clear();
                }
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


    private Pose[] findNestestTwoPoints(ArrayList<Pose> inputData, float target, Coordinate c){
        Pose[] points = new Pose[2];
        float point0 = 888;
        float point1 = 999;
        for (Pose pose : inputData){
            float diff = Math.abs( valueOfCoordinate(pose, c) - target);
            if ( diff < point0){
                point1 = point0;
                point0 = diff;
                points[1] = points[0];
                points[0] = pose;

            }else if( diff < point1){
                point1 = diff;
                points[1] = pose;
            }else{

            }
        }
        return points;
    }


    protected DATA_RESULT preAnalyseData(ArrayList<Entry> data){

        float firstValue = data.get(0).getX();
        boolean pureIncrease = true;

        for(int i = 1 ; i < data.size()-1; i ++){
            Entry entry = data.get(i);
            if (entry.getX() < firstValue){
                Log.e("temp", "DATA_RESULT.illegal " + entry.getX() + " " + firstValue + " "+i);
                return DATA_RESULT.illegal;
            }
            Entry preEntry = data.get(i-1);
            if (entry.getX() < preEntry.getX()){
                Log.e("temp", "DATA_RESULT.warning " + entry.getX() + " " + firstValue + " "+i);
                pureIncrease = false;
            }
        }
        if (pureIncrease){
            return DATA_RESULT.success;
        }
        return DATA_RESULT.warning;
    }

}
