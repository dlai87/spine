package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.view.CompanyView;

/**
 * Created by dehualai on 7/12/18.
 */

public class InputTextDialog extends Dialog implements View.OnClickListener {

    private View btnClose;
    EditText inputText;
    Button button;
    CompanyView callback;

    public interface InputTextDialogInterface {
        public void onConfirmButtonPressed(String newClass);
    }

    public InputTextDialog(Context context) {
        super(context, R.style.ReportportDialog);
    }

    public void setCallback(CompanyView callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_text);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        findViews();
        addAction();
    }

    private void findViews() {
        btnClose = findViewById(R.id.btnClose);
        inputText = findViewById(R.id.class_input_text);
        button = findViewById(R.id.confirm_button);
    }

    private void addAction() {
        btnClose.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = inputText.getText().toString();
                Log.i("show", "inputtext[" + s + "]");
                s.replace(" ", "");  // 去除空格
                if (!TextUtils.isEmpty(s)) {
                    if (callback != null) {
                        callback.saveCompayClassInfoCallBack(true, s);
                    }
                }
                dismiss();
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
