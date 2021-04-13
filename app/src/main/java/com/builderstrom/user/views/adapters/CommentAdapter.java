package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DrawingCommentModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<DrawingCommentModel.Comment> commentList;
    private Context context;

    public CommentAdapter(Context context, List<DrawingCommentModel.Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, final int position) {
        holder.ll_Container.setBackgroundColor(ContextCompat.getColor(context, R.color.status_bar));
        DrawingCommentModel.Comment model = commentList.get(position);
        holder.tvComment.setText(model.getComment());
        if (!model.getCommentDate().isEmpty() && model.getCommentDate() != null) {
            holder.tvCommentDate.setText(CommonMethods.convertDate(CommonMethods.DF_1, model.getCommentDate(), CommonMethods.DF_6));
        }
        holder.tvUserName.setText(model.getCommentorName());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvTime) TextView tvCommentDate;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.llContainer) ConstraintLayout ll_Container;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
