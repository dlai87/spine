package com.vasomedical.spinetracer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenter;
import com.vasomedical.spinetracer.activity.presenter.PatientPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.ProjectPresenter;
import com.vasomedical.spinetracer.activity.presenter.ProjectPresenterCompl;
import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.activity.view.ProjectView;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.dialog.ReportDialog;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.ProjectModel;
import com.vasomedical.spinetracer.util.Global;

import java.util.List;

//界面--项目选择
public class SelProjectcAtivity extends AppCompatActivity implements View.OnClickListener, PatientView, ProjectView, View.OnLongClickListener {

    private View buttonBack, buttonItem1, buttonItem2, buttonItem3, buttonItem4, buttonItem5, buttonItem6;
    private TextView tvName, tvPatientName;

    private PatientPresenter patientPresenter;
    private ProjectPresenter projectPresenter;
    private List<ProjectModel> projectModelList;

    private ReportDialog reportDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_projectctivity);
        findViews();
        addAction();

        //选择了用户了
        String patinetId = getIntent().getStringExtra("patinet_no");
        if (!TextUtils.isEmpty(patinetId)) {
            patientPresenter.selectPatientByNo(patinetId);
        }
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonItem1 = findViewById(R.id.buttonItem1);
        buttonItem2 = findViewById(R.id.buttonItem2);
        buttonItem3 = findViewById(R.id.buttonItem3);
        buttonItem4 = findViewById(R.id.buttonItem4);
        buttonItem5 = findViewById(R.id.buttonItem5);
        buttonItem6 = findViewById(R.id.buttonItem6);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);
    }

    private void addAction() {
        patientPresenter = new PatientPresenterCompl(this, this);
        projectPresenter = new ProjectPresenterCompl(this, this);
        projectPresenter.reqProjectList();
        buttonBack.setOnClickListener(this);
        buttonItem1.setOnClickListener(this);
        buttonItem2.setOnClickListener(this);
        buttonItem3.setOnClickListener(this);
        buttonItem4.setOnClickListener(this);
        buttonItem5.setOnClickListener(this);
        buttonItem6.setOnClickListener(this);
        buttonBack.setOnLongClickListener(this);
        buttonItem1.setOnLongClickListener(this);
        buttonItem2.setOnLongClickListener(this);
        buttonItem3.setOnLongClickListener(this);
        buttonItem4.setOnLongClickListener(this);
        buttonItem5.setOnLongClickListener(this);
        buttonItem6.setOnLongClickListener(this);

        String name = Global.userModel == null ? "测试用户" : Global.userModel.getName();
        tvName.setText(name);
    }

    @Override
    public void onClick(View v) {


        if (v == buttonBack) {
            onBackPressed();
        } else if (v == buttonItem1) {
            if (projectModelList.get(0).isEnable()) {
                Toast.makeText(this, "躯干倾斜角", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_1);
                intent.putExtras(bundle);
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_1;
                startActivity(intent);

            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonItem2) {
            if (projectModelList.get(1).isEnable()) {
                Toast.makeText(this, "脊柱弯曲COBB角", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_3);
                intent.putExtras(bundle);

                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_3;

                startActivity(intent);


            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonItem3) {
            if (projectModelList.get(2).isEnable()) {
                Toast.makeText(this, "左右侧弯倾角", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_4);
                intent.putExtras(bundle);

                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_4;
                startActivity(intent);

            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonItem4) {
            if (projectModelList.get(5).isEnable()) {
                Toast.makeText(this, "身体平衡度", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_7);
                intent.putExtras(bundle);

                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_7;
                startActivity(intent);
            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonItem5) {
            if (projectModelList.get(3).isEnable()) {
                Toast.makeText(this, "前倾/后仰角", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_5);
                intent.putExtras(bundle);

                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_5;
                startActivity(intent);
            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        } else if (v == buttonItem6) {
            if (projectModelList.get(4).isEnable()) {
                Toast.makeText(this, "旋转角", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DetectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AlgorithmFactory.AlgorithmFactoryDetectOption, AlgorithmFactory.DETECT_OPT_6);
                intent.putExtras(bundle);

                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_6;
                startActivity(intent);
            } else {
                Toast.makeText(this, "该功能已被管理员停用", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void updatePatient(PatientModel peopleModel) {
        tvPatientName.setText("病人:" + peopleModel.getName());
    }

    @Override
    public void updatePatientList(List<PatientModel> peopleModelListp) {

    }

    @Override
    public void savePatientCallBack(boolean success, String msg) {

    }

    @Override
    public void delPatientCallBack(boolean success, String no, String msg) {

    }

    @Override
    public void updateUIProjetList(List<ProjectModel> projectModels) {
        this.projectModelList = projectModels;
    }

    @Override
    public void updateProjetListCallBack(boolean success, String msg) {

    }

    @Override
    public boolean onLongClick(View v) {
        if (v == buttonItem1) {
            showReportDialog("躯干倾斜角", getString(R.string.item1_desc));
        } else if (v == buttonItem2) {
            showReportDialog("脊柱弯曲COBB角", getString(R.string.item1_desc));
        } else if (v == buttonItem3) {
            showReportDialog("左右侧弯倾角", getString(R.string.item1_desc));
        } else if (v == buttonItem4) {
            showReportDialog("身体平衡度", getString(R.string.item1_desc));
        } else if (v == buttonItem5) {
            showReportDialog("前倾/后仰角", getString(R.string.item1_desc));
        } else if (v == buttonItem6) {
            showReportDialog("旋转角", getString(R.string.item1_desc));
        }
        return true;
    }

    private void showReportDialog(String title, String mesg) {
        if (reportDialog == null) {
            reportDialog = new ReportDialog(this);
        }
        if (!reportDialog.isShowing()) {
            reportDialog.setTitle(title);
            reportDialog.setMessage(mesg);
            reportDialog.show();
        }
    }
}
