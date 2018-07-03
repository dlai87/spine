package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class HistoryTestViewHolder extends RecyclerView.ViewHolder {

    public RadioButton radioButton;
    public TextView tvName, tvDate;
    public View item1, item2, item3, item4, item5, item6;
    private CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6;

    public HistoryTestViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
        item1 = (View) itemView.findViewById(R.id.item1);
        item2 = (RadioButton) itemView.findViewById(R.id.item2);
        item3 = (RadioButton) itemView.findViewById(R.id.item3);
        item4 = (RadioButton) itemView.findViewById(R.id.item4);
        item5 = (RadioButton) itemView.findViewById(R.id.item5);
        item6 = (RadioButton) itemView.findViewById(R.id.item6);
        checkbox1 = (CheckBox) itemView.findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox) itemView.findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox) itemView.findViewById(R.id.checkbox3);
        checkbox4 = (CheckBox) itemView.findViewById(R.id.checkbox4);
        checkbox5 = (CheckBox) itemView.findViewById(R.id.checkbox5);
        checkbox6 = (CheckBox) itemView.findViewById(R.id.checkbox6);
    }

}
