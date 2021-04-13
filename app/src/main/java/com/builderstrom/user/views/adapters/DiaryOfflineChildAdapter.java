package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.AddAttachModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class DiaryOfflineChildAdapter extends RecyclerView.Adapter<DiaryOfflineChildAdapter.MyViewHolder> {

    public List<AddAttachModel> fileModel;
    private Context context;
    private CallbackImage mCallback;

    public DiaryOfflineChildAdapter(Context ctx, List<AddAttachModel> fileModel, CallbackImage mCallback) {
        this.context = ctx;
        this.fileModel = fileModel;
        this.mCallback = mCallback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_attachment, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {
        if (fileModel.get(i).getFileurl() == null || fileModel.get(i).getFileurl().isEmpty()) {
            if (fileModel.get(i).getName() != null && fileModel.get(i).getName() != null) {
                String extension = fileModel.get(i).getName().substring(fileModel.get(i).getName().lastIndexOf("."));

                if (CommonMethods.isImageUrl(fileModel.get(i).getName())) {
                    try {
                        holder.ivImage.setImageBitmap(fileModel.get(i).getImageBitmap());
                        holder.itemView.setOnClickListener(view -> mCallback.openFullImage(fileModel.get(i).getFilePath(), holder.ivImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (CommonMethods.getAllExtensions().contains(extension)) {
                        int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                        holder.ivImage.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
                    } else {
                        holder.ivImage.setImageResource(R.drawable.ic_file);
                    }
                }
                holder.tvFileName.setText(fileModel.get(i).getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return fileModel.size();
    }


    public interface CallbackImage {
        void openFullImage(String imageUrl, ImageView imageView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private ImageView ivRemove;
        private TextView tvFileName;

        public MyViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            ivRemove = view.findViewById(R.id.ivRemove);
            ivRemove.setVisibility(View.GONE);
            tvFileName = view.findViewById(R.id.tvFileName);
        }
    }
}
