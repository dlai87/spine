package com.vasomedical.spinetracer.model;

import java.util.ArrayList;

/**
 * Created by Zhitao on 9/24/2017.
 */

public class InspectionRecord {

    String timestamp;
    String patientId;
    String doctorId;
    ArrayList<Pose> inspectionData;

    public InspectionRecord(InspectionRecordBuilder builder) {
        timestamp = builder.timestamp;
        patientId = builder.patientId;
        doctorId = builder.doctorId;
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
        ArrayList<Pose> inspectionData;

        public InspectionRecordBuilder(String timestamp, String patientId, String doctorId, ArrayList<Pose> inspectionData) {
            this.timestamp = timestamp;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.inspectionData = inspectionData;
        }

        public InspectionRecord build() {
            return new InspectionRecord(this);
        }
    }
}
