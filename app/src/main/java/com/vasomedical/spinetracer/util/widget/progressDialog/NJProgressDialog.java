package com.vasomedical.spinetracer.util.widget.progressDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.vasomedical.spinetracer.R;

/**
 * Created by dehualai on 7/8/17.
 */


public class NJProgressDialog  {

    protected static Dialog dialog;
    protected static ProgressWheel pw;



    public static void showDialog(Context mContext){
        dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_progress, null);

        pw = (ProgressWheel) view.findViewById(R.id.pw_spinner);
        pw.spin();

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }



    public static void dismiss(){
        try{
            if(pw!=null){
                pw.stopSpinning();
            }
            if(dialog!=null){
                dialog.dismiss();
            }
        }catch (Exception e){

        }

    }

}