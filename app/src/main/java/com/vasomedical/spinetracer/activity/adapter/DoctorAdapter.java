package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.DoctorViewHolder;
import com.vasomedical.spinetracer.model.DoctorModel;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<DoctorModel> data;
    private DoctorModel selectModel;

    public DoctorAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorViewHolder(mLayoutInflater.inflate(R.layout.activity_doctor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        if (data != null) {
            DoctorModel peopleModel = data.get(position);
            holder.tvName.setText(peopleModel.getName());
            holder.tvClass.setText(peopleModel.getDepartment());
            holder.tvCompany.setText(peopleModel.getHospital());
            holder.selectView.setVisibility(peopleModel == selectModel ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<DoctorModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<DoctorModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<DoctorModel> getData() {
        return data;
    }

    public DoctorModel getDataItem(int index) {
        if (data == null || index >= data.size()) {
            return null;
        } else {
            return data.get(index);
        }
    }

    public DoctorModel getSelectItem() {
        return selectModel;
    }

    public void selectItem(int index) {
        selectModel = data.get(index);
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void removeItem(DoctorModel item) {
        data.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(String no) {
        for (DoctorModel patientModel : data) {
            if (no.equals(patientModel.getId())) {
                data.remove(patientModel);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
