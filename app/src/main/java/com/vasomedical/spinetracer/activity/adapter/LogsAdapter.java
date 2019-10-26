package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.LogViewHolder;
import com.vasomedical.spinetracer.model.LogModel;

import java.util.ArrayList;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<LogModel> data = new ArrayList<>();

    public LogsAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogViewHolder(mLayoutInflater.inflate(R.layout.activity_admin_logs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        if (data != null) {
            LogModel model = data.get(position);
            holder.tvTime.setText(model.getTime());
            holder.tvName.setText(model.getDoctorModel().getName());
            holder.tvThing.setText(model.getThing());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<LogModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<LogModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<LogModel> getData() {
        return data;
    }
}
