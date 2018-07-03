package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBPatient {

    public void insert(SQLiteDatabase db, PatientModel model) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_ID, model.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_ID_DOCTOR, model.getId_doctor());
        DBUtil.smartPut(cv, DBGlobal.COL_NAME, model.getName());
        DBUtil.smartPut(cv, DBGlobal.COL_GENDER, model.getGender());
        DBUtil.smartPut(cv, DBGlobal.COL_DATE_OF_BIRTH, model.getDate_of_birth());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, model.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_PHOTO, model.getPhoto());
        DBUtil.smartPut(cv, DBGlobal.COL_NOTE, model.getNote());

        db.insert(DBGlobal.TABLE_PATIENT, null, cv);
    }

    public void update(SQLiteDatabase db, PatientModel model) {
        ContentValues cv = new ContentValues();
        //    DBUtil.smartPut(cv, DBGlobal.COL_ID, model.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_NAME, model.getName());
        DBUtil.smartPut(cv, DBGlobal.COL_GENDER, model.getGender());
        DBUtil.smartPut(cv, DBGlobal.COL_DATE_OF_BIRTH, model.getDate_of_birth());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, model.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_PHOTO, model.getPhoto());
        DBUtil.smartPut(cv, DBGlobal.COL_NOTE, model.getNote());
        String selection = DBGlobal.COL_ID + " =? ";
        String[] selectionArgs = {model.getId()};
        db.update(DBGlobal.TABLE_PATIENT, cv, selection, selectionArgs);
    }

    public void delete(SQLiteDatabase db, String id) {
        db.delete(DBGlobal.TABLE_PATIENT, DBGlobal.COL_ID, new String[]{id});
    }


    public String checkTransmissionStatus(SQLiteDatabase db) {

        String status = DBGlobal.TRANS_STATUS_INSERTED;
        Cursor result = db.query(DBGlobal.TABLE_PATIENT, null, null, null, null, null, null);
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
        db.update(DBGlobal.TABLE_PATIENT, cv, null, null);
    }


    public ArrayList<PatientModel> getPatientList(SQLiteDatabase db) {
        /*  NOTE:
        * Cursor query (String table,
                String[] columns,
                String selection,
                String[] selectionArgs,
                String groupBy,
                String having,
                String orderBy,
                String limit)
                */
        ArrayList<PatientModel> queryResult = new ArrayList<PatientModel>();
        Cursor result = db.query(DBGlobal.TABLE_PATIENT, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_id_doctor = result.getColumnIndexOrThrow(DBGlobal.COL_ID_DOCTOR);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_date_of_birth = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_photo = result.getColumnIndexOrThrow(DBGlobal.COL_PHOTO);
            int col_note = result.getColumnIndexOrThrow(DBGlobal.COL_NOTE);

            queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                    result.getString(col_name),
                    result.getString(col_gender),
                    result.getString(col_date_of_birth))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .photo(result.getString(col_photo))
                    .note(result.getString(col_note))
                    .setId_doctor(result.getString(col_id_doctor))
            ).build());

            while (result.moveToNext()) {
                queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                        result.getString(col_name),
                        result.getString(col_gender),
                        result.getString(col_date_of_birth))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .photo(result.getString(col_photo))
                        .note(result.getString(col_note))
                        .setId_doctor(result.getString(col_id_doctor))
                ).build());

            }

        }
        result.close();
        return queryResult;
    }

    public ArrayList<PatientModel> getPatientListByDoctor(SQLiteDatabase db, String id_doctor) {
        /*  NOTE:
        * Cursor query (String table,
                String[] columns,
                String selection,
                String[] selectionArgs,
                String groupBy,
                String having,
                String orderBy,
                String limit)
                */
        ArrayList<PatientModel> queryResult = new ArrayList<PatientModel>();
        Cursor result = db.query(DBGlobal.TABLE_PATIENT, null, DBGlobal.COL_ID_DOCTOR + "=?",
                new String[]{id_doctor}, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_id_doctor = result.getColumnIndexOrThrow(DBGlobal.COL_ID_DOCTOR);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_date_of_birth = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_photo = result.getColumnIndexOrThrow(DBGlobal.COL_PHOTO);
            int col_note = result.getColumnIndexOrThrow(DBGlobal.COL_NOTE);

            queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                    result.getString(col_name),
                    result.getString(col_gender),
                    result.getString(col_date_of_birth))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .photo(result.getString(col_photo))
                    .note(result.getString(col_note))
                    .setId_doctor(result.getString(col_id_doctor))
            ).build());

            while (result.moveToNext()) {
                queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                        result.getString(col_name),
                        result.getString(col_gender),
                        result.getString(col_date_of_birth))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .photo(result.getString(col_photo))
                        .note(result.getString(col_note))
                        .setId_doctor(result.getString(col_id_doctor))
                ).build());

            }

        }
        result.close();
        return queryResult;
    }

    public ArrayList<PatientModel> getPatientListByDoctorAndName(SQLiteDatabase db, String id_doctor, String name) {
        ArrayList<PatientModel> queryResult = new ArrayList<PatientModel>();
        String selection = DBGlobal.COL_ID_DOCTOR + "=? and " + DBGlobal.COL_NAME + " like ?";
        Cursor result = db.query(DBGlobal.TABLE_PATIENT, null,
                selection,
                new String[]{id_doctor, "%" + name + "%"}, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_id_doctor = result.getColumnIndexOrThrow(DBGlobal.COL_ID_DOCTOR);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_date_of_birth = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_photo = result.getColumnIndexOrThrow(DBGlobal.COL_PHOTO);
            int col_note = result.getColumnIndexOrThrow(DBGlobal.COL_NOTE);

            queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                    result.getString(col_name),
                    result.getString(col_gender),
                    result.getString(col_date_of_birth))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .photo(result.getString(col_photo))
                    .note(result.getString(col_note))
                    .setId_doctor(result.getString(col_id_doctor))
            ).build());

            while (result.moveToNext()) {
                queryResult.add((new PatientModel.PatientBuilder(result.getString(col_id),
                        result.getString(col_name),
                        result.getString(col_gender),
                        result.getString(col_date_of_birth))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .photo(result.getString(col_photo))
                        .note(result.getString(col_note))
                        .setId_doctor(result.getString(col_id_doctor))
                ).build());

            }

        }
        result.close();
        return queryResult;
    }

    public PatientModel getPatientByNo(SQLiteDatabase db, String no) {
        Cursor result = db.query(DBGlobal.TABLE_PATIENT,
                null,
                DBGlobal.COL_ID + "=?",
                new String[]{no}, null, null, null);

        PatientModel patientModel = null;
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_id_doctor = result.getColumnIndexOrThrow(DBGlobal.COL_ID_DOCTOR);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_date_of_birth = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_photo = result.getColumnIndexOrThrow(DBGlobal.COL_PHOTO);
            int col_note = result.getColumnIndexOrThrow(DBGlobal.COL_NOTE);

            patientModel = new PatientModel.PatientBuilder(result.getString(col_id),
                    result.getString(col_name),
                    result.getString(col_gender),
                    result.getString(col_date_of_birth))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .photo(result.getString(col_photo))
                    .note(result.getString(col_note))
                    .setId_doctor(result.getString(col_id_doctor))
                    .build();
        }
        result.close();
        return patientModel;
    }

    public PatientModel getPatientByNameAndIddoctor(SQLiteDatabase db, String no, String id_doctor) {
        Cursor result = db.query(DBGlobal.TABLE_PATIENT,
                null,
                DBGlobal.COL_ID + "=? and " + DBGlobal.COL_ID_DOCTOR + "=?",
                new String[]{no, id_doctor}, null, null, null);

        PatientModel patientModel = null;
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_id_doctor = result.getColumnIndexOrThrow(DBGlobal.COL_ID_DOCTOR);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_date_of_birth = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_photo = result.getColumnIndexOrThrow(DBGlobal.COL_PHOTO);
            int col_note = result.getColumnIndexOrThrow(DBGlobal.COL_NOTE);

            patientModel = new PatientModel.PatientBuilder(result.getString(col_id),
                    result.getString(col_name),
                    result.getString(col_gender),
                    result.getString(col_date_of_birth))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .photo(result.getString(col_photo))
                    .note(result.getString(col_note))
                    .setId_doctor(result.getString(col_id_doctor))
                    .build();
        }
        result.close();
        return patientModel;
    }

    public void smartInsert(SQLiteDatabase db, PatientModel model) {
        ArrayList<PatientModel> models = getPatientList(db);
        boolean exist = false;
        for (PatientModel eachModel : models) {
            if (eachModel.getId().equals(model.getId())) {
                exist = true;
            }
        }

        if (exist) {
            update(db, model);
        } else {
            insert(db, model);
        }
    }


    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_PATIENT, null, null);
    }
}
