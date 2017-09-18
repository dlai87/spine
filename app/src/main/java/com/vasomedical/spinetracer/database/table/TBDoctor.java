package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/13/17.
 */

public class TBDoctor {


    public void insert(SQLiteDatabase db, DoctorModel model){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_ID, model.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_NAME, model.getName());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, model.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_EMAIL, model.getEmail());
        DBUtil.smartPut(cv, DBGlobal.COL_HOSPITAL, model.getHospital());
        DBUtil.smartPut(cv, DBGlobal.COL_DEPARTMENT, model.getDepartment());

        db.insert(DBGlobal.TABLE_DOCTOR, null, cv);
    }

    public void update(SQLiteDatabase db, DoctorModel model){
        ContentValues cv = new ContentValues();
    //    DBUtil.smartPut(cv, DBGlobal.COL_ID, model.getId());
        DBUtil.smartPut(cv, DBGlobal.COL_NAME, model.getName());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, model.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_EMAIL, model.getEmail());
        DBUtil.smartPut(cv, DBGlobal.COL_HOSPITAL, model.getHospital());
        DBUtil.smartPut(cv, DBGlobal.COL_DEPARTMENT, model.getDepartment());
        String selection = DBGlobal.COL_ID + " =? ";
        String[] selectionArgs = {model.getId()};
        db.update(DBGlobal.TABLE_DOCTOR, cv, selection, selectionArgs);
    }


    public String checkTransmissionStatus(SQLiteDatabase db){

        String status = DBGlobal.TRANS_STATUS_INSERTED;
        Cursor result = db.query(DBGlobal.TABLE_DOCTOR, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_status = result.getColumnIndexOrThrow(DBGlobal.COL_TRANSMISSTION_STATUS);
            status = result.getString(col_status);
        }
        result.close();
        return status;
    }

    public void updateTransmissionStatus(SQLiteDatabase db, String status){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSTION_STATUS, status);
        db.update(DBGlobal.TABLE_DOCTOR, cv, null, null);
    }



    public ArrayList<DoctorModel> getDoctorList(SQLiteDatabase db){
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
        ArrayList<DoctorModel> queryResult = new ArrayList<DoctorModel>();
        Cursor result = db.query(DBGlobal.TABLE_DOCTOR, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_ID);
            int col_name = result.getColumnIndexOrThrow(DBGlobal.COL_NAME);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_hospital = result.getColumnIndexOrThrow(DBGlobal.COL_HOSPITAL);
            int col_department = result.getColumnIndexOrThrow(DBGlobal.COL_DEPARTMENT);

            queryResult.add( (new DoctorModel.DoctorBuilder(result.getString(col_id), result.getString(col_name))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .hospital(result.getString(col_hospital))
                    .department(result.getString(col_department))
            ).build());

            while(result.moveToNext()){
                queryResult.add( (new DoctorModel.DoctorBuilder(result.getString(col_id), result.getString(col_name))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .hospital(result.getString(col_hospital))
                        .department(result.getString(col_department))
                ).build());

            }

        }
        result.close();
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, DoctorModel model){
        ArrayList<DoctorModel> models = getDoctorList(db);
        boolean exist = false;
        for (DoctorModel eachModel : models){
            if (eachModel.getId().equals(model.getId())){
                exist = true;
            }
        }

        if (exist){
            update(db, model);
        }else {
            insert(db, model);
        }
    }


    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DOCTOR, null, null);
    }

}
