package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.HistoryTestViewHolder;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.List;

public class HistoryTestAdapter extends RecyclerView.Adapter<HistoryTestViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<InspectionRecord> data;

    public HistoryTestAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HistoryTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryTestViewHolder(mLayoutInflater.inflate(R.layout.activity_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTestViewHolder holder, int position) {
        if (data != null) {
            InspectionRecord model = data.get(position);
            holder.tvName.setText(model.getPatient().getName());
            holder.tvDate.setText(model.getPatient().getDate_of_birth() + " No." + model.getPatient().getId());
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 10 : data.size();
    }

    public void setData(List<InspectionRecord> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<InspectionRecord> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<InspectionRecord> getData() {
        return data;
    }

    public InspectionRecord getDataItem(int index) {
        if (data == null || index >= data.size()) {
            return null;
        } else {
            return data.get(index);
        }
    }

    public void removeItem(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void removeItem(PatientModel item) {
        data.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(String no) {
        for (InspectionRecord model : data) {
            if (no.equals(model.getId())) {
                data.remove(model);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
