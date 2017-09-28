package com.vasomedical.spinetracer.fragment.doctor;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.table.TBDoctor;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.NJButton;

import java.util.ArrayList;

/**
 * Created by Zhitao Fan on 9/4/2017.
 */

public class DoctorSettingsFragment extends BaseFragment {
    EditText doctorNameEditText;
    EditText doctorPhoneEditText;
    EditText doctorEmailEditText;
    EditText doctorHospitalEditText;
    EditText doctorDepartmentEditText;
    NJButton saveButton;

    @Override
    protected void assignViews() {
        doctorNameEditText = (EditText) view.findViewById(R.id.doctor_name);
        doctorPhoneEditText = (EditText) view.findViewById(R.id.doctor_phone);
        doctorEmailEditText = (EditText) view.findViewById(R.id.doctor_email);
        doctorHospitalEditText = (EditText) view.findViewById(R.id.doctor_hospital);
        doctorDepartmentEditText = (EditText) view.findViewById(R.id.doctor_department);
        saveButton = (NJButton) view.findViewById(R.id.save_doctor_settings);
    }

    @Override
    protected void addActionToViews() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorModel doctor = Util.getCurrentDoctor();
                DoctorModel.DoctorBuilder doctorBuilder = new DoctorModel.DoctorBuilder(
                        doctor.getId(),
                        doctorNameEditText.getText().toString());
                doctorBuilder.phone(doctorPhoneEditText.getText().toString());
                doctorBuilder.email(doctorEmailEditText.getText().toString());
                doctorBuilder.hospital(doctorHospitalEditText.getText().toString());
                doctorBuilder.department(doctorDepartmentEditText.getText().toString());

                final SQLiteDatabase database = DBAdapter.getDatabase(mContext);
                TBDoctor tbDoctor = new TBDoctor();
                tbDoctor.smartInsert(database, doctorBuilder.build());
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_settings, container, false);
        keyboardAdvance(view);

        View superView = super.onCreateView(inflater, container, savedInstanceState);
        final SQLiteDatabase database = DBAdapter.getDatabase(mContext);
        TBDoctor tbDoctor = new TBDoctor();
        ArrayList<DoctorModel> doctorList = tbDoctor.getDoctorList(database);
        if (doctorList.size() > 0) {
            DoctorModel doctor = Util.getCurrentDoctor();

            doctorNameEditText.setText(doctor.getName());
            doctorPhoneEditText.setText(doctor.getPhone());
            doctorEmailEditText.setText(doctor.getEmail());
            doctorHospitalEditText.setText(doctor.getHospital());
            doctorDepartmentEditText.setText(doctor.getDepartment());
        }

        return superView;
    }

    /**
     * Enable dismiss keyboard when touch out-side of the edit box
     */
    public void keyboardAdvance(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard((Activity) mContext);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                keyboardAdvance(innerView);
            }
        }
    }
}
