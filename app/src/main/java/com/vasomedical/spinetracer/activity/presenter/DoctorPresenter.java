package com.vasomedical.spinetracer.activity.presenter;

import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;

//用户管理的数据操作-接口
public interface DoctorPresenter {
    void login(String username, String password);

    void register(DoctorModel userModel);

    void selectAllDoctor();

    void selectDoctor(String id);

    void updateDoctorInfo(DoctorModel doctorModel);

    void delectDoctor(String doctorid);
}
