package com.builderstrom.user.views.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.CompanyComment;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyCommentListAdapter extends RecyclerView.Adapter<CompanyCommentListAdapter.CommentHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<CompanyComment> dataList;

    public CompanyCommentListAdapter(Context mContext, List<CompanyComment> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CommentHolder(mLayoutInflater.inflate(R.layout.item_comment,
                viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull CommentHolder viewHolder, int position) {
        CompanyComment comment = dataList.get(position);
        viewHolder.llContainer.setBackgroundResource(R.drawable.drawable_rfi_comment_back);
        GlideApp.with(mContext).load(comment.getProfileimg()).apply(new RequestOptions().
                error(R.drawable.ic_person).placeholder(R.drawable.ic_person).circleCrop()).
                into(viewHolder.ivProfile);
        viewHolder.tvUsername.setText(comment.getFullname());
        viewHolder.tvComment.setText(comment.getComment());
        viewHolder.tvTime.setText(CommonMethods.convertDate(CommonMethods.DF_1, comment.getComment_date(), CommonMethods.DF_6));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llContainer) ConstraintLayout llContainer;
        @BindView(R.id.ivProfile) ImageView ivProfile;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvTime) TextView tvTime;

        CommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}