package com.vasomedical.spinetracer.model;

import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zhitao on 9/24/2017.
 */

public class InspectionRecord {


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
    String patientId;
    String doctorId;
    String type;
    ArrayList<Pose> inspectionData;

    public InspectionRecord(InspectionRecordBuilder builder) {
        timestamp = builder.timestamp;
        patientId = builder.patientId;
        doctorId = builder.doctorId;
        type = builder.type;
        inspectionData = builder.inspectionData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Pose> getInspectionData() {
        return inspectionData;
    }

    public void setInspectionData(ArrayList<Pose> inspectionData) {
        this.inspectionData = inspectionData;
    }

    public static class InspectionRecordBuilder {

        String timestamp;
        String patientId;
        String doctorId;
        String type;
        ArrayList<Pose> inspectionData;

        public InspectionRecordBuilder(String timestamp,
                                       String patientId,
                                       String doctorId,
                                       int detect_opt,
                                       ArrayList<Pose> inspectionData) {
            this.timestamp = timestamp;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.type = TypeTable.get(detect_opt);
            this.inspectionData = inspectionData;
        }

        public InspectionRecord build() {
            return new InspectionRecord(this);
        }
    }



}
