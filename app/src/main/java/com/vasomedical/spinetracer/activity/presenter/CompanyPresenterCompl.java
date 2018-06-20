package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.vasomedical.spinetracer.activity.view.CompanyView;
import com.vasomedical.spinetracer.model.CompanyClassModel;
import com.vasomedical.spinetracer.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

//公司-接口
public class CompanyPresenterCompl implements CompanyPresenter {

    private static final String compnay_name = "Locale.Helper.Compant.Name";
    private static final String compnay_address = "Locale.Helper.Compant.Address";
    private static final String compnay_phone = "Locale.Helper.Compant.Phone";
    private static final String compnay_class = "Locale.Helper.Compant.Class";

    private CompanyView companyView;
    private Handler handler;
    private Context mContext;

    public CompanyPresenterCompl(Context context, CompanyView companyView) {
        this.companyView = companyView;
        this.mContext = context;
        handler = new Handler();
    }

    @Override
    public void reqCompanyInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CompanyModel companyModel = new CompanyModel();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                companyModel.setName(preferences.getString(compnay_name, ""));
                companyModel.setPhone(preferences.getString(compnay_phone, ""));
                companyModel.setAddress(preferences.getString(compnay_address, ""));

                List<CompanyClassModel> companyClassModelList = new ArrayList<>();
                String classNameStr = preferences.getString(compnay_class, "");
                for (String className : classNameStr.split(",")) {
                    CompanyClassModel classModel = new CompanyClassModel();
                    classModel.setName(className);
                    companyClassModelList.add(classModel);
                }
                companyModel.setClassModelList(companyClassModelList);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        companyView.updateCompanyUI(companyModel);
                    }
                });
            }
        }).start();
    }

    @Override
    public void updateCompanyInfo(final CompanyModel companyModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(compnay_name, companyModel.getName());
                editor.putString(compnay_address, companyModel.getAddress());
                editor.putString(compnay_phone, companyModel.getPhone());
                editor.apply();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        companyView.saveCompayInfoCallBack(true, "更新成功");
                    }
                });
            }
        }).start();
    }

    @Override
    public void addCompanyClassInfo(CompanyClassModel companyClassModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
