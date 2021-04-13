package com.builderstrom.user.views.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DiaryData;
import com.builderstrom.user.repository.retrofit.modals.DiaryManLabour;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.views.activities.AddDiary;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.builderstrom.user.views.adapters.metaDataAdapters.MDDescOfflineListAdapter;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiaryOfflineAdapter extends RecyclerView.Adapter<DiaryOfflineAdapter.MyViewHolder> {

    public Context context;
    private boolean clicked = false;
    private List<DiaryData> diaryModelList;
    private String selectedPosition = "";
    private LayoutInflater mLayoutInflater;


    public DiaryOfflineAdapter(Context context, List<DiaryData> diaryModelList) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.diaryModelList = diaryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.item_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DiaryData model = diaryModelList.get(position);
        holder.tvUser.setText(model.getUsername());
        holder.tvDiary.setText(model.getTitle());
        holder.tvSubmitDate.setText(String.format("%s %s", model.getCreatedOn(), model.getTime()));
        holder.tvTitle.setText(model.getTitle());
        holder.tvDescription.setText(model.getDescription());
        holder.rvMetadata.setVisibility((model.getLocalMetaData() != null
                && !model.getLocalMetaData().isEmpty()) ? View.VISIBLE : View.GONE);
        holder.rvMetadata.setAdapter(new MDDescOfflineListAdapter(context, model.getLocalMetaData()));

        holder.ivAttached.setVisibility(model.getAttachments() != null &&
                !model.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);
        holder.rvDiaryChild.setVisibility(model.getAttachments() != null &&
                !model.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);

        if (model.getAttachments() != null && !model.getAttachments().isEmpty()) {
            holder.rvDiaryChild.setAdapter(new DiaryOfflineChildAdapter(context, model.getAttachments(),
                    (imageUrl, imageView) -> {
                        if (!imageUrl.isEmpty()) {
                            Intent intent = new Intent(context, ImageFullScreenActivity.class);
                            intent.putExtra("filePath", imageUrl);
                            context.startActivity(intent);
                        }
                    }));

        }


        holder.relativeLayoutItemParent.setOnClickListener(view -> {
            if (clicked) {
                holder.relativeLayoutItemChild.setVisibility(View.GONE);
                clicked = false;
            } else {
                holder.relativeLayoutItemChild.setVisibility(View.VISIBLE);
                clicked = true;
            }
        });

        holder.ivEdit.setOnClickListener(view -> {
            Intent intentDiary = new Intent(context, AddDiary.class);
            intentDiary.putExtra("DiaryData", new Gson().toJson(model));
            context.startActivity(intentDiary);
        });
        holder.ivRefresh.setVisibility(View.VISIBLE);

        holder.llSiteLabour.setVisibility(model.getDiaryManHours() == null
                || model.getDiaryManHours().isEmpty() ? View.GONE : View.VISIBLE);

        if (holder.llSiteLabour.getVisibility() == View.VISIBLE) {
            holder.rvSiteLabour.setAdapter(new LaborListViewAdapter(context, model.getDiaryManHours()));
            holder.tvWorkHours.setText(String.format(Locale.getDefault(), "%s Work Hours", calculateWorkHours(model.getDiaryManHours())));
            holder.tvPWorkHours.setText(String.format(Locale.getDefault(), "Project Work Hours - %s ", "Not available"));
        }

    }

    @Override
    public int getItemCount() {
        return diaryModelList.size();
    }

    private String calculateWorkHours(List<DiaryManLabour> diaryManHours) {
        float sum = 0;
        try {
            for (DiaryManLabour manLabour : diaryManHours) {
                float swh = Float.parseFloat(manLabour.getSwh().trim());
                float wh = Float.parseFloat(manLabour.getWorkHours().trim());
//                int swh = Integer.parseInt(manLabour.getSwh().trim());
//                int wh = Integer.parseInt(manLabour.getWorkHours().trim());
                sum = sum + (swh * wh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(sum);
    }


    private void dialogDeleteDiary(String title, int rowID) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_gallery_operations);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(String.format("Do you want to delete the '%s' diary item?", title));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            BuilderStormApplication.mLocalDatabase.deleteDiary(rowID);
            diaryModelList.remove(Integer.parseInt(selectedPosition));
            selectedPosition = "";
            notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUser) TextView tvUser;
        @BindView(R.id.tvDiary) TextView tvDiary;
        @BindView(R.id.tvSubmitDate) TextView tvSubmitDate;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.tvWorkHours) TextView tvWorkHours;
        @BindView(R.id.tvPWorkHours) TextView tvPWorkHours;
        @BindView(R.id.ivAttached) ImageView ivAttached;
        @BindView(R.id.ivEdit) ImageView ivEdit;
        @BindView(R.id.rvDiaryChild) RecyclerView rvDiaryChild;
        @BindView(R.id.rvMetaData) RecyclerView rvMetadata;
        @BindView(R.id.rvLabor) RecyclerView rvSiteLabour;
        @BindView(R.id.relativeLayoutItemParent) RelativeLayout relativeLayoutItemParent;
        @BindView(R.id.relativeLayoutItemChild) LinearLayout relativeLayoutItemChild;
        @BindView(R.id.ivRefresh) ImageView ivRefresh;
        @BindView(R.id.llSiteLabour) LinearLayout llSiteLabour;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            ivEdit.setImageDrawable(context.getDrawable(R.drawable.ic_edit_disable));
        }

        @OnClick(R.id.ivDelete)
        public void onClick() {
            selectedPosition = String.valueOf(getAdapterPosition());
            dialogDeleteDiary(diaryModelList.get(getAdapterPosition()).getTitle(),
                    diaryModelList.get(getAdapterPosition()).getId());
        }

    }
}


