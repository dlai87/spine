package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.CompanyClassModel;
import com.vasomedical.spinetracer.model.CompanyModel;

public interface CompanyView {
    void updateCompanyUI(CompanyModel companyModel);

    void saveCompayInfoCallBack(boolean success, String msg);

    void saveCompayClassInfoCallBack(boolean success, String msg);
}
