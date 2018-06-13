package com.vasomedical.spinetracer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenter;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.ProjectPresenterCompl;
import com.vasomedical.spinetracer.activity.view.CompanyView;
import com.vasomedical.spinetracer.model.CompanyClassModel;
import com.vasomedical.spinetracer.model.CompanyModel;
import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.ArrayList;
import java.util.List;

//界面---医院管理
public class AdminCompanyActivity extends AppCompatActivity implements View.OnClickListener, CompanyView {

    private View buttonSubmit, buttonCacnel, buttonBack, buttonAddClass;
    private EditText editName, editPhone, editAddress;
    private TextView tvClassList;
    private CompanyPresenter companyPresenter;
    private CompanyModel companyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_company);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonCacnel = findViewById(R.id.buttonCacnel);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAddClass = findViewById(R.id.buttonAddClass);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editAddress = (EditText) findViewById(R.id.editAddress);
        tvClassList = (TextView) findViewById(R.id.tvClassList);
    }

    private void addAction() {
        buttonCacnel.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        companyPresenter = new CompanyPresenterCompl(this, this);
        companyPresenter.reqCompanyInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack || v == buttonCacnel) {
            onBackPressed();
        } else if (v == buttonSubmit) {
            if (this.companyModel == null) {
                this.companyModel = new CompanyModel();
            }
            companyModel.setAddress(editAddress.getText().toString());
            companyModel.setName(editName.getText().toString());
            companyModel.setPhone(editPhone.getText().toString());
            companyPresenter.updateCompanyInfo(companyModel);
        }
    }

    @Override
    public void updateCompanyUI(CompanyModel companyModel) {
        this.companyModel = companyModel;
        if (companyModel != null) {
            editName.setText(companyModel.getName());
            editAddress.setText(companyModel.getAddress());
            editPhone.setText(companyModel.getPhone());
            if (companyModel.getClassModelList() != null) {
                for (int index = 0; index < companyModel.getClassModelList().size(); index++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(index + 1);
                    stringBuilder.append("、");
                    stringBuilder.append(companyModel.getClassModelList().get(index).getName());
                }
            }
        }
    }

    @Override
    public void saveCompayInfoCallBack(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            onBackPressed();
        }
    }

    @Override
    public void saveCompayClassInfoCallBack(boolean success, String msg) {

    }

}
