package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.ProjectCustomData;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.PMViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.NotesDialogFragment;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PMAdapter extends RecyclerView.Adapter<PMAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProjectDetails> projectList;
    private PMViewModel viewModel;

    public PMAdapter(Context mContext, List<ProjectDetails> projectList, PMViewModel viewModel) {
        this.mContext = mContext;
        this.projectList = projectList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProjectDetails model = projectList.get(position);
        holder.tvProject_id.setText(model.getProject_id());
        holder.tvTitle.setText(model.getTitle());
        holder.tvAddress.setText(model.getAddress());
        holder.tvStatus.setText(model.getStatus());
        GlideApp.with(mContext).load("https://dev.builderstorm.com/uploads/images/project-icon.png")
                .apply(new RequestOptions().override(180, 180)
                        .placeholder(mContext.getResources().getDrawable(R.drawable.ic_project)).
                                error(mContext.getResources().getDrawable(R.drawable.ic_project)).
                                centerCrop()).into(holder.ivProject);

        holder.llTop.setOnClickListener(v -> {
            if (model.isOpened()) {
                model.setOpened(false);
                holder.llHidden.setVisibility(View.GONE);
            } else {
                model.setOpened(true);
                holder.llHidden.setVisibility(View.VISIBLE);
            }

        });

        holder.btnNotes.setOnClickListener(v -> {
            if (((BaseActivity) mContext).getSupportFragmentManager() != null) {
                NotesDialogFragment.newInstance(model.getUid()).show(((BaseActivity) mContext).getSupportFragmentManager(), "Project Management");
            }
        });

        holder.tvProjectID.setText(CommonMethods.spannedText(String.format("Project ID: %s", model.getProject_id()), 0, 11));
        holder.tvProject.setText(CommonMethods.spannedText(String.format("Project Title: %s", model.getTitle()), 0, 14));
        holder.tvProjectAddress.setText(CommonMethods.spannedText(String.format("Project Address: %s", model.getAddress()), 0, 16));
        holder.tvZip.setText(CommonMethods.spannedText(String.format("Post Code: %s", model.getZip()), 0, 10));
        holder.tvContact.setText(CommonMethods.spannedText(String.format("Contact: %s", model.getContact()), 0, 8));
        holder.tvStartDate.setText(CommonMethods.spannedText(String.format("Start Date: %s", CommonMethods.convertDate(CommonMethods.DF_2, model.getStartDate(), CommonMethods.DF_8)), 0, 12));
        holder.tvEndDate.setText(CommonMethods.spannedText(String.format("End Date: %s", CommonMethods.convertDate(CommonMethods.DF_2, model.getEndDate(), CommonMethods.DF_8)), 0, 9));
        holder.tvOrderNumber.setText(CommonMethods.spannedText(String.format("Order Number: %s", model.getProject_order_no()), 0, 12));
        holder.tvProjectStatus.setText(CommonMethods.spannedText(String.format("Status: %s", model.getStatus()), 0, 7));
        holder.tvStage.setText(CommonMethods.spannedText(String.format("Stage: %s", model.getStage()), 0, 6));
        holder.tvClient.setText(CommonMethods.spannedText(String.format("Client: %s", model.getClient()), 0, 7));


        if (model.getCustomFieldData() != null && !model.getCustomFieldData().isEmpty()) {
            List<ProjectCustomData> metaData = new ArrayList<>();
            for (int k = 0; k < model.getCustomFieldData().size(); k++) {
                for (int j = 0; j < model.getCustomFieldData().get(k).size(); j++) {
                    metaData.add(model.getCustomFieldData().get(k).get(j));
                }
            }

            holder.llMetaData.setVisibility(View.VISIBLE);
            holder.rvMetaData.setAdapter(new PMMetaAdapter(mContext, metaData, viewModel));
        } else {
            holder.llMetaData.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProject)
        ImageView ivProject;
        @BindView(R.id.tvProject_id)
        TextView tvProject_id;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.llTop)
        LinearLayout llTop;
        @BindView(R.id.llHidden)
        LinearLayout llHidden;
        @BindView(R.id.tvProjectID)
        TextView tvProjectID;
        @BindView(R.id.tvProject)
        TextView tvProject;
        @BindView(R.id.tvProjectAddress)
        TextView tvProjectAddress;
        @BindView(R.id.tvZip)
        TextView tvZip;
        @BindView(R.id.tvContact)
        TextView tvContact;
        @BindView(R.id.tvStartDate)
        TextView tvStartDate;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvOrderNumber)
        TextView tvOrderNumber;
        @BindView(R.id.tvProjectStatus)
        TextView tvProjectStatus;
        @BindView(R.id.tvStage)
        TextView tvStage;
        @BindView(R.id.tvClient)
        TextView tvClient;
        @BindView(R.id.btnNotes)
        AppCompatButton btnNotes;
        @BindView(R.id.llMetaData)
        LinearLayout llMetaData;
        @BindView(R.id.rvMetaData)
        RecyclerView rvMetaData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

//        @OnClick({R.id.llTop})
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.llTop:
//
//                    ProjectDetails details = projectList.get(getAdapterPosition());
//                    rfiModel.setOpened(!details.isOpened());
//                    notifyItemChanged(getAdapterPosition(), rfiModel);
//                    break;
//            }
//
//        }
    }
}
