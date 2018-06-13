package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenter;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenterCompl;
import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.List;

//界面--项目选择
public class SelProjectcAtivity extends AppCompatActivity implements View.OnClickListener, PatientView {

    private View buttonBack, buttonItem1, buttonItem2, buttonItem3, buttonItem4, buttonItem5, buttonItem6;
    private TextView tvName, tvPatientName;

    private PatientPresenter patientPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_projectctivity);
        findViews();
        addAction();

        //选择了用户了
        String patinetId = getIntent().getStringExtra("patinet_no");
        if (!TextUtils.isEmpty(patinetId)) {
            patientPresenter.selectPatientByNo(patinetId);
        }
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonItem1 = findViewById(R.id.buttonItem1);
        buttonItem2 = findViewById(R.id.buttonItem2);
        buttonItem3 = findViewById(R.id.buttonItem3);
        buttonItem4 = findViewById(R.id.buttonItem4);
        buttonItem5 = findViewById(R.id.buttonItem5);
        buttonItem6 = findViewById(R.id.buttonItem6);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);
    }

    private void addAction() {
        patientPresenter = new PatientPresenterCompl(this, this);
        buttonBack.setOnClickListener(this);
        buttonItem1.setOnClickListener(this);
        buttonItem2.setOnClickListener(this);
        buttonItem3.setOnClickListener(this);
        buttonItem4.setOnClickListener(this);
        buttonItem5.setOnClickListener(this);
        buttonItem6.setOnClickListener(this);

        String name = Global.userModel == null ? "测试用户" : Global.userModel.getName();
        tvName.setText(name);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (v == buttonItem1) {
            Toast.makeText(this, "躯干倾斜角", Toast.LENGTH_SHORT).show();
        } else if (v == buttonItem2) {
            Toast.makeText(this, "脊柱弯曲COBB角", Toast.LENGTH_SHORT).show();
        } else if (v == buttonItem3) {
            Toast.makeText(this, "左右侧弯倾角", Toast.LENGTH_SHORT).show();
        } else if (v == buttonItem4) {
            Toast.makeText(this, "身体平衡度", Toast.LENGTH_SHORT).show();
        } else if (v == buttonItem5) {
            Toast.makeText(this, "前倾/后仰角", Toast.LENGTH_SHORT).show();
        } else if (v == buttonItem6) {
            Toast.makeText(this, "旋转角", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updatePatient(PatientModel peopleModel) {
        tvPatientName.setText("病人:" + peopleModel.getName());
    }

    @Override
    public void updatePatientList(List<PatientModel> peopleModelListp) {

    }

    @Override
    public void savePatientCallBack(boolean success, String msg) {

    }

    @Override
    public void delPatientCallBack(boolean success, String no, String msg) {

    }
}
