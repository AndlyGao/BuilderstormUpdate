package com.builderstrom.user.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.Rfi;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.RFIViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.AddCommentDialogFragment;
import com.builderstrom.user.views.dialogFragments.ShareDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RfiOnlineAdapter extends RecyclerView.Adapter<RfiOnlineAdapter.MyViewHolder> {

    private Context context;
    private List<Rfi> dataList;
    private CallbackRfi mCallbackRfi;
    private RFIViewModel viewModel;

    public RfiOnlineAdapter(Context context, List<Rfi> dataList, CallbackRfi mCallbackRfi, RFIViewModel viewModel) {
        this.context = context;
        this.dataList = dataList;
        this.mCallbackRfi = mCallbackRfi;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rfi, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Rfi model = dataList.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.tvDateCreatedRfi.setText(CommonMethods.convertDate(CommonMethods.DF_1, model.getCreatedOn(), CommonMethods.DF_6));
        holder.tvDueDateRfi.setText(CommonMethods.convertDate(CommonMethods.DF_1, model.getDuedate(), CommonMethods.DF_7));
        holder.tvTitleExpanded.setText(model.getTitle());
        holder.wvDescription.loadDataWithBaseURL(null, setHtmlFormattedText(model.getDescription()), "text/html", "utf-8", null);
        holder.wvDescription.setVisibility(model.getDescription() == null || model.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        holder.tvTitleRFI.setText(CommonMethods.spannedText(String.format("Request From: %s %s", model.getRequestBy(), model.getRequestByLastname()), 0, 14));
        holder.tvIssueDate.setText(CommonMethods.spannedText(String.format("Date Issued: %s", CommonMethods.convertDate(CommonMethods.DF_1, model.getCreatedOn(), CommonMethods.DF_3)), 0, 12));
        holder.tvduedate.setText(CommonMethods.spannedText(String.format("Due Date: %s", CommonMethods.convertDate(CommonMethods.DF_1, model.getDuedate(), CommonMethods.DF_3)), 0, 9));
        holder.ivSyncRfi.setImageResource(model.getSynced() ? R.drawable.ic_refresh_not_found : R.drawable.ic_refresh);

        /* Users Modified field*/
        if (isUserNotified(model.getTousers(), model.getCcusers())) {
            holder.tvToUsersModified.setText(CommonMethods.spannedText(String.format("User Notified: %s", ""), 0, 14));
            holder.llMoreUsers.setVisibility(View.VISIBLE);
            holder.tvToUsers.setVisibility(model.getTousers().isEmpty() ? View.GONE : View.VISIBLE);
            holder.tvCcUsers.setVisibility(model.getCcusers().isEmpty() ? View.GONE : View.VISIBLE);
            if (!model.getTousers().isEmpty()) {
                holder.tvToUsers.setText(CommonMethods.spannedText(String.format("To User(s) : %s", CommonMethods.getCommaSeparatedString(model.getTousers())), 0, 12));
            }

            if (!model.getCcusers().isEmpty()) {
                holder.tvCcUsers.setText(CommonMethods.spannedText(String.format("CC User(s) : %s", CommonMethods.getCommaSeparatedString(model.getCcusers())), 0, 12));
            }

        } else {
            holder.llMoreUsers.setVisibility(View.GONE);
            holder.tvToUsersModified.setText(CommonMethods.spannedText(String.format("User Notified: %s", "No User Notified"), 0, 14));
        }

        holder.llHidden.setVisibility(model.isOpened() ? View.VISIBLE : View.GONE);

        /* files online attachments */
        if (model.getAttachments() == null || model.getAttachments().isEmpty()) {
            holder.llFiles.setVisibility(View.GONE);
        } else {
            holder.llFiles.setVisibility(View.VISIBLE);
            holder.rvFiles.setAdapter(new ChildAttachOnlineAdapter(context, model.getAttachments()));
        }

        if (model.getShowStatus() != null) {
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.ivStatus.setImageResource(setStatusLock(model.getShowStatus()));
        } else {
            holder.ivStatus.setVisibility(View.INVISIBLE);
        }

        /* Answer View */
        holder.rvAnsAttachment.setVisibility(model.getAnswerAttachmentFiles() != null && !model.getAnswerAttachmentFiles().isEmpty() ? View.VISIBLE : View.GONE);
        /* answer upload files */
        if (model.getAnswerAttachmentFiles() != null && !model.getAnswerAttachmentFiles().isEmpty()) {
            holder.rvAnsAttachment.setAdapter(new ChildAttachOfflineAdapter(context, model.getAnswerAttachmentFiles()));
        }
        /*set text on etAnswer */
        holder.etAnswer.setText(model.getAnswerText() == null || model.getAnswerText().isEmpty() ? null : model.getAnswerText());

        holder.llAddAnswer.setVisibility(model.getCan_answer() && !model.getIsAnswered() ? View.VISIBLE : View.GONE);
        holder.llViewAnswer.setVisibility(model.getIsAnswered() ? View.VISIBLE : View.GONE);
        if (model.getIsAnswered()) {
            holder.tvAnswer.setText(model.getAnswer());
            holder.tvAnswerBy.setText(String.format("%s - %s", model.getAnswered_by(), CommonMethods.convertDate(CommonMethods.DF_1, model.getAnswered_on(), CommonMethods.DF_3)));
            holder.rvAnsFiles.setVisibility(model.getAnswerfiles() != null && !model.getAnswerfiles().isEmpty() ? View.VISIBLE : View.GONE);
            /* answer online files */
            if (model.getAnswerfiles() != null && !model.getAnswerfiles().isEmpty()) {
                holder.rvAnsFiles.setAdapter(new ChildAttachOnlineAdapter(context, model.getAnswerfiles()));
            }
        }

        if (model.getIsDraft()==1) {
            holder.llMain.setBackgroundColor(Color.parseColor("#FEF9E7"));
        } else {
            holder.llMain.setBackgroundColor(Color.parseColor("#eeeeee"));
        }

    }

    private boolean isUserNotified(List<User> toUsers, List<User> ccUsers) {
        return toUsers != null && !toUsers.isEmpty() || null != ccUsers && !ccUsers.isEmpty();
    }

    private String setHtmlFormattedText(String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            return Html.fromHtml(description).toString();
        }
    }

    private int setStatusLock(Integer status) {
        return status == 0 ? R.drawable.ic_padunlock_gold :
                (status == 1 ? R.drawable.ic_padlock_black :
                        (status == 2 ? R.drawable.ic_padunlock_orange :
                                (status == 3 ? R.drawable.ic_padlock_green :
                                        (status == 4 ? R.drawable.ic_padunlock_red :
                                                R.drawable.ic_padlock_red))));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CallbackRfi {

        void addAnswer(String answerString, int position);

        void initiateFilePicker(int position, String answerText);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDateCreatedRfi)
        TextView tvDateCreatedRfi;
        @BindView(R.id.tvDueDateRfi)
        TextView tvDueDateRfi;
        @BindView(R.id.tvTitleRFI)
        TextView tvTitleRFI;
        @BindView(R.id.tvIssueDate)
        TextView tvIssueDate;
        @BindView(R.id.tvduedate)
        TextView tvduedate;
        @BindView(R.id.tvFiles)
        TextView tvFiles;
        @BindView(R.id.tvToUsersModified)
        TextView tvToUsersModified;
        @BindView(R.id.tvTitleExpanded)
        TextView tvTitleExpanded;
        @BindView(R.id.tvAnswer)
        TextView tvAnswer;
        @BindView(R.id.tvAnswerBy)
        TextView tvAnswerBy;
        @BindView(R.id.tvToUsers)
        TextView tvToUsers;
        @BindView(R.id.tvCcUsers)
        TextView tvCcUsers;
        @BindView(R.id.wvDescription)
        WebView wvDescription;
        @BindView(R.id.etAnswer)
        EditText etAnswer;
        @BindView(R.id.ivSync)
        ImageView ivSyncRfi;
        @BindView(R.id.ivStatus)
        ImageView ivStatus;
        @BindView(R.id.llHidden)
        LinearLayout llHidden;
        @BindView(R.id.llFiles)
        LinearLayout llFiles;
        @BindView(R.id.llAddAnswer)
        LinearLayout llAddAnswer;
        @BindView(R.id.llViewAnswer)
        LinearLayout llViewAnswer;
        @BindView(R.id.llMoreUsers)
        LinearLayout llMoreUsers;
        @BindView(R.id.llMain)
        LinearLayout llMain;
        @BindView(R.id.rvAnsAttachment)
        RecyclerView rvAnsAttachment;
        @BindView(R.id.rvFiles)
        RecyclerView rvFiles;
        @BindView(R.id.rvAnsFiles)
        RecyclerView rvAnsFiles;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            setupWebView();
        }

        @SuppressLint("SetJavaScriptEnabled")
        private void setupWebView() {
            if (null != wvDescription) {
                wvDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRfiDescriptionBack));
                wvDescription.loadUrl("javascript:(function() { " + "document.querySelector('[role=\"toolbar\"]').remove();})()");
                WebSettings webSettings = wvDescription.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
            }
        }

        @OnClick({R.id.ivSync, R.id.btnAddAnswer, R.id.btnAddCommentRfi, R.id.ivShare,
                R.id.btnAnsAttachment, R.id.llTop})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivSync:
                    if (!viewModel.isAlreadyScheduleRfiJob()) {
                        if (!dataList.get(getAdapterPosition()).getSynced()) {
                            if (CommonMethods.downloadFile(context)) {
                                syncRfi(getAdapterPosition());
                            }
                        } else {
                            viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "RFI Already synced"));
                        }
                    } else {
                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "RFI syncing is already in progress"));
                    }
                    break;
                case R.id.btnAddAnswer:
                    etAnswer.setError(null);
                    if (mCallbackRfi != null) {
                        if (!etAnswer.getText().toString().trim().isEmpty()) {
                            mCallbackRfi.addAnswer(etAnswer.getText().toString().trim(), getAdapterPosition());
                        } else {
                            etAnswer.setError("Required Field");
                        }
                    }
                    break;
                case R.id.btnAddCommentRfi:
                    AddCommentDialogFragment.newInstance(dataList.get(getAdapterPosition()).getId(),
                            dataList.get(getAdapterPosition()).getCan_comment())
                            .show(((BaseActivity) context).getSupportFragmentManager(), "add Comment");
                    break;
                case R.id.ivShare:
                    Rfi model = dataList.get(getAdapterPosition());
                    ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(model.getId(), model.getTitle(),
                            model.getTousers() == null || model.getTousers().isEmpty() ? "" : CommonMethods.getCommaSeparatedString(model.getTousers()),
                            model.getCcusers() == null || model.getCcusers().isEmpty() ? "" : CommonMethods.getCommaSeparatedString(model.getCcusers()),
                            DataNames.SHARE_RFI);
                    dialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), "share Rfi");
                    break;
                case R.id.btnAnsAttachment:
                    if (mCallbackRfi != null)
                        mCallbackRfi.initiateFilePicker(getAdapterPosition(), etAnswer.getText().toString().trim());
                    break;
                case R.id.llTop:
                    Rfi rfiModel = dataList.get(getAdapterPosition());
                    rfiModel.setOpened(!rfiModel.isOpened());
                    notifyItemChanged(getAdapterPosition(), rfiModel);
                    break;
            }
        }


        private void syncRfi(int adapterPosition) {
            Rfi rfi = dataList.get(adapterPosition);
            rfi.setSynced(true);
            rfi.setOpened(false);
            viewModel.syncRfiToDatabase(rfi);
        }
    }

}





