package com.vasomedical.spinetracer.activity.presenter;

import com.vasomedical.spinetracer.activity.view.LogsView;
import com.vasomedical.spinetracer.model.LogModel;

//日志-接口
public interface LogsPresenter {
    void addLog(LogModel logModel);

    void addLog(String thing);

    void getLogs(LogsView logsView);
}
