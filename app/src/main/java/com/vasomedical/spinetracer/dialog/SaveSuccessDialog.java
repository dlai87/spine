package com.vasomedical.spinetracer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

/**
 * Created by dehualai on 7/13/18.
 */

public class SaveSuccessDialog extends Dialog implements View.OnClickListener {


    Button okButton;


    DialogInterface callback ;

    public void setCallback(DialogInterface callback){
        this.callback = callback;
    }

    public interface DialogInterface{
        public void onOKButtonPressed();
    }

    public SaveSuccessDialog(Context context) {
        super(context, R.style.ReportportDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_data_save);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        findViews();
        addAction();
    }

    private void findViews() {
        okButton = (Button) findViewById(R.id.okButton);
    }

    private void addAction() {
       okButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dismiss();
               if(callback!=null){
                   callback.onOKButtonPressed();
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
