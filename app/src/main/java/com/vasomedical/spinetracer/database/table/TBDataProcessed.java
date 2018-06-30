package com.vasomedical.spinetracer.database.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.database.util.DBGlobal;
import com.vasomedical.spinetracer.database.util.DBUtil;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/29/18.
 */

public class TBDataProcessed {


    /**
     * 这个内部算法实验用到
     * 
     *
     * **/
    public void insert(SQLiteDatabase db, Entry dataProcessed, String type) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_ENTRY_X, String.format("%.2f",dataProcessed.getX()));
        DBUtil.smartPut(cv, DBGlobal.COL_ENTRY_Y, String.format("%.2f",dataProcessed.getY()));
        DBUtil.smartPut(cv, DBGlobal.COL_TYPE, type);
        db.insert(DBGlobal.TABLE_DATA_PROCESSED, null, cv);
    }

    public void smartInsert(SQLiteDatabase db, ArrayList<Entry> dataProcessed, String type){
        for(Entry entry : dataProcessed){
            insert(db, entry, type);
        }
    }

    public static void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DATA_PROCESSED, null, null);
    }


}
