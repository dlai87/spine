package com.vasomedical.spinetracer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenter;
import com.vasomedical.spinetracer.activity.presenter.CompanyPresenterCompl;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenter;
import com.vasomedical.spinetracer.activity.presenter.DoctorPresenterCompl;
import com.vasomedical.spinetracer.activity.view.CompanyView;
import com.vasomedical.spinetracer.activity.view.DoctorView;
import com.vasomedical.spinetracer.model.CompanyModel;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//界面--用户注册
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, DoctorView, CompanyView {
    private EditText editName, editPass, editPass2, editRealName, editEmail, editPhone;
    private TextView tvTitle, editCompany, editClass;
    private View buttonBack, buttonCompanyMore, buttonClassMore;
    private TextView buttonRegister;
    private CheckBox buttonEye, buttonEye2;
    private DoctorPresenter doctorPresenter;
    private CompanyPresenter companyPresenter;
    private CompanyModel companyModel;
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        addAction();
    }

    private void findViews() {
        buttonBack = findViewById(R.id.buttonBack);
        buttonCompanyMore = findViewById(R.id.buttonCompanyMore);
        buttonClassMore = findViewById(R.id.buttonClassMore);
        buttonRegister = (TextView) findViewById(R.id.buttonRegister);
        editName = (EditText) findViewById(R.id.editName);
        editPass = (EditText) findViewById(R.id.editPass);
        editPass2 = (EditText) findViewById(R.id.editPass2);
        editRealName = (EditText) findViewById(R.id.editRealName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        editCompany = (TextView) findViewById(R.id.editCompany);
        editClass = (TextView) findViewById(R.id.editClass);
        buttonEye = (CheckBox) findViewById(R.id.buttonEye);
        buttonEye2 = (CheckBox) findViewById(R.id.buttonEye2);
    }

    private void addAction() {
        doctorPresenter = new DoctorPresenterCompl(this, this);
        companyPresenter = new CompanyPresenterCompl(this, this);
        companyPresenter.reqCompanyInfo();
        buttonRegister.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        editClass.setOnClickListener(this);
        buttonEye.setOnCheckedChangeListener(this);
        buttonEye2.setOnCheckedChangeListener(this);

        editName.setFocusable(true);
        editName.setFocusableInTouchMode(true);
        editName.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) RegisterActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);//这里的时间大概是自己测试的


        //编辑模式
        doctorId = getIntent().getStringExtra("doctor_no");
        if (!TextUtils.isEmpty(doctorId)) {
            doctorPresenter.delectDoctor(doctorId);
            tvTitle.setText("编辑医生");
            buttonRegister.setText("更新");
        } else {
            tvTitle.setText("注册医生");
            buttonRegister.setText("注冊");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            if (TextUtils.isEmpty(doctorId)) {
                userRegister();//注册
            } else {
                userUpdate();//更新
            }
        } else if (v == buttonBack) {
            onBackPressed();
        } else if (v == editClass) {
            ShowClassChoise();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        EditText passVew = buttonView == buttonEye ? editPass : editPass2;
        if (isChecked) {
            passVew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passVew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void loginCallBack(boolean success, String msg) {

    }

    @Override
    public void registerCallBack(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    @Override
    public void selectAllDoctor(List<DoctorModel> doctorModelList) {

    }

    @Override
    public void selectDoctor(DoctorModel doctorMode) {
        if (doctorMode != null) {
            editName.setText(doctorMode.getName());
//            editPass.setText(doctorMode.getPassword());
            editRealName.setText(doctorMode.getRealName());
            editEmail.setText(doctorMode.getEmail());
            editPhone.setText(doctorMode.getPhone());
            editCompany.setText(doctorMode.getHospital());
            editClass.setText(doctorMode.getDepartment());
        }
    }

    @Override
    public void updateDoctorInfo(boolean success, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        if (success) {
            finish();
        }
    }

    @Override
    public void removeDoctor(boolean success, String msg) {

    }

    private void userRegister() {
        DoctorModel.DoctorBuilder builder = new DoctorModel.DoctorBuilder(
                null,
                editName.getText().toString()
        );
        builder.setPassword(editPass.getText().toString());
        builder.setRealName(editRealName.getText().toString());
        builder.setEmail(editEmail.getText().toString());
        builder.setPhone(editPhone.getText().toString());
        builder.setHospital(editCompany.getText().toString());
        builder.setDepartment(editClass.getText().toString());
        DoctorModel doctorModel = builder.build();

        if (TextUtils.isEmpty(doctorModel.getName()) || TextUtils.isEmpty(doctorModel.getPassword())) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editEmail.getText())) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editPhone.getText())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editCompany.getText())) {
            Toast.makeText(this, "请在医院管理里面添加医院名称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editClass.getText())) {
            Toast.makeText(this, "请选择医院科室", Toast.LENGTH_SHORT).show();
        } else {
            doctorPresenter.register(doctorModel);
        }
    }

    private void userUpdate() {
        DoctorModel.DoctorBuilder builder = new DoctorModel.DoctorBuilder(
                null,
                editName.getText().toString()
        );
        builder.setPassword(editPass.getText().toString());
        builder.setRealName(editRealName.getText().toString());
        builder.setEmail(editEmail.getText().toString());
        builder.setPhone(editPhone.getText().toString());
        builder.setHospital(editCompany.getText().toString());
        builder.setDepartment(editClass.getText().toString());
        DoctorModel doctorModel = builder.build();
        doctorModel.setId(doctorId);

        if (TextUtils.isEmpty(doctorModel.getName()) || TextUtils.isEmpty(doctorModel.getPassword())) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (!doctorModel.getPassword().equals(editPass2.getText().toString())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editEmail.getText())) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editPhone.getText())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editCompany.getText())) {
            Toast.makeText(this, "请在医院管理里面添加医院名称", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editClass.getText())) {
            Toast.makeText(this, "请选择医院科室", Toast.LENGTH_SHORT).show();
        } else {
            doctorPresenter.updateDoctorInfo(doctorModel);
        }
    }

    private void ShowClassChoise() {
        if (companyModel != null && companyModel.getClassModelList() != null && companyModel.getClassModelList().size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
            //builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择科室");
            //    指定下拉列表的显示数据
            final String[] className = new String[companyModel.getClassModelList().size()];
            for (int i = 0; i < className.length; i++) {
                className[i] = companyModel.getClassModelList().get(i).getName();
            }
            //    设置一个下拉的列表选择项
            builder.setItems(className, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editClass.setText(className[which]);
                }
            });
            builder.show();
        } else {
            Toast.makeText(this, "请在医院管理里面添加科室", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCompanyUI(CompanyModel companyModel) {
        this.companyModel = companyModel;
        if (companyModel != null) {
            editCompany.setText(companyModel.getName());
            if (companyModel.getClassModelList() != null && companyModel.getClassModelList().size() > 0) {
                editClass.setText(companyModel.getClassModelList().get(0).getName());
            }
        }
    }

    @Override
    public void saveCompayInfoCallBack(boolean success, String msg) {

    }

    @Override
    public void saveCompayClassInfoCallBack(boolean success, String msg) {

    }
}
