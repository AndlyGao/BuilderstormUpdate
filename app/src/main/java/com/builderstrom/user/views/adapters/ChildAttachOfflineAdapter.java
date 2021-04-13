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

public class ChildAttachOfflineAdapter extends RecyclerView.Adapter<ChildAttachOfflineAdapter.AttachmentHolder> {
    private Context mContext;
    private List<String> data;
    private LayoutInflater mLayoutInflater;
    private UpdateLastDelete mUpdateLastDelete;
    private boolean showDelete = true;

    public ChildAttachOfflineAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public ChildAttachOfflineAdapter(Context mContext, List<String> data, boolean showDelete) {
        this.showDelete = showDelete;
        this.mContext = mContext;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public AttachmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AttachmentHolder(mLayoutInflater.inflate(R.layout.row_attachment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder attachmentHolder, int i) {
        String filename = CommonMethods.getFileName(mContext, CommonMethods.getUriFromFileName(mContext, data.get(i)));
        String extension = filename.substring(filename.lastIndexOf("."));
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            attachmentHolder.ivImage.setImageBitmap(BitmapFactory.decodeFile(data.get(i)));
        } else {
            if (CommonMethods.getAllExtensions().contains(extension)) {
                int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                attachmentHolder.ivImage.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
            } else {
                attachmentHolder.ivImage.setImageResource(R.drawable.ic_file);
            }
        }
        attachmentHolder.tvFileName.setText(filename);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setUpdateLastDelete(UpdateLastDelete mUpdateLastDelete) {
        this.mUpdateLastDelete = mUpdateLastDelete;
    }

    private void removeItem(int adapterPosition) {
        if (adapterPosition < data.size()) {
            data.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, data.size());
        }
        if (data.isEmpty() && mUpdateLastDelete != null) {
            mUpdateLastDelete.updateRemove();
        }
    }

    public interface UpdateLastDelete {
        void updateRemove();
    }

    class AttachmentHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        ImageView ivImage;

        AttachmentHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            ivImage = itemView.findViewById(R.id.ivImage);
            itemView.findViewById(R.id.ivRemove).setVisibility(showDelete ? View.VISIBLE : View.GONE);

            itemView.findViewById(R.id.ivRemove).setOnClickListener(v -> removeItem(getAdapterPosition()));
        }
    }

}
