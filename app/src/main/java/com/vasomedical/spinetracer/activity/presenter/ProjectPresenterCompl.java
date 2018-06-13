package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.activity.view.ProjectView;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.List;

//模块管理-接口
public class ProjectPresenterCompl implements ProjectPresenter {

    private ProjectView patientView;

    private SQLiteDatabase db;
    private Handler handler;

    public ProjectPresenterCompl(Context context, ProjectView patientView) {
        this.patientView = patientView;
        db = DBAdapter.getDatabase(context);
        handler = new Handler();
    }

    @Override
    public void reqProjectList() {

    }

    @Override
    public void updateProjet(ProjectModel projectModel) {

    }

    @Override
    public void updateProjetList(List<ProjectModel> projectModel) {

    }
}
