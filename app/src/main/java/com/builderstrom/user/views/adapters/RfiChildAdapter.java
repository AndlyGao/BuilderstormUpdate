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
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

public class RfiChildAdapter extends RecyclerView.Adapter<RfiChildAdapter.MyViewHolder> {

    private List<String> filesList;
    private Context context;

    public RfiChildAdapter(Context context, List<String> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_file_upload, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String fileName = CommonMethods.getFileName(context, CommonMethods.getUriFromFileName(context, filesList.get(i)));

        if (fileName != null && !fileName.isEmpty()) {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (CommonMethods.isImageUrl(fileName)) {
                try {
                    holder.ivItemChild.setImageBitmap(BitmapFactory.decodeFile(filesList.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (CommonMethods.getAllExtensions().contains(extension)) {
                    holder.ivItemChild.setImageResource(CommonMethods.getAllIcons().get(CommonMethods.getAllExtensions().indexOf(extension)));
                } else {
                    holder.ivItemChild.setImageResource(R.drawable.ic_file);
                }
            }
            holder.tvComment.setText(fileName);
        }
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItemChild;
        public TextView tvComment;

        public MyViewHolder(View view) {
            super(view);
            ivItemChild = view.findViewById(R.id.ivItemChild);
            tvComment = view.findViewById(R.id.tvComment);
            view.findViewById(R.id.ivDeleteGalleryImage).setOnClickListener(v -> removeItem(getAdapterPosition()));
        }
    }

    private void removeItem(int adapterPosition) {
        if (adapterPosition < filesList.size()) {
            filesList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, filesList.size());
        }
    }

}
