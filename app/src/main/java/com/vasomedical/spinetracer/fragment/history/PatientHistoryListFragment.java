package com.vasomedical.spinetracer.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linearlistview.LinearListView;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by home on 9/24/2017.
 */

public class PatientHistoryListFragment extends BaseFragment {
    LinearListView patientHistoryListView;

    @Override
    protected void assignViews() {
        patientHistoryListView = (LinearListView) view.findViewById(R.id.patient_history_list);
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
