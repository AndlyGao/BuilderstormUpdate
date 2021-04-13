package com.builderstrom.user.views.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.PojoAttachment;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;
import java.util.List;


public class ChildAttachOnlineAdapter extends RecyclerView.Adapter<ChildAttachOnlineAdapter.AttachmentHolder> {

    private Context mContext;
    private List<PojoAttachment> attachments;
    private List<String> attachmentsList;
    private LayoutInflater mLayoutInflater;
    private DownloadManager downloadManager;
    private boolean isSimpleAttachment = false;

    public ChildAttachOnlineAdapter(Context mContext, List<PojoAttachment> attachments) {
        this.mContext = mContext;
        this.attachments = attachments;
        isSimpleAttachment = false;
        mLayoutInflater = LayoutInflater.from(mContext);
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public ChildAttachOnlineAdapter(Context mContext, List<String> attachmentsList, boolean isSimpleAttachment) {
        this.mContext = mContext;
        this.attachmentsList = attachmentsList;
        this.isSimpleAttachment = isSimpleAttachment;
        mLayoutInflater = LayoutInflater.from(mContext);
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @NonNull
    @Override
    public AttachmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AttachmentHolder(mLayoutInflater.inflate(R.layout.row_attachment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder attachmentHolder, int i) {
        if (attachments != null) {
            PojoAttachment attachment = attachments.get(i);
            if (attachment.getOriginal_name() != null && !attachment.getOriginal_name().isEmpty()) {
                String extension = attachment.getOriginal_name().substring(attachment.getOriginal_name().lastIndexOf("."));
                if (CommonMethods.isImageUrl(attachment.getOriginal_name())) {
                    try {
                        Glide.with(mContext).load(URLDecoder.decode(attachment.getUrl().isEmpty() ? attachment.getFile_url() : attachment.getUrl(), "UTF-8")).apply(RequestOptions.placeholderOf(R.drawable.ic_file)).into(attachmentHolder.ivFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (CommonMethods.getAllExtensions().contains(extension)) {
                        int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                        attachmentHolder.ivFile.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
                    } else {
                        attachmentHolder.ivFile.setImageResource(R.drawable.ic_file);
                    }
                }
                attachmentHolder.tvFileName.setText(attachment.getOriginal_name());
            }
        } else {
            Glide.with(mContext).load(attachmentsList.get(i)).apply(RequestOptions.placeholderOf(R.drawable.ic_file)).into(attachmentHolder.ivFile);
            attachmentHolder.tvFileName.setText(attachmentsList.get(i));
        }


    }

    private void imagePreview(PojoAttachment model, View v) {
        if (CommonMethods.isImageUrl(model.getOriginal_name() != null && !model.getOriginal_name().isEmpty() ? model.getOriginal_name() : model.getDisplay_name())) {
            if (!model.getUrl().isEmpty() || !model.getFile_url().isEmpty()) {
                Intent intent = new Intent(mContext, ImageFullScreenActivity.class);
                intent.putExtra("imageUrl", CommonMethods.decodeUrl(model.getUrl().isEmpty() ? model.getFile_url() : model.getUrl()));
                mContext.startActivity(intent);
            }
        } else {
            if (CommonMethods.downloadFile(mContext)) {
                try {
                    CommonMethods.download(downloadManager, model.getUrl().isEmpty() ? model.getFile_url() : model.getUrl(), model.getFile_name(), mContext, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        return attachments == null ? attachmentsList.size() : attachments.size();
    }

    private void imageSimplePreview(String imageUrl, View imageView) {
        if (CommonMethods.isNetworkAvailable(mContext)) {
            if (!imageUrl.isEmpty()) {
                Intent intent = new Intent(mContext, ImageFullScreenActivity.class);
                intent.putExtra("imageUrl", imageUrl);
                mContext.startActivity(intent);
            }

//            if (!imageLink.isEmpty()) {
//                Intent intent = new Intent(mContext, ImageFullScreenActivity.class);
//                intent.putExtra("imageUrl", imageLink);
//                if (CommonMethods.hasLollipop()) {
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity) mContext, imageView, "profile");
//                    mContext.startActivity(intent, options.toBundle());
//                } else {
//                    mContext.startActivity(intent);
//                }
//            }
        } else {
            ((BaseActivity) mContext).errorMessage(mContext.getResources().getString(R.string.no_internet), null, false);
        }
    }

    class AttachmentHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;
        private ImageView ivFile;


        AttachmentHolder(@NonNull View itemView) {
            super(itemView);
            ivFile = itemView.findViewById(R.id.ivImage);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            itemView.setOnClickListener(v -> {
                if (attachments != null) {
                    imagePreview(attachments.get(getAdapterPosition()), v);
                } else {
                    imageSimplePreview(attachmentsList.get(getAdapterPosition()), v);
                }
            });
            itemView.findViewById(R.id.ivRemove).setVisibility(View.GONE);
        }
    }
}
