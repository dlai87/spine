package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class DoctorViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvCompany,tvClass;
    public View selectView;

    public DoctorViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvCompany = (TextView) itemView.findViewById(R.id.tvCompany);
        tvClass = (TextView) itemView.findViewById(R.id.tvClass);
        selectView = itemView.findViewById(R.id.selectView);
    }

}
