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
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ProjectCustomData;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.PMViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PMMetaAdapter extends RecyclerView.Adapter<PMMetaAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProjectCustomData> dataList;
    private PMViewModel viewModel;
    private PermissionUtils permUtils;

    public PMMetaAdapter(Context mContext, List<ProjectCustomData> dataList, PMViewModel viewModel) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.viewModel = viewModel;
        permUtils = new PermissionUtils((BaseActivity) mContext);
    }

//    public PMMetaAdapter(Context mContext, List<List<ProjectCustomData>> dataList) {
//        this.mContext = mContext;
//        this.dataList = dataList;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_row_meta, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProjectCustomData model = dataList.get(position);
        int endText = model.getFieldLabel().length();
        if (model.getFieldType() != null) {
            if (model.getFieldType().equalsIgnoreCase("signature")) {
                holder.tvText.setText(CommonMethods.spannedText(String.format(model.getFieldLabel() + ":", ""), 0, endText + 1));
                GlideApp.with(mContext).load(RestClient.getBaseImageUrl() + CommonMethods.decodeUrl(model.getValue()))
                        .apply(new RequestOptions().override(180, 180)
                                .placeholder(mContext.getResources().getDrawable(R.drawable.mainlogo)).
                                        error(mContext.getResources().getDrawable(R.drawable.mainlogo)).
                                        centerCrop()).into(holder.ivPhoto);
                holder.ivPhoto.setVisibility(View.VISIBLE);
            } else if ((model.getFieldType().equalsIgnoreCase("file"))) {
                if (model.getValue() != null && !model.getValue().isEmpty() && model.getFileName() != null && !model.getFileName().isEmpty()) {
                    String url = RestClient.getBaseImageUrl() + model.getValue();
                    holder.tvText.setText(CommonMethods.coloredSpannedTextMultiples(String.format(model.getFieldLabel() + ": %s", model.getFileName()), endText, model.getFileName().length()));
                    holder.tvText.setOnClickListener(v -> {
//                        viewModel.filePreview(url, model.getFileName());
                        if (permUtils.isPermissionGrantedForExtStorage()) {
                            viewModel.downloadFile(url, model.getFileName(), true);
                        } else {
                            permUtils.requestPermissionForExtStorage();
                        }
                    });
                }

                holder.ivPhoto.setVisibility(View.GONE);
            } else {
                holder.tvText.setText(CommonMethods.spannedText(String.format(model.getFieldLabel() + ": %s", model.getValue()), 0, endText + 1));
                holder.ivPhoto.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
