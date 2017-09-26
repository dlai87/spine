package com.vasomedical.spinetracer.model;

import android.content.Context;

import com.google.atap.tangoservice.TangoPoseData;
import com.vasomedical.spinetracer.database.table.TBMotion;
import com.vasomedical.spinetracer.database.util.DBAdapter;

import java.util.ArrayList;

/**
 * Created by dehualai on 4/1/17.
 */

public class PoseLog {

    ArrayList<Pose> poseList;

    public ArrayList<Pose> getPoseList(){
        return poseList;
    }


    public PoseLog(){
        poseList = new ArrayList<Pose>();
    }

    public void recordPoseData(TangoPoseData poseData, TangoPoseData initPose){
        float translation[] = poseData.getTranslationAsFloats();
        float orientation[] = poseData.getRotationAsFloats();


        if (initPose!=null){
            float translationInit[] = initPose.getTranslationAsFloats();
            float orientationInit[] = initPose.getRotationAsFloats();

            Pose pose = new Pose(
                    translation[0] - translationInit[0],
                    translation[1] - translationInit[1],
                    translation[2] - translationInit[2],
                    orientation[0] - orientationInit[0],
                    orientation[1] - orientationInit[1],
                    orientation[2] - orientationInit[2],
                    orientation[3] - orientationInit[3]
            );
            poseList.add(pose);
        }else{
            Pose pose = new Pose(
                    translation[0] ,
                    translation[1] ,
                    translation[2] ,
                    orientation[0] ,
                    orientation[1] ,
                    orientation[2] ,
                    orientation[3]
            );
            poseList.add(pose);

        }


    }


    public void save(Context context){
        TBMotion tbMotion = new TBMotion();
        tbMotion.clean(DBAdapter.getDatabase(context));
        for (Pose pose : poseList){
            tbMotion.insert(DBAdapter.getDatabase(context), pose);
        }
    }


}


