package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.LogsAdapter;
import com.vasomedical.spinetracer.activity.presenter.LogsPresenter;
import com.vasomedical.spinetracer.activity.presenter.LogsPresenterCompl;
import com.vasomedical.spinetracer.activity.view.LogsView;
import com.vasomedical.spinetracer.model.LogModel;

import java.util.List;

//界面--设备日志
public class AdminLogsActivity extends AppCompatActivity implements LogsView, View.OnClickListener {

    private View buttonBack;
    private RecyclerView recyclerview;
    private LogsAdapter adapter;
    private LogsPresenter logsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logs);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void addAction() {
        buttonBack.setOnClickListener(this);
        adapter = new LogsAdapter(this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        logsPresenter = new LogsPresenterCompl(this);
        logsPresenter.getLogs(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        }
    }

    @Override
    public void updateUILogList(List<LogModel> logModels) {
        adapter.addData(logModels);
    }
}
