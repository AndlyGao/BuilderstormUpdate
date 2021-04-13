package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.PDCommentModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class PDocsCommentsListAdapter extends RecyclerView.Adapter<PDocsCommentsListAdapter.CommentHolder> {

    private List<PDCommentModel.Comment> commentList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PDocsCommentsListAdapter(Context mContext, List<PDCommentModel.Comment> commentList) {
        this.commentList = commentList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentHolder(mLayoutInflater.inflate(R.layout.row_rfi_comments, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        PDCommentModel.Comment comment = commentList.get(i);
        commentHolder.tvComment.setText(comment.getComment());
        commentHolder.tvCommentedBy.setText(comment.getAdded_by());
        commentHolder.tvCommentTime.setText(CommonMethods.convertDate(CommonMethods.DF_1, comment.getCommentDate(), CommonMethods.DF_6));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView tvCommentedBy, tvComment, tvCommentTime;
        RecyclerView rvFiles;

        private CommentHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCommentedBy = itemView.findViewById(R.id.tvUsername);
            tvCommentTime = itemView.findViewById(R.id.tvTime);
            rvFiles = itemView.findViewById(R.id.rvFiles);
        }
    }

}
