package com.builderstrom.user.views.adapters;

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
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItem;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.DigitalDocumentVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.builderstrom.user.views.dialogFragments.IssueDocumentDialogFragment;
import com.builderstrom.user.views.dialogFragments.MyItemsStatusDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalDocMyItemsListAdapter extends RecyclerView.Adapter<DigitalDocMyItemsListAdapter.MyItemHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PojoMyItem> dataList;
    private DigitalDocumentVM viewModel;
    private Boolean isOffline = false;
    private String catSection = "", categoryId = "";

    public DigitalDocMyItemsListAdapter(Context mContext, List<PojoMyItem> dataList, DigitalDocumentVM viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
    }

    /* changes regarding recent doc on 7/1/2020*/
    public DigitalDocMyItemsListAdapter(Context mContext, List<PojoMyItem> dataList, DigitalDocumentVM viewModel, String catSection, String categoryId) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
        this.catSection = catSection;
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public MyItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyItemHolder(mLayoutInflater.inflate(R.layout.row_my_item_digi_doc, viewGroup,
                false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyItemHolder viewHolder, int position) {
        viewHolder.ivSync.setVisibility(isOffline ? View.INVISIBLE : View.VISIBLE);
        viewHolder.ivSync.setSelected(dataList.get(position).isSynced());
        viewHolder.tvTitle.setText(dataList.get(position).getTemplateTitle());
        viewHolder.tvCompletedFor.setText(CommonMethods.convertDate(CommonMethods.DF_2, dataList.get(position).getForCompleteDate(), CommonMethods.DF_8));
        viewHolder.tvIssuedBy.setText(dataList.get(position).getIssuedBy());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOffline(Boolean isOffline) {
        this.isOffline = isOffline;
        notifyDataSetChanged();
    }

    class MyItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCompletedFor)
        TextView tvCompletedFor;
        @BindView(R.id.tvIssuedBy)
        TextView tvIssuedBy;
        @BindView(R.id.ivSync)
        ImageView ivSync;

        MyItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivSync, R.id.ivPreview, R.id.ivStatusInfo, R.id.btnComplete, R.id.btnIssue})
        public void onclick(View view) {
            PojoMyItem item = dataList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.ivSync:
                    if (!item.isSynced()) {
                        item.setSynced(true);
                        viewModel.syncMyItemToDb(item,catSection,categoryId);
                    } else {
                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                "Document Already synced"));
                    }
                    break;
                case R.id.ivPreview:
                    CommonMethods.displayToast(mContext, "Preview document if allowed");
                    break;
                case R.id.ivStatusInfo:
                    MyItemsStatusDialogFragment.newInstance(item.getTemplateTitle(), item.getStatus())
                            .show(((BaseActivity) mContext).getSupportFragmentManager(), "info dialog");
                    break;
                case R.id.btnComplete:
                    mContext.startActivity(new Intent(mContext, DigitalFormActivity.class)
                            .putExtra("data", item));
                    break;
                case R.id.btnIssue:
                    if (mContext != null) {
                        IssueDocumentDialogFragment.newInstance(item)
                                .show(((BaseActivity) mContext).getSupportFragmentManager(), "issue doc");
                    }
                    break;
            }
        }

    }


}