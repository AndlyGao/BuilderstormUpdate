package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.List;


public class AddGalleryImageAdapter extends RecyclerView.Adapter<AddGalleryImageAdapter.MyViewHolder> {

    private Context context;
    private List<GalleryPicModel> imagesList;

    private int selectedPosition;
    private ProjectPhotosVM viewModel;

    public AddGalleryImageAdapter(Context ctx, List<GalleryPicModel> imagesList, ProjectPhotosVM viewModel) {
        this.context = ctx;
        this.imagesList = imagesList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_child, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GalleryPicModel item = imagesList.get(position);
        if (imagesList.get(position).isClient()) {
            GlideApp.with(context).load(item.getImagePath())
                    .apply(RequestOptions.placeholderOf(R.color.gray)).into(holder.ivItemChild);
//            holder.ivItemChild.setImageBitmap(BitmapFactory.decodeFile(imagesList.get(position).getImagePath()));
            holder.ivDeleteGalleryImage.setOnClickListener(view -> {
                imagesList.remove(position);
                notifyDataSetChanged();
            });
        } else {

            Glide.with(context).load(CommonMethods.decodeUrl(item.getImageName()))
                    .apply(RequestOptions.placeholderOf(R.color.gray)).into(holder.ivItemChild);
            holder.ivDeleteGalleryImage.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                viewModel.deleteGalleryImageAPI(item.getImageId(), position, imagesList);
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemChild;
        private ImageView ivDeleteGalleryImage;
        private TextView tvComment;

        public MyViewHolder(View view) {
            super(view);
            ivItemChild = view.findViewById(R.id.ivItemChild);
            ivDeleteGalleryImage = view.findViewById(R.id.ivDeleteGalleryImage);
            tvComment = view.findViewById(R.id.tvComment);
            tvComment.setVisibility(View.GONE);
        }
    }


}
