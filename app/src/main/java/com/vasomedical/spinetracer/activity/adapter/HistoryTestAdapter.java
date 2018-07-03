package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.HistoryTestViewHolder;
import com.vasomedical.spinetracer.model.HistoryTestModel;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HistoryTestAdapter extends RecyclerView.Adapter<HistoryTestViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<HistoryTestModel> data;
    private Map<HistoryTestModel, Set<InspectionRecord>> selectModel;

    public HistoryTestAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        selectModel = new HashMap<>();
    }

    @NonNull
    @Override
    public HistoryTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryTestViewHolder viewHolder = new HistoryTestViewHolder(mLayoutInflater.inflate(R.layout.activity_historytest_item, parent, false));
        viewHolder.radioButton.setOnCheckedChangeListener(this);
        viewHolder.checkbox1.setOnCheckedChangeListener(this);
        viewHolder.checkbox2.setOnCheckedChangeListener(this);
        viewHolder.checkbox3.setOnCheckedChangeListener(this);
        viewHolder.checkbox4.setOnCheckedChangeListener(this);
        viewHolder.checkbox5.setOnCheckedChangeListener(this);
        viewHolder.checkbox6.setOnCheckedChangeListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTestViewHolder holder, int position) {
        if (data != null) {
            HistoryTestModel model = data.get(position);
            holder.tvName.setText(model.getPatientModel().getName());
            holder.tvDate.setText(model.getDate() + " No." + model.getPatientModel().getId());
            //控制数据
            holder.radioButton.setTag(R.id.HistoryTestModel, model);
            holder.checkbox1.setTag(R.id.HistoryTestModel, model);
            holder.checkbox2.setTag(R.id.HistoryTestModel, model);
            holder.checkbox3.setTag(R.id.HistoryTestModel, model);
            holder.checkbox4.setTag(R.id.HistoryTestModel, model);
            holder.checkbox5.setTag(R.id.HistoryTestModel, model);
            holder.checkbox6.setTag(R.id.HistoryTestModel, model);
            //控制显示
            Set<InspectionRecord> selInspectionRecord = selectModel.get(model);
            holder.radioButton.setChecked(selectModel.containsKey(model));
            holder.item1.setVisibility(View.GONE);
            holder.item2.setVisibility(View.GONE);
            holder.item3.setVisibility(View.GONE);
            holder.item4.setVisibility(View.GONE);
            holder.item5.setVisibility(View.GONE);
            holder.item6.setVisibility(View.GONE);
            for (InspectionRecord inspectionRecord : model.getInspectionRecordList()) {
                if ("躯干倾斜角".contains(inspectionRecord.getType())) {
                    holder.item1.setVisibility(View.VISIBLE);
                    holder.checkbox1.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox1.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                } else if ("脊柱弯曲COBB角".contains(inspectionRecord.getType())) {
                    holder.item2.setVisibility(View.VISIBLE);
                    holder.checkbox2.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox2.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                } else if ("左右侧弯倾角".contains(inspectionRecord.getType())) {
                    holder.item3.setVisibility(View.VISIBLE);
                    holder.checkbox3.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox3.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                } else if ("身体平衡度".contains(inspectionRecord.getType())) {
                    holder.item4.setVisibility(View.VISIBLE);
                    holder.checkbox4.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox4.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                } else if ("前倾/后仰角".contains(inspectionRecord.getType())) {
                    holder.item5.setVisibility(View.VISIBLE);
                    holder.checkbox5.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox5.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                } else if ("旋转角".contains(inspectionRecord.getType())) {
                    holder.item6.setVisibility(View.VISIBLE);
                    holder.checkbox6.setTag(R.id.InspectionRecord, inspectionRecord);
                    holder.checkbox6.setChecked(selInspectionRecord != null && selInspectionRecord.contains(inspectionRecord));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 10 : data.size();
    }

    public void setData(List<HistoryTestModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<HistoryTestModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<HistoryTestModel> getData() {
        return data;
    }

    public HistoryTestModel getDataItem(int index) {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        HistoryTestModel model = (HistoryTestModel) buttonView.getTag(R.id.HistoryTestModel);

        if (buttonView.getId() == R.id.radioButton) {
            Set<InspectionRecord> setInspectionRecord = selectModel.get(model);
            if (setInspectionRecord == null) {
                setInspectionRecord = new HashSet<>();
            }
            if (isChecked) {
                selectModel.put(model, setInspectionRecord);
            } else {
                selectModel.remove(model);
            }
        } else {
            Set<InspectionRecord> setInspectionRecord = selectModel.get(model);
            InspectionRecord inspectionRecord = (InspectionRecord) buttonView.getTag(R.id.InspectionRecord);
            if (setInspectionRecord == null) {
                setInspectionRecord = new HashSet<>();
            }
            if (isChecked) {
                setInspectionRecord.add(inspectionRecord);
            } else {
                setInspectionRecord.remove(inspectionRecord);
            }
            selectModel.put(model, setInspectionRecord);
        }
    }

    public Map<HistoryTestModel, Set<InspectionRecord>> getSelectModel() {
        return selectModel;
    }
}
