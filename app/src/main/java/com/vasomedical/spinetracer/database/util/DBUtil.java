package com.vasomedical.spinetracer.database.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.table.TBDataProcessed;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.table.TBDoctor;
import com.vasomedical.spinetracer.database.table.TBMotion;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.table.TBPose;

/**
 * Created by dehualai on 1/7/17.
 */

public class DBUtil {
    public static void smartPut(ContentValues cv, String colName, String object ){
        if(object!=null) cv.put(colName, object);
    }
    public static void smartPut(ContentValues cv, String colName, Integer object ){
        if(object!=null) cv.put(colName, object);
    }
    public static void smartPut(ContentValues cv, String colName, Float object ){
        if(object!=null) cv.put(colName, object);
    }

    public static void cleanAllTable(Context context){
        SQLiteDatabase db = DBAdapter.getDatabase(context);
        TBDataProcessed tbDataProcessed = new TBDataProcessed();
        tbDataProcessed.clean(db);
        TBDetection tbDetection = new TBDetection();
        tbDetection.clean(db);
        TBDoctor tbDoctor = new TBDoctor();
        tbDoctor.clean(db);
        TBMotion tbMotion = new TBMotion();
        tbMotion.clean(db);
        TBPatient tbPatient = new TBPatient();
        tbPatient.clean(db);
        TBPose tbPose = new TBPose();
        tbPose.clean(db);
    }
}
