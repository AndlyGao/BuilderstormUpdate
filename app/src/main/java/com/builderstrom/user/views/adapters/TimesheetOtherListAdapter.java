package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.TimesheetMetaData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimesheetOtherListAdapter extends RecyclerView.Adapter<TimesheetOtherListAdapter.OtherHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<TimesheetMetaData.MetaSetup> dataList;

    public TimesheetOtherListAdapter(Context mContext, List<TimesheetMetaData.MetaSetup> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public OtherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OtherHolder(
                mLayoutInflater.inflate(R.layout.row_time_sheet_other, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OtherHolder otherHolder, int i) {
        otherHolder.cbOther.setText(String.format("%s £%s", dataList.get(i).getMetadata_name(),
                dataList.get(i).getMetadata_value()));
        otherHolder.cbOther.setChecked(dataList.get(i).isChecked());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class OtherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cbOther) CheckBox cbOther;

        public OtherHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cbOther.setOnCheckedChangeListener((buttonView, isChecked) ->
                    dataList.get(getAdapterPosition()).setChecked(isChecked));
        }
    }


    public List<String> getSelectedMetaData() {
        List<String> selectedIds = new ArrayList<>();

        for (TimesheetMetaData.MetaSetup other : dataList) {
            if (other.isChecked()) {
                selectedIds.add(other.getId());
            }
        }
        return selectedIds;
    }


}
