package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class PatientViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvDate, tvNo, tvSex;
    public View selectView;

    public PatientViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        tvNo = (TextView) itemView.findViewById(R.id.tvNo);
        tvSex = (TextView) itemView.findViewById(R.id.tvSex);
        selectView = itemView.findViewById(R.id.selectView);
    }

}
