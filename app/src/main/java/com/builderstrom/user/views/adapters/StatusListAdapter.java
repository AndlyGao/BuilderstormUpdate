package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;

import java.util.List;

public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.CommentsHolder> {
    public CallBack mCallback;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> statusListing;

    public StatusListAdapter(Context mContext, List<String> statusListing, CallBack mCallBack) {
        this.mContext = mContext;
        this.statusListing = statusListing;
        this.mCallback = mCallBack;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.item_text_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {
        commentsHolder.tvTitle.setText(statusListing.get(i));
    }

    @Override
    public int getItemCount() {
        return statusListing.size();
    }

    public interface CallBack {
        void setData(Integer statusId);
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvText);
            tvTitle.setOnClickListener(v -> {
                mCallback.setData(getAdapterPosition());
            });

        }
    }
}
