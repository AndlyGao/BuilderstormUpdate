package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.DocsMODialogFragment;
import com.builderstrom.user.views.dialogFragments.ProjectMoreInfoDialogFragment;
import com.builderstrom.user.views.fragments.ProjectDocumentFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectDocumentAdapter extends RecyclerView.Adapter<ProjectDocumentAdapter.DocumentViewHolder> implements Filterable {
    public Callback callback;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PDocsDataModel> dataList;
    private List<PDocsDataModel> filteredList;
    private ItemFilter mFilter = new ItemFilter();
    private ProjectDocumentFragment fragment;
    private ProjectDocumentVM viewModel;

    public ProjectDocumentAdapter(ProjectDocumentFragment context, List<PDocsDataModel> dataList, ProjectDocumentVM viewModel, Callback callback) {
        this.fragment = context;
        this.mContext = context.getActivity();
        this.dataList = dataList;
        this.filteredList = dataList;
        this.callback = callback;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new DocumentViewHolder(mLayoutInflater.inflate(R.layout.item_document_online,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder viewHolder, int position) {
        PDocsDataModel listData = filteredList.get(position);
        if (listData.getUid() != 0 && CommonMethods.isNetworkAvailable(mContext)) {
            viewHolder.ivEditDoc.setVisibility(View.VISIBLE);
            viewHolder.ivDot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEditDoc.setVisibility(listData.isSynced() ? View.GONE : View.VISIBLE);
            viewHolder.ivDot.setVisibility(View.GONE);
        }

        viewHolder.ivSync.setImageResource((listData.getUid() == null || listData.getUid() == 0) ? R.drawable.ic_un_uploaded : (listData.isSynced() ? R.drawable.ic_refresh_not_found : R.drawable.ic_refresh));

        viewHolder.tvDocumentTitle.setText(listData.getTitle());
        if (listData.getOriginalName() != null && !listData.getOriginalName().isEmpty()) {
            viewHolder.ivPDF.setImageResource(CommonMethods.getFileImageFromName(listData.getOriginalName()));
        } else {
            viewHolder.ivPDF.setImageResource(R.drawable.ic_pdf_grey);
        }
        viewHolder.ivEditDigitalDoc.setVisibility(listData.getCustomDocumentId() != null && CommonMethods.isNetworkAvailable(mContext) ? (listData.getCustomDocumentId() > 0 ? View.VISIBLE : View.INVISIBLE) : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface Callback {
        void callEditFunction(PDocsDataModel pDocsDataModel);

        void callEditDigitalDoc(Integer pDocId, Integer template_id, Integer customDocId);
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDocumentTitle)
        TextView tvDocumentTitle;
        @BindView(R.id.ivPDF)
        ImageView ivPDF;
        @BindView(R.id.ivEditDoc)
        ImageView ivEditDoc;
        @BindView(R.id.ivEditDigitalDoc)
        ImageView ivEditDigitalDoc;
        @BindView(R.id.ivDot)
        ImageView ivDot;
        @BindView(R.id.ivSync)
        ImageView ivSync;

        DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivSync, R.id.ivPDF, R.id.ivPreview, R.id.ivEditDoc, R.id.ivEditDigitalDoc, R.id.ivMoreInfo, R.id.ivDot})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivSync:
                    if (!filteredList.isEmpty() && getAdapterPosition() < filteredList.size()) {
                        if (!filteredList.get(getAdapterPosition()).isSynced()) {
                            if (CommonMethods.downloadFile(mContext)) {
                                filteredList.get(getAdapterPosition()).setSynced(true);
                                viewModel.syncDocs(filteredList.get(getAdapterPosition()));
                            }
                        } else {
                            ((BaseActivity) mContext).errorMessage("Document Already synced", null, false);
                        }
                    }
                    break;
                case R.id.ivPDF:
                case R.id.ivPreview:
                    if (!filteredList.isEmpty() && getAdapterPosition() < filteredList.size())
                        viewModel.filePreview(filteredList.get(getAdapterPosition()).getUrlFile(), filteredList.get(getAdapterPosition()).getOriginalName());
                    break;

                case R.id.ivEditDoc:
                    if (fragment != null && !filteredList.isEmpty() && getAdapterPosition() < filteredList.size()) {
                        callback.callEditFunction(filteredList.get(getAdapterPosition()));
                    }
                    break;
                case R.id.ivEditDigitalDoc:
                    if (fragment != null && !filteredList.isEmpty() && getAdapterPosition() < filteredList.size()) {
                        if (filteredList.get(getAdapterPosition()).getCustomTemplateId() != null) {
                            callback.callEditDigitalDoc(
                                    filteredList.get(getAdapterPosition()).getUid(),
                                    filteredList.get(getAdapterPosition()).getCustomTemplateId(),
                                    filteredList.get(getAdapterPosition()).getCustomDocumentId()
                            );
                        } else {
                            CommonMethods.displayToast(mContext, "There is some error at server");
                        }
                    }
                    break;

                case R.id.ivMoreInfo:
                    if (!filteredList.isEmpty() && getAdapterPosition() < filteredList.size())
                        ProjectMoreInfoDialogFragment.newInstance(1, new Gson().toJson(filteredList.get(getAdapterPosition())))
                                .show(((BaseActivity) mContext).getSupportFragmentManager(), "more info");
//                    if (!filteredList.isEmpty() && getAdapterPosition() < filteredList.size())
//                        ProjectMoreInfoDialogFragment.newInstance(1, filteredList.get(getAdapterPosition()))
//                                .show(((BaseActivity) mContext).getSupportFragmentManager(), "more info");
                    break;
                case R.id.ivDot:
                    if (fragment != null && !filteredList.isEmpty() && getAdapterPosition() < filteredList.size()) {
                        DocsMODialogFragment moreOptions = DocsMODialogFragment.newInstance(
                                new Gson().toJson(filteredList.get(getAdapterPosition())));
                        moreOptions.setEditSuccessCallback(fragment);
                        moreOptions.show(((BaseActivity) mContext).getSupportFragmentManager(), "edit view");
                    }

//                    if (fragment != null && !filteredList.isEmpty() && getAdapterPosition() < filteredList.size()) {
//                        DocsMODialogFragment moreOptions = DocsMODialogFragment.newInstance(
//                                filteredList.get(getAdapterPosition()));
//                        moreOptions.setEditSuccessCallback(fragment);
//                        moreOptions.show(((BaseActivity) mContext).getSupportFragmentManager(), "edit view");
//                    }
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
            ArrayList<PDocsDataModel> nlist = new ArrayList<>();
            if (!filterString.equalsIgnoreCase("All Documents")) {
                String filterableString;
                for (int i = 0; i < dataList.size(); i++) {
                    filterableString = dataList.get(i).getTitle();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        nlist.add(dataList.get(i));
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
            filteredList = (ArrayList<PDocsDataModel>) results.values;
            notifyDataSetChanged();
        }
    }
}