package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.vasomedical.spinetracer.activity.view.LogsView;
import com.vasomedical.spinetracer.database.table.TBLog;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.LogModel;
import com.vasomedical.spinetracer.util.Global;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//日志信息-实现
public class LogsPresenterCompl implements LogsPresenter {

    private SQLiteDatabase db;
    private TBLog tbLog;
    private Handler handler;
    private SimpleDateFormat format;


    public LogsPresenterCompl(Context context) {
        db = DBAdapter.getDatabase(context);
        tbLog = new TBLog();
        handler = new Handler();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void addLog(final LogModel logModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (logModel.getDoctorModel() != null) {
                    tbLog.insert(db, logModel);
                }
            }
        }).start();
    }

    @Override
    public void addLog(String thing) {
        LogModel logModel = new LogModel();
        logModel.setThing(thing);
        logModel.setTime(format.format(new Date()));
        logModel.setDoctorModel(Global.userModel);
        addLog(logModel);
    }

    @Override
    public void getLogs(final LogsView logsView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<LogModel> logModelList = tbLog.getLogsList(db);
                if (logModelList != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            logsView.updateUILogList(logModelList);
                        }
                    });
                }
            }
        }).start();
    }
}
