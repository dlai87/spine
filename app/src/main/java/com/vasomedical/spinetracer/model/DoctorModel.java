package com.vasomedical.spinetracer.model;

/**
 * Created by dehualai on 5/13/17.
 */

public class DoctorModel {

    //required
    private String id;
    private String name;
    //optional
    private String phone;
    private String email;
    private String hospital;
    private String department;

    public DoctorModel(DoctorBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
        this.hospital = builder.hospital;
        this.department = builder.department;
    }

    public static class DoctorBuilder{

        private String id;
        private String name;
        private String phone;
        private String email;
        private String hospital;
        private String department;

        public DoctorBuilder(String id, String name){
            this.id = id;
            this.name = name;
        }

        public DoctorBuilder phone(String phone){
            this.phone = phone;
            return this;
        }

        public DoctorBuilder email(String email){
            this.email = email;
            return this;
        }

        public DoctorBuilder hospital(String hospital){
            this.hospital = hospital;
            return this;
        }

        public DoctorBuilder department(String department){
            this.department = department;
            return this;
        }

        public DoctorModel build(){
            return new DoctorModel(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
