package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBDetection {


    // FixMe :  detection table also need following information: detection_type ; score ; doctor_comments


    public void insert(SQLiteDatabase db, String detectionId, String timestamp, DoctorModel doctor, PatientModel patient) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, doctor.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, patient.getId());

        db.insert(DBGlobal.TABLE_POSE, null, cv);
    }

    public void update(SQLiteDatabase db, String detectionId, String timestamp, DoctorModel doctor, PatientModel patient) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, detectionId);
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, doctor.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, patient.getId());

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

    public ArrayList<InspectionRecord> getDetectionList(SQLiteDatabase db) {
        ArrayList<InspectionRecord> queryResult = new ArrayList<>();
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
        // TODO
//        if (result.getCount() > 0) {
//            result.moveToFirst();
//            int col_detection_id = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_ID);
//
//            queryResult.add(result.getString(col_detection_id));
//            while (result.moveToNext()) {
//                queryResult.add(result.getString(col_detection_id));
//            }
//        }
        result.close();
        return queryResult;
    }

    public ArrayList<InspectionRecord> getDetectionList(SQLiteDatabase db, PatientModel patient) {
        ArrayList<InspectionRecord> queryResult = new ArrayList<>();
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
        // TODO
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, String detectionId, String timestamp, DoctorModel doctor, PatientModel patient) {
        ArrayList<InspectionRecord> detectionList = getDetectionList(db);
        boolean exist = false;
        for (InspectionRecord record : detectionList) {
            if (record.getTimestamp().equals(timestamp)) { // TEMP: compare timestamp
                exist = true;
            }
        }

        if (exist) {
            update(db, detectionId, timestamp, doctor, patient);
        } else {
            insert(db, detectionId, timestamp, doctor, patient);
        }
    }

    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DETECTION, null, null);
    }

}
