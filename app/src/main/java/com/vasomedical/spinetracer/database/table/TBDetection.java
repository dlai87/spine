package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBDetection {



    /**
     *
     * 测量表
     *
     * 测量表里面是每一次测量的记录
     *
     * COL_DETECTION_ID    唯一key
     * COL_TIMESTAMP       测量的时间戳
     * COL_DOCTOR_ID       医生id，可以通过这个id到医生表里面找到医生的信息
     * COL_PATIENT_ID      病人id，可以通过这个id到病人表里面找到病人信息
     * COL_DETECTION_TYPE  测量的类型  如 躯干倾斜角、驼背角、侧弯cobb角 等等
     * COL_SCORE           测量评定、优、良、健康、不健康等
     * COL_COMMENT         医生写的评语
     *
     *
     * **/

    public void insert(SQLiteDatabase db, InspectionRecord record) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, record.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, record.getTimestamp());
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, record.getDoctor().getId());
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, record.getPatient().getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_TYPE, record.getType());
        DBUtil.smartPut(cv, DBGlobal.COL_SCORE, record.getScore());
        DBUtil.smartPut(cv, DBGlobal.COL_COMMENT, record.getDoctorComments());

        db.insert(DBGlobal.TABLE_POSE, null, cv);
    }

    public void update(SQLiteDatabase db, InspectionRecord record) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_ID, record.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_TIMESTAMP, record.getTimestamp());
        DBUtil.smartPut(cv, DBGlobal.COL_DOCTOR_ID, record.getDoctor().getId());
        DBUtil.smartPut(cv, DBGlobal.COL_PATIENT_ID, record.getPatient().getId());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECTION_TYPE, record.getType());
        DBUtil.smartPut(cv, DBGlobal.COL_SCORE, record.getScore());
        DBUtil.smartPut(cv, DBGlobal.COL_COMMENT, record.getDoctorComments());

        String selection = DBGlobal.COL_DETECTION_ID + " =? "; // TEMP use timestamp to compare
        String[] selectionArgs = {record.getId()};

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
        String selection = DBGlobal.COL_PATIENT_ID + " =? "; // TEMP use timestamp to compare
        String[] selectionArgs = {patient.getId()};
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, selection, selectionArgs, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_detection_id = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_ID);
            int col_timestamp = result.getColumnIndexOrThrow(DBGlobal.COL_TIMESTAMP);
            int col_patient_id = result.getColumnIndexOrThrow(DBGlobal.COL_PATIENT_ID);
            int col_doctor_id = result.getColumnIndexOrThrow(DBGlobal.COL_DOCTOR_ID);
            int col_type = result.getColumnIndexOrThrow(DBGlobal.COL_DETECTION_TYPE);
            int col_score = result.getColumnIndexOrThrow(DBGlobal.COL_SCORE);
            int col_comment = result.getColumnIndexOrThrow(DBGlobal.COL_COMMENT);

            TBPose tbPose = new TBPose();
            String detectionId = result.getString(col_detection_id);
            ArrayList<Pose> poseList = tbPose.getPoseList(db, detectionId);
            TBDoctor tbDoctor = new TBDoctor();
            String doctorId = result.getString(col_doctor_id);
            ArrayList<DoctorModel> doctorList = tbDoctor.getDoctorList(db, doctorId);
            DoctorModel doctor = doctorList.get(0);
            queryResult.add(new InspectionRecord.InspectionRecordBuilder(result.getString(col_detection_id),
                    result.getString(col_timestamp),
                    patient,
                    doctor,
                    result.getInt(col_type),
                    poseList,
                    result.getInt(col_score),
                    result.getString(col_comment)).build());
            while (result.moveToNext()) {
                detectionId = result.getString(col_detection_id);
                poseList = tbPose.getPoseList(db, detectionId);
                doctorId = result.getString(col_doctor_id);
                doctorList = tbDoctor.getDoctorList(db, doctorId);
                doctor = doctorList.get(0);
                queryResult.add(new InspectionRecord.InspectionRecordBuilder(result.getString(col_detection_id),
                        result.getString(col_timestamp),
                        patient,
                        doctor,
                        result.getInt(col_type),
                        poseList,
                        result.getInt(col_score),
                        result.getString(col_comment)).build());
            }
        }
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, InspectionRecord record) {
        ArrayList<InspectionRecord> detectionList = getDetectionList(db);
        boolean exist = false;
        for (InspectionRecord aRecord : detectionList) {
            if (aRecord.getId().equals(record.getId())) {
                exist = true;
            }
        }

        if (exist) {
            update(db, record);
        } else {
            insert(db, record);
        }
    }

    public static void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DETECTION, null, null);
    }

}
