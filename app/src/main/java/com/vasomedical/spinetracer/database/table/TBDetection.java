package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBDetection {

    public void insert(SQLiteDatabase db, String detectionId, String timestamp) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);

        db.insert(DBGlobal.TABLE_POSE, null, cv);
    }

    public void update(SQLiteDatabase db, String detectionId, String timestamp) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);

        String selection = DBGlobal.COL_DETECTION_ID + " =? "; // TEMP use timestamp to compare
        String[] selectionArgs = {detectionId};

        db.update(DBGlobal.TABLE_DETECTION, cv, selection, selectionArgs);
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
        db.update(DBGlobal.TABLE_DETECTION, cv, null, null);
    }

    public ArrayList<String> getDetectionIdList(SQLiteDatabase db) {
        ArrayList<String> queryResult = new ArrayList<>();
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_detection_id = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_ID);

            queryResult.add(result.getString(col_detection_id));
            while (result.moveToNext()) {
                queryResult.add(result.getString(col_detection_id));
            }
        }
        result.close();
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, String detectionId, String timestamp) {
        ArrayList<String> detectionIds = getDetectionIdList(db);
        boolean exist = false;
        for (String id : detectionIds) {
            if (id.equals(detectionId)) {
                exist = true;
            }
        }

        if (exist) {
            update(db, detectionId, timestamp);
        } else {
            insert(db, detectionId, timestamp);
        }
    }

    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DETECTION, null, null);
    }
}
