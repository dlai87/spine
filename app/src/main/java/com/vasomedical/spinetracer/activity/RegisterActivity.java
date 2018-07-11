package com.vasomedical.spinetracer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.Timer;
import java.util.TimerTask;

//界面--用户注册
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, DoctorView {
    private EditText editName, editPass, editPass2, editRealName, editEmail, editPhone;
    private EditText editCompany, editClass;
    private View buttonBack, buttonRegister, buttonCompanyMore, buttonClassMore;
    private CheckBox buttonEye, buttonEye2;
    private DoctorPresenter doctorPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonCompanyMore = findViewById(R.id.buttonCompanyMore);
        buttonClassMore = findViewById(R.id.buttonClassMore);
        buttonRegister = findViewById(R.id.buttonRegister);
        editName = (EditText) findViewById(R.id.editName);
        editPass = (EditText) findViewById(R.id.editPass);
        editPass2 = (EditText) findViewById(R.id.editPass2);
        editRealName = (EditText) findViewById(R.id.editRealName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editCompany = (EditText) findViewById(R.id.editCompany);
        editClass = (EditText) findViewById(R.id.editClass);
        buttonEye = (CheckBox) findViewById(R.id.buttonEye);
        buttonEye2 = (CheckBox) findViewById(R.id.buttonEye2);
    }

    private void addAction() {
        doctorPresenter = new DoctorPresenterCompl(this, this);
        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonEye.setOnCheckedChangeListener(this);
        buttonEye2.setOnCheckedChangeListener(this);

        editName.setFocusable(true);
        editName.setFocusableInTouchMode(true);
        editName.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) RegisterActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);//这里的时间大概是自己测试的
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            //注册
            userRegister();
        } else if (v == buttonBack) {
            onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EditText passVew = buttonView == buttonEye ? editPass : editPass2;
        if (isChecked) {
            passVew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passVew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void loginCallBack(boolean success, String msg) {

    }

    @Override
    public void registerCallBack(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    private void userRegister() {
        DoctorModel.DoctorBuilder builder = new DoctorModel.DoctorBuilder(
                null,
                editName.getText().toString()
        );
        builder.setPassword(editPass.getText().toString());
        builder.setRealName(editRealName.getText().toString());
        builder.setEmail(editEmail.getText().toString());
        builder.setPhone(editPhone.getText().toString());
        builder.setHospital(editCompany.getText().toString());
        builder.setDepartment(editClass.getText().toString());
        DoctorModel doctorModel = builder.build();

        if (TextUtils.isEmpty(doctorModel.getName()) || TextUtils.isEmpty(doctorModel.getPassword())) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editEmail.getText())) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editPhone.getText())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editCompany.getText())) {
            Toast.makeText(this, "请输入医院", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editClass.getText())) {
            Toast.makeText(this, "请输入科室空", Toast.LENGTH_SHORT).show();
        } else {
            doctorPresenter.register(doctorModel);
        }
    }
}
