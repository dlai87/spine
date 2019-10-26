package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.LogModel;

import java.util.ArrayList;

/**
 * 日志纪录表
 */
public class TBLog {


    public void insert(SQLiteDatabase db, LogModel model) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_LOGS_DOCTORID, model.getDoctorModel().getId());
        DBUtil.smartPut(cv, DBGlobal.COL_LOGS_DOCTORNAME, model.getDoctorModel().getName());
        DBUtil.smartPut(cv, DBGlobal.COL_LOGS_THING, model.getThing());
        DBUtil.smartPut(cv, DBGlobal.COL_LOGS_TIME, model.getTime());

        db.insert(DBGlobal.TABLE_LOGS, null, cv);
    }


    public ArrayList<LogModel> getLogsList(SQLiteDatabase db) {
        try {
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
            ArrayList<LogModel> queryResult = new ArrayList<LogModel>();
            Cursor result = db.query(DBGlobal.TABLE_LOGS, null, null, null, null, null, null);
            if (result.getCount() > 0) {
                result.moveToFirst();
                int col_id = result.getColumnIndexOrThrow(DBGlobal.COL_AUTO_ID);
                int col_doctor_id = result.getColumnIndexOrThrow(DBGlobal.COL_LOGS_DOCTORID);
                int col_doctor_name = result.getColumnIndexOrThrow(DBGlobal.COL_LOGS_DOCTORNAME);
                int col_thing = result.getColumnIndexOrThrow(DBGlobal.COL_LOGS_THING);
                int col_time = result.getColumnIndexOrThrow(DBGlobal.COL_LOGS_TIME);

                LogModel logModel = new LogModel();
                logModel.setId(result.getInt(col_id));
                logModel.setThing(result.getString(col_thing));
                logModel.setTime(result.getString(col_time));
                DoctorModel doctorModel = new DoctorModel();
                doctorModel.setId(result.getString(col_doctor_id));
                doctorModel.setName(result.getString(col_doctor_name));
                logModel.setDoctorModel(doctorModel);
                queryResult.add(logModel);
                while (result.moveToNext()) {
                    logModel = new LogModel();
                    logModel.setId(result.getInt(col_id));
                    logModel.setThing(result.getString(col_thing));
                    logModel.setTime(result.getString(col_time));
                    doctorModel = new DoctorModel();
                    doctorModel.setId(result.getString(col_doctor_id));
                    doctorModel.setName(result.getString(col_doctor_name));
                    logModel.setDoctorModel(doctorModel);
                    queryResult.add(logModel);
                }
            }
            result.close();
            return queryResult;
        }catch (Exception e){
            return null;
        }
    }

    public void smartInsert(SQLiteDatabase db, LogModel model) {
        ArrayList<LogModel> models = getLogsList(db);
        boolean exist = false;
        for (LogModel eachModel : models) {
            if (eachModel.getId() == model.getId()) {
                exist = true;
            }
        }
        if (!exist) {
            insert(db, model);
        }
    }


    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_LOGS, null, null);
    }

}
