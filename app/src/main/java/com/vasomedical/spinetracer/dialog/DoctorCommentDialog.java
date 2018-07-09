package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

/**
 * Created by dehualai on 7/7/18.
 */

public class DoctorCommentDialog extends Dialog implements View.OnClickListener {

   // private TextView tvTitle, tvDesc;
    private View btnClose;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private LinearLayout commentLayout;

    public DoctorCommentDialog(Context context) {
        super(context, R.style.ReportportDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleStr = "诊断";
        messageStr = "";
        setContentView(R.layout.dialog_doctor_comment);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        findViews();
        addAction();
    }

    private void findViews() {
    //    tvTitle = (TextView) findViewById(R.id.tvTitle);
    //    tvDesc = (TextView) findViewById(R.id.tvDesc);
        btnClose = findViewById(R.id.btnClose);
        commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
    }

    private void addAction() {
        btnClose.setOnClickListener(this);
     //   tvTitle.setText(titleStr);
     //   tvDesc.setText(messageStr);
        Button saveButton1 = new Button(getContext());
        saveButton1.setText("SAVE");
        commentLayout.addView(saveButton1);
        Button saveButton2 = new Button(getContext());
        saveButton2.setText("SAVE");
        commentLayout.addView(saveButton2);
        Button saveButton3 = new Button(getContext());
        saveButton3.setText("SAVE");
        commentLayout.addView(saveButton3);
        Button saveButton4 = new Button(getContext());
        saveButton4.setText("SAVE");
        commentLayout.addView(saveButton4);
        Button saveButton5 = new Button(getContext());
        saveButton5.setText("SAVE");
        commentLayout.addView(saveButton5);
        Button saveButton6 = new Button(getContext());
        saveButton6.setText("SAVE");
        commentLayout.addView(saveButton6);
        Button saveButton = new Button(getContext());
        saveButton.setText("SAVE");
        commentLayout.addView(saveButton);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
     //   if (tvTitle != null) {
     //       tvTitle.setText(title);
     //   }
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
     //   if (tvDesc != null) {
     //       tvDesc.setText(message);
     //   }
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
