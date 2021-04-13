package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.CommentsHolder> {
    public CallBack mCallback;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<User> userListing;

    public UserListAdapter(Context mContext, List<User> userListing, CallBack mCallBack) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.userListing = userListing;
        this.mCallback = mCallBack;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.item_text_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {
        commentsHolder.tvTitle.setText(userListing.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return userListing.size();
    }

    public interface CallBack {

        void setData(String categoryId, String category);

    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public CommentsHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvText);
            tvTitle.setOnClickListener(v -> {
                if (userListing != null) {
                    mCallback.setData(userListing.get(getAdapterPosition()).getUserId(), userListing.get(getAdapterPosition()).getName());
                }

            });
        }
    }
}
