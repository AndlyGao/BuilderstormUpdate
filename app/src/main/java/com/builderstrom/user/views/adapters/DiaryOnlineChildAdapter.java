package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.AddAttachModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class DiaryOnlineChildAdapter extends RecyclerView.Adapter<DiaryOnlineChildAdapter.MyViewHolder> {

    public List<AddAttachModel> fileModel;
    private Context context;
    private CallbackImage mCallback;

    public DiaryOnlineChildAdapter(Context ctx, List<AddAttachModel> fileModel, CallbackImage mCallback) {
        this.context = ctx;
        this.fileModel = fileModel;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_attachment, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int i) {
        holder.setIsRecyclable(false);
        if (fileModel.get(i).getName() != null && fileModel.get(i).getName() != null) {
            String extension = fileModel.get(i).getName().substring(fileModel.get(i).getName().lastIndexOf("."));
            if (CommonMethods.isImageUrl(fileModel.get(i).getName())) {
                GlideApp.with(context).load(CommonMethods.decodeUrl(fileModel.get(i).getFileurl())).apply(RequestOptions.placeholderOf(R.drawable.ic_file)).into(holder.ivImage);
                holder.container.setOnClickListener(view -> mCallback.openFullImage(fileModel.get(i).getFileurl(), holder.ivImage));
            } else {
                if (CommonMethods.getAllExtensions().contains(extension)) {
                    int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                    holder.ivImage.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
                } else {
                    holder.ivImage.setImageResource(R.drawable.ic_file);
                }
                holder.container.setOnClickListener(view -> mCallback.downloadFile(fileModel.get(i).getFileurl(), fileModel.get(i).getName()));
            }
            holder.tvFileName.setText(fileModel.get(i).getName());
        }
    }

    @Override
    public int getItemCount() {
        return fileModel.size();
    }


    public interface CallbackImage {
        void openFullImage(String imageUrl, ImageView imageView);

        void downloadFile(String fileUrl, String fileName);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private ImageView ivRemove;
        private TextView tvFileName;
        private ConstraintLayout container;

        public MyViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            ivImage = view.findViewById(R.id.ivImage);
            ivRemove = view.findViewById(R.id.ivRemove);
            ivRemove.setVisibility(View.GONE);
            tvFileName = view.findViewById(R.id.tvFileName);
            tvFileName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }
}
