package com.vasomedical.spinetracer.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.adapter.viewholder.HistoryTestViewHolder;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.PatientModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HistoryTestAdapter extends RecyclerView.Adapter<HistoryTestViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<InspectionRecord> data;
    private Map<InspectionRecord, Set<InspectionRecord>> selectModel;

    public HistoryTestAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        selectModel = new HashMap<>();
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryTestViewHolder viewHolder;
        if (viewType == 1) {
            viewHolder = new HistoryTestViewHolder(mLayoutInflater.inflate(R.layout.activity_historytest_item1, parent, false));
            viewHolder.radioButton.setOnCheckedChangeListener(this);
        } else {
            viewHolder = new HistoryTestViewHolder(mLayoutInflater.inflate(R.layout.activity_historytest_item2, parent, false));
            viewHolder.checkbox1.setOnCheckedChangeListener(this);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTestViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (data != null) {
            InspectionRecord model = data.get(position);

            if (viewType == 1) {
                holder.tvName.setText(model.getPatient().getName());
                holder.tvDate.setText(model.getTimestamp() + " No." + model.getPatient().getId());
                holder.radioButton.setTag(R.id.HistoryTestModel, model);
                model.tempView1 = holder.radioButton;
            } else {
                String type = model.getType();
                String typeName = "";
                if ("slant".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_1);
                } else if ("humpback".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_2);
                } else if ("bending".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_3);
                } else if ("left_right".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_4);
                } else if ("forward_back".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_5);
                } else if ("rotate".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_6);
                } else if ("balance".equals(type)) {
                    typeName = mContext.getResources().getString(R.string.detect_option_7);
                }
                holder.itemName1.setText(typeName);
                holder.checkbox1.setTag(R.id.InspectionRecord, model);
                model.tempView12 = holder.checkbox1;
                //向上查找
                for (int index = position; index >= 0; index--) {
                    InspectionRecord lastRecode = data.get(index);
                    if (lastRecode.getId() == null) {
                        holder.checkbox1.setTag(R.id.HistoryTestModel, lastRecode);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getId() == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<InspectionRecord> data) {
        this.data = data;
        selectModel.clear();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        InspectionRecord model = (InspectionRecord) buttonView.getTag(R.id.HistoryTestModel);

        if (buttonView.getId() == R.id.radioButton) {
            Set<InspectionRecord> setInspectionRecord = selectModel.get(model);
            if (setInspectionRecord == null) {
                setInspectionRecord = new HashSet<>();
            }
            if (isChecked) {
                selectModel.put(model, setInspectionRecord);
            } else {
                while(!setInspectionRecord.isEmpty()){
                    InspectionRecord record = setInspectionRecord.iterator().next();
                    if (record.tempView12 != null) {
                        record.tempView12.setChecked(false);
                    }
                    if (record.tempView1 != null) {
                        record.tempView1.setChecked(false);
                    }
                }
                selectModel.remove(model);
            }
        } else {
            Set<InspectionRecord> setInspectionRecord = selectModel.get(model);
            if (setInspectionRecord == null) {
                Toast.makeText(mContext, "请在前面先打上勾", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
            } else {
                InspectionRecord inspectionRecord = (InspectionRecord) buttonView.getTag(R.id.InspectionRecord);
                if (isChecked) {
                    setInspectionRecord.add(inspectionRecord);
                } else {
                    setInspectionRecord.remove(inspectionRecord);
                }
                selectModel.put(model, setInspectionRecord);
            }
        }
    }

    public Map<InspectionRecord, Set<InspectionRecord>> getSelectModel() {
        return selectModel;
    }
}
