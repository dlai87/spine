package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.HistoryTestModel;

import java.util.List;

public interface HistoryTestView {
    void updateUI(List<HistoryTestModel> recordList);
}
