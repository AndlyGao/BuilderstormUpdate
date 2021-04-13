package com.builderstrom.user.views.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.MyItemsStatusModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyItemsStatusListAdapter extends RecyclerView.Adapter<MyItemsStatusListAdapter.StatusHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MyItemsStatusModel> dataList;

    public MyItemsStatusListAdapter(Context mContext, List<MyItemsStatusModel> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public StatusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new StatusHolder(mLayoutInflater.inflate(R.layout.row_status_my_items,
                viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull StatusHolder viewHolder, int position) {
        MyItemsStatusModel data = dataList.get(position);
        viewHolder.tvDate.setText(data.getDate());
        if (data.getType().equalsIgnoreCase("issue_document")) {
            viewHolder.tvStatus.setText(CommonMethods.coloredSpannedTextMultiple(
                    String.format("%s %s %s.", data.getUsername(), data.getBetweenText(),
                            data.getStrongText()), data.getUsername().length(),
                    data.getStrongText().length() + 1));
            viewHolder.llStatus.setSelected(false);
        } else if (data.getType().equalsIgnoreCase("completed_document")) {
            viewHolder.tvStatus.setText(CommonMethods.coloredSpannedText(
                    String.format("%s %s.", data.getUsername(), data.getBetweenText()),
                    data.getUsername().length()));
            viewHolder.llStatus.setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class StatusHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvStatus) TextView tvStatus;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.llStatus) View llStatus;

        StatusHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}