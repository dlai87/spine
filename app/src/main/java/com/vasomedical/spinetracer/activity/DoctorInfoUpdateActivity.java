package com.vasomedical.spinetracer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenter;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.CompanyView;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.model.CompanyModel;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DoctorInfoUpdateActivity extends AppCompatActivity implements View.OnClickListener, DoctorView, CompanyView {
    private EditText editEmail, editPhone;
    private TextView tvTitle, editCompany, editClass, tvName;
    private View buttonBack, buttonCompanyMore, buttonClassMore;
    private TextView buttonRegister;
    private DoctorPresenter doctorPresenter;
    private CompanyPresenter companyPresenter;
    private CompanyModel companyModel;
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorinfoupdate);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonCompanyMore = findViewById(R.id.buttonCompanyMore);
        buttonClassMore = findViewById(R.id.buttonClassMore);
        buttonRegister = (TextView) findViewById(R.id.buttonRegister);
        tvName = (TextView) findViewById(R.id.tvName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        editCompany = (TextView) findViewById(R.id.editCompany);
        editClass = (TextView) findViewById(R.id.editClass);
    }

    private void addAction() {
        doctorPresenter = new DoctorPresenterCompl(this, this);
        companyPresenter = new CompanyPresenterCompl(this, this);
        companyPresenter.reqCompanyInfo();
        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        editClass.setOnClickListener(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) DoctorInfoUpdateActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);//这里的时间大概是自己测试的


        //编辑模式
        doctorId = getIntent().getStringExtra("doctor_no");
        doctorPresenter.selectDoctor(doctorId);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            userUpdate();//更新
        } else if (v == buttonBack) {
            onBackPressed();
        } else if (v == editClass) {
            ShowClassChoise();
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

    @Override
    public void selectAllDoctor(List<DoctorModel> doctorModelList) {

    }

    private DoctorModel findDoctorModel;

    @Override
    public void selectDoctor(DoctorModel doctorMode) {
        if (doctorMode != null) {
            this.findDoctorModel = doctorMode;
            tvName.setText("姓名：" + doctorMode.getName());
            editEmail.setText(doctorMode.getEmail());
            editPhone.setText(doctorMode.getPhone());
            editCompany.setText(doctorMode.getHospital());
            editClass.setText(doctorMode.getDepartment());
        }
    }

    @Override
    public void updateDoctorInfo(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    @Override
    public void removeDoctor(boolean success, String msg) {

    }

    private void userUpdate() {
        findDoctorModel.setEmail(editEmail.getText().toString());
        findDoctorModel.setPhone(editPhone.getText().toString());
        findDoctorModel.setHospital(editCompany.getText().toString());
        findDoctorModel.setDepartment(editClass.getText().toString());

        if (TextUtils.isEmpty(editEmail.getText())) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editPhone.getText())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editCompany.getText())) {
            Toast.makeText(this, "请在医院管理里面添加医院名称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editClass.getText())) {
            Toast.makeText(this, "请选择医院科室", Toast.LENGTH_SHORT).show();
        } else {
            doctorPresenter.updateDoctorInfo(findDoctorModel);
        }
    }

    private void ShowClassChoise() {
        if (companyModel != null && companyModel.getClassModelList() != null && companyModel.getClassModelList().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择科室");
            //    指定下拉列表的显示数据
            final String[] className = new String[companyModel.getClassModelList().size()];
            for (int i = 0; i < className.length; i++) {
                className[i] = companyModel.getClassModelList().get(i).getName();
            }
            //    设置一个下拉的列表选择项
            builder.setItems(className, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editClass.setText(className[which]);
                }
            });
            builder.show();
        } else {
            Toast.makeText(this, "请在医院管理里面添加科室", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCompanyUI(CompanyModel companyModel) {
        this.companyModel = companyModel;
        if (companyModel != null) {
            editCompany.setText(companyModel.getName());
            if (companyModel.getClassModelList() != null && companyModel.getClassModelList().size() > 0) {
                editClass.setText(companyModel.getClassModelList().get(0).getName());
            }
        }
    }

    @Override
    public void saveCompayInfoCallBack(boolean success, String msg) {

    }

    @Override
    public void saveCompayClassInfoCallBack(boolean success, String msg) {

    }
}
