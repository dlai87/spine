package com.vasomedical.spinetracer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.Util;

import java.util.List;

//页面--测试模式
public class TestModeActivity extends AppCompatActivity implements View.OnClickListener {

    private View buttonLogin, buttonTest, buttonAbout;
    private DoctorPresenter userPresenter;
    String userName, password;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);


        if (!hasPermissionsGranted(Global.PERMISSIONS)) {
            requestPermissions();
        }else{
            runAfterPermissionGranted();
        }


        findViews();
        addAction();
        Util.checkFolder();

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, Global.PERMISSIONS, Global.REQUEST_PERMISSIONS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        if (requestCode == Global.REQUEST_PERMISSIONS) {
            boolean allPermissionGranted = true;
            if (grantResults.length == Global.PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false;
                        // fixme :  show toast
                        break;
                    }
                }
                if(allPermissionGranted){
                    runAfterPermissionGranted();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this,permission)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e( "show", "No permission granted for :" + permission);
                return false;
            }
        }
        Log.i("show", "All permissions have been granted.");
        return true;
    }


    private void runAfterPermissionGranted(){


    }

    private void findViews() {
        buttonLogin = findViewById(R.id.bg1);
        buttonTest = findViewById(R.id.bg2);
        buttonAbout = findViewById(R.id.bg3);
    }

    private void addAction() {
        buttonLogin.setOnClickListener(this);
        buttonTest.setOnClickListener(this);
        buttonAbout.setOnClickListener(this);
        //是否登录了
        SharedPreferences sharedata = getSharedPreferences("autologin", 0);
        userName = sharedata.getString("username", null);
        password = sharedata.getString("password", null);
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            userPresenter = new DoctorPresenterCompl(this, new DoctorView() {
                @Override
                public void loginCallBack(boolean success, String msg) {
                    if (success) {
                        if ("admin".equalsIgnoreCase(userName)) {
                            Intent intent = new Intent(TestModeActivity.this, AdminActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(TestModeActivity.this, MianToolsActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void registerCallBack(boolean success, String msg) {

                }

                @Override
                public void selectAllDoctor(List<DoctorModel> doctorModelList) {

                }

                @Override
                public void selectDoctor(DoctorModel doctorMode) {

                }

                @Override
                public void updateDoctorInfo(boolean success, String msg) {

                }

                @Override
                public void removeDoctor(boolean success, String msg) {

                }
            });
            userPresenter.login(userName, password);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (v == buttonTest) {
            Intent intent = new Intent(this, MianToolsActivity.class);
            startActivity(intent);
        } else if (v == buttonAbout) {
            Toast.makeText(this, "关于我们", Toast.LENGTH_SHORT).show();
        }
    }
}
