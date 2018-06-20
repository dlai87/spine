package com.vasomedical.spinetracer.activity.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vasomedical.spinetracer.R;

public class LogViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTime, tvName, tvThing;

    public LogViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        tvThing = (TextView) itemView.findViewById(R.id.tvThing);
    }

}
