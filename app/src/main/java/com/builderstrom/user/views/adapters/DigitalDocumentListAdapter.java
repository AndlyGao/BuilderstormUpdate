package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.builderstrom.user.views.customViews.toolTip.Tooltip;
import com.builderstrom.user.repository.retrofit.modals.DigitalDoc;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalDocumentListAdapter extends RecyclerView.Adapter<DigitalDocumentListAdapter.MyViewHolder> {
    private Context context;
    private List<DigitalDoc> templateList;
    private boolean isOffline = false;
    private SyncDocument syncDocument;

    public DigitalDocumentListAdapter(Context context, List<DigitalDoc> templateList) {
        this.context = context;
        this.templateList = templateList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_document, viewGroup, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.ivSync.setVisibility(isOffline ? View.GONE : View.VISIBLE);
        myViewHolder.ivSync.setSelected(templateList.get(position).getSynced());
        myViewHolder.tvTemplate.setText(templateList.get(position).getTemplateTitle());
    }

    @Override
    public int getItemCount() {
        return templateList.size();
    }

    public void setSyncDocument(SyncDocument syncDocument) {
        this.syncDocument = syncDocument;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTemplate) TextView tvTemplate;
        @BindView(R.id.ivSync) ImageView ivSync;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivSync, R.id.tvTemplate, R.id.btnCreateDocument})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivSync:
                    if (syncDocument != null) {
                        if (!templateList.get(getAdapterPosition()).getSynced()) {
                            templateList.get(getAdapterPosition()).setSynced(true);
                            syncDocument.syncDoc(templateList.get(getAdapterPosition()).getTemplateId(), getAdapterPosition());
                        } else {
                            ((BaseActivity) context).errorMessage("Document Already synced", null, false);
                        }
                    }
                    break;
                case R.id.tvTemplate:
                    new Tooltip.Builder(view)
                            .setText(templateList.get(getAdapterPosition()).getTemplateTitle())
                            .setBackgroundColor(ContextCompat.getColor(context, R.color.theme_color))
                            .setCornerRadius(10f)
                            .setTextColor(ContextCompat.getColor(context, android.R.color.white))
                            .show();
                    break;
                case R.id.btnCreateDocument:
                    context.startActivity(new Intent(context, DigitalFormActivity.class)
                            .putExtra("TemplateId", templateList.get(getAdapterPosition()).getTemplateId())
                            .putExtra("rec_type", templateList.get(getAdapterPosition()).getRecurrence_type()));
                    break;
            }
        }
    }

    public interface SyncDocument {
        void syncDoc(String templateId, int position);
    }

    void updateAdapter(int position) {
        if (position != -1) {
            templateList.get(position).setSynced(true);
            notifyItemChanged(position);
        }
    }

    public void setOffline(Boolean isOffline) {
        this.isOffline = isOffline;
        notifyDataSetChanged();
    }


}
