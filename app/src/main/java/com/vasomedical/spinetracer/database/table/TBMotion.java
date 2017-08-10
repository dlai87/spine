package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.Pose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dehualai on 4/1/17.
 */

public class TBMotion {


    public static String convertTimeToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public void insert(SQLiteDatabase db, Pose pose){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_X, pose.getX());
        DBUtil.smartPut(cv, DBGlobal.COL_Y, pose.getY());
        DBUtil.smartPut(cv, DBGlobal.COL_Z, pose.getZ());
        DBUtil.smartPut(cv, DBGlobal.COL_R_X, pose.getRotation_x());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Y, pose.getRotation_y());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Z, pose.getRotation_z());
        DBUtil.smartPut(cv, DBGlobal.COL_R_W, pose.getRotation_w());
        DBUtil.smartPut(cv, DBGlobal.COL_EULER_X, pose.getEuler_x());
        DBUtil.smartPut(cv, DBGlobal.COL_EULER_Y, pose.getEuler_y());
        DBUtil.smartPut(cv, DBGlobal.COL_EULER_Z, pose.getEuler_z());

        DBUtil.smartPut(cv, DBGlobal.COL_TIME, convertTimeToString(pose.getTimeStamp(), "HH:mm:ss.SSS"));
        db.insert(DBGlobal.TABLE_MOTION, null, cv);
    }

    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_MOTION, null, null);
    }

}
