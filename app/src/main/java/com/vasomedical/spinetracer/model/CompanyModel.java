package com.vasomedical.spinetracer.model;

import java.util.List;

//公司
public class CompanyModel {
    private String id;
    private String name;
    private String address;
    private String phone;
    private List<CompanyClassModel> classModelList;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<CompanyClassModel> getClassModelList() {
        return classModelList;
    }

    public void setClassModelList(List<CompanyClassModel> classModelList) {
        this.classModelList = classModelList;
    }
}
