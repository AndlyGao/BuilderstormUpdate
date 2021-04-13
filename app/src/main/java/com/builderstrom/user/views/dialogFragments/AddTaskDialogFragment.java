package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.TaskListing;
import com.builderstrom.user.views.adapters.TimeSheetTaskAdapter;
import com.builderstrom.user.views.viewInterfaces.TaskReturnInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTaskDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.rvWorkTimeTask) RecyclerView rvWorkTimeTask;

    private List<TaskListing> taskList = new ArrayList<>();
    private TaskReturnInterface mTaskReturnInterface;

    public static AddTaskDialogFragment newInstance(List<TaskListing> taskList) {
        AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<TaskListing>) taskList);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_add_task;
    }

    @Override
    protected void bindViews(View view) {
        if (null != getArguments()) {
            taskList = getArguments().getParcelableArrayList("data");
        }
        rvWorkTimeTask.setAdapter(new TimeSheetTaskAdapter(getActivity(), taskList));
    }

    @OnClick({R.id.ivCancel, R.id.btnSave, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCancel:
                dismiss();
                break;
            case R.id.btnSave:
                if (mTaskReturnInterface != null) {
                    mTaskReturnInterface.updateTaskList(rvWorkTimeTask.getAdapter() != null ?
                            ((TimeSheetTaskAdapter) rvWorkTimeTask.getAdapter()).selectedTaskID() : null);
                }
                dismiss();
                break;

            case R.id.btnCancel:
                if (mTaskReturnInterface != null) {
                    mTaskReturnInterface.cancelTaskList(rvWorkTimeTask.getAdapter() != null ?
                            ((TimeSheetTaskAdapter) rvWorkTimeTask.getAdapter()).UnselectAllID() : null);
                }
                dismiss();
                break;
        }
    }

    @Override
    protected void init() {
        /* set title */
        tvTitle.setText(getString(R.string.add_task));
    }


    void setTaskReturnInterface(TaskReturnInterface mTaskReturnInterface) {
        this.mTaskReturnInterface = mTaskReturnInterface;
    }


}
