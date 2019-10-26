package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;

//登录用户-管理员-接口
public interface DoctorView {
    void loginCallBack(boolean success, String msg);

    void registerCallBack(boolean success, String msg);

    void selectAllDoctor(List<DoctorModel> doctorModelList);

    void selectDoctor(DoctorModel doctorMode);

    void updateDoctorInfo(boolean success, String msg);

    void removeDoctor(boolean success, String msg);
}
