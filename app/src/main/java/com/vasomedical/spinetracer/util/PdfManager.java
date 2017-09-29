package com.vasomedical.spinetracer.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dehualai on 9/27/17.
 */

public class PdfManager {



    Activity mActivity;
    int pageW;
    int pageH;
    int marginHorizontal;
    int marginVertical;

    int chartTop = 600;


    PatientModel patientModel;
    DoctorModel doctorModel;
    InspectionRecord inspectionRecord;

    public PdfManager(Activity activity){
        mActivity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        pageW = size.x;
        pageH = pageW * 10 / 7;  // ratio for A4 size
        marginHorizontal = pageW / 10;
        marginVertical = marginHorizontal/2;

        Log.e("show", "screen W " + pageW + "x" + pageH);
    }

    public boolean generatePDF(String filename,
                               PatientModel patientModel,
                               View chartView,
                               String scoreText,
                               String doctorComment){

        try {
            File dir = new File(Global.FOLDER_PDF);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, filename);
            FileOutputStream outputStream = new FileOutputStream(file);

            PrintedPdfDocument document = new PrintedPdfDocument(mActivity,
                    getPrintAttributes());

            // start a page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageW, pageH, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // patient info
            drawMultiLineText(canvas,
                    patientModel.getStringForPdf(mActivity), 17,
                    marginVertical,marginHorizontal);
            // chart
            drawViewAtPosition(canvas, chartView, chartTop, marginVertical);
            // score

            // doctor comments


            // time stamp
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            drawMultiLineText(canvas,
                    mActivity.getResources().getString(R.string.generate_report_time) + " : `" + dateFormat.format(date),
                    17,
                    pageH - marginVertical,
                    marginHorizontal
                    );

            // finish the page
            document.finishPage(page);

            // write the document content
            document.writeTo(outputStream);

            //close the document
            document.close();
        }catch (Exception e){
            Log.e("show", "exception " + e.getMessage());
        }


        return false;
    }





    private PrintAttributes getPrintAttributes() {
        PrintAttributes.Builder builder = new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A0)
                .setResolution(new PrintAttributes.Resolution("res1","Resolution",50,50)).setMinMargins(new PrintAttributes.Margins(50, 50, 50, 50));
        PrintAttributes printAttributes = builder.build();
        return printAttributes;
    }

    private void drawMultiLineText(Canvas canvas,
                                   String gText,
                                   int textSize,
                                   int top,
                                   int left) {
        float scale = mActivity.getResources().getDisplayMetrics().density;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize((int) (textSize * scale));
        // draw text to the Canvas center
        Rect bounds = new Rect();

        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = left;
        int y = top;

        for (String line: gText.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }
    }


    private void drawViewAtPosition(Canvas canvas,
                                    View view,
                                    int top,
                                    int left){
        canvas.save();
        canvas.translate(left, top);
        view.draw(canvas);
        canvas.restore();

    }




}
