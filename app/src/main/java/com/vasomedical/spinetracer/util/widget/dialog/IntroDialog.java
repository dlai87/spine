package com.vasomedical.spinetracer.util.widget.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;

/**
 * Created by dehualai on 8/10/17.
 */

public class IntroDialog {


    Context mContext = null;
    Dialog dialog = null;
    View view;

    Button closeButton;
    WebView webView;






    public IntroDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_dialog_intro, null);
        assignViews();
        addActionToViews();
    }

    private void assignViews(){
        closeButton = (Button)view.findViewById(R.id.close_button);
        webView = (WebView) view.findViewById(R.id.webview);
    }

    private void addActionToViews(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



    public void showDialog() {

        dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

        String url;
        switch (AlgorithmFactory.detectionOption) {
            case AlgorithmFactory.DETECT_OPT_1:
                url = "https://www.reddit.com/";
                break;
            case AlgorithmFactory.DETECT_OPT_2:
                url = "https://www.4chan.org/";
                break;
            case AlgorithmFactory.DETECT_OPT_3:
                url = "https://www.facebook.com/";
                break;
            case AlgorithmFactory.DETECT_OPT_4:
                url = "https://www.instagram.com/";
                break;
            case AlgorithmFactory.DETECT_OPT_5:
                url = "https://www.quora.com/";
                break;
            default:
                url = "https://www.google.com/";
        }
        webView.loadUrl(url);
    }

    public void dismiss()
    {
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}
