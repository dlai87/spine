package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBPose {




    /**
     *
     * 单次测量的每个测量点的记录
     * 这个表只是内部算法开发实验用，在app应该不需要用到
     *
     * **/
    public void insert(SQLiteDatabase db, Pose pose, String detectionId) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, pose.getTimeStamp().toString());
        DBUtil.smartPut(cv, DBGlobal.COL_X, pose.getX());
        DBUtil.smartPut(cv, DBGlobal.COL_Y, pose.getY());
        DBUtil.smartPut(cv, DBGlobal.COL_Z, pose.getZ());
        DBUtil.smartPut(cv, DBGlobal.COL_R_X, pose.getRotation_x());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Y, pose.getRotation_y());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Z, pose.getRotation_z());
        DBUtil.smartPut(cv, DBGlobal.COL_R_W, pose.getRotation_w());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);

        db.insert(DBGlobal.TABLE_POSE, null, cv);
    }

    public void update(SQLiteDatabase db, Pose pose, String detectionId) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, pose.getTimeStamp().toString());
        DBUtil.smartPut(cv, DBGlobal.COL_X, pose.getX());
        DBUtil.smartPut(cv, DBGlobal.COL_Y, pose.getY());
        DBUtil.smartPut(cv, DBGlobal.COL_Z, pose.getZ());
        DBUtil.smartPut(cv, DBGlobal.COL_R_X, pose.getRotation_x());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Y, pose.getRotation_y());
        DBUtil.smartPut(cv, DBGlobal.COL_R_Z, pose.getRotation_z());
        DBUtil.smartPut(cv, DBGlobal.COL_R_W, pose.getRotation_w());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);

        String selection = DBGlobal.COL_TIMESTAMP + " =? "; // TEMP use timestamp to compare
        String[] selectionArgs = {pose.getTimeStamp().toString()};

        db.update(DBGlobal.TABLE_POSE, cv, selection, selectionArgs);
    }

    public String checkTransmissionStatus(SQLiteDatabase db) {
        String status = DBGlobal.TRANS_STATUS_INSERTED;
        Cursor result = db.query(DBGlobal.TABLE_POSE, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_status = result.getColumnIndexOrThrow(DBGlobal.COL_TRANSMISSTION_STATUS);
            status = result.getString(col_status);
        }
        result.close();
        return status;
    }

    public void updateTransmissionStatus(SQLiteDatabase db, String status) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSTION_STATUS, status);
        db.update(DBGlobal.TABLE_POSE, cv, null, null);
    }

    public ArrayList<Pose> getPoseList(SQLiteDatabase db) {
        ArrayList<Pose> queryResult = new ArrayList<>();
        Cursor result = db.query(DBGlobal.TABLE_DOCTOR, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
//            int col_timestamp = result.getColumnIndexOrThrow(DBGlobal.COL_TIMESTAMP);
            int col_x = result.getColumnIndexOrThrow(DBGlobal.COL_X);
            int col_y = result.getColumnIndexOrThrow(DBGlobal.COL_Y);
            int col_z = result.getColumnIndexOrThrow(DBGlobal.COL_Z);
            int col_r_x = result.getColumnIndexOrThrow(DBGlobal.COL_R_X);
            int col_r_y = result.getColumnIndexOrThrow(DBGlobal.COL_R_Y);
            int col_r_z = result.getColumnIndexOrThrow(DBGlobal.COL_R_Z);
            int col_r_w = result.getColumnIndexOrThrow(DBGlobal.COL_R_W);
//            int col_detection_id = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_ID);

            queryResult.add(new Pose(result.getFloat(col_x),
                    result.getFloat(col_y),
                    result.getFloat(col_z),
                    result.getFloat(col_r_x),
                    result.getFloat(col_r_y),
                    result.getFloat(col_r_z),
                    result.getFloat(col_r_w))
            );

            while (result.moveToNext()) {
                queryResult.add(new Pose(result.getFloat(col_x),
                        result.getFloat(col_y),
                        result.getFloat(col_z),
                        result.getFloat(col_r_x),
                        result.getFloat(col_r_y),
                        result.getFloat(col_r_z),
                        result.getFloat(col_r_w))
                );
            }
        }
        result.close();
        return queryResult;
    }

    public ArrayList<Pose> getPoseList(SQLiteDatabase db, String detectionId) {
        ArrayList<Pose> queryResult = new ArrayList<>();
        String selection = DBGlobal.COL_DETECTION_ID + " =? ";
        String[] selectionArgs = {detectionId};
        Cursor result = db.query(DBGlobal.TABLE_DOCTOR, null, selection, selectionArgs, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
//            int col_timestamp = result.getColumnIndexOrThrow(DBGlobal.COL_TIMESTAMP);
            int col_x = result.getColumnIndexOrThrow(DBGlobal.COL_X);
            int col_y = result.getColumnIndexOrThrow(DBGlobal.COL_Y);
            int col_z = result.getColumnIndexOrThrow(DBGlobal.COL_Z);
            int col_r_x = result.getColumnIndexOrThrow(DBGlobal.COL_R_X);
            int col_r_y = result.getColumnIndexOrThrow(DBGlobal.COL_R_Y);
            int col_r_z = result.getColumnIndexOrThrow(DBGlobal.COL_R_Z);
            int col_r_w = result.getColumnIndexOrThrow(DBGlobal.COL_R_W);
//            int col_detection_id = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_ID);

            queryResult.add(new Pose(result.getFloat(col_x),
                    result.getFloat(col_y),
                    result.getFloat(col_z),
                    result.getFloat(col_r_x),
                    result.getFloat(col_r_y),
                    result.getFloat(col_r_z),
                    result.getFloat(col_r_w))
            );

            while (result.moveToNext()) {
                queryResult.add(new Pose(result.getFloat(col_x),
                        result.getFloat(col_y),
                        result.getFloat(col_z),
                        result.getFloat(col_r_x),
                        result.getFloat(col_r_y),
                        result.getFloat(col_r_z),
                        result.getFloat(col_r_w))
                );
            }
        }
        result.close();
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, Pose pose, String detectionId) {
        ArrayList<Pose> poses = getPoseList(db);
        boolean exist = false;
        for (Pose onePose : poses) {
            if (pose.getTimeStamp().equals(onePose.getTimeStamp())) {
                exist = true;
            }
        }

        if (exist) {
            update(db, pose, detectionId);
        } else {
            insert(db, pose, detectionId);
        }
    }

    public static void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_POSE, null, null);
    }

}
