package com.vasomedical.spinetracer.activity.view;

//登录用户-管理员-接口
public interface DoctorView {
    void loginCallBack(boolean success, String msg);

    void registerCallBack(boolean success, String msg);
}
