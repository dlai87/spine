package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.PatientViewHolder;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<PatientModel> data;
    private PatientModel selectPeopleModel;

    public PatientAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientViewHolder(mLayoutInflater.inflate(R.layout.activity_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        if (data != null) {
            PatientModel peopleModel = data.get(position);
            holder.tvName.setText(peopleModel.getName());
            holder.tvDate.setText(peopleModel.getDate_of_birth());
            holder.tvSex.setText(peopleModel.getGender());
            holder.tvNo.setText("No." + peopleModel.getId());
            holder.selectView.setVisibility(peopleModel == selectPeopleModel ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 10 : data.size();
    }

    public void setData(List<PatientModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<PatientModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<PatientModel> getData() {
        return data;
    }

    public PatientModel getDataItem(int index) {
        if (data == null || index >= data.size()) {
            return null;
        } else {
            return data.get(index);
        }
    }

    public PatientModel getSelectItem() {
        return selectPeopleModel;
    }

    public void selectItem(int index) {
        selectPeopleModel = data.get(index);
        notifyDataSetChanged();
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
        for (PatientModel patientModel : data) {
            if (no.equals(patientModel.getId())) {
                data.remove(patientModel);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
