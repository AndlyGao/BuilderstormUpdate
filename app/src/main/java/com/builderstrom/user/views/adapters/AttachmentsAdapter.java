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
import com.builderstrom.user.data.retrofit.modals.Attachment;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.BaseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttachmentsAdapter extends RecyclerView.Adapter<AttachmentsAdapter.AttachmentHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Attachment> dataList;
    private boolean showDelete = false;
    private BaseViewModel viewModel;

    public AttachmentsAdapter(Context mContext, List<Attachment> dataList, BaseViewModel viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.showDelete = false;
        this.viewModel = viewModel;
    }

    public AttachmentsAdapter(Context mContext, List<Attachment> dataList, boolean showDelete, BaseViewModel viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.showDelete = showDelete;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AttachmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new AttachmentHolder(mLayoutInflater.inflate(R.layout.row_attachment, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder viewHolder, int position) {
        Attachment attachment = dataList.get(position);
        viewHolder.ivRemove.setVisibility(showDelete ? View.VISIBLE : View.INVISIBLE);
        viewHolder.tvFileName.setText(attachment.getFileName());
        if (CommonMethods.isImageUrl(attachment.getFileName())) {
            GlideApp.with(mContext).load(attachment.isOffline() ? attachment.getFileUrl() :
                    CommonMethods.decodeUrl(attachment.getFileUrl())).into(viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(CommonMethods.getFileImageFromName(attachment.getFileName()));
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class AttachmentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.ivRemove) ImageView ivRemove;
        @BindView(R.id.tvFileName) TextView tvFileName;

        AttachmentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivImage, R.id.tvFileName, R.id.ivRemove})
        public void onClick(View view) {
            Attachment attachment = dataList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.tvFileName:
                case R.id.ivImage:
                    if (null != viewModel) {
                        viewModel.filePreview(attachment.getFileUrl(), attachment.getFileName());
                    }
                    break;
                case R.id.ivRemove:
                    removeItem(getAdapterPosition());
                    break;
            }
        }

        private void removeItem(int adapterPosition) {
            if (adapterPosition < dataList.size()) {
                dataList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, dataList.size());
            }
//            if (data.isEmpty() && mUpdateLastDelete != null) {
//                mUpdateLastDelete.updateRemove();
//            }

        }
    }

}