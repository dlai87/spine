package com.vasomedical.spinetracer.activity.presenter;

import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.PatientModel;

//病人的数据控制-接口
public interface PatientPresenter {
    void selectPatientByNo(String no);

    void selectPatientList(DoctorModel userModel);

    void savePatient(PatientModel peopleModel);

    void updatePatient(PatientModel peopleModel);

    void deletPatient(String no);
}
