package com.vasomedical.spinetracer.algorithm.spineObj;

import android.util.Log;

import com.vasomedical.spinetracer.algorithm.AlgorithmBase;
import com.vasomedical.spinetracer.model.Pose;

import java.util.List;

/**
 * Created by dehualai on 6/24/18.
 */

public class SpineShape {

    AlgorithmBase.Coordinate xAxis = AlgorithmBase.Coordinate.y;
    AlgorithmBase.Coordinate yAxis = AlgorithmBase.Coordinate.z;

    List<Pose> rawData;

    Pose avergePose;


    float x;
    float y;
    float z;
    float rotation_x;
    float rotation_y;
    float rotation_z;
    float rotation_w;

    public SpineShape(List<Pose> dataSegment){

        Log.e("show", "dataSegment length " + dataSegment.size());
        this.rawData = dataSegment;
        calculate();
    }

    private void calculate(){


        for(Pose pose: rawData){
            x += pose.getX();
            y += pose.getY();
            z += pose.getZ();
            rotation_x += pose.getRotation_x();
            rotation_y += pose.getRotation_y();
            rotation_z += pose.getRotation_z();
            rotation_w += pose.getRotation_w();
        }
        x = x / rawData.size();
        y = y / rawData.size();
        z = z / rawData.size();
        rotation_x = rotation_x / rawData.size();
        rotation_y = rotation_y / rawData.size();
        rotation_z = rotation_z / rawData.size();
        rotation_w = rotation_w / rawData.size();

        avergePose = new Pose(x,y,z,rotation_x, rotation_y, rotation_z, rotation_w);

    }

    public Pose getPose(){
        return avergePose;
    }

}
