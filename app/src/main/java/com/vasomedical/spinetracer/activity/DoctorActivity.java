package com.vasomedical.spinetracer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.DoctorAdapter;
import com.vasomedical.spinetracer.activity.listener.ItemClickListener;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.dialog.ChangePasswordDialog;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;

//界面--医生管理
public class DoctorActivity extends AppCompatActivity implements View.OnClickListener, DoctorView {
    private View buttonBack, buttonAdd, groupAdd, buttonEdit, buttonDel;
    private RecyclerView recyclerview;
    private DoctorAdapter adapter;
    private DoctorPresenter doctorPresenter;
    private boolean selectModel = false;//选择模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonAdd = findViewById(R.id.bg1);
        groupAdd = findViewById(R.id.buttonAdd);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDel = findViewById(R.id.buttonDel);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void addAction() {
        doctorPresenter = new DoctorPresenterCompl(this, this);
        buttonBack.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonDel.setOnClickListener(this);

        adapter = new DoctorAdapter(this);
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
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v == buttonDel) {
            if (adapter.getSelectItem() != null) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("你确定要删除：" + adapter.getSelectItem().getName() + "医生吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doctorPresenter.delectDoctor(adapter.getSelectItem().getId());
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                Toast.makeText(this, "需要先选择医生", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonEdit) {
            if (adapter.getSelectItem() != null) {
//                Intent intent = new Intent(this, RegisterActivity.class);
//                intent.putExtra("doctor_no", adapter.getSelectItem().getId());
//                startActivity(intent);
                ChangePasswordDialog dialog = new ChangePasswordDialog(this);
                dialog.setDoctorPresenter(doctorPresenter);
                dialog.setDoctorModel(adapter.getSelectItem());
                dialog.show();
            } else {
                Toast.makeText(this, "需要先选择医生", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doctorPresenter.selectAllDoctor();
    }

    @Override
    public void loginCallBack(boolean success, String msg) {

    }

    @Override
    public void registerCallBack(boolean success, String msg) {

    }

    @Override
    public void selectAllDoctor(List<DoctorModel> doctorModelList) {
        adapter.setData(doctorModelList);
    }

    @Override
    public void selectDoctor(DoctorModel doctorMode) {

    }

    @Override
    public void updateDoctorInfo(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeDoctor(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        doctorPresenter.selectAllDoctor();
    }
}
