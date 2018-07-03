package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.vasomedical.spinetracer.activity.view.HistoryTestView;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.HistoryTestModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                List<InspectionRecord> inspectionRecordList = tbDetection.getDetectionList(db, Global.userModel);
                final List<HistoryTestModel> historyTestModelList = recodeToHistoryModel(inspectionRecordList);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        historyTestView.updateUI(historyTestModelList);
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
                List<InspectionRecord> inspectionRecordList = new ArrayList<>();
                for (PatientModel patientModel : patientModels) {
                    inspectionRecordList.addAll(tbDetection.getDetectionList(db, patientModel));
                }
                final List<HistoryTestModel> historyTestModelList = recodeToHistoryModel(inspectionRecordList);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        historyTestView.updateUI(historyTestModelList);
                    }
                });
                addLog("查询检测历史信息");
            }
        }).start();
    }


    private void addLog(String thing) {
        logsPresenter.addLog(thing);
    }

    private List<HistoryTestModel> recodeToHistoryModel(List<InspectionRecord> inspectionRecordList) {
        List<HistoryTestModel> historyTestModelList = new ArrayList<>();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<InspectionRecord>> data = new HashMap<>();
        for (InspectionRecord inspectionRecord : inspectionRecordList) {
            String key = "";
            try {
                Date date = format1.parse(inspectionRecord.getTimestamp());
                key = format2.format(date) + inspectionRecord.getPatient().getId();
            } catch (Exception ignored) {
            }
            List<InspectionRecord> temp = data.get(key);
            if (temp == null) {
                temp = new ArrayList<>();
            }
            temp.add(inspectionRecord);
            data.put(key, temp);
        }

        for (List<InspectionRecord> temp : data.values()) {
            HistoryTestModel historyTestModel = new HistoryTestModel();
            historyTestModel.setDate(temp.get(0).getTimestamp());
            try {
                Date date = format1.parse(temp.get(0).getTimestamp());
                historyTestModel.setDate(format2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            historyTestModel.setInspectionRecordList(temp);
            historyTestModel.setPatientModel(temp.get(0).getPatient());
            historyTestModelList.add(historyTestModel);
        }

        return historyTestModelList;
    }
}
