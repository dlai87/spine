package com.vasomedical.spinetracer.fragment.pdf;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.util.Global;

import java.io.File;

/**
 * Created by dehualai on 9/28/17.
 */

public class PdfViewFragment extends BaseFragment {

    public final static String FILE_NAME = "FILE_NAME";
    PDFView pdfView;
    String filename;

    Button backButton;
    Button emailButton;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pdf_view, container, false);
        Bundle args = getArguments();
        if (args!=null) {
            filename = args.getString(FILE_NAME);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void assignViews(){
        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        backButton = (Button)view.findViewById(R.id.backButton);
        emailButton = (Button)view.findViewById(R.id.emailButton);
    }

    @Override
    protected void addActionToViews() {
        displayFromFile();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentUtil.back();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void displayFromFile(){
        if (filename!=null){

            File file = new File(Global.FOLDER_PDF, filename);
            pdfView.fromFile(file)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .spacing(10) // in dp
                    .load();;
        }

    }




}
