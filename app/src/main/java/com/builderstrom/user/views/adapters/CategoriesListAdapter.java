package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.CatListing;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.CommentsHolder> {
    public CallBack mCallback;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<CatListing> catListings;

    public CategoriesListAdapter(Context mContext, List<CatListing> catListings, CallBack mCallBack) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.catListings = catListings;
        this.mCallback = mCallBack;
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsHolder(mLayoutInflater.inflate(R.layout.item_text_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder commentsHolder, int i) {
        commentsHolder.tvTitle.setText(catListings.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return catListings.size();
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
                if (catListings!=null){
                    mCallback.setData(catListings.get(getAdapterPosition()).getId(), catListings.get(getAdapterPosition()).getTitle());
                }

            });
        }
    }
}
