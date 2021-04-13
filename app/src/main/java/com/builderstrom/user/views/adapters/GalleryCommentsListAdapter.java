package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.GalleryComment;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class GalleryCommentsListAdapter extends RecyclerView.Adapter<GalleryCommentsListAdapter.CommentsHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GalleryComment> commentList;

    public GalleryCommentsListAdapter(Context mContext, List<GalleryComment> commentList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.row_rfi_comments, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {
        commentsHolder.tvUserName.setText(commentList.get(i).getFullname());
        commentsHolder.tvComment.setText(commentList.get(i).getComments());
        commentsHolder.tvTime.setText(CommonMethods.convertDate(CommonMethods.DF_1,
                commentList.get(i).getDate_added(), CommonMethods.DF_3));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName, tvComment, tvTime;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvTime = itemView.findViewById(R.id.tvTime);
            itemView.findViewById(R.id.rvFiles).setVisibility(View.GONE);
        }
    }
}
