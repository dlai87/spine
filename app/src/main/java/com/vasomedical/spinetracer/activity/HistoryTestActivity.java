package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.HistoryTestAdapter;
import com.vasomedical.spinetracer.activity.presenter.HistoryTestPresenter;
import com.vasomedical.spinetracer.activity.presenter.HistoryTestPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.LogsPresenterCompl;
import com.vasomedical.spinetracer.activity.view.HistoryTestView;
import com.vasomedical.spinetracer.model.HistoryTestModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.util.PdfUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HistoryTestActivity extends AppCompatActivity implements View.OnClickListener, HistoryTestView {

    private View buttonBack, btnReport, btnSearch;
    private EditText edSearch;
    private RecyclerView recyclerview;
    private HistoryTestAdapter historyTestAdapter;
    private HistoryTestPresenter historyTestPresenter;
    private PdfUtils mPdfUtils;

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
        mPdfUtils = new PdfUtils(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            onBackPressed();
        } else if (v == btnSearch) {
            historyTestPresenter.reqName(edSearch.getText().toString());
        } else if (v == btnReport) {
            Map<InspectionRecord, Set<InspectionRecord>> modelSetMap = historyTestAdapter.getSelectModel();
            String pdfName = System.currentTimeMillis() + ".pdf";
            mPdfUtils.output(pdfName, modelSetMap);

            Toast.makeText(this, "文件保存在SD卡，Spine文件夹里面，文件名为:" + pdfName+" 保存时间需要一定的时间，保存完成后将自动开", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateUI(List<InspectionRecord> recordList) {
        historyTestAdapter.setData(recordList);
    }
}
