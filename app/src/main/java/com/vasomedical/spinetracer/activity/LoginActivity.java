package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;

import static android.provider.Telephony.Mms.Part.FILENAME;

//页面--登录
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DoctorView {

    private View buttonBack, buttonRegister, buttonLogin;
    private EditText editName, editPass;
    private CheckBox buttonEye;
    private DoctorPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonBack = findViewById(R.id.buttonBack);
        buttonRegister = findViewById(R.id.buttonRegister);
        editName = (EditText) findViewById(R.id.editName);
        editPass = (EditText) findViewById(R.id.editPass);
        buttonEye = (CheckBox) findViewById(R.id.buttonEye);
    }

    private void addAction() {
        userPresenter = new DoctorPresenterCompl(this, this);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

        buttonEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (v == buttonLogin) {
            //登录
            userLogin();
        } else if (v == buttonRegister) {
            //注册
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void userLogin() {
        String name = editName.getText().toString();
        String pass = editPass.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            userPresenter.login(name, pass);
        }
    }

    @Override
    public void loginCallBack(boolean success, String msg) {
        if (success) {
            // 保存对象
            SharedPreferences.Editor sharedata = getSharedPreferences("autologin", 0).edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            sharedata.putString("username", editName.getText().toString());
            sharedata.putString("password", editPass.getText().toString());
            sharedata.commit();

            if ("admin".equalsIgnoreCase(editName.getText().toString())) {
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, MianToolsActivity.class);
                startActivity(intent);
            }
            finish();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
}
