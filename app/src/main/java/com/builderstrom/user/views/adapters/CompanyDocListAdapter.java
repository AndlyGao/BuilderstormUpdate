package com.builderstrom.user.views.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.CompanyDocActionDialogFragment;
import com.builderstrom.user.views.dialogFragments.CompanyMoreInfoDialogFragment;
import com.builderstrom.user.views.fragments.CompanyDocListFragment;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyDocListAdapter extends RecyclerView.Adapter<CompanyDocListAdapter.CompanyDocHolder> implements Filterable {

    private Context mContext;
    private CompanyDocListFragment fragment;
    private LayoutInflater mLayoutInflater;
    private List<CompanyDocument> dataList;
    private List<CompanyDocument> filteredList;
    private ItemFilter mFilter = new ItemFilter();
    private PermissionUtils permUtils;
    private CompanyViewModel viewModel;
    private UpdateCompanyDocsListing updateCallBack;
    private Callback mCallback;


    public CompanyDocListAdapter(CompanyDocListFragment context, List<CompanyDocument> dataList,
                                 UpdateCompanyDocsListing callBack, CompanyViewModel viewModel, Callback mCallback) {
        this.fragment = context;
        this.mContext = context.getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
        filteredList = dataList;
        this.dataList = dataList;
        this.viewModel = viewModel;
        permUtils = new PermissionUtils((BaseActivity) mContext);
        this.updateCallBack = callBack;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public CompanyDocHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CompanyDocHolder(mLayoutInflater.inflate(R.layout.row_company_doc_main, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull CompanyDocHolder viewHolder, int position) {
        CompanyDocument data = filteredList.get(position);
        viewHolder.llContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.status_bar));
        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.ivMore.setVisibility(((BaseActivity) mContext).isInternetAvailable()
                ? View.VISIBLE : View.INVISIBLE);
        viewHolder.ivPreview.setVisibility((!data.getFilename().isEmpty() && CommonMethods.isPreviewAvailable(
                data.getFilename())) ? View.VISIBLE : View.INVISIBLE);

        if (data.getFilename() != null && !data.getFilename().isEmpty()) {
            viewHolder.ivFile.setImageResource(CommonMethods.getFileImageFromName(data.getFilename()));
        } else {
            viewHolder.ivFile.setImageResource(R.drawable.ic_pdf_grey);
        }

        if (viewModel.isInternetAvailable() && data.getChilddocs() != null) {
            viewHolder.tvSubItems.setText(String.valueOf(data.getChilddocs().size()));
            viewHolder.rvChildDoc.setAdapter(new ChildCompanyDocListAdapter(mContext, data.getChilddocs(), updateCallBack));
        }
        viewHolder.tvSubItems.setVisibility((data.getChilddocs() != null && !data.getChilddocs().isEmpty()) ? ((CommonMethods.isNetworkAvailable(mContext)) ? View.VISIBLE : View.INVISIBLE) : View.INVISIBLE);

        if (viewModel.isInternetAvailable()) {
            viewHolder.ivSync.setImageResource(R.drawable.selector_sync);
            viewHolder.ivSync.setSelected(data.isDocSynced());
        } else {
            if (isOfflineEntry(data.getId())) {
                viewHolder.ivSync.setImageResource(R.drawable.ic_un_uploaded);
            } else {
                viewHolder.ivSync.setImageResource(R.drawable.selector_sync);
                viewHolder.ivSync.setSelected(true);
            }
        }

        if (data.getCustomDocumentId() != null && CommonMethods.isNetworkAvailable(mContext)) {
            viewHolder.ivEditDigitalDoc.setVisibility(data.getCustomDocumentId() > 0 ? View.VISIBLE : View.INVISIBLE);
        }

    }

    private boolean isOfflineEntry(String id) {
        return id == null || id.isEmpty() || id.equals("0");

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class CompanyDocHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSubItems)
        TextView tvSubItems;
        @BindView(R.id.rvChildDoc)
        RecyclerView rvChildDoc;
        @BindView(R.id.ivFile)
        ImageView ivFile;
        @BindView(R.id.ivPreview)
        ImageView ivPreview;
        @BindView(R.id.ivMore)
        ImageView ivMore;
        @BindView(R.id.ivSync)
        ImageView ivSync;
        @BindView(R.id.ivEditDigitalDoc)
        ImageView ivEditDigitalDoc;
        @BindView(R.id.linearLayout)
        LinearLayout llContainer;

        CompanyDocHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivSync, R.id.ivFile, R.id.ivPreview, R.id.ivInfo, R.id.ivMore, R.id.tvSubItems, R.id.ivEditDigitalDoc,})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivSync:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        viewModel.syncCompanyDocument(filteredList.get(getAdapterPosition()));
                    }

                    break;
                case R.id.ivFile:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        String fileName = filteredList.get(getAdapterPosition()).getFilename();
                        if (fileName != null && !fileName.isEmpty()) {
                            if (permUtils.isPermissionGrantedForExtStorage()) {
                                viewModel.downloadFile(filteredList.get(getAdapterPosition()).getFilelocation(),
                                        fileName, true);
                            } else {
                                permUtils.requestPermissionForExtStorage();
                            }
                        }
                    }
                    break;
                case R.id.ivPreview:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        viewModel.filePreview(filteredList.get(getAdapterPosition()).getFilelocation(),
                                filteredList.get(getAdapterPosition()).getFilename());
                    }
                    break;
                case R.id.ivInfo:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        CompanyMoreInfoDialogFragment.newInstance(1, filteredList.get(getAdapterPosition()))
                                .show(((BaseActivity) mContext).getSupportFragmentManager(), "more info");
                    }
                    break;
                case R.id.ivMore:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        CompanyDocActionDialogFragment dialogFragment = CompanyDocActionDialogFragment
                                .newInstance(filteredList.get(getAdapterPosition()), getAdapterPosition());
                        dialogFragment.setCallback(updateCallBack);
                        dialogFragment.setActionCallback((action, companyDocument, sPosition) -> {
                            if (action == DataNames.ACTION_DOC_DELETE) {
                                filteredList.remove(companyDocument);
                                dataList.remove(companyDocument);
                            } else {
                                dataList.set(dataList.indexOf(
                                        filteredList.get(sPosition == -1 ? getAdapterPosition() : sPosition)),
                                        companyDocument);
                                filteredList.set(sPosition == -1 ? getAdapterPosition() : sPosition, companyDocument);
                            }
                            notifyDataSetChanged();
                        });
                        dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "more action");
                    }
                    break;
                case R.id.tvSubItems:
                    if (null != filteredList && getAdapterPosition() < filteredList.size()) {
                        tvSubItems.setCompoundDrawablesWithIntrinsicBounds(rvChildDoc.getVisibility() == View.GONE ? R.drawable.ic_minus : R.drawable.ic_add, 0, 0, 0);
                        rvChildDoc.setVisibility(rvChildDoc.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                    }
                    break;

                case R.id.ivEditDigitalDoc:
                    if (fragment != null) {
                        if (filteredList.get(getAdapterPosition()).getCustomTemplateId() != null) {
                            mCallback.callEditDigitalDoc(
                                    filteredList.get(getAdapterPosition()).getId(),
                                    filteredList.get(getAdapterPosition()).getCustomTemplateId(),
                                    filteredList.get(getAdapterPosition()).getCustomDocumentId()
                            );
                        } else {
                            CommonMethods.displayToast(mContext, "There is some error at server");
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Filter original list according to select item in document dropdown
     */
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            List<CompanyDocument> nlist = new ArrayList<>();
            if (!filterString.equalsIgnoreCase("All Documents")) {
                for (CompanyDocument document : dataList) {
                    if (document.getTitle().toLowerCase().contains(filterString)) {
                        nlist.add(document);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            } else {
                results.values = dataList;
                results.count = dataList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<CompanyDocument>) results.values;
            notifyDataSetChanged();
        }
    }

    public interface Callback {
        void callEditFunction(PDocsDataModel pDocsDataModel);

        void callEditDigitalDoc(String pDocId, String template_id, Integer customDocId);
    }

}