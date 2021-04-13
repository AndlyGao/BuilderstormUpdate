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
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.builderstrom.user.views.dialogFragments.CompanyDocActionDialogFragment;
import com.builderstrom.user.views.dialogFragments.CompanyMoreInfoDialogFragment;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChildCompanyDocListAdapter extends RecyclerView.Adapter<ChildCompanyDocListAdapter.ChildDocHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<CompanyDocument> dataList;
    private PermissionUtils permUtils;
    private DownloadManager manager;
    private UpdateCompanyDocsListing callBack;

    ChildCompanyDocListAdapter(Context mContext, List<CompanyDocument> dataList, UpdateCompanyDocsListing callBack) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        permUtils = new PermissionUtils((BaseActivity) mContext);
        manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ChildDocHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ChildDocHolder(mLayoutInflater.inflate(R.layout.row_company_doc_main,
                viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ChildDocHolder viewHolder, int position) {
        CompanyDocument data = dataList.get(position);
        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.ivPreview.setVisibility(CommonMethods.isPreviewAvailable(
                data.getFilename()) ? View.VISIBLE : View.INVISIBLE);
        viewHolder.ivMore.setVisibility(((BaseActivity) mContext).isInternetAvailable()
                ? View.VISIBLE : View.INVISIBLE);
        if (data.getFilename() != null && !data.getFilename().isEmpty()) {
            viewHolder.ivFile.setImageResource(CommonMethods.getFileImageFromName(data.getFilename()));
        } else {
            viewHolder.ivFile.setImageResource(R.drawable.ic_pdf_grey);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ChildDocHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSubItems)
        TextView tvSubItems;
        @BindView(R.id.ivMore)
        ImageView ivMore;
        @BindView(R.id.ivPreview)
        ImageView ivPreview;
        @BindView(R.id.ivFile)
        ImageView ivFile;
        @BindView(R.id.ivSync)
        ImageView ivSync;
        @BindView(R.id.ivEditDigitalDoc)
        ImageView ivEditDigitalDoc;

        ChildDocHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvSubItems.setVisibility(View.GONE);
            ivSync.setVisibility(View.INVISIBLE);
            ivEditDigitalDoc.setVisibility(View.INVISIBLE);

        }

        @OnClick({R.id.ivPreview, R.id.ivInfo, R.id.ivMore})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivFile:
                    String fileName = dataList.get(getAdapterPosition()).getFilename();
                    if (fileName != null && !fileName.isEmpty()) {
                        if (permUtils.isPermissionGrantedForExtStorage()) {
                            if (((BaseActivity) mContext).isInternetAvailable()) {
                                if (!CommonMethods.isFileDownloaded(fileName)) {
                                    CommonMethods.download(manager, dataList.get(getAdapterPosition()).getFilelocation(), fileName, mContext, "");
                                } else {
                                    CommonMethods.displayToast(mContext, "File Already Downloaded");
                                }
                            }
                        } else {
                            permUtils.requestPermissionForExtStorage();
                        }
                    }

                    break;

                case R.id.ivPreview:
                    String fileLocation = dataList.get(getAdapterPosition()).getFilelocation();
                    if (fileLocation != null && !fileLocation.isEmpty()) {
                        if (CommonMethods.isImageUrl(fileLocation)) {
                            mContext.startActivity(new Intent(mContext, ImageFullScreenActivity.class)
                                    .putExtra(((BaseActivity) mContext).isInternetAvailable() ?
                                                    "imageUrl" : "filePath",
                                            ((BaseActivity) mContext).isInternetAvailable()
                                                    ? CommonMethods.decodeUrl(fileLocation) : fileLocation));
                        } else {
                            CommonMethods.displayToast(mContext, "Preview Not available");
                        }
                    } else {
                        CommonMethods.displayToast(mContext, "Preview Not available");
                    }
                    break;
                case R.id.ivInfo:
                    CompanyMoreInfoDialogFragment.newInstance(2, dataList.get(getAdapterPosition()))
                            .show(((BaseActivity) mContext).getSupportFragmentManager(), "more info");
                    break;
                case R.id.ivMore:
                    CompanyDocActionDialogFragment dialogFragment = CompanyDocActionDialogFragment
                            .newInstance(dataList.get(getAdapterPosition()), getAdapterPosition());
                    dialogFragment.setCallback(callBack);
                    dialogFragment.setActionCallback((action, companyDocument, sPosition) -> {
                        switch (action) {
                            case DataNames.ACTION_DOC_DELETE:
                                dataList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                break;
                            case DataNames.ACTION_DOC_TRACK:
                            case DataNames.ACTION_DOC_FAVORITE:
                            case DataNames.ACTION_DOC_COMMENT:
                                dataList.set(dataList.indexOf(
                                        dataList.get(sPosition == -1 ? getAdapterPosition() : sPosition)),
                                        companyDocument);
                                notifyDataSetChanged();
                                break;
                            default:
                                dataList.set(dataList.indexOf(dataList.get(getAdapterPosition())),
                                        companyDocument);
                                notifyDataSetChanged();
                                break;
                        }
                    });

                    dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(),
                            "more actions");


                    break;
            }

        }

    }

}