package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenter;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenterCompl;
import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.List;

//界面-病人添加或编辑
public class PatientEditActivity extends AppCompatActivity implements View.OnClickListener, PatientView {
    private View buttonBack, buttonSave;
    private TextView tvName;
    private EditText editNo, editName, editSex, editBirthDate, editPhone, editIdCard, editAddress, editRemarks;
    private PatientPresenter patientPresenter;
    private boolean isUpdateModel = false;//是否编辑模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_edit);
        findViews();
        addAction();

        //编辑模式
        String patinetId = getIntent().getStringExtra("patinet_no");
        if (!TextUtils.isEmpty(patinetId)) {
            patientPresenter.selectPatientByNo(patinetId);
        }
    }

    private void findViews() {
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);
        tvName = (TextView) findViewById(R.id.tvName);
        editNo = (EditText) findViewById(R.id.editNo);
        editSex = (EditText) findViewById(R.id.editSex);
        editBirthDate = (EditText) findViewById(R.id.editBirthDate);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editIdCard = (EditText) findViewById(R.id.editIdCard);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editName = (EditText) findViewById(R.id.editName);
        editRemarks = (EditText) findViewById(R.id.editRemarks);
    }

    private void addAction() {
        String name = Global.userModel == null ? "测试用户" : Global.userModel.getName();
        tvName.setText(name);
        patientPresenter = new PatientPresenterCompl(this, this);
        buttonSave.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (buttonSave == v) {
            savePeople();
        }
    }

    private void savePeople() {
        PatientModel.PatientBuilder builder = new PatientModel.PatientBuilder(
                editNo.getText().toString(),
                editName.getText().toString(),
                editSex.getText().toString(),
                editBirthDate.getText().toString()
        );
//        builder.setAddress(editAddress.getText().toString());
//        builder.setIdcard(editIdCard.getText().toString());
//        builder.setPhone(editPhone.getText().toString());
//        builder.setRemarks(editRemarks.getText().toString());

        builder.setId_doctor(Global.userModel.getId());
        PatientModel peopleModel = builder.build();

        if (TextUtils.isEmpty(peopleModel.getId())) {
            Toast.makeText(this, "编号不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(peopleModel.getName())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(peopleModel.getDate_of_birth())) {
            Toast.makeText(this, "出生日期不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if(isUpdateModel){
                patientPresenter.updatePatient(peopleModel);
            }else {
                patientPresenter.savePatient(peopleModel);
            }
        }
    }

    @Override
    public void updatePatient(PatientModel peopleModel) {
        if (peopleModel != null) {
            isUpdateModel = true;
            editNo.setText(peopleModel.getId());
            editNo.setEnabled(false);
            editName.setText(peopleModel.getName());
            editSex.setText(peopleModel.getGender());
            editBirthDate.setText(peopleModel.getDate_of_birth());
            editPhone.setText(peopleModel.getPhone());
            editRemarks.setText(peopleModel.getNote());
        }
    }

    @Override
    public void updatePatientList(List<PatientModel> peopleModelListp) {

    }

    @Override
    public void savePatientCallBack(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    @Override
    public void delPatientCallBack(boolean success, String no, String msg) {

    }
}
