package com.vasomedical.spinetracer.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

public class PdfUtils {

    String FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/Spine/";
    String dexCacheDirPath = Environment.getExternalStorageDirectory().getPath() + "/Spine/cache";
    int pageWidth = 210, pageHeight = 297;
    Context mContext;

    public PdfUtils(Context context) {
        this.mContext = context;
    }

    public void output(final String fileName, final Map<InspectionRecord, Set<InspectionRecord>> modelSetMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                output2(fileName, modelSetMap);
            }
        }).start();
    }

    private void output3(View view) {
        PdfDocument document = new PdfDocument();//1, 建立PdfDocument
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                view.getMeasuredWidth(), view.getMeasuredHeight(), 1).create();//2
        PdfDocument.Page page = document.startPage(pageInfo);
        view.draw(page.getCanvas());//3
        document.finishPage(page);//4</code>
    }

    private void output2(final String fileName, Map<InspectionRecord, Set<InspectionRecord>> modelSetMap) {
        try {
            File file = new File(FILE_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            PdfDocument document = new PdfDocument();

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(6);
            textPaint.setTextAlign(Paint.Align.LEFT);

            Typeface textTypeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
            textPaint.setTypeface(textTypeface);

            StringBuilder content = new StringBuilder();
            //医生
            DoctorModel doctor = ((InspectionRecord) modelSetMap.keySet().toArray()[0]).getDoctor();
            content.append("\n  医生姓名：" + doctor.getName());
            content.append("\n  所在医院：" + doctor.getHospital());
            content.append("\n  科室：" + doctor.getDepartment());

            for (Map.Entry<InspectionRecord, Set<InspectionRecord>> entry : modelSetMap.entrySet()) {
                content.append("\n");
                //病人
                PatientModel patient = entry.getKey().getPatient();
                content.append("\n  被测者姓名：" + patient.getName());
                content.append("\n  被测者姓名：" + patient.getGender());
                content.append("\n  被测者出生日期：" + patient.getDate_of_birth());
                content.append("\n");
                //项目
                for (InspectionRecord inspectionRecord : entry.getValue()) {
                    String typeName = "";
                    if ("slant".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_1);
                    } else if ("humpback".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_2);
                    } else if ("bending".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_3);
                    } else if ("left_right".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_4);
                    } else if ("forward_back".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_5);
                    } else if ("rotate".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_6);
                    } else if ("balance".equals(inspectionRecord.getType())) {
                        typeName = mContext.getResources().getString(R.string.detect_option_7);
                    }
                    content.append("\n  测量项目：" + typeName);
                    content.append("\n  测量时间：" + inspectionRecord.getTimestamp());
                    if (!TextUtils.isEmpty(inspectionRecord.getDoctorComments())) {
                        content.append("\n  医生诊断：" + inspectionRecord.getDoctorComments());
                    }
                }
            }


            StaticLayout mTextLayout = new StaticLayout(content.toString(), textPaint, pageWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

            final int lines = mTextLayout.getLineCount();
            final CharSequence text = mTextLayout.getText();
            int startOffset = 0;
            int pageNUm = 1;
            int height = pageHeight;
            boolean drawFirst = false;
            for (int i = 0; i < lines; i++) {
                int x = mTextLayout.getLineBottom(i);
                if (height < mTextLayout.getLineBottom(i)) {
                    //When the layout height has been exceeded
                    drawFirst = true;
                    addPage(document, textPaint, pageNUm, text.subSequence(startOffset, mTextLayout.getLineStart(i)));
                    startOffset = mTextLayout.getLineStart(i);
                    height = mTextLayout.getLineTop(i) + pageHeight;
                }
            }
            if (!drawFirst) {
                addPage(document, textPaint, 1, text);
            }

//            mTextLayout.draw(page.getCanvas());
//            document.finishPage(page);
//
//            int pageNum = 1;
//            int nowHeight = 0;
//            while (nowHeight > pageHeight) {
//                /***宽 高 页数******/
//                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
//                PdfDocument.Page page = document.startPage(pageInfo);
//
//                TextPaint textPaint = new TextPaint();
//                textPaint.setColor(Color.BLACK);
//                textPaint.setTextSize(16);
//                textPaint.setTextAlign(Paint.Align.LEFT);
//
//                Typeface textTypeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
//                textPaint.setTypeface(textTypeface);
//
//
//                String text = "测试";
//                StaticLayout mTextLayout = new StaticLayout(text, textPaint, page.getCanvas().getWidth(),
//                        Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//
//                mTextLayout.draw(page.getCanvas());
//                document.finishPage(page);
//                pageNum++;
//            }


            File file1 = new File(file, fileName);

            try {
                FileOutputStream mFileOutStream = new FileOutputStream(file1);

                document.writeTo(mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPage(PdfDocument document, TextPaint textPaint, int pageNum, CharSequence text) {
        //第一页
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        StaticLayout mTextLayout = new StaticLayout(text, textPaint, pageWidth,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        mTextLayout.draw(page.getCanvas());
        document.finishPage(page);
    }
}
