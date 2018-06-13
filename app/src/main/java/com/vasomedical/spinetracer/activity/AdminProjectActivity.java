package com.vasomedical.spinetracer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.ProjectPresenter;
import com.vasomedical.spinetracer.activity.presenter.ProjectPresenterCompl;
import com.vasomedical.spinetracer.activity.view.ProjectView;
import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.ArrayList;
import java.util.List;

//界面--测量模块管理
public class AdminProjectActivity extends AppCompatActivity implements ProjectView, View.OnClickListener {

    private List<Switch> switchItemList;
    private View buttonSubmit, buttonCacnel, buttonBack;

    private ProjectPresenter projectPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_project);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonCacnel = findViewById(R.id.buttonCacnel);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonBack = findViewById(R.id.buttonBack);
        switchItemList = new ArrayList<>();
        switchItemList.add((Switch) findViewById(R.id.switchItem1));
        switchItemList.add((Switch) findViewById(R.id.switchItem2));
        switchItemList.add((Switch) findViewById(R.id.switchItem3));
        switchItemList.add((Switch) findViewById(R.id.switchItem4));
        switchItemList.add((Switch) findViewById(R.id.switchItem5));
        switchItemList.add((Switch) findViewById(R.id.switchItem6));
    }

    private void addAction() {
        buttonCacnel.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        projectPresenter = new ProjectPresenterCompl(this, this);
        projectPresenter.reqProjectList();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack || v == buttonCacnel) {
            onBackPressed();
        } else if (v == buttonSubmit) {
            List<ProjectModel> projectModels = new ArrayList<>();
            for (Switch switchItem : switchItemList) {
                ProjectModel projectModel = (ProjectModel) switchItem.getTag();
                projectModel.setEnable(switchItem.isChecked());
                projectModels.add(projectModel);
            }
            projectPresenter.updateProjetList(projectModels);
        }
    }

    @Override
    public void updateUIProjetList(List<ProjectModel> projectModels) {
        for (Switch switchItem : switchItemList) {
            for (ProjectModel projectModel : projectModels) {
                if (String.valueOf(switchItem.getId()).equals(projectModel.getId())) {
                    switchItem.setChecked(projectModel.isEnable());
                    switchItem.setTag(projectModel);
                }
            }
        }
    }

    @Override
    public void updateProjetListCallBack(boolean success, String msg) {
        finish();
    }
}
