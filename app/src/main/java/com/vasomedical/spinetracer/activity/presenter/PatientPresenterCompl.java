package com.vasomedical.spinetracer.activity.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;

import com.vasomedical.spinetracer.activity.view.PatientView;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.List;
import java.util.UUID;

//病人的数据控制-实现
public class PatientPresenterCompl implements PatientPresenter {

    private PatientView patientView;

    private SQLiteDatabase db;
    private TBPatient tbPatient;
    private Handler handler;
    private LogsPresenter logsPresenter;

    public PatientPresenterCompl(Context context, PatientView patientView) {
        this.patientView = patientView;
        db = DBAdapter.getDatabase(context);
        tbPatient = new TBPatient();
        handler = new Handler();
        logsPresenter = new LogsPresenterCompl(context);
    }

    // todo 这里我暂时注销，因为patient table的几个方法涉及到doctor id 的需要更新。

    @Override
    public void selectPatientByNo(final String no) {

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                final PatientModel patientModel = tbPatient.getPatientByNo(db, no);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.updatePatient(patientModel);
                    }
                });
                addLog("查询病人:" + no);
            }
        }).start();
        */
    }

    @Override
    public void selectPatientList(final DoctorModel userModel) {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PatientModel> patientModel = tbPatient.getPatientListByDoctor(db, userModel.getId());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.updatePatientList(patientModel);
                    }
                });
                addLog("查询病人信息");
            }
        }).start();
        */
    }

    @Override
    public void savePatient(final PatientModel patientModel) {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                PatientModel exitPatientModel = tbPatient.getPatientByNameAndIddoctor(db, patientModel.getId(), patientModel.getId_doctor());
                final boolean isExist = exitPatientModel != null;//是否存在
                if (!isExist) {
                    if (TextUtils.isEmpty(patientModel.getId())) {
                        patientModel.setId(UUID.randomUUID().toString());
                    }
                    tbPatient.insert(db, patientModel);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.savePatientCallBack(!isExist, isExist ? "用户已经存在" : "添加成功");
                    }
                });
                if (!isExist) {
                    addLog("添加病人编号:" + patientModel.getId());
                }
            }
        }).start();
        */
    }


    @Override
    public void updatePatient(final PatientModel peopleModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tbPatient.update(db, peopleModel);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.savePatientCallBack(true, "修改成功");
                    }
                });
                addLog("删除病人编号:" + peopleModel.getId());
            }
        }).start();
    }

    @Override
    public void deletPatient(final String no) {
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                tbPatient.delete(db, no);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        patientView.delPatientCallBack(true, no, "刪除成功");
                    }
                });
                addLog("删除病人编号:" + no);
            }
        }).start();
        */
    }

    private void addLog(String thing) {
        logsPresenter.addLog(thing);
    }
}
