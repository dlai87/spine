package com.vasomedical.spinetracer.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by home on 9/24/2017.
 */

public class RecordDetailFragment extends BaseFragment {

    @Override
    protected void assignViews() {
    }

    @Override
    protected void addActionToViews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record_detail, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
