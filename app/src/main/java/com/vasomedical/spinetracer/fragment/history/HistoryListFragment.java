package com.vasomedical.spinetracer.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linearlistview.LinearListView;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by Zhitao on 9/24/2017.
 */

public class HistoryListFragment extends BaseFragment {

    LinearListView historyListView;

    @Override
    protected void assignViews() {

    }

    @Override
    protected void addActionToViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_list, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
