package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.vasomedical.spinetracer.activity.view.CompanyView;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.CompanyClassModel;
import com.vasomedical.spinetracer.model.CompanyModel;

//公司-接口
public class CompanyPresenterCompl implements CompanyPresenter {

    private CompanyView companyView;

    private SQLiteDatabase db;
    private Handler handler;

    public CompanyPresenterCompl(Context context, CompanyView companyView) {
        this.companyView = companyView;
        db = DBAdapter.getDatabase(context);
        handler = new Handler();
    }

    @Override
    public void reqCompanyInfo() {

    }

    @Override
    public void updateCompanyInfo(CompanyModel companyModel) {

    }

    @Override
    public void addCompanyClassInfo(CompanyClassModel companyClassModel) {

    }
}
