package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.PatientAdapter;
import com.vasomedical.spinetracer.activity.listener.ItemClickListener;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenter;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenterCompl;
import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.List;

//界面--用户管理
public class PatientActivity extends AppCompatActivity implements View.OnClickListener, PatientView {
    private View buttonBack, buttonAdd, groupAdd, buttonSelected, buttonEdit, buttonDel;
    private TextView tvName;
    private RecyclerView recyclerview;
    private PatientAdapter adapter;
    private PatientPresenter patientPresenter;
    private boolean selectModel = false;//选择模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        findViews();
        addAction();

//        selectModel = getIntent().getBooleanExtra("model_select", false);
//        if (selectModel) {//选择模式
//            groupAdd.setVisibility(View.GONE);
//            buttonEdit.setVisibility(View.GONE);
//            buttonDel.setVisibility(View.GONE);
//        } else {
//            buttonSelected.setVisibility(View.GONE);
//        }
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonAdd = findViewById(R.id.bg1);
        groupAdd = findViewById(R.id.buttonAdd);
        buttonSelected = findViewById(R.id.buttonSelected);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDel = findViewById(R.id.buttonDel);
        tvName = (TextView) findViewById(R.id.tvName);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void addAction() {
        patientPresenter = new PatientPresenterCompl(this, this);
        buttonBack.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonSelected.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonDel.setOnClickListener(this);

        String name = Global.userModel == null ? "测试用户" : Global.userModel.getName();
        tvName.setText(name);
        adapter = new PatientAdapter(this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addOnItemTouchListener(new ItemClickListener(this, recyclerview, new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.selectItem(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (v == buttonAdd) {
            Intent intent = new Intent(this, PatientEditActivity.class);
            startActivity(intent);
        } else if (v == buttonSelected) {
            Global.patientModel = adapter.getSelectItem();
//            Intent intent = new Intent(this, SelProjectcAtivity.class);
//            intent.putExtra("patinet_no", adapter.getSelectItem().getId());
//            startActivity(intent);
            finish();
        } else if (v == buttonDel) {
            if (adapter.getSelectItem() != null) {
                patientPresenter.deletPatient(adapter.getSelectItem().getId());
            } else {
                Toast.makeText(this, "需要先选择病人", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonEdit) {
            if (adapter.getSelectItem() != null) {
                Intent intent = new Intent(this, PatientEditActivity.class);
                intent.putExtra("patinet_no", adapter.getSelectItem().getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "需要先选择病人", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        patientPresenter.selectPatientList(Global.userModel);
    }

    @Override
    public void updatePatient(PatientModel peopleModel) {

    }

    @Override
    public void updatePatientList(List<PatientModel> peopleModelListp) {
        adapter.setData(peopleModelListp);
    }

    @Override
    public void savePatientCallBack(boolean success, String msg) {

    }

    @Override
    public void delPatientCallBack(boolean success, String no, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            adapter.removeItem(no);
        }
    }
}
