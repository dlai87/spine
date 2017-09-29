package com.vasomedical.spinetracer.model;

import android.content.Context;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zhitao on 9/24/2017.
 */

public class InspectionRecord {

    // FixMe :  InspectionRecord also need following information:  type ; score ; doctorComments


    public final static HashMap<Integer,String> TypeTable = new HashMap<Integer,String>() {
        {
            put(AlgorithmFactory.DETECT_OPT_1, "slant");
            put(AlgorithmFactory.DETECT_OPT_2, "humpback");
            put(AlgorithmFactory.DETECT_OPT_3, "bending");
            put(AlgorithmFactory.DETECT_OPT_4, "left_right");
            put(AlgorithmFactory.DETECT_OPT_5, "forward_back");
            put(AlgorithmFactory.DETECT_OPT_6, "rotate");
            put(AlgorithmFactory.DETECT_OPT_7, "balance");
        }
    };



    String timestamp;
    PatientModel patient;
    DoctorModel doctor;
    String type;
    int score;
    String doctorComments;


    ArrayList<Pose> inspectionData;

    public InspectionRecord(InspectionRecordBuilder builder) {
        timestamp = builder.timestamp;
        patient = builder.patient;
        doctor = builder.doctor;
        type = builder.type;
        inspectionData = builder.inspectionData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public DoctorModel getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorModel doctor) {
        this.doctor = doctor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDoctorComments() {
        return doctorComments;
    }

    public void setDoctorComments(String doctorComments) {
        this.doctorComments = doctorComments;
    }

    public ArrayList<Pose> getInspectionData() {
        return inspectionData;
    }

    public void setInspectionData(ArrayList<Pose> inspectionData) {
        this.inspectionData = inspectionData;
    }

    public static class InspectionRecordBuilder {

        String timestamp;
        PatientModel patient;
        DoctorModel doctor;
        String type;
        ArrayList<Pose> inspectionData;

        public InspectionRecordBuilder(String timestamp,
                                       PatientModel patient,
                                       DoctorModel doctor,
                                       int detect_opt,
                                       ArrayList<Pose> inspectionData) {
            this.timestamp = timestamp;
            this.patient = patient;
            this.doctor = doctor;
            this.type = TypeTable.get(detect_opt);
            this.inspectionData = inspectionData;
        }

        public InspectionRecord build() {
            return new InspectionRecord(this);
        }
    }


    public String getStringForPdf(Context context){

        StringBuffer buffer = new StringBuffer();
        String typeName = "";
        if ("slant".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_1);
        }else if("humpback".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_2);
        }else if("bending".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_3);
        }else if("left_right".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_4);
        }else if("forward_back".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_5);
        }else if("rotate".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_6);
        }else if("balance".equals(type)){
            typeName = context.getResources().getString(R.string.detect_option_7);
        }


        buffer.append(context.getResources().getString(R.string.inspection_type) + " : " + typeName + "\n");


        Date time = Util.convertStringToTime(timestamp, "yyyyMMdd_HHmmss");
        buffer.append(context.getResources().getString(R.string.inspection_time) + " : " +
                Util.convertTimeToString(time, "yyyy-MM-dd HH:mm") + "\n");

        buffer.append(context.getResources().getString(R.string.doctor) + " : " + doctor.getName() + "\n");

        return buffer.toString();
    }



    public String getComments(Context context){
        StringBuffer buffer = new StringBuffer();

        buffer.append(context.getResources().getString(R.string.result_score) + " : ");
        switch (score%4){
            case 0:
                buffer.append(context.getResources().getString(R.string.result_score_0) + "\n\n");
                break;
            case 1:
                buffer.append(context.getResources().getString(R.string.result_score_1) + "\n\n");
                break;
            case 2:
                buffer.append(context.getResources().getString(R.string.result_score_2) + "\n\n");
                break;
            case 3:
                buffer.append(context.getResources().getString(R.string.result_score_3) + "\n\n");
                break;
        }

        buffer.append(context.getResources().getString(R.string.doctor_comment) + " :\n" );
        buffer.append(doctorComments);
        return buffer.toString();
    }

}
