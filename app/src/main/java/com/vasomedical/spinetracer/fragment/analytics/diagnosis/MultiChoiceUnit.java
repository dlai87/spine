package com.vasomedical.spinetracer.fragment.analytics.diagnosis;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

import java.util.Locale;

/**
 * Created by dehualai on 10/25/16.
 */

public class MultiChoiceUnit extends RelativeLayout {


    public static final int STATUS_POSITIVE = 1;
    public static final int STATUS_NEGETIVE = 2;

    private Context mContext;
    private String answerOption;
    int isNoneOfAboveButton;

    private int status;
    private ActionHandler actionHandler;

    //  View view;

    Button button;
    ImageView buttonImageRight;
    ImageView buttonImage;
    TextView questionText;
    RelativeLayout imageButtonLayoutLeft;
    RelativeLayout imageButtonLayoutRight;


    public interface ActionHandler{
        public void onSelect(int status);
        public void onNoticeParent();
    }

    public MultiChoiceUnit(Context context) {
        super(context);
        this.mContext = context;
        initViews(context, null);

    }

    public MultiChoiceUnit(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews(context, attrs);

    }

    public MultiChoiceUnit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mContext = context;
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs){

        if (context == null || attrs == null) {
            return;
        }

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiChoiceUnit);
            answerOption = typedArray.getString(R.styleable.MultiChoiceUnit_answerOption);
            isNoneOfAboveButton = typedArray.getInt(R.styleable.MultiChoiceUnit_isNoneOfAbove, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.unit_multi_choice, this, true);

        assignViews();
        addActionToViews();
        updateButtons();
    }



    public void setActionHandler(ActionHandler actionHandler){
        this.actionHandler = actionHandler;
    }


    private void assignViews(){
        button = (Button)this.findViewById(R.id.button);
        buttonImageRight = (ImageView) this.findViewById(R.id.image_button_right);
        buttonImage = (ImageView)this.findViewById(R.id.image_button);
        questionText = (TextView)this.findViewById(R.id.text);
        imageButtonLayoutLeft = (RelativeLayout)this.findViewById(R.id.image_button_layout_left);
        imageButtonLayoutRight = (RelativeLayout)this.findViewById(R.id.image_button_layout_right);
        Locale current = getResources().getConfiguration().locale;
        if(current.getLanguage().equals("iw")){
            imageButtonLayoutRight.setVisibility(VISIBLE);
            imageButtonLayoutLeft.setVisibility(GONE);
        }else{
            imageButtonLayoutRight.setVisibility(GONE);
            imageButtonLayoutLeft.setVisibility(VISIBLE);
        }

    }

    private void addActionToViews(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed();
            }
        });
        questionText.setText(answerOption);
        status = STATUS_NEGETIVE;

        updateButtons();

    }



    private void updateButtons(){

        switch (status){
            case STATUS_NEGETIVE:
                buttonImage.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_blank));
                buttonImageRight.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_blank));
                break;
            case STATUS_POSITIVE:
                buttonImage.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_checked_black));
                buttonImageRight.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_checked_black));
                break;
            default:
                break;
        }
    }

    private void buttonPressed(){
        // call handler first
        if (actionHandler!=null){
            actionHandler.onSelect(isNoneOfAboveButton);
        }

        switch (this.status){
            case STATUS_NEGETIVE:
                this.status = STATUS_POSITIVE;
                break;
            case STATUS_POSITIVE:
                this.status = STATUS_NEGETIVE;
                break;
        }

        // notice parent that action complete
        if (actionHandler!=null){
            actionHandler.onNoticeParent();
        }
        updateButtons();

    }


    public void reset(){
        this.status = STATUS_NEGETIVE;
        updateButtons();
    }


    public String getAnswer(){
        switch (status){
            case STATUS_NEGETIVE:
                return "";
            case STATUS_POSITIVE:
                return this.answerOption;
            default:
                return "";
        }
    }

    public int getIsNoneOfAboveButton(){
        return isNoneOfAboveButton;
    }


}
