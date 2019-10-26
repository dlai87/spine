package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.widget.button.OnOffButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dehualai on 7/7/18.
 */

public class DoctorCommentDialog extends Dialog implements View.OnClickListener {

   // private TextView tvTitle, tvDesc;
    private View btnClose;
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private LinearLayout commentLayout;

    private String[] commentOptions;
    private Set<String> selectedComment = new HashSet<String>();

    private DoctorCommentInterface callback;

    public void setDoctorCommentInterface(DoctorCommentInterface callback){
        this.callback = callback;
    }

    public interface DoctorCommentInterface{
        public void setDocComment(String docComment);
    }

    public void setCommentOptions(String[] comments){
        this.commentOptions = comments;
    }


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


        for(int i = 0 ; i < commentOptions.length; i ++){
            final String str = commentOptions[i];
            final OnOffButton button = new OnOffButton(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1800, 160);
            params.setMargins(10, 15, 10, 15);
            button.setLayoutParams(params);
            button.setBackground(getContext().getResources().getDrawable(R.drawable.grey_round_rect));
            button.setText(str);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.onClick();
                    String buttonText = button.getText()+ "";
                    if(button.isOn()){
                        button.setBackground(getContext().getResources().getDrawable(R.drawable.green_round_rect) );
                        selectedComment.add(buttonText);
                    }else {
                        button.setBackground(getContext().getResources().getDrawable(R.drawable.grey_round_rect) );
                        if(selectedComment.contains(buttonText)){
                            selectedComment.remove(buttonText);
                        }
                    }

                }
            });
            commentLayout.addView(button);
        }

        Button saveButton = new Button(getContext());
        saveButton.setText("确定");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer buf = new StringBuffer();
                for(String s : selectedComment){
                    buf.append(s + "; ");
                }
                callback.setDocComment(buf.toString());
                //Log.e("show", "select " + buf.toString());
                dismiss();
            }
        });
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
