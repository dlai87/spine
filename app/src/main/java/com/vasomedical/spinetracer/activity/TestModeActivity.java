package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.util.Global;

//页面--测试模式
public class TestModeActivity extends AppCompatActivity implements View.OnClickListener {

    private View buttonLogin, buttonTest, buttonAbout;
    private DoctorPresenter userPresenter;
    String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);
        findViews();
        addAction();
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
