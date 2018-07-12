package com.vasomedical.spinetracer.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenter;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenterCompl;
import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.Calendar;
import java.util.List;

//界面-病人添加或编辑
public class PatientEditActivity extends AppCompatActivity implements View.OnClickListener, PatientView {
    private View buttonBack, buttonSave;
    private TextView tvName, tvTitle, editSex, editBirthDate;
    private EditText editNo, editName, editPhone, editIdCard, editAddress, editRemarks;
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
            tvTitle.setText("编辑病人");
        } else {
            tvTitle.setText("新建病人");
        }
    }

    private void findViews() {
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName = (TextView) findViewById(R.id.tvName);
        editNo = (EditText) findViewById(R.id.editNo);
        editSex = (TextView) findViewById(R.id.editSex);
        editBirthDate = (TextView) findViewById(R.id.editBirthDate);
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
        editSex.setOnClickListener(this);
        editBirthDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (buttonSave == v) {
            savePeople();
        } else if (editSex == v) {
            ShowSexChoise();
        } else if (editBirthDate == v) {
            showDatePickerDialog();
        }
    }

    private void ShowSexChoise() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择性别");
        //    指定下拉列表的显示数据
        final String[] cities = {"男", "女"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editSex.setText(cities[which]);
            }
        });
        builder.show();
    }


    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

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
            if (isUpdateModel) {
                patientPresenter.updatePatient(peopleModel);
            } else {
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
