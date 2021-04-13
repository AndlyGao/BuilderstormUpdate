package com.builderstrom.user.views.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DiaryData;
import com.builderstrom.user.repository.retrofit.modals.DiaryManLabour;
import com.builderstrom.user.repository.retrofit.modals.MetaValues;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.activities.AddDiary;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.builderstrom.user.views.customViews.toolTip.Tooltip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiaryOnlineAdapter extends RecyclerView.Adapter<DiaryOnlineAdapter.MyViewHolder> {

    public Context context;
    private DownloadManager manager;
    private boolean clicked = false;
    private List<DiaryData> diaryModelList;
    private CallbackDiary mCallbackDiary;

    public DiaryOnlineAdapter(Context context, List<DiaryData> diaryModelList, CallbackDiary mCallbackDiary) {
        this.context = context;
        this.diaryModelList = diaryModelList;
        this.mCallbackDiary = mCallbackDiary;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        DiaryData data = diaryModelList.get(position);
        holder.tvUser.setText(data.getUsername());
        holder.tvDiary.setText(data.getTitle());
        holder.tvSubmitDate.setText(CommonMethods.convertDate(CommonMethods.DF_1, data.getSubmittedOn(), CommonMethods.DF_6));
        holder.tvTitle.setText(data.getTitle());
        holder.tvDescription.setText(data.getDescription());

        holder.rvMetaData.setAdapter(new MDDescListAdapter(context, filterMetaDataList(data.getCustom_field_data())));

        holder.tvUser.setOnClickListener(view -> new Tooltip.Builder(holder.tvUser)
                .setText(data.getUsername())
                .setBackgroundColor(ContextCompat.getColor(context, R.color.theme_color))
                .setCornerRadius(10f)
                .setTextColor(ContextCompat.getColor(context, android.R.color.white))
                .show());
        holder.ivEdit.setOnClickListener(view -> {
            Intent intentDiary = new Intent(context, AddDiary.class);
            intentDiary.putExtra("DiaryData", new Gson().toJson(data));
            context.startActivity(intentDiary);
        });

        holder.ivDelete.setOnClickListener(view -> mCallbackDiary.deleteDiary(data.getTitle(), data.getUid()));

        holder.rvDiaryChild.setAdapter(new DiaryOnlineChildAdapter(context, data.getAttachments(), new DiaryOnlineChildAdapter.CallbackImage() {
            @Override
            public void openFullImage(String imageUrl, ImageView imageView) {
                if (CommonMethods.isNetworkAvailable(context)) {
                    if (!imageUrl.isEmpty()) {
                        Intent intent = new Intent(context, ImageFullScreenActivity.class);
                        intent.putExtra("imageUrl", CommonMethods.decodeUrl(imageUrl));
                        context.startActivity(intent);
                    }
                } else {
                    ((BaseActivity) context).errorMessage(context.getResources().getString(R.string.no_internet), null, false);
                }
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void downloadFile(String fileUrl, String fileName) {
                if (CommonMethods.downloadFile(context)) {
                    new AsyncTask<Void, Void, Long>() {
                        @Override
                        protected Long doInBackground(Void... voids) {
                            try {
                                CommonMethods.download(manager, fileUrl, fileName, context, "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Long aLong) {
                            super.onPostExecute(aLong);
                        }
                    }.execute();
                }
            }
        }));

        if (data.getAttachments().size() > 0) {
            holder.ivAttached.setVisibility(View.VISIBLE);
            holder.rvDiaryChild.setVisibility(View.VISIBLE);
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

        holder.llSiteLabour.setVisibility(data.getDiaryManHours() == null
                || data.getDiaryManHours().isEmpty() ? View.GONE : View.VISIBLE);

        if (holder.llSiteLabour.getVisibility() == View.VISIBLE) {
            holder.rvSiteLabour.setAdapter(new LaborListViewAdapter(context, data.getDiaryManHours()));
            holder.tvWorkHours.setText(String.format(Locale.getDefault(), "%s Work Hours", calculateWorkHours(data.getDiaryManHours())));
            holder.tvPWorkHours.setText(String.format(Locale.getDefault(), "%s Project Work Hours", "900"));
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

    private List<List<MetaValues>> filterMetaDataList(List<List<MetaValues>> fieldData) {
        List<List<MetaValues>> filteredList = new ArrayList<>();
        for (List<MetaValues> values : fieldData) {
            if (null != values.get(0).getValue() && !values.get(0).getValue().isEmpty()) {
                filteredList.add(values);
            }
        }

        return filteredList;
    }

    public interface CallbackDiary {
        void deleteDiary(String title, String diaryID);
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
        @BindView(R.id.ivDelete) ImageView ivDelete;
        @BindView(R.id.rvMetaData) RecyclerView rvMetaData;
        @BindView(R.id.rvDiaryChild) RecyclerView rvDiaryChild;
        @BindView(R.id.rvLabor) RecyclerView rvSiteLabour;
        @BindView(R.id.relativeLayoutItemParent) RelativeLayout relativeLayoutItemParent;
        @BindView(R.id.relativeLayoutItemChild) LinearLayout relativeLayoutItemChild;
        @BindView(R.id.llSiteLabour) LinearLayout llSiteLabour;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rvMetaData.setVisibility(View.VISIBLE);
        }
    }
}


