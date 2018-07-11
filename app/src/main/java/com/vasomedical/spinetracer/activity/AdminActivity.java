package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Global;

//界面--管理员账户
public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private View buttonUser, buttonItem, buttonCompany, buttonDevice, buttonTestMain, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonUser = findViewById(R.id.bg1);
        buttonItem = findViewById(R.id.bg2);
        buttonCompany = findViewById(R.id.bg3);
        buttonDevice = findViewById(R.id.bg4);
        buttonTestMain = findViewById(R.id.buttonTestMain);
        buttonLogout = findViewById(R.id.buttonLogout);
    }

    private void addAction() {
        buttonUser.setOnClickListener(this);
        buttonItem.setOnClickListener(this);
        buttonCompany.setOnClickListener(this);
        buttonDevice.setOnClickListener(this);
        buttonTestMain.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (buttonUser == v) {//用户管理
            //Toast.makeText(this, "用户管理", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v == buttonItem) {//测量模块
            Intent intent = new Intent(this, AdminProjectActivity.class);
            startActivity(intent);
        } else if (v == buttonCompany) {//医院管理
            Intent intent = new Intent(this, AdminCompanyActivity.class);
            startActivity(intent);
        } else if (v == buttonDevice) {//设备日志
            Intent intent = new Intent(this, AdminLogsActivity.class);
            startActivity(intent);
        } else if (v == buttonTestMain) {//测量主界面
            Intent intent = new Intent(this, SelProjectcAtivity.class);
            startActivity(intent);
        } else if (v == buttonLogout) {
            Global.login = false;
            Global.userModel = null;
            SharedPreferences.Editor sharedata = getSharedPreferences("autologin", 0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            sharedata.putString("username", null);
            sharedata.putString("password", null);
            sharedata.commit();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Global.login = false;
        Global.userModel = null;
        super.onBackPressed();
    }
}
