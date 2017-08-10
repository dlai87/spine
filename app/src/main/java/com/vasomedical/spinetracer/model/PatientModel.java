package com.vasomedical.spinetracer.model;

/**
 * Created by dehualai on 5/13/17.
 */

public class PatientModel {

    // required
    String id;
    String name;
    String gender;
    String date_of_birth;

    // optional
    String phone;
    String email;
    String photo;
    String note;

    public PatientModel(PatientBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.gender = builder.gender;
        this.date_of_birth = builder.date_of_birth;
        this.phone = builder.phone;
        this.email = builder.email;
        this.photo = builder.photo;
        this.note = builder.note;
    }

    public static class PatientBuilder{
        String id;
        String name;
        String gender;
        String date_of_birth;
        String phone;
        String email;
        String photo;
        String note;
        public PatientBuilder(String id, String name, String gender, String date_of_birth){
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.date_of_birth = date_of_birth;
        }

        public PatientBuilder phone(String phone){
            this.phone = phone;
            return this;
        }

        public PatientBuilder email(String email){
            this.email = email;
            return this;
        }
        public PatientBuilder photo(String photo){
            this.photo = photo;
            return this;
        }

        public PatientBuilder note(String note){
            this.note = note;
            return this;
        }

        public PatientModel build(){
            return new PatientModel(this);
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
