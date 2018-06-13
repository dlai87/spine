package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Global;

//界面--管理员账户
public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private View buttonUser, buttonItem, buttonCompany, buttonDevice, buttonTestMain;

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
    }

    private void addAction() {
        buttonUser.setOnClickListener(this);
        buttonItem.setOnClickListener(this);
        buttonCompany.setOnClickListener(this);
        buttonDevice.setOnClickListener(this);
        buttonTestMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (buttonUser == v) {//用户管理
            Toast.makeText(this, "用户管理", Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public void onBackPressed() {
        Global.login = false;
        Global.userModel = null;
        super.onBackPressed();
    }
}
