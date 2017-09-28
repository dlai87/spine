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
import com.vasomedical.spinetracer.model.InspectionRecord;

import java.util.ArrayList;

/**
 * Created by home on 9/24/2017.
 */

public class PatientHistoryListFragment extends BaseFragment {
    LinearListView patientHistoryListView;

    @Override
    protected void assignViews() {
        patientHistoryListView = (LinearListView) view.findViewById(R.id.patient_history_list);
        BaseAdapter patientHistoryListViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                TBDetection tbDetection = new TBDetection();
                SQLiteDatabase database = DBAdapter.getDatabase(mContext);
                ArrayList<InspectionRecord> recordList = tbDetection.getDetectionList(database);
                return recordList.size();
            }

            @Override
            public Object getItem(int i) {
                TBDetection tbDetection = new TBDetection();
                SQLiteDatabase database = DBAdapter.getDatabase(mContext);
                ArrayList<InspectionRecord> recordList = tbDetection.getDetectionList(database);
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
                nameTextView.setText(record.getTimestamp()); // TEMP
                descriptionTextView.setText(record.getDoctorId()); // TEMP
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
                fragmentUtil.showFragment(new RecordDetailFragment());
            }
        };
        patientHistoryListView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_history_list, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
