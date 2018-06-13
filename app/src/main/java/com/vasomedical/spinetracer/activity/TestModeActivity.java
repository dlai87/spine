package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
//页面--测试模式
public class TestModeActivity extends AppCompatActivity implements View.OnClickListener {

    private View buttonLogin, buttonTest, buttonAbout;

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
