package com.builderstrom.user.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.PojoAttachment;
import com.builderstrom.user.repository.retrofit.modals.Rfi;
import com.builderstrom.user.repository.retrofit.modals.RfiFileModel;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RfiOfflineAdapter extends RecyclerView.Adapter<RfiOfflineAdapter.MyViewHolder> {

    public Context context;
    private List<Rfi> rfiModelList;
    private CallbackRfi mCallbackRfi;

    public RfiOfflineAdapter(Context context, List<Rfi> rfiModelList, CallbackRfi mCallbackRfi) {
        this.context = context;
        this.rfiModelList = rfiModelList;
        this.mCallbackRfi = mCallbackRfi;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rfi, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Rfi data = rfiModelList.get(position);

        if (isOfflineEntry(data.getId())) {
            if (!data.getOfflineFiles().isEmpty()) {
                List<String> rfiData = new Gson().fromJson(data.getOfflineFiles(), new TypeToken<List<String>>() {
                }.getType());
                List<RfiFileModel> fileModels = new ArrayList<>();
                for (String path : rfiData) {
                    fileModels.add(new RfiFileModel(CommonMethods.getFileName(context, CommonMethods.getUriFromFileName(context, path)), path));
                }
                if (!rfiData.isEmpty()) {
                    holder.rvFiles.setAdapter(new RfiOfflineChildAdapter(context, fileModels, null));
                    holder.llFiles.setVisibility(View.VISIBLE);
                } else {
                    holder.llFiles.setVisibility(View.GONE);
                }
            } else {
                holder.llFiles.setVisibility(View.GONE);
            }
        } else {

            holder.llFiles.setVisibility(data.getAttachments() != null && !data.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);
            if (data.getAttachments() != null && !data.getAttachments().isEmpty()) {
                List<RfiFileModel> rfiFileModels = new ArrayList<>();

                for (PojoAttachment attachment : data.getAttachments()) {
                    RfiFileModel fileModel = new RfiFileModel();
                    fileModel.setName(attachment.getOriginal_name());
                    rfiFileModels.add(fileModel);
                }
                holder.rvFiles.setAdapter(new RfiOfflineChildAdapter(context, rfiFileModels, null));
            }
        }
        holder.ivStatus.setImageResource(setStatusLock(data.getShowStatus(), data.getIsAnswered(), data.getDuedate()));
        holder.ivSync.setImageResource(isOfflineEntry(data.getId()) ? R.drawable.ic_un_uploaded : R.drawable.ic_refresh_not_found);

        holder.tvTitle.setText(data.getTitle());
        holder.tvDateCreatedRfi.setText(CommonMethods.convertDate(CommonMethods.DF_1, data.getCreatedOn(), CommonMethods.DF_6));
        holder.tvDueDateRfi.setText(CommonMethods.convertDate(CommonMethods.DF_1, data.getDuedate(), CommonMethods.DF_6));
        holder.tvTitleExpanded.setText(data.getTitle());
        holder.wvDescription.loadDataWithBaseURL(null, setHtmlFormattedText(data.getDescription()), "text/html", "utf-8", null);
        holder.wvDescription.setVisibility(data.getDescription() == null || data.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        holder.tvTitleRFI.setText(CommonMethods.spannedText(String.format("Request From:  %s", data.getRequestBy()), 0, 14));
        holder.tvIssueDate.setText(CommonMethods.spannedText(String.format("Date Issued:  %s", CommonMethods.convertDate(CommonMethods.DF_1, data.getCreatedOn(), CommonMethods.DF_3)), 0, 12));
        holder.tvDueDate.setText(CommonMethods.spannedText(String.format("Due Date:  %s", CommonMethods.convertDate(CommonMethods.DF_1, data.getDuedate(), CommonMethods.DF_3)), 0, 9));

        /* Users Modified field*/
        if (isUserNotified(data.getTousers(), data.getCcusers())) {
            holder.tvToUsersModified.setText(CommonMethods.spannedText(String.format("User Notified: %s", ""), 0, 14));
            holder.llMoreUsers.setVisibility(View.VISIBLE);
            holder.tvToUsers.setVisibility(data.getTousers().isEmpty() ? View.GONE : View.VISIBLE);
            holder.tvCcUsers.setVisibility(data.getCcusers().isEmpty() ? View.GONE : View.VISIBLE);

            if (!data.getTousers().isEmpty()) {
                holder.tvToUsers.setText(CommonMethods.spannedText(String.format("To User(s) : %s", CommonMethods.getCommaSeparatedString(data.getTousers())), 0, 12));
            }

            if (!data.getCcusers().isEmpty()) {
                holder.tvCcUsers.setText(CommonMethods.spannedText(String.format("CC User(s) : %s", CommonMethods.getCommaSeparatedString(data.getCcusers())), 0, 12));
            }
        } else {
            holder.llMoreUsers.setVisibility(View.GONE);
            holder.tvToUsersModified.setText(CommonMethods.spannedText(String.format("User Notified: %s", "No User Notified"), 0, 14));
        }

        holder.llHidden.setVisibility(data.isOpened() ? View.VISIBLE : View.GONE);

        /* Answer View */
        holder.rvAnsAttachment.setVisibility(data.getAnswerAttachmentFiles() != null && !data.getAnswerAttachmentFiles().isEmpty() ? View.VISIBLE : View.GONE);
        /* answer upload files */
        if (data.getAnswerAttachmentFiles() != null && !data.getAnswerAttachmentFiles().isEmpty()) {
            holder.rvAnsAttachment.setAdapter(new ChildAttachOfflineAdapter(context, data.getAnswerAttachmentFiles()));
        }
        /*set text on etAnswer */
        holder.etAnswer.setText(data.getAnswerText() == null || data.getAnswerText().isEmpty() ? null : data.getAnswerText());

        holder.llAddAnswer.setVisibility(data.getCan_answer() && !data.getIsAnswered() ? View.VISIBLE : View.GONE);
        holder.llViewAnswer.setVisibility(data.getIsAnswered() ? View.VISIBLE : View.GONE);
        if (data.getIsAnswered()) {
            holder.tvAnswer.setText(data.getAnswer());
            holder.tvAnswerBy.setText(String.format("%s - %s", data.getAnswered_by(), CommonMethods.convertDate(CommonMethods.DF_1, data.getAnswered_on(), CommonMethods.DF_3)));
            holder.rvAnsFiles.setVisibility(data.getAnswerfiles() != null && !data.getAnswerfiles().isEmpty() ? View.VISIBLE : View.GONE);
            if (data.getAnswerfiles() != null && !data.getAnswerfiles().isEmpty()) {
                holder.rvAnsFiles.setAdapter(new ChildAttachOnlineAdapter(context, data.getAnswerfiles()));
            }
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

    private int setStatusLock(Integer status, Boolean isAnswered, String dueDateString) {
        Date dueDate = CommonMethods.convertToDate(dueDateString);
        Date currentDate = new Date();
        if (status == 3) {
            return R.drawable.ic_padlock_black;
        } else if (status != 2) {
            if (isAnswered) {
                if (currentDate.before(dueDate)) {
                    return status == 0 ? R.drawable.ic_padunlock_orange : R.drawable.ic_padlock_green;
                } else {
                    return status == 0 ? R.drawable.ic_padunlock_orange : R.drawable.ic_padlock_red;
                }
                /* if current date before due date && status==0 ==> R.drawable.ic_padunlock_orange
                 * if current date before due date && status==1 ==> R.drawable.ic_padlock_green
                 * if currentdate after due date && status==0 ==> R.drawable.ic_padunlock_orange
                 * if currrentdate after due date && status==1 ==> R.drawable.ic_padlock_red*/

            } else {
                return currentDate.before(dueDate) ? R.drawable.ic_padunlock_gold : R.drawable.ic_padunlock_red;
                /* if current date before due date ==>  R.drawable.ic_padunlock_gold
                 * if currrentdate after due date  ==> R.drawable.ic_padunlock_red*/
            }
        }

        return R.drawable.ic_padlock_gray;
    }

    private boolean isOfflineEntry(String rowId) {
        return rowId == null || rowId.equals("0");
    }

    @Override
    public int getItemCount() {
        return rfiModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvTitleRFI) TextView tvTitleRFI;
        @BindView(R.id.tvDateCreatedRfi) TextView tvDateCreatedRfi;
        @BindView(R.id.tvDueDateRfi) TextView tvDueDateRfi;
        @BindView(R.id.tvduedate) TextView tvDueDate;
        @BindView(R.id.tvIssueDate) TextView tvIssueDate;
        @BindView(R.id.tvToUsers) TextView tvToUsers;
        @BindView(R.id.tvCcUsers) TextView tvCcUsers;
        @BindView(R.id.tvAnswer) TextView tvAnswer;
        @BindView(R.id.tvAnswerBy) TextView tvAnswerBy;
        @BindView(R.id.tvTitleExpanded) TextView tvTitleExpanded;
        @BindView(R.id.tvToUsersModified) TextView tvToUsersModified;
        @BindView(R.id.tvFiles) TextView tvFiles;

        @BindView(R.id.wvDescription) WebView wvDescription;

        @BindView(R.id.etAnswer) EditText etAnswer;

        @BindView(R.id.ivSync) ImageView ivSync;
        @BindView(R.id.ivStatus) ImageView ivStatus;
        @BindView(R.id.ivShare) ImageView ivShare;
        @BindView(R.id.btnAddCommentRfi) AppCompatButton btnAddCommentRfi;


        @BindView(R.id.llFiles) LinearLayout llFiles;
        @BindView(R.id.llTop) LinearLayout llTop;
        @BindView(R.id.llHidden) LinearLayout llHidden;
        @BindView(R.id.llAddAnswer) LinearLayout llAddAnswer;
        @BindView(R.id.llViewAnswer) LinearLayout llViewAnswer;
        @BindView(R.id.llMoreUsers) LinearLayout llMoreUsers;

        @BindView(R.id.rvAnsAttachment) RecyclerView rvAnsAttachment;
        @BindView(R.id.rvAnsFiles) RecyclerView rvAnsFiles;
        @BindView(R.id.rvFiles) RecyclerView rvFiles;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setupWebView();
            btnAddCommentRfi.setVisibility(View.GONE);
            ivShare.setVisibility(View.GONE);

        }


        @SuppressLint("SetJavaScriptEnabled")
        private void setupWebView() {
            if (null != wvDescription) {
                wvDescription.getSettings().setJavaScriptEnabled(true);
                wvDescription.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                wvDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRfiDescriptionBack));
                wvDescription.loadUrl("javascript:(function() { " + "document.querySelector('[role=\"toolbar\"]').remove();})()");
            }
        }

        @OnClick({R.id.llTop, R.id.btnAnsAttachment, R.id.btnAddAnswer})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llTop:
                    Rfi model = rfiModelList.get(getAdapterPosition());
                    model.setOpened(!model.isOpened());
                    notifyItemChanged(getAdapterPosition(), model);
                    break;
                case R.id.btnAnsAttachment:
                    if (mCallbackRfi != null)
                        mCallbackRfi.initiateFilePicker(getAdapterPosition(), etAnswer.getText().toString().trim());

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
            }
        }


    }


    public interface CallbackRfi {

        void addAnswer(String answerString, int position);

        void initiateFilePicker(int position, String answerText);

    }

}





