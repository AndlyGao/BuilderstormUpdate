package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.utils.GlideApp;
import com.bumptech.glide.annotation.GlideModule;

import java.util.List;

public class GalleryOfflineChildAdapter extends RecyclerView.Adapter<GalleryOfflineChildAdapter.MyViewHolder> {

    private List<GalleryPicModel> picModel;
    private Context context;
    private GalleryOfflineAdapter mAdapter;
    private CallbackImage mCallback;

    public GalleryOfflineChildAdapter(Context ctx, List<GalleryPicModel> picModel, GalleryOfflineAdapter mAdapter, CallbackImage mCallback) {
        this.context = ctx;
        this.picModel = picModel;
        this.mAdapter = mAdapter;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_child, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if (picModel.get(i).getImagePath() != null) {
            GlideApp.with(context)/*.asBitmap()*/.load(picModel.get(i).getImagePath()).into(holder.ivItemChild);
            holder.ivItemChild.setOnClickListener(view -> mCallback.openFullImage(picModel.get(i).getImagePath(), holder.ivItemChild));
        }


    }

    @Override
    public int getItemCount() {
        return picModel.size();
    }


    public interface CallbackImage {
        void openFullImage(String imagePath, ImageView imageView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemChild;
        private ImageView ivDeleteGalleryImage;
        private TextView tvComment;

        public MyViewHolder(View view) {
            super(view);
            ivItemChild = view.findViewById(R.id.ivItemChild);
            ivDeleteGalleryImage = view.findViewById(R.id.ivDeleteGalleryImage);
            ivDeleteGalleryImage.setVisibility(View.GONE);
            tvComment = view.findViewById(R.id.tvComment);
            tvComment.setVisibility(View.GONE);
        }
    }
}
