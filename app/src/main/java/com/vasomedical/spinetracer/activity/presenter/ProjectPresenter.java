package com.vasomedical.spinetracer.activity.presenter;

import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.List;

//模块管理-接口
public interface ProjectPresenter {
    void reqProjectList();

    void updateProjet(ProjectModel projectModel);

    void updateProjetList(List<ProjectModel> projectModel);
}
