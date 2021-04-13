package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DigitalFormModel;
import com.builderstrom.user.repository.retrofit.modals.RowFormModel;
import com.builderstrom.user.repository.retrofit.modals.TemplateData;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestRowFormAdapter extends RecyclerView.Adapter<TestRowFormAdapter.RowHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<TemplateData> dataList;
    private boolean isTabular;
    private int screenWidth;

    public TestRowFormAdapter(Context mContext, List<TemplateData> dataList, boolean isTabular, int screenWidth) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.isTabular = isTabular;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new RowHolder(mLayoutInflater.inflate(R.layout.row_form_test_main,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder viewHolder, int position) {
        TemplateData data = dataList.get(position);
        DigitalFormModel.Rowdata rowData = data.getRowData();
        viewHolder.ivRepeat.setVisibility(null != rowData && !data.isDeletable() && null != rowData.getRepeatStatus() &&
                rowData.getRepeatStatus().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
//        viewHolder.ivDelete.setVisibility(data.isDeletable() ? View.VISIBLE : View.GONE);
        if (null != rowData.getEditableRow()) {
            viewHolder.ivRepeat.setClickable(rowData.getEditableRow());
            viewHolder.ivDelete.setClickable(rowData.getEditableRow());
            viewHolder.ivRepeat.setImageDrawable(rowData.getEditableRow() ? mContext.getResources().getDrawable(R.drawable.ic_add) : mContext.getResources().getDrawable(R.drawable.ic_add_disable));
            viewHolder.ivDelete.setImageDrawable(rowData.getEditableRow() ? mContext.getResources().getDrawable(R.drawable.deleteicon) : mContext.getResources().getDrawable(R.drawable.deleteicon_disable));
        }
        /** Show delete row button*/
        // Edited on 03/09/2020 bu anil_singhania
        if ((null != rowData.getRepeatData() && rowData.getRepeatData()) || data.isDeletable()) {
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivDelete.setVisibility(View.GONE);
        }

        if (isTabular) {
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        } else {
            viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        }
        viewHolder.recyclerView.setAdapter(new FormListAdapter(mContext, position, data.getFormModelList(), isTabular, screenWidth));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.ivRepeat)
        ImageView ivRepeat;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        RowHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            if (isTabular) {
                params.width = RecyclerView.LayoutParams.WRAP_CONTENT;
            } else {
                params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            }
            params.height = RecyclerView.LayoutParams.MATCH_PARENT;
            recyclerView.setLayoutParams(params);
        }

        @OnClick({R.id.ivRepeat, R.id.ivDelete})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivRepeat:
                    addAnotherRow(getAdapterPosition());
                    break;
                case R.id.ivDelete:
                    removeRow(getAdapterPosition());
                    break;
            }
        }
    }

    private void addAnotherRow(int adapterPosition) {
        TemplateData test = dataList.get(adapterPosition);
        List<RowFormModel> modelList = new ArrayList<>();
        for (RowFormModel model : test.getFormModelList()) {
            try {
                modelList.add(model.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        TemplateData data = new TemplateData(test.getRowData(), test.getRowColumns(),
                modelList, true);

        /** Change to repeat row functionality
         *
         * Add new row in the last index of all repeated rows of the main row
         * */
        int newPosition = adapterPosition + 1;
        String rowId = dataList.get(adapterPosition).getRowData().getId();
        if (newPosition < dataList.size()) {
            String nextRowId = dataList.get(newPosition).getRowData().getId();
            if (rowId.equals(nextRowId)) {
                while (rowId.equals(nextRowId)) {
                    newPosition = newPosition + 1;
                    if (newPosition < dataList.size()) {
                        nextRowId = dataList.get(newPosition).getRowData().getId();
                    } else {
                        break;
                    }
                }
            }
        }
        dataList.add(newPosition, data);
        notifyDataSetChanged();
    }

    private void removeRow(int adapterPosition) {
        if (adapterPosition != -1 && adapterPosition < dataList.size()) {
            dataList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

}