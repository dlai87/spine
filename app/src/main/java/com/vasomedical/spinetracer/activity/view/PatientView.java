package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.PatientModel;

import java.util.List;

//病人-普通用户-接口
public interface PatientView {
    void updatePatient(PatientModel peopleModel);

    void updatePatientList(List<PatientModel> peopleModelListp);

    void savePatientCallBack(boolean success, String msg);

    void delPatientCallBack(boolean success, String no, String msg);
}
