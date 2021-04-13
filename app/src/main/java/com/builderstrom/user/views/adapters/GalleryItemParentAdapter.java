package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;
import java.util.List;


public class GalleryItemParentAdapter extends RecyclerView.Adapter<GalleryItemParentAdapter.MyViewHolder> {

    private List<GalleryPicModel> picModel;
    private CallbackChild callbackChild;
    private Context context;

    public GalleryItemParentAdapter(Context ctx, List<GalleryPicModel> picModel, CallbackChild callbackChild) {
        this.context = ctx;
        this.picModel = picModel;
        this.callbackChild = callbackChild;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_parent, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (null != picModel.get(i).getImageName()) {
            GlideApp.with(context).load(CommonMethods.decodeUrl(picModel.get(i).getImageName()))
                    .apply(new RequestOptions().placeholder(R.color.gray).error(R.color.gray)
                            .override(100, 100).centerCrop())
                    .into(myViewHolder.ivItemParent);

        } else if (picModel.get(i).getImagePath() != null) {
            myViewHolder.ivItemParent.setImageBitmap(BitmapFactory.decodeFile(picModel.get(i).getImagePath()));
        }
        myViewHolder.ivItemParent.setOnClickListener(view -> callbackChild.onImageClick());

    }

    @Override
    public int getItemCount() {
        return picModel.size();
    }

    public interface CallbackChild {
        void onImageClick();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemParent;

        public MyViewHolder(View view) {
            super(view);
            ivItemParent = view.findViewById(R.id.ivItemParent);
        }
    }
}

