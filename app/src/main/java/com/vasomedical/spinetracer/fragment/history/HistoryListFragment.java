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
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.ArrayList;

/**
 * Created by Zhitao on 9/24/2017.
 */

public class HistoryListFragment extends BaseFragment {

    LinearListView historyListView;

    @Override
    protected void assignViews() {
        historyListView = (LinearListView) view.findViewById(R.id.history_patient_list);
        BaseAdapter historyListViewAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                TBPatient tbPatient = new TBPatient();
                SQLiteDatabase database = DBAdapter.getDatabase(mContext);
                ArrayList<PatientModel> patientList = tbPatient.getPatientList(database);
                return patientList.size();
            }

            @Override
            public Object getItem(int i) {
                TBPatient tbPatient = new TBPatient();
                SQLiteDatabase database = DBAdapter.getDatabase(mContext);
                ArrayList<PatientModel> patientList = tbPatient.getPatientList(database);
                return patientList.get(i);
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
                PatientModel patientModel = (PatientModel) getItem(i);
                nameTextView.setText(patientModel.getName());
                descriptionTextView.setText(patientModel.getNote());
                return view;
            }
        };
        historyListView.setAdapter(historyListViewAdapter);
    }

    @Override
    protected void addActionToViews() {
        LinearListView.OnItemClickListener onItemClickListener = new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                fragmentUtil.showFragment(new PatientHistoryListFragment());
            }
        };
        historyListView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_list, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
