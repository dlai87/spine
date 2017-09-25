package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.Pose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBDetection {

    PatientModel patientModel;
    ArrayList<Pose> data;

    public void insert(SQLiteDatabase db, String timestamp, PatientModel patientModel, DoctorModel doctorModel, ArrayList<Pose> data) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, patientModel.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, doctorModel.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_TYPE, AlgorithmFactory.detectionOption);
        JSONObject json = new JSONObject();
        try {
            json.put("uniqueArrays", new JSONArray(data));
            DBUtil.smartPut(cv, DBGlobal.COL_POSE_DATA, json.toString());
        } catch (JSONException e) {

        }
        db.insert(DBGlobal.TABLE_DETECTION, null, cv);
    }

    public void update(SQLiteDatabase db, String timestamp, PatientModel patientModel, DoctorModel doctorModel, ArrayList<Pose> data) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, timestamp);
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, patientModel.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, doctorModel.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_TYPE, AlgorithmFactory.detectionOption);
        JSONObject json = new JSONObject();
        try {
            json.put("uniqueArrays", new JSONArray(data));
            DBUtil.smartPut(cv, DBGlobal.COL_POSE_DATA, json.toString());
        } catch (JSONException e) {

        }
        String selection = DBGlobal.COL_TIMESTAMP + " =? ";
        String[] selectionArgs = {timestamp};
        db.update(DBGlobal.TABLE_DETECTION, cv, selection, selectionArgs);
    }


    public String checkTransmissionStatus(SQLiteDatabase db) {

        String status = DBGlobal.TRANS_STATUS_INSERTED;
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
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

    public ArrayList<InspectionRecord> getInspectionList(SQLiteDatabase db) {

        ArrayList<InspectionRecord> queryResult = new ArrayList<InspectionRecord>();
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_timestamp = result.getColumnIndexOrThrow(DBGlobal.COL_TIMESTAMP);
            int col_patientId = result.getColumnIndexOrThrow(DBGlobal.COL_PATIENT_ID);
            int col_doctorId = result.getColumnIndexOrThrow(DBGlobal.COL_DOCTOR_ID);
            int col_poseData = result.getColumnIndexOrThrow(DBGlobal.COL_POSE_DATA);
            // TODO
/*
            JSONObject json = new JSONObject(result.getString(col_poseData));
            ArrayList<Pose> items = json.optJSONArray("uniqueArrays");
            queryResult.add( (new InspectionRecord.InspectionRecordBuilder(result.getString(col_timestamp),
                    result.getString(col_patientId),
                    result.getString(col_doctorId),
                    items)
            .build());

            while(result.moveToNext()){
                queryResult.add( (new PatientModel.PatientBuilder(result.getString(col_id),
                        result.getString(col_name),
                        result.getString(col_gender),
                        result.getString(col_date_of_birth))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .photo(result.getString(col_photo))
                        .note(result.getString(col_note))
                ).build());
            }
*/
        }
        result.close();
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, String timestamp, PatientModel patientModel, DoctorModel doctorModel, ArrayList<Pose> data) {
        ArrayList<InspectionRecord> records = getInspectionList(db);
        boolean exist = false;
        for (InspectionRecord record : records) {
            if (record.getTimestamp().equals(record.getTimestamp())) {
                // TEMP: use timestamp as id to compare
                exist = true;
            }
        }

        if (exist) {
            update(db, timestamp, patientModel, doctorModel, data);
        } else {
            insert(db, timestamp, patientModel, doctorModel, data);
        }
    }

    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DETECTION, null, null);
    }

}
