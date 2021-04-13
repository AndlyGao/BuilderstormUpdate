package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoToDo;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ToDoViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.ToDoCommentsDialogFragment;
import com.builderstrom.user.views.fragments.AddToDoFragment;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PojoToDo> dataList;
    private ToDoViewModel viewModel;
    private boolean isOffline = false;
    private UpdateCompanyDocsListing updateCallBack;

    public ToDoListAdapter(Context mContext, List<PojoToDo> dataList, ToDoViewModel viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ToDoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ToDoHolder(mLayoutInflater.inflate(R.layout.row_to_do, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoHolder viewHolder, int position) {
        PojoToDo data = dataList.get(position);
        String issueDate = CommonMethods.convertDate(CommonMethods.DF_1, data.getAddedOn(), CommonMethods.DF_7);
        String dueDate = CommonMethods.convertDate(CommonMethods.DF_2, data.getDueDate(), CommonMethods.DF_7);
        String completedDate = data.getCompletedOn().isEmpty() ? "" : CommonMethods.convertDate(CommonMethods.DF_2, data.getCompletedOn(), CommonMethods.DF_7);
//        viewHolder.llHidden.setVisibility(data.isSelected() ? View.VISIBLE : View.GONE);
        viewHolder.llHidden.setVisibility(View.GONE);
        viewHolder.tvHeadTitle.setText(data.getTitle());
        viewHolder.tvHeadDueDate.setText(dueDate);
        viewHolder.tvHeadIssueDate.setText(issueDate);

        if (data.getStatus() == 4) {
            viewHolder.ivStatus.setColorFilter(ContextCompat.getColor(mContext, R.color.green_color_picker));
            viewHolder.ivStatus.setImageResource(R.drawable.ic_check_mark_red);
        } else {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_circle_red);
            viewHolder.ivStatus.setColorFilter(ContextCompat.getColor(mContext, getStatusColor(data.getStatus())));
        }
        viewHolder.tvTitle.setText(CommonMethods.spannedText(String.format("Title: %s", data.getTitle()), 0, 7));
        viewHolder.tvCategory.setText(CommonMethods.spannedText(String.format("Category: %s", data.getCategoryTitle()), 0, 10));
        viewHolder.tvIssueDate.setText(CommonMethods.spannedText(String.format("Date Issued: %s", issueDate), 0, 13));
        viewHolder.tvDueDate.setText(CommonMethods.spannedText(String.format("Due Date: %s", dueDate), 0, 10));
        viewHolder.tvCompletedDate.setText(CommonMethods.spannedText(String.format("Completed: %s", completedDate), 0, 10));
        viewHolder.tvDescription.setText(CommonMethods.spannedText(String.format("Description: %s", data.getDescription()), 0, 13));
        viewHolder.tvToUsers.setVisibility(data.getToUsers() != null && !data.getToUsers().isEmpty() ? View.VISIBLE : View.GONE);
        viewHolder.tvCcUsers.setVisibility(data.getCcUsers() != null && !data.getCcUsers().isEmpty() ? View.VISIBLE : View.GONE);
        viewHolder.tvCompletedDate.setVisibility(completedDate == null || completedDate.isEmpty() ? View.GONE : View.VISIBLE);
        viewHolder.ivMarkComplete.setImageDrawable(completedDate == null || completedDate.isEmpty() ?
                mContext.getDrawable(R.drawable.drawable_tick_with_background_grey) : mContext.getDrawable(R.drawable.drawable_tick_with_background)
        );

        if (viewHolder.tvToUsers.getVisibility() == View.VISIBLE) {
            viewHolder.tvToUsers.setText(CommonMethods.spannedText(String.format("To Users: %s", CommonMethods.getCommaSeparatedString(data.getToUsers())), 0, 10));
        }
        if (viewHolder.tvCcUsers.getVisibility() == View.VISIBLE) {
            viewHolder.tvCcUsers.setText(CommonMethods.spannedText(String.format("CC Users: %s", CommonMethods.getCommaSeparatedString(data.getCcUsers())), 0, 10));
        }
        viewHolder.llFiles.setVisibility(data.getAttachments() != null &&
                !data.getAttachments().isEmpty() ? View.VISIBLE : View.GONE);

        if (data.getAttachments() != null && !data.getAttachments().isEmpty()) {
            viewHolder.rvAttachments.setAdapter(new AttachmentsAdapter(mContext, data.getAttachments(), viewModel));
        }

        if (CommonMethods.isOfflineEntry(String.valueOf(data.getId()))) {
            viewHolder.ivSync.setImageResource(R.drawable.ic_un_uploaded);
//            viewHolder.ivMarkComplete.setVisibility(View.GONE);
        } else {
            viewHolder.ivSync.setImageResource(data.isSynced() ? R.drawable.ic_refresh_not_found : R.drawable.ic_refresh);
            if (userPermissions != null
                    && userPermissions.getTodos().getDueDateCompleted() != null
                    && userPermissions.getTodos().getDueDateCompleted().equalsIgnoreCase("on")
                    && viewModel.mPrefs.getTodoUserType().equalsIgnoreCase("admin")) {
                viewHolder.ivMarkComplete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivMarkComplete.setVisibility(View.GONE);
            }
        }
    }

    private int getStatusColor(Integer status) {
        switch (status) {
            case 1:
                return R.color.colorToDoNormal;
            case 2:
                return R.color.colorToDoWarning;
            case 3:
                return R.color.colorOverDue;
        }
        return android.R.color.white;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public void setUpdateCallBack(UpdateCompanyDocsListing updateCallBack) {
        this.updateCallBack = updateCallBack;
    }

    class ToDoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHeadTitle)
        TextView tvHeadTitle;
        @BindView(R.id.tvHeadDueDate)
        TextView tvHeadDueDate;
        @BindView(R.id.tvHeadIssueDate)
        TextView tvHeadIssueDate;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.tvIssueDate)
        TextView tvIssueDate;
        @BindView(R.id.tvDueDate)
        TextView tvDueDate;
        @BindView(R.id.tvCompletedDate)
        TextView tvCompletedDate;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvToUsers)
        TextView tvToUsers;
        @BindView(R.id.tvCcUsers)
        TextView tvCcUsers;
        @BindView(R.id.llFiles)
        LinearLayout llFiles;
        @BindView(R.id.rvAttachments)
        RecyclerView rvAttachments;
        @BindView(R.id.ivSync)
        ImageView ivSync;
        @BindView(R.id.ivStatus)
        ImageView ivStatus;
        @BindView(R.id.llHidden)
        LinearLayout llHidden;
        @BindView(R.id.ivEdit)
        ImageView ivEdit;
        @BindView(R.id.ivMarkComplete)
        ImageView ivMarkComplete;
        @BindView(R.id.ll_actions)
        LinearLayout ll_actions;

        ToDoHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (userPermissions != null) {
                CommonMethods.checkVisiblePermission(userPermissions.getTodos().getAddEditTodos(), ivEdit);
            }
        }

        @OnClick({R.id.llTop, R.id.ivSync, R.id.ivEdit, R.id.ivComment, R.id.ivMarkComplete})
        public void onClick(View view) {
            PojoToDo toDo = dataList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.llTop:
                    if (llHidden.getVisibility() == View.VISIBLE) {
                        llHidden.setVisibility(View.GONE);
                    } else {
                        llHidden.setVisibility(View.VISIBLE);
                    }

//                    toDo.setSelected(!toDo.isSelected());
//                    notifyItemChanged(getAdapterPosition());
                    break;
                case R.id.ivSync:
                    if (viewModel.isInternetAvailable() && toDo.getId() != null) {
                        if (CommonMethods.downloadFile(mContext)) {
                            if (!viewModel.isAlreadyScheduleToDoJob()) {
                                toDo.setSynced(true);
                                viewModel.syncDocument(toDo);
                            }
                        } else {
                            viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                    mContext.getString(R.string.already_syncing)));
                        }
                    }
                    break;
                case R.id.ivEdit:
                    if (!isOffline) {
                        AddToDoFragment editFragment = AddToDoFragment.newInstance(toDo);
                        editFragment.setCallback(updateCallBack);
                        ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction()
                                .add(R.id.flTestContainer, editFragment, "edit ToDo")
                                .addToBackStack("edit ToDo").commit();
                    } else {
                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                mContext.getString(R.string.not_available_in_offline)));
                    }
                    break;
                case R.id.ivComment:
                    if (!isOffline) {
                        ToDoCommentsDialogFragment.newInstance(toDo.getId()).show(
                                ((BaseActivity) mContext).getSupportFragmentManager(), "comments");
                    } else {
                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                mContext.getString(R.string.not_available_in_offline)));

                    }
                    break;
                case R.id.ivMarkComplete:
                    if (CommonMethods.isOfflineEntry(String.valueOf(dataList.get(getAdapterPosition()).getId()))) {
                        if (dataList.get(getAdapterPosition()).getStatus() == 4) {
                            dataList.get(getAdapterPosition()).setCompletedOn("");
                            dataList.get(getAdapterPosition()).setStatus(0);
                        } else {
                            dataList.get(getAdapterPosition()).setCompletedOn(CommonMethods.getCurrentDate(CommonMethods.DF_2));
                            dataList.get(getAdapterPosition()).setStatus(4);
                        }
                        viewModel.markOfflineEntry(dataList.get(getAdapterPosition()));
                    } else {
                        viewModel.markTodo(toDo.getId(), toDo.getStatus() == 4 ? 0 : 1, toDo);
                    }
//                    viewModel.markTodo(toDo.getId(), toDo.getStatus() == 4 ? 0 : 1, toDo);
//                    if (!isOffline) {
//
//                    } else {
//                        viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
//                                mContext.getString(R.string.not_available_in_offline)));
//                    }
                    break;
            }
        }
    }

}