package com.vasomedical.spinetracer.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//历史测量 数据
public class HistoryTestModel {
    private PatientModel patientModel;
    private List<InspectionRecord> inspectionRecordList;
    private String date;

    public PatientModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
    }

    public List<InspectionRecord> getInspectionRecordList() {
        return inspectionRecordList;
    }

    public void setInspectionRecordList(List<InspectionRecord> inspectionRecordList) {
        this.inspectionRecordList = inspectionRecordList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
