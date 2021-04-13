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
import com.builderstrom.user.repository.retrofit.modals.AddAttachModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAttachmentAdapter extends RecyclerView.Adapter<AddAttachmentAdapter.MyViewHolder> {

    private List<AddAttachModel> fileModel;
    private Context context;


    public AddAttachmentAdapter(Context ctx, List<AddAttachModel> fileModel) {
        this.context = ctx;
        this.fileModel = fileModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_attachment, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AddAttachModel data = fileModel.get(position);
        if (data.getName() != null && data.getName() != null) {
            String extension = data.getName().substring(data.getName().lastIndexOf("."));
//            if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            if (CommonMethods.isImageUrl(data.getName())) {
                try {
                    if (data.getFileurl() == null || data.getFileurl().isEmpty()) {
                        holder.ivImage.setImageBitmap(data.getImageBitmap());
                    } else {
                        GlideApp.with(context).load(URLDecoder.decode(data.getFileurl()))
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_file))
                                .into(holder.ivImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (CommonMethods.getAllExtensions().contains(extension)) {
                    int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                    holder.ivImage.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
                } else {
                    holder.ivImage.setImageResource(R.drawable.ic_file);
                }
            }

            holder.tvFileName.setText(data.getName());

        }
    }

    @Override
    public int getItemCount() {
        return fileModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;
        //        @BindView(R.id.ivRemove) ImageView ivRemove;
        @BindView(R.id.tvFileName) TextView tvFileName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.ivRemove)
        public void onClick() {
            removePosition(getAdapterPosition());
        }
    }

    private void removePosition(int adapterPosition) {
        if (adapterPosition < fileModel.size()) {
            fileModel.remove(adapterPosition);
            notifyDataSetChanged();
        }
    }


}
