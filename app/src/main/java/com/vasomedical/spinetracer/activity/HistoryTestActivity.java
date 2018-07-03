package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.HistoryTestAdapter;
import com.vasomedical.spinetracer.activity.presenter.HistoryTestPresenter;
import com.vasomedical.spinetracer.activity.presenter.HistoryTestPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.LogsPresenterCompl;
import com.vasomedical.spinetracer.activity.view.HistoryTestView;
import com.vasomedical.spinetracer.model.InspectionRecord;

import java.util.List;

public class HistoryTestActivity extends AppCompatActivity implements View.OnClickListener, HistoryTestView {

    private View buttonBack, btnReport, btnSearch;
    private EditText edSearch;
    private RecyclerView recyclerview;
    private HistoryTestAdapter historyTestAdapter;
    private HistoryTestPresenter historyTestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historytest);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        btnReport = findViewById(R.id.btnReport);
        btnSearch = findViewById(R.id.btnSearch);
        edSearch = (EditText) findViewById(R.id.edSearch);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void addAction() {
        buttonBack.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        historyTestAdapter = new HistoryTestAdapter(this);
        recyclerview.setAdapter(historyTestAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        historyTestPresenter = new HistoryTestPresenterCompl(this, this);
        historyTestPresenter.reqAll();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (v == btnSearch) {
            historyTestPresenter.reqName(edSearch.getText().toString());
        } else if (v == btnReport) {

        }
    }

    @Override
    public void updateUI(List<InspectionRecord> recordList) {
        historyTestAdapter.setData(recordList);
    }
}
