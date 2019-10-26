package com.vasomedical.spinetracer.activity.presenter;

import com.vasomedical.spinetracer.model.CompanyClassModel;
import com.vasomedical.spinetracer.model.CompanyModel;

//公司-接口
public interface CompanyPresenter {
    void reqCompanyInfo();

    void updateCompanyInfo(CompanyModel companyModel);

    void addCompanyClassInfo(CompanyClassModel companyClassModel);
}
