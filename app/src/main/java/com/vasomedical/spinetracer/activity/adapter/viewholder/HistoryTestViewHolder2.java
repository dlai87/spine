package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class HistoryTestViewHolder2 extends RecyclerView.ViewHolder {

    public CheckBox radioButton;
    public TextView tvName, tvDate;
    public View item1, item2, item3, item4, item5, item6;
    public CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6;

    public HistoryTestViewHolder2(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        radioButton = (CheckBox) itemView.findViewById(R.id.radioButton);
        item1 = (View) itemView.findViewById(R.id.item1);
        item2 = (View) itemView.findViewById(R.id.item2);
        item3 = (View) itemView.findViewById(R.id.item3);
        item4 = (View) itemView.findViewById(R.id.item4);
        item5 = (View) itemView.findViewById(R.id.item5);
        item6 = (View) itemView.findViewById(R.id.item6);
        checkbox1 = (CheckBox) itemView.findViewById(R.id.checkbox1);
        checkbox2 = (CheckBox) itemView.findViewById(R.id.checkbox2);
        checkbox3 = (CheckBox) itemView.findViewById(R.id.checkbox3);
        checkbox4 = (CheckBox) itemView.findViewById(R.id.checkbox4);
        checkbox5 = (CheckBox) itemView.findViewById(R.id.checkbox5);
        checkbox6 = (CheckBox) itemView.findViewById(R.id.checkbox6);
    }

}
