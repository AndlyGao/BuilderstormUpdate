package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.views.adapters.TimeListAdapter;
import com.builderstrom.user.views.viewInterfaces.ConfirmSignCallback;
import com.builderstrom.user.repository.retrofit.modals.OverTimeModel;
import com.builderstrom.user.repository.retrofit.modals.StandardBreaks;
import com.builderstrom.user.utils.BuilderStormApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfirmSignDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvMessage) TextView tvMessage;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.ivAddOvertime) ImageView ivAddOvertime;
    @BindView(R.id.ivAddBreak) ImageView ivAddBreaks;
    @BindView(R.id.cbOvertime) CheckBox cbOvertime;
    @BindView(R.id.cbBreaks) CheckBox cbBreaks;
    @BindView(R.id.rlBreaks) RelativeLayout rlBreaks;
    @BindView(R.id.rlOvertime) RelativeLayout rlOvertime;
    @BindView(R.id.llMainBreaks) LinearLayout llMainBreaks;
    @BindView(R.id.llMainOvertime) LinearLayout llMainOvertime;
    @BindView(R.id.rvOvertime) RecyclerView rvOverTime;
    @BindView(R.id.rvBreaks) RecyclerView rvBreaks;

    private List<StandardBreaks> overtimeList = new ArrayList<>();
    private List<StandardBreaks> breaksList = new ArrayList<>();
    private ConfirmSignCallback mCallback;
    private TimeListAdapter mOvertimeAdapter, mBreakAdapter;
    private OverTimeModel model;
    private String message = "";

    public static ConfirmSignDialogFragment newInstance(String message, OverTimeModel model) {
        ConfirmSignDialogFragment dialogFragment = new ConfirmSignDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putParcelable("model", model);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static ConfirmSignDialogFragment newInstance(String message) {
        ConfirmSignDialogFragment dialogFragment = new ConfirmSignDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_confirm_sign;
    }

    @Override
    protected void bindViews(View view) {

        mBreakAdapter = new TimeListAdapter(requireActivity(), breaksList, true);
        rvBreaks.setAdapter(mBreakAdapter);
        mOvertimeAdapter = new TimeListAdapter(requireActivity(), overtimeList, false);
        rvOverTime.setAdapter(mOvertimeAdapter);

        /* click listeners */

        cbOvertime.setOnCheckedChangeListener((buttonView, isChecked) -> rlOvertime.setVisibility(isChecked ? View.GONE : View.VISIBLE));

        cbBreaks.setOnCheckedChangeListener((buttonView, isChecked) -> rlBreaks.setVisibility(isChecked ? View.GONE : View.VISIBLE));
    }

    @OnClick({R.id.btnConfirm, R.id.btnCancel, R.id.ivAddOvertime, R.id.ivAddBreak})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (mCallback != null) {
                    mCallback.onConfirmClicked(mOvertimeAdapter.getOverTimeList(),
                            mBreakAdapter.getSelectedList(), mBreakAdapter.getBreakTimeList());
                }
                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.ivAddOvertime:
                openTimeDialog(true);
                break;
            case R.id.ivAddBreak:
                openTimeDialog(false);
                break;
        }
    }

    private void openTimeDialog(boolean isOvertime) {
        AddTimeDialogFragment dialogFragment = AddTimeDialogFragment.newInstance(isOvertime);
        dialogFragment.setCallback((startTime, endTime) -> {
            if (isOvertime) {
                overtimeList.add(new StandardBreaks(startTime, endTime));
                mOvertimeAdapter.notifyDataSetChanged();
            } else {
                breaksList.add(new StandardBreaks(startTime, endTime));
                mBreakAdapter.notifyDataSetChanged();
            }
        });

        dialogFragment.show(getParentFragmentManager(), "Add Time");

    }

    @Override
    protected void init() {
        if (null != getDialog()) {
            getDialog().setCancelable(false);
        }
        if (getArguments() != null) {
            message = getArguments().getString("message");
            model = getArguments().getParcelable("model");
        }
        setInitialView();
    }

    private void setInitialView() {

        tvTitle.setText(getString(BuilderStormApplication.mPrefs.isProjectSignIn() ? R.string.sign_out : R.string.sign_in));
        tvMessage.setText(message);

        /* visibility */
        cbBreaks.setVisibility(BuilderStormApplication.mPrefs.isProjectSignIn() ? View.VISIBLE : View.GONE);
        cbOvertime.setVisibility(BuilderStormApplication.mPrefs.isProjectSignIn() ? View.VISIBLE : View.GONE);
        rlBreaks.setVisibility(BuilderStormApplication.mPrefs.isProjectSignIn() ? View.VISIBLE : View.GONE);
        rlOvertime.setVisibility(BuilderStormApplication.mPrefs.isProjectSignIn() ? View.VISIBLE : View.GONE);

        if (BuilderStormApplication.mPrefs.isProjectSignIn()) {
            cbOvertime.setChecked(true);
            cbBreaks.setChecked(true);
            /* overtime model is not null */
            if (model != null) {
                llMainOvertime.setVisibility(model.getAllowOvertime() == 1 ? View.VISIBLE : View.GONE);
                llMainBreaks.setVisibility(model.getAllowStandardBreaks() == 1 ? View.VISIBLE : View.GONE);

                /* for extra safety put checks on Add buttons*/
                ivAddOvertime.setVisibility(model.getAllowOvertime() == 1 ? View.VISIBLE : View.INVISIBLE);
                ivAddBreaks.setVisibility(model.getAllowStandardBreaks() == 1 ? View.VISIBLE : View.INVISIBLE);


                /* overtime Adapter*/
                overtimeList.clear();
                if (null != model.getOvertime()) {
                    overtimeList.addAll(model.getOvertime());
                }
                mOvertimeAdapter.notifyDataSetChanged();
                /* breaks Adapter*/
                breaksList.clear();
                if (null != model.getStandardBreaks()) {
                    breaksList.addAll(model.getStandardBreaks());
                }
                mBreakAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setCallback(ConfirmSignCallback mCallback) {
        this.mCallback = mCallback;
    }
}
