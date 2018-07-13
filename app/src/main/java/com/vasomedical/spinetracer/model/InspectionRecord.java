package com.vasomedical.spinetracer.model;

import android.content.Context;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dehualai on 6/29/18.
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


    String id;
    String timestamp;
    PatientModel patient;
    DoctorModel doctor;
    String type;
    String score;
    String doctorComments;
    String saveChartPath;


    ArrayList<Pose> inspectionData;

    public InspectionRecord(InspectionRecordBuilder builder) {
        id = builder.id;
        timestamp = builder.timestamp;
        patient = builder.patient;
        doctor = builder.doctor;
        type = builder.type;
        inspectionData = builder.inspectionData;
        score = builder.score;
        doctorComments = builder.doctorComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDoctorComments() {
        return doctorComments;
    }

    public void setDoctorComments(String doctorComments) {
        this.doctorComments = doctorComments;
    }

    public String getSaveChartPath() {
        return saveChartPath;
    }

    public void setSaveChartPath(String saveChartPath) {
        this.saveChartPath = saveChartPath;
    }

    public ArrayList<Pose> getInspectionData() {
        return inspectionData;
    }

    public void setInspectionData(ArrayList<Pose> inspectionData) {
        this.inspectionData = inspectionData;
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


        Date time = Util.convertStringToTime(timestamp, Util.FORMAT_DATE_TIME);
        buffer.append(context.getResources().getString(R.string.inspection_time) + " : " +
                Util.convertTimeToString(time, "yyyy-MM-dd HH:mm") + "\n");

        buffer.append(context.getResources().getString(R.string.doctor) + " : " + doctor.getName() + "\n");

        return buffer.toString();
    }



    public static class InspectionRecordBuilder {

        String id;
        String timestamp;
        PatientModel patient;
        DoctorModel doctor;
        String type;
        ArrayList<Pose> inspectionData;
        String score;
        String doctorComments;

        public InspectionRecordBuilder(String id,
                                       String timestamp,
                                       PatientModel patient,
                                       DoctorModel doctor,
                                       int detect_opt,
                                       ArrayList<Pose> inspectionData,
                                       String score,
                                       String doctorComments
        ) {
            this.id = id;
            this.timestamp = timestamp;
            this.patient = patient;
            this.doctor = doctor;
            this.type = TypeTable.get(detect_opt);
            this.inspectionData = inspectionData;
            this.score = score;
            this.doctorComments = doctorComments;
        }

        public InspectionRecordBuilder(String id,
                                       String timestamp,
                                       PatientModel patient,
                                       DoctorModel doctor,
                                       String type,
                                       ArrayList<Pose> inspectionData,
                                       String score,
                                       String doctorComments) {
            this.id = id;
            this.timestamp = timestamp;
            this.patient = patient;
            this.doctor = doctor;
            this.type = type;
            this.inspectionData = inspectionData;
            this.score = score;
            this.doctorComments = doctorComments;
        }

        public String getDoctorComments() {
            return doctorComments;
        }

        public void setDoctorComments(String doctorComments) {
            this.doctorComments = doctorComments;
        }



        public InspectionRecord build() {
            return new InspectionRecord(this);
        }
    }

}
