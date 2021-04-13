package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.DiaryManLabour;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaborListViewAdapter extends RecyclerView.Adapter<LaborListViewAdapter.LaborHolder> {

    private Context mContext;
    private List<DiaryManLabour> dataList;
    private LayoutInflater mLayoutInflater;

    public LaborListViewAdapter(Context mContext, List<DiaryManLabour> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull @Override public LaborHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LaborHolder(mLayoutInflater.inflate(R.layout.row_site_labour, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull LaborHolder holder, int position) {
        DiaryManLabour data = dataList.get(position);
        holder.tvQuantity.setText(data.getWorkHours());
        holder.tvTitle.setText(data.getLabel());
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    class LaborHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvQuantity) TextView tvQuantity;

        public LaborHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
