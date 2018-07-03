package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.InspectionRecord;

import java.util.List;

public interface HistoryTestView {
    void updateUI(List<InspectionRecord> recordList);
}
