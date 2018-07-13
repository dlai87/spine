package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

/**
 * Created by dehualai on 7/12/18.
 */

public class InputTextDialog extends Dialog implements View.OnClickListener {

    private TextView tvTitle, tvDesc;
    private View btnClose;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本

    public InputTextDialog(Context context) {
        super(context, R.style.ReportportDialog);
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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        btnClose = findViewById(R.id.btnClose);
    }

    private void addAction() {
        btnClose.setOnClickListener(this);
        tvTitle.setText(titleStr);
        tvDesc.setText(messageStr);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
        if (tvDesc != null) {
            tvDesc.setText(message);
        }
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
