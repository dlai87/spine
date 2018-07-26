package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.model.DoctorModel;

/**
 * Created by dehualai on 7/12/18.
 */

public class ChangePasswordDialog extends Dialog implements View.OnClickListener {

    private View btnClose;
    EditText plassword_old, plassword_new2, plassword_new1;
    Button button;
    DoctorModel doctorModel;
    private DoctorPresenter doctorPresenter;

    public interface InputTextDialogInterface {
        public void onConfirmButtonPressed(String newClass);
    }

    public ChangePasswordDialog(Context context) {
        super(context, R.style.ReportportDialog);
    }

    public void setDoctorPresenter(DoctorPresenter doctorPresenter) {
        this.doctorPresenter = doctorPresenter;
    }

    public void setDoctorModel(DoctorModel doctorModel) {
        this.doctorModel = doctorModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_password);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        findViews();
        addAction();
    }

    private void findViews() {
        btnClose = findViewById(R.id.btnClose);
        plassword_old = findViewById(R.id.plassword_old);
        plassword_new1 = findViewById(R.id.plassword_new1);
        plassword_new2 = findViewById(R.id.plassword_new2);
        button = findViewById(R.id.confirm_button);
    }

    private void addAction() {
        btnClose.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (plassword_old.getText().toString().equals(doctorModel.getPassword())) {
                    if (plassword_new1.getText().toString().equals(plassword_new2.getText().toString())) {
                        doctorModel.setPassword(plassword_new1.getText().toString());
                        doctorPresenter.updateDoctorInfo(doctorModel);
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "旧密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
