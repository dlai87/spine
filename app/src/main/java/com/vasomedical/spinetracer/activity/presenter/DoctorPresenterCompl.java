package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;

import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.database.table.TBDoctor;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.UUID;

//用户管理的数据操作-实现
public class DoctorPresenterCompl implements DoctorPresenter {

    private DoctorView doctorView;
    private SQLiteDatabase db;
    private TBDoctor tbDoctor;
    private Handler handler;
    private LogsPresenter logsPresenter;

    public DoctorPresenterCompl(Context context, DoctorView doctorView) {
        this.doctorView = doctorView;
        db = DBAdapter.getDatabase(context);
        tbDoctor = new TBDoctor();
        handler = new Handler();
        logsPresenter = new LogsPresenterCompl(context);
    }

    @Override
    public void login(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DoctorModel doctorModel = tbDoctor.getDoctorByNameAndPass(db, username, password);
                final boolean success = doctorModel != null;
                Global.login = success;
                Global.userModel = doctorModel;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doctorView.loginCallBack(success, success ? "登录成功" : "用户名或密码错误");
                    }
                });
                if (success) {
                    addLog(doctorModel, "登录");
                }
            }
        }).start();
    }

    @Override
    public void register(final DoctorModel doctorModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DoctorModel existDoctorModel = tbDoctor.getDoctorByName(db, doctorModel.getName());
                final boolean isExist = existDoctorModel != null;//是否存在
                if (!isExist) {
                    if (TextUtils.isEmpty(doctorModel.getId())) {
                        doctorModel.setId(UUID.randomUUID().toString());
                    }
                    tbDoctor.insert(db, doctorModel);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doctorView.registerCallBack(!isExist, isExist ? "用户已经存在" : "注册成功");
                    }
                });
                if (!isExist) {
                    addLog(doctorModel, "注册成功");
                }
            }
        }).start();
    }

    private void addLog(DoctorModel doctorModel, String thing) {
        logsPresenter.addLog(thing);
    }

}
