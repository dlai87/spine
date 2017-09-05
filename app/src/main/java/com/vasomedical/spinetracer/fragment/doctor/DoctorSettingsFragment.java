package com.vasomedical.spinetracer.fragment.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by Zhitao Fan on 9/4/2017.
 */

public class DoctorSettingsFragment extends BaseFragment {
    EditText doctorNameEditText;
    EditText doctorPhoneEditText;
    EditText doctorEmailEditText;
    EditText doctorHospitalEditText;
    EditText doctorDepartmentEditText;

    @Override
    protected void assignViews() {
        doctorNameEditText = (EditText) view.findViewById(R.id.doctor_name);
        doctorPhoneEditText = (EditText) view.findViewById(R.id.doctor_phone);
        doctorEmailEditText = (EditText) view.findViewById(R.id.doctor_email);
        doctorHospitalEditText = (EditText) view.findViewById(R.id.doctor_hospital);
        doctorDepartmentEditText = (EditText) view.findViewById(R.id.doctor_department);
    }

    @Override
    protected void addActionToViews() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_settings, container, false);
        return view;
    }
}
