package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.algorithm.spineObj.SpineSegmentManager;
import com.vasomedical.spinetracer.algorithm.spineObj.SpineShape;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/28/17.
 */

public abstract class AlgorithmBase  {

    protected int score = 0;

    public enum Coordinate{
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
     *   create data for Chart with 2 Coordinates -- discrete method ; using raw data
     *
     *   c2 is the function of c1:   c2 = f(c1)    c1 position value , c2 degree value
     *
     * */
    protected ArrayList<Entry> createDataForChart(ArrayList<Pose> inputData,
                                                  Coordinate c1 ,
                                                  Coordinate c2,
                                                  int numSamples){

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();
        int step = inputData.size() / numSamples;
        Log.e("show", "++++ step length ++++" + step);
        for (int i = 0 ; i < inputData.size(); i+=step ){
            Pose temp = inputData.get(i);

            int offset = 0 ;
            switch (c2){
                case rx:
                    offset = -90;
                    break;
                case ry:
                    offset = 0 ;
                    break;
                case rz:
                    offset = 90 ;
                    break;
            }

            float eulurDegree =  Util.radianToDegree(Util.valueOfCoordinate(temp, c2), offset, c2==Coordinate.rz);
            Log.e("show", Util.valueOfCoordinate(temp, c1) + "  ++++ eulurDegree ++++ " + eulurDegree);
            dataWithProcess.add(new Entry( Math.abs(Util.valueOfCoordinate(temp, c1)) , eulurDegree));
        }

        int i = 0 ;
        for (Entry entry : dataWithProcess){
            Log.e("show", "entry " + entry.toString() + "==" + i);
            i ++;
        }


        int minIndex = indexOfMinElement(dataWithProcess);
        dataWithProcess = shift(dataWithProcess, minIndex);
        dataWithProcess = sortArrayBeforeMinIndex(dataWithProcess, minIndex);

        i = 0 ;
        for (Entry entry : dataWithProcess){
            Log.e("show", "After sorting entry " + entry.toString() + "==" + i);
            i ++;
        }

        if (preAnalyseData(dataWithProcess) == DATA_RESULT.illegal ){
            dataWithProcess.clear();
        }

        calScore(dataWithProcess);


        return dataWithProcess;
    }



    protected ArrayList<Entry> createDataForChartSpineSegment(ArrayList<Pose> inputData,
                                                              Coordinate c1 ,
                                                              Coordinate c2){

        SpineSegmentManager spineSegmentManager = new SpineSegmentManager();
        ArrayList<SpineShape> spineList = spineSegmentManager.covert(inputData);

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();
        for(int i = 0 ; i < spineList.size(); i++){
            Pose temp = spineList.get(i).getPose();
            dataWithProcess.add(new
                    Entry(
                    Util.valueOfCoordinate(temp, c1) ,
                    Util.valueOfCoordinate(temp, c2)));
        }

        return dataWithProcess;
    }


    protected ArrayList<Entry> createDataForChartSpineSegment2( ArrayList<ArrayList<Pose>> inputData,
                                                                Coordinate c1 ,
                                                                Coordinate c2){
        SpineSegmentManager spineSegmentManager = new SpineSegmentManager();
        ArrayList<SpineShape> spineList = spineSegmentManager.covert2(inputData);

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();
        for(int i = 0 ; i < spineList.size(); i++){
            Pose temp = spineList.get(i).getPose();
            dataWithProcess.add(new
                    Entry(
                    Util.valueOfCoordinate(temp, c1) ,
                    Util.valueOfCoordinate(temp, c2)));
        }

        return dataWithProcess;
    }



    /**
     *
     *   create data for Chart with 2 Coordinates -- discrete method ; using raw data
     *
     *   c2 is the function of c1:   c2 = f(c1)    c1 position value , c2 position value
     *
     * */
    protected ArrayList<Entry> createDataForChartPositionVsPosition(ArrayList<Pose> inputData,
                                                                    Coordinate c1 ,
                                                                    Coordinate c2,
                                                                    int numSamples){

        ArrayList<Entry> dataWithProcess = new ArrayList<Entry>();
        int step = inputData.size() / numSamples;
        Log.e("show", "++++ step length ++++" + step);
        for (int i = 0 ; i < inputData.size(); i+=step ){
            Pose temp = inputData.get(i);
            dataWithProcess.add(new
                    Entry(
                    Util.valueOfCoordinate(temp, c1) ,
                    Util.valueOfCoordinate(temp, c2)));
        }

        int i = 0 ;
        for (Entry entry : dataWithProcess){
            Log.e("show", "entry " + entry.toString() + "==" + i);
            i ++;
        }


        calScore(dataWithProcess);

        return dataWithProcess;
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
            Log.e("temp", "DATA_RESULT.success ");
            return DATA_RESULT.success;
        }
        return DATA_RESULT.warning;
    }



    protected int indexOfMinElement(ArrayList<Entry> data){
        float minValue = Float.MAX_VALUE;
        int minIndex = 0 ;
        for(int i = 0 ; i < data.size(); i ++){
            if (data.get(i).getX() < minValue){
                minValue = data.get(i).getX();
                minIndex = i;
            }
        }
        return minIndex;
    }


    protected ArrayList<Entry> sortArrayBeforeMinIndex(ArrayList<Entry> data, int minIndex){

        Log.e("show", "+++++ minIndex ++++++ " + minIndex);
        ArrayList<Entry> arrayBeforeMinIndex = new ArrayList<Entry>(data.subList(0, minIndex+1)) ;
        ArrayList<Entry> arrayAfterMinIndex = new ArrayList<Entry>(data.subList(minIndex+1, data.size()));

        //sorting the array
        for(int i=0; i < arrayBeforeMinIndex.size(); i++){
            for(int j=1; j < arrayBeforeMinIndex.size()-i; j++){
                if (arrayBeforeMinIndex.get(j-1).getX() > arrayBeforeMinIndex.get(j).getX()){
                    Entry temp = arrayBeforeMinIndex.get(j-1);
                    arrayBeforeMinIndex.set(j-1, arrayBeforeMinIndex.get(j));
                    arrayBeforeMinIndex.set(j,temp);
                }
            }
        }

        ArrayList<Entry> sortList = new ArrayList<Entry>();
        sortList.addAll(arrayBeforeMinIndex);
        sortList.addAll(arrayAfterMinIndex);

        return sortList;

    }


    protected ArrayList<Entry> shift(ArrayList<Entry> data, int minIndex){
        float minValue = data.get(minIndex).getX();

        for (int i = 0 ; i < data.size() ;i ++){
            data.get(i).setX( data.get(i).getX() - minValue);
        }

        return data;
    }


    protected void calScore(ArrayList<Entry> data){

    }


    public int getScore(){
        return score;
    }

}
