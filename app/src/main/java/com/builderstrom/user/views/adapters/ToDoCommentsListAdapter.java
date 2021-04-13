package com.builderstrom.user.views.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.ToDoComments;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoCommentsListAdapter extends RecyclerView.Adapter<ToDoCommentsListAdapter.CommentsHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ToDoComments> dataList;

    public ToDoCommentsListAdapter(Context mContext, List<ToDoComments> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.row_rfi_comments, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull CommentsHolder viewHolder, int position) {
        ToDoComments data = dataList.get(position);
        viewHolder.tvComment.setText(data.getComments());
        viewHolder.tvTime.setText(data.getDateTime());
        viewHolder.tvUsername.setText(data.getFullName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvTime) TextView tvTime;

        CommentsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}