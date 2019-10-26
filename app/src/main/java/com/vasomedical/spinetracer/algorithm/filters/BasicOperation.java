package com.vasomedical.spinetracer.algorithm.filters;

import android.util.Log;

import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Coor;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dehualai on 6/24/18.
 */

public class BasicOperation {

    String TAG = "BasicOperation";


    public ArrayList<Pose> removeHeadAndTale(ArrayList<Pose> inputPose, float headPercent, float talePercent){

        int size = inputPose.size();


        float maxDistanceHead = 0 ;
        float maxDistanceTale = 0 ;

        for(int i = 0 ; i < (int)(headPercent * size); i++){
            Pose p1 = inputPose.get(i);
            Pose p2 = inputPose.get(i+1);
            float dis = distance(p1, p2);
            if (dis > maxDistanceHead){
                maxDistanceHead = dis;
            }
        }


        for(int i = 1 ; i < (int)(talePercent * size); i++){
            Pose p1 = inputPose.get(size - i);
            Pose p2 = inputPose.get(size - i - 1);
            float dis = distance(p1, p2);
            if (dis > maxDistanceTale){
                maxDistanceTale = dis;
            }
        }

        Log.i(TAG, "maxDistanceHead " + maxDistanceHead + " maxDistanceTale " + maxDistanceTale);

        ArrayList<Pose> newList = new ArrayList<Pose>();

        for(int i = (int)(headPercent * size) ; i < size - (int)(talePercent * size); i ++){
            newList.add(inputPose.get(i));
        }

        return newList;
    }


    private float distance(Pose pose1 , Pose pose2 ){
        return  (float) Math.sqrt( (pose1.getX() - pose2.getX()) * (pose1.getX() - pose2.getX()) * 1000 +
                (pose1.getY() - pose2.getY()) * (pose1.getY() - pose2.getY()) * 1000 +
                (pose1.getZ() - pose2.getZ()) * (pose1.getZ() - pose2.getZ()) * 1000 );
    }


    public ArrayList<Pose> midianFilter(ArrayList<Pose> inputPose){

        int size = inputPose.size();
        Float[] x = new Float[size];
        Float[] y = new Float[size];
        Float[] z = new Float[size];
        Float[] rx = new Float[size];
        Float[] ry = new Float[size];
        Float[] rz = new Float[size];
        Float[] rw = new Float[size];

        for (int i = 0 ; i < size ; i++){
            Pose pose = inputPose.get(i);
            x[i] = pose.getX();
            y[i] = pose.getY();
            z[i] = pose.getZ();
            rx[i] = pose.getRotation_x();
            ry[i] = pose.getRotation_y();
            rz[i] = pose.getRotation_z();
            rw[i] = pose.getRotation_w();
        }

        MedianFilter medianFilter = new MedianFilter();
        int window_size = 21;  // must be odd number
        Float[] newX = medianFilter.applyFilter(x, window_size);
        Float[] newY = medianFilter.applyFilter(y, window_size);
        Float[] newZ = medianFilter.applyFilter(z, window_size);
        Float[] newRX = medianFilter.applyFilter(rx, window_size);
        Float[] newRY = medianFilter.applyFilter(ry, window_size);
        Float[] newRZ = medianFilter.applyFilter(rz, window_size);
        Float[] newRW = medianFilter.applyFilter(rw, window_size);

        ArrayList<Pose> newList = new ArrayList<Pose>();
        for(int i = 0 ;  i< newX.length; i ++){
            if(newX[i] == null || newY[i] == null || newZ[i] == null|| newRX[i] == null|| newRY[i] == null|| newRZ[i] == null ||
                    newRW[i] == null )
                break;
            newList.add(new Pose(newX[i], newY[i], newZ[i], newRX[i], newRY[i], newRZ[i], newRW[i]));
        }


        return newList;
    }




    public ArrayList<Pose> sortBy(ArrayList<Pose> inputList, Coor coor){
        Pose.sortBy = coor;
        Collections.sort(inputList);
        return inputList;
    }

    public ArrayList<Pose> moveToOrigin(ArrayList<Pose> inputList, Coor coor){
        return null;
    }

    public ArrayList<ArrayList<Pose>> chop(ArrayList<Pose> inputList, float[] ratios, Coor coor){
        ArrayList<Pose> sortedList = sortBy(inputList, coor);
        ArrayList<Pose> trimedList = removeHeadAndTale(sortedList, 0.05f, 0.05f);
        ArrayList<Pose> filterList = midianFilter(trimedList);

        float min = filterList.get(0).getY();
        float max = filterList.get(filterList.size()-1).getY();

        Log.e("TestChop", "=== sort list ====");
        Util.printArrayList(sortedList, "TestChop");
        Log.e("TestChop", "=== trimed list ====");
        Util.printArrayList(trimedList, "TestChop");
        Log.e("TestChop", "=== filter list ====");
        Util.printArrayList(filterList, "TestChop");
        Log.e("TestChop", "min " + min + " max " + max);
        float[] milestone = new float[ratios.length];
        for(int i = 0 ; i < ratios.length ; i++){
            milestone[i] = min + ratios[i] * (max - min) ;
            Log.e("TestChop", i  + "  == " + milestone[i]);
        }


        ArrayList<ArrayList<Pose>> output = new ArrayList<ArrayList<Pose>>();
        int i = 0 ;
        ArrayList<Pose> segment = new ArrayList<Pose>();
        for(Pose pose : filterList){
            if(pose.getY() < milestone[i]){
                segment.add(pose);
            }else{
                output.add(segment);
                i ++;
                segment = new ArrayList<Pose>();
            }
        }
        Log.e("TestChop",  " output  == " + output.size());
        return output;
    }
}
