package com.vasomedical.spinetracer.activity.view;

import com.vasomedical.spinetracer.model.ProjectModel;

import java.util.List;

//项目模块管理
public interface ProjectView {
    void updateUIProjetList(List<ProjectModel> projectModels);

    void updateProjetListCallBack(boolean success, String msg);
}
