package com.vasomedical.spinetracer.fragment.history;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linearlistview.LinearListView;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticBaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptBalanceFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptForwardBackFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptHumpbackFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptLeftRightFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptRotateFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptSlantFragment;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.ArrayList;

/**
 * Created by home on 9/24/2017.
 */

public class PatientHistoryListFragment extends BaseFragment {
    LinearListView patientHistoryListView;
    ArrayList<InspectionRecord> recordList;
    PatientModel patient;

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    @Override
    protected void assignViews() {
        patientHistoryListView = (LinearListView) view.findViewById(R.id.patient_history_list);
        BaseAdapter patientHistoryListViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return recordList.size();
            }

            @Override
            public Object getItem(int i) {
                return recordList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.patient_line, viewGroup, false);
                }
                TextView nameTextView = (TextView) view.findViewById(R.id.patient_line_headline);
                TextView descriptionTextView = (TextView) view.findViewById(R.id.patient_line_sidehead);
                ImageView avatarImageView = (ImageView) view.findViewById(R.id.patient_line_avatar);
                InspectionRecord record = (InspectionRecord) getItem(i);
                nameTextView.setText(record.getTimestamp());
                descriptionTextView.setText(record.getDoctor().getName());
                return view;
            }
        };
        patientHistoryListView.setAdapter(patientHistoryListViewAdapter);
    }

    @Override
    protected void addActionToViews() {
        LinearListView.OnItemClickListener onItemClickListener = new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                InspectionRecord record = recordList.get(position);
                AnalyticBaseFragment analyticFragment = null;
                switch (record.getType()) {
                    case "slant": {
                        analyticFragment = new AnalyticOptSlantFragment();
                    }
                    break;
                    case "humpback": {
                        analyticFragment = new AnalyticOptHumpbackFragment();
                    }
                    break;
                    case "bending": {
                        analyticFragment = new AnalyticOptHumpbackFragment();

                    }
                    break;
                    case "left_right": {
                        analyticFragment = new AnalyticOptLeftRightFragment();

                    }
                    break;
                    case "forward_back": {
                        analyticFragment = new AnalyticOptForwardBackFragment();
                    }
                    break;
                    case "rotate": {
                        analyticFragment = new AnalyticOptRotateFragment();
                    }
                    break;
                    case "balance": {
                        analyticFragment = new AnalyticOptBalanceFragment();
                    }
                    break;
                    default: {
                    }
                    break;
                }
                if (analyticFragment != null) {
                    analyticFragment.setRecord(record);
                    fragmentUtil.showFragment(new RecordDetailFragment());
                }
            }
        };
        patientHistoryListView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_history_list, container, false);

        TBDetection tbDetection = new TBDetection();
        SQLiteDatabase database = DBAdapter.getDatabase(mContext);
//        recordList = tbDetection.getDetectionList(database, patient);
        recordList = tbDetection.getDetectionList(database);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
