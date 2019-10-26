package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class HistoryTestViewHolder extends RecyclerView.ViewHolder {

    public CheckBox radioButton;
    public TextView tvName, tvDate, itemName1;
    public View item1;
    public CheckBox checkbox1;

    public HistoryTestViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        itemName1 = (TextView) itemView.findViewById(R.id.itemName1);
        radioButton = (CheckBox) itemView.findViewById(R.id.radioButton);
        item1 = (View) itemView.findViewById(R.id.item1);
        checkbox1 = (CheckBox) itemView.findViewById(R.id.checkbox1);
    }

}
