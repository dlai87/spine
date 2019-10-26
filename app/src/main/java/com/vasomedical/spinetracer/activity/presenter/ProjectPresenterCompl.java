package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.activity.view.ProjectView;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.ArrayList;
import java.util.List;

//模块管理-接口
public class ProjectPresenterCompl implements ProjectPresenter {

    public static final String[] PROJECT_CONTORS = new String[]{
            "Locale.Helper.Project.Contor.item1",
            "Locale.Helper.Project.Contor.item2",
            "Locale.Helper.Project.Contor.item3",
            "Locale.Helper.Project.Contor.item4",
            "Locale.Helper.Project.Contor.item5",
            "Locale.Helper.Project.Contor.item6",
    };

    private ProjectView patientView;

    private Handler handler;
    private LogsPresenter logsPresenter;
    private Context mContext;


    public ProjectPresenterCompl(Context context, ProjectView patientView) {
        this.patientView = patientView;
        handler = new Handler();
        logsPresenter = new LogsPresenterCompl(context);
        this.mContext = context;
    }

    @Override
    public void reqProjectList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                final List<ProjectModel> projectModelList = new ArrayList<>();
                for (String key : PROJECT_CONTORS) {
                    ProjectModel projectModel = new ProjectModel();
                    projectModel.setId(key);
                    projectModel.setEnable(preferences.getBoolean(key, true));
                    projectModelList.add(projectModel);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.updateUIProjetList(projectModelList);
                    }
                });
                logsPresenter.addLog("查询模块状态");

            }
        }).start();
    }

    @Override
    public void updateProjet(final ProjectModel projectModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(projectModel.getId(), projectModel.isEnable());
                editor.apply();
                logsPresenter.addLog("修改‘" + projectModel.getName() + "’:" + projectModel.isEnable());
            }
        }).start();
    }

    @Override
    public void updateProjetList(List<ProjectModel> projectModel) {
        //提供 开关状态
        for (ProjectModel projectModel1 : projectModel) {
            updateProjet(projectModel1);
        }
        patientView.updateProjetListCallBack(true,"修改成功");
    }
}
