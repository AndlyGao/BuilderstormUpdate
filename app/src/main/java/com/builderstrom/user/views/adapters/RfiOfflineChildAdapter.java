package com.builderstrom.user.views.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.builderstrom.user.data.retrofit.modals.RfiFileModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class RfiOfflineChildAdapter extends RecyclerView.Adapter<RfiOfflineChildAdapter.MyViewHolder> {

    private Context context;
    private List<RfiFileModel> fileModel;
    private CallbackImage mCallback;

    public RfiOfflineChildAdapter(Context ctx, List<RfiFileModel> fileModel, CallbackImage mCallback) {
        this.context = ctx;
        this.fileModel = fileModel;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rfi_child, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        RfiFileModel model = fileModel.get(i);

        if (model.getName() != null && !model.getName().isEmpty()) {
            String extension = model.getName().substring(model.getName().lastIndexOf("."));
            if (CommonMethods.isImageUrl(model.getName())) {
                try {
                    if (model.getFilePath() != null && !model.getFilePath().isEmpty()) {
                        holder.ivItemChild.setImageBitmap(BitmapFactory.decodeFile(model.getFilePath()));
                    } else {
                        holder.ivItemChild.setImageBitmap(CommonMethods.getBitmapFromName(context, model.getName()));
                    }

                    holder.clRoot.setOnClickListener(v -> {
                        if (model.getFilePath() != null && !model.getFilePath().isEmpty()) {
                            viewBigImage(model.getFilePath(), false);
                        } else {
                            viewBigImage(model.getName(), true);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (CommonMethods.getAllExtensions().contains(extension)) {
                    int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                    holder.ivItemChild.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
                } else {
                    holder.ivItemChild.setImageResource(R.drawable.ic_file);
                }
            }
            holder.tvComment.setText(model.getName());
        }
    }

    private void viewBigImage(String fileName, boolean isFileName) {
        if (!fileName.isEmpty()) {
            Intent intent = new Intent(context, ImageFullScreenActivity.class);
            intent.putExtra(isFileName ? "fileName" : "filePath", fileName);
            context.startActivity(intent);
        } else {
            ((BaseActivity)context).errorMessage("Can't access this file",null,false);
        }
    }

    @Override
    public int getItemCount() {
        return fileModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItemChild;
        public ImageView ivDeleteRfiImage;
        public TextView tvComment;
        public ConstraintLayout clRoot;

        public MyViewHolder(View view) {
            super(view);
            ivItemChild = view.findViewById(R.id.ivItemChild);
            ivDeleteRfiImage = view.findViewById(R.id.ivDeleteGalleryImage);
            ivDeleteRfiImage.setVisibility(View.GONE);
            tvComment = view.findViewById(R.id.tvComment);
            clRoot = view.findViewById(R.id.clRoot);
        }
    }

    public interface CallbackImage {
        void openFullImage(Bitmap imageUrl, ImageView imageView);
    }

}


