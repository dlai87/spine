package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Global;

//界面--使用模式
public class MianToolsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private View buttonLogout, buttonStartTest, buttonRecod, buttonPeople;
    private View groupRecod, groupPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian_tools);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonStartTest = findViewById(R.id.bg1);
        buttonRecod = findViewById(R.id.bg2);
        buttonPeople = findViewById(R.id.bg3);
        groupRecod = findViewById(R.id.buttonRecod);
        groupPeople = findViewById(R.id.buttonPeople);
        tvName = (TextView) findViewById(R.id.tvName);
    }

    private void addAction() {
        String name = Global.userModel == null ? "测试用户" : Global.userModel.getName();
        tvName.setText("欢迎您," + name);
        buttonLogout.setOnClickListener(this);
        buttonStartTest.setOnClickListener(this);
        buttonRecod.setOnClickListener(this);
        buttonPeople.setOnClickListener(this);

        if (!Global.login) {
            groupRecod.setVisibility(View.GONE);
            groupPeople.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonPeople) {
            if (Global.login) {
                Intent intent = new Intent(this, PatientActivity.class);
                intent.putExtra("model_select", false);
                startActivity(intent);
            } else {
                Toast.makeText(this, "测试用户无法添加病人", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonLogout) {
            Global.login = false;
            Global.userModel = null;
            Global.patientModel=null;
            SharedPreferences.Editor sharedata = getSharedPreferences("autologin", 0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            sharedata.putString("username", null);
            sharedata.putString("password", null);
            sharedata.commit();
            onBackPressed();
        } else if (v == buttonStartTest) {
            if (Global.login) {
                if (Global.patientModel == null) {
                    Toast.makeText(this, "请在病人管理里面选择一个病人", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SelProjectcAtivity.class);
                    intent.putExtra("patinet_no", Global.patientModel.getId());
                    startActivity(intent);
                }
//                Intent intent = new Intent(this, PatientActivity.class);
//                intent.putExtra("model_select", true);
//                startActivity(intent);
            } else {
                Intent intent = new Intent(this, SelProjectcAtivity.class);
                startActivity(intent);
            }
        } else if (v == buttonRecod) {
            if (Global.login) {
                Intent intent = new Intent(this, HistoryTestActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "测试用户无法历史记录", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
