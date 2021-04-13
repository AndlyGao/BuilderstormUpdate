package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.Snag;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.SnagViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.ShareDialogFragment;
import com.builderstrom.user.views.dialogFragments.SnagCommentsDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SnagOnLineAdapter extends RecyclerView.Adapter<SnagOnLineAdapter.SnagHolder> {
    private Context mContext;
    private List<Snag> snagList;
    private LayoutInflater mLayoutInflater;
    private SnagViewModel viewModel;

    public SnagOnLineAdapter(Context mContext, List<Snag> snagList, SnagViewModel viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.snagList = snagList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SnagHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SnagHolder(mLayoutInflater.inflate(R.layout.row_snag, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SnagHolder snagHolder, int i) {
        Snag model = snagList.get(i);
        snagHolder.tvHeadTitle.setText(model.getDefect_details());
        snagHolder.llHidden.setVisibility(model.isSelected() ? View.VISIBLE : View.GONE);
        snagHolder.tvHeadIssueDate.setText(CommonMethods.convertDate(CommonMethods.DF_1, model.getCreated_on(), CommonMethods.DF_5));
        snagHolder.tvRequestedFrom.setText(CommonMethods.spannedText(String.format("Requested From: %s", model.getRequestedFrom()), 0, 15));
        snagHolder.tvTitle.setText(CommonMethods.spannedText(String.format("Title: %s", model.getDefect_details()), 0, 7));
        snagHolder.tvIssueDate.setText(CommonMethods.spannedText(String.format("Date Issued: %s", CommonMethods.convertDate(CommonMethods.DF_1, model.getCreated_on(), CommonMethods.DF_5)), 0, 12));
        snagHolder.tvDueDate.setText(CommonMethods.spannedText(String.format("Due Date: %s", CommonMethods.convertDate(CommonMethods.DF_1, model.getDueDate(), CommonMethods.DF_5)), 0, 9));
        snagHolder.tvLocation.setText(CommonMethods.spannedText(String.format("Location: %s", model.getLocationId()), 0, 10));
//        snagHolder.llMarkAs.setVisibility(model.getCanMark() != null && model.getCanMark() == 1 /* && model.getMark_as() != null*/ && !model.getMark_as().isEmpty() ? View.VISIBLE : View.INVISIBLE);
        snagHolder.llMarkAs.setVisibility(model.getCanMark() != null && model.getCanMark() == 1 && model.getMark_as() != null && !model.getMark_as().isEmpty() ? View.VISIBLE : View.INVISIBLE);
//        snagHolder.ivStatus.setImageResource(setStatusLock(model.getShow_status()));
        snagHolder.ivStatus.setImageResource(setStatusLock(model.getShow_status() == null ? 6 : model.getShow_status()));

        snagHolder.btnAddComment.setVisibility(model.getCanComment() != null && model.getCanComment() == 1 ? View.VISIBLE : View.GONE);
        snagHolder.ivFiles.setVisibility(model.getAttachments() == null || model.getAttachments().isEmpty() ? View.INVISIBLE : View.VISIBLE);
        snagHolder.ivSync.setImageResource(model.isSynced() ? R.drawable.ic_refresh_not_found : R.drawable.ic_refresh);

        /* Mark as locks*/

        if (snagHolder.llMarkAs.getVisibility() == View.VISIBLE) {
            snagHolder.ivMarkAs2.setVisibility(model.getMark_as().size() == 2 ? View.VISIBLE : View.GONE);
            snagHolder.ivMarkAs.setVisibility(model.getMark_as().get(0) != null && model.getMark_as().get(0) != 2 ? View.VISIBLE : View.GONE);
            if (snagHolder.ivMarkAs.getVisibility() == View.VISIBLE) {
                snagHolder.ivMarkAs.setSelected(model.getMark_as().get(0) == 1);
            }
        }

        /* Users Modified field*/
        if (isDistributed(model.getToUsers(), model.getCcUsers())) {
            snagHolder.tvDistribution.setText(CommonMethods.spannedText(String.format("Distribution: %s", ""), 0, 13));
            snagHolder.llMoreUsers.setVisibility(View.VISIBLE);
            snagHolder.tvToUsers.setVisibility(model.getToUsers().isEmpty() ? View.GONE : View.VISIBLE);
            snagHolder.tvCcUsers.setVisibility(model.getCcUsers().isEmpty() ? View.GONE : View.VISIBLE);
            if (!model.getToUsers().isEmpty()) {
                snagHolder.tvToUsers.setText(CommonMethods.spannedText(String.format("To User(s) : %s", CommonMethods.getCommaSeparatedString(model.getToUsers())), 0, 12));
            }

            if (!model.getCcUsers().isEmpty()) {
                snagHolder.tvCcUsers.setText(CommonMethods.spannedText(String.format("CC User(s) : %s", CommonMethods.getCommaSeparatedString(model.getCcUsers())), 0, 12));
            }

        } else {
            snagHolder.llMoreUsers.setVisibility(View.GONE);
            snagHolder.tvDistribution.setText(CommonMethods.spannedText(String.format("Distribution: %s", "None"), 0, 14));
        }
        /* Attachments*/
        snagHolder.llFiles.setVisibility(model.getAttachments() == null || model.getAttachments().isEmpty() ? View.GONE : View.VISIBLE);
        if (model.getAttachments() != null) {
            snagHolder.rvFiles.setAdapter(new ChildAttachOnlineAdapter(mContext, model.getAttachments()));
        }
    }

    private boolean isDistributed(List<User> toUsers, List<User> ccUsers) {
        return toUsers != null && !toUsers.isEmpty() || null != ccUsers && !ccUsers.isEmpty();
    }

    private int setStatusLock(Integer status) {
        return status == 0 ? R.drawable.ic_padunlock_gold :
                (status == 1 ? R.drawable.ic_padunlock_orange :
                        (status == 2 ? R.drawable.ic_padlock_green :
                                (status == 3 ? R.drawable.ic_padlock_black :
                                        (status == 4 ? R.drawable.ic_padunlock_red :
                                                R.drawable.ic_padlock_red))));
    }

    @Override
    public int getItemCount() {
        return snagList.size();
    }

    class SnagHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvHeadTitle) TextView tvHeadTitle;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvLocation) TextView tvLocation;
        @BindView(R.id.tvHeadIssueDate) TextView tvHeadIssueDate;
        @BindView(R.id.tvIssueDate) TextView tvIssueDate;
        @BindView(R.id.tvDuedate) TextView tvDueDate;
        @BindView(R.id.tvDistribution) TextView tvDistribution;
        @BindView(R.id.tvToUsers) TextView tvToUsers;
        @BindView(R.id.tvCcUsers) TextView tvCcUsers;
        @BindView(R.id.tvRequestedFrom) TextView tvRequestedFrom;

        @BindView(R.id.llHidden) LinearLayout llHidden;
        @BindView(R.id.llMoreUsers) LinearLayout llMoreUsers;
        @BindView(R.id.llFiles) LinearLayout llFiles;
        @BindView(R.id.llMarkAs) LinearLayout llMarkAs;

        @BindView(R.id.ivSync) ImageView ivSync;
        @BindView(R.id.ivStatus) ImageView ivStatus;
        @BindView(R.id.ivFiles) ImageView ivFiles;
        @BindView(R.id.ivMarkAs) ImageView ivMarkAs;
        @BindView(R.id.ivMarkAs2) ImageView ivMarkAs2;

        @BindView(R.id.btnAddComment) AppCompatButton btnAddComment;
        @BindView(R.id.rvFiles) RecyclerView rvFiles;

        private SnagHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.llTop, R.id.ivSync, R.id.ivShare, R.id.btnAddComment, R.id.ivMarkAs,
                R.id.ivMarkAs2})
        public void onClick(View view) {
            Snag snag = snagList.get(getAdapterPosition());


            switch (view.getId()) {
                case R.id.llTop:
                    snagList.get(getAdapterPosition()).setSelected(!snagList.get(getAdapterPosition()).isSelected());
                    notifyItemChanged(getAdapterPosition());
                    break;
                case R.id.ivSync:
                    if (!viewModel.isAlreadyScheduleSnagJob()) {
                        if (CommonMethods.downloadFile(mContext)) {
                            viewModel.syncSnagToDataBase(snag);
                        }
                    } else {
                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Snags syncing is already in progress"));
                    }
                    break;
                case R.id.ivShare:
                    Snag model = snagList.get(getAdapterPosition());
                    ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(model.getId(), model.getDefect_details(),
                            model.getToUsers() == null || model.getToUsers().isEmpty() ? "" : CommonMethods.getCommaSeparatedString(model.getToUsers()),
                            model.getCcUsers() == null || model.getCcUsers().isEmpty() ? "" : CommonMethods.getCommaSeparatedString(model.getCcUsers()),
                            DataNames.SHARE_SNAG);
                    dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "share Snag");
                    break;
                case R.id.btnAddComment:
                    SnagCommentsDialogFragment.newInstance(snagList.get(getAdapterPosition()).getId())
                            .show(((BaseActivity) mContext).getSupportFragmentManager(), "add Comment");
                    break;

                case R.id.ivMarkAs:
                    List<Integer> statusArray = snagList.get(getAdapterPosition()).getMark_as();
                    showMarkDialog(getAdapterPosition(), statusArray.get(0) == 1 ? 2 : 0, statusArray.get(0) == 1 ? "for closure" : "Closed");
                    break;
                case R.id.ivMarkAs2:
                    showMarkDialog(getAdapterPosition(), 1, "Reopen");
                    break;
            }
        }
    }

    private void showMarkDialog(int adapterPosition, Integer markPosition, String status) {
        new AlertDialog.Builder(mContext)
                .setMessage(String.format("Are you sure you want to mark this issue as %s %s", status, "?"))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    viewModel.updateSnagMark(snagList.get(adapterPosition).getId(), markPosition);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .create().show();
    }

}
