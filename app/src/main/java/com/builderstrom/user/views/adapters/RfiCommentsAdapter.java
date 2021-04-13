package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.RfiComment;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class RfiCommentsAdapter extends RecyclerView.Adapter<RfiCommentsAdapter.CommentsHolder> {

    private Context mContext;
    private List<RfiComment> dataList;
    private LayoutInflater mLayoutInflater;

    public RfiCommentsAdapter(Context mContext, List<RfiComment> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.row_rfi_comments, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {

        RfiComment rfiComment = dataList.get(i);
        commentsHolder.tvComment.setText(rfiComment.getComment());
        commentsHolder.tvCommentTime.setText(CommonMethods.convertDate(CommonMethods.DF_1, rfiComment.getCommented_on(), CommonMethods.DF_3));
        commentsHolder.tvCommentedBy.setText(String.format("%s %s", rfiComment.getName(), rfiComment.getLastname()));

        if (rfiComment.getCommentfiles() != null && !rfiComment.getCommentfiles().isEmpty()) {
            commentsHolder.rvFiles.setVisibility(View.VISIBLE);
            commentsHolder.rvFiles.setAdapter(new ChildAttachOnlineAdapter(mContext, rfiComment.getCommentfiles()));
        } else {
            commentsHolder.rvFiles.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        private TextView tvCommentedBy, tvComment, tvCommentTime;
        private RecyclerView rvFiles;

        CommentsHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCommentedBy = itemView.findViewById(R.id.tvUsername);
            tvCommentTime = itemView.findViewById(R.id.tvTime);
            rvFiles = itemView.findViewById(R.id.rvFiles);
        }
    }


}
