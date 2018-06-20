package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.LogModel;

import java.util.List;

//日志视图
public interface LogsView {
    void updateUILogList(List<LogModel> logModels);
}
