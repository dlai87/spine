package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.vasomedical.spinetracer.activity.view.HistoryTestView;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.ArrayList;
import java.util.List;

public class HistoryTestPresenterCompl implements HistoryTestPresenter {

    private HistoryTestView historyTestView;
    private Handler handler;
    private Context mContext;

    private SQLiteDatabase db;
    private TBDetection tbDetection;
    private TBPatient tbPatient;
    private LogsPresenter logsPresenter;

    public HistoryTestPresenterCompl(Context context, HistoryTestView historyTestView) {
        this.historyTestView = historyTestView;
        db = DBAdapter.getDatabase(context);
        tbDetection = new TBDetection();
        tbPatient = new TBPatient();
        handler = new Handler();
        logsPresenter = new LogsPresenterCompl(context);
    }

    @Override
    public void reqAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<InspectionRecord> inspectionRecordList = tbDetection.getDetectionList(db, Global.userModel);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        historyTestView.updateUI(inspectionRecordList);
                    }
                });
                addLog("查询检测历史信息");
            }
        }).start();
    }

    @Override
    public void reqName(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PatientModel> patientModels = tbPatient.getPatientListByDoctorAndName(db, Global.userModel.getId(), name);
                final List<InspectionRecord> inspectionRecordList = new ArrayList<>();
                for (PatientModel patientModel : patientModels) {
                    inspectionRecordList.addAll(tbDetection.getDetectionList(db, patientModel));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        historyTestView.updateUI(inspectionRecordList);
                    }
                });
                addLog("查询检测历史信息");
            }
        }).start();
    }


    private void addLog(String thing) {
        logsPresenter.addLog(thing);
    }
}
