package com.builderstrom.user.views.dialogFragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.viewInterfaces.AddTimeInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTimeDialogFragment extends BaseDialogFragment {

    private boolean isOverTime = false;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvStartTime) TextView tvStartTime;
    @BindView(R.id.tvEndTime) TextView tvEndTime;

    private AddTimeInterface mCallback;

    public static AddTimeDialogFragment newInstance(boolean isOvertime) {
        AddTimeDialogFragment dialogFragment = new AddTimeDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isOvertime", isOvertime);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_add_time;
    }

    @Override
    protected void bindViews(View view) {
    }

    @OnClick({R.id.ivCancel, R.id.tvStartTime, R.id.tvEndTime, R.id.btnSave, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCancel:
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.tvStartTime:
                openTimePicker(true);
                break;
            case R.id.tvEndTime:
                openTimePicker(false);
                break;
            case R.id.btnSave:
                if (!tvStartTime.getText().toString().trim().isEmpty() &&
                        !tvEndTime.getText().toString().trim().isEmpty()) {
                    if (CommonMethods.isStartBeforeEndTime(tvStartTime.getText().toString().trim(),
                            tvEndTime.getText().toString().trim())) {
                        if (null != mCallback) {
//                            mCallback.onSaved(CommonMethods.convertDateUK(CommonMethods.DF_9, tvStartTime.getText().toString().trim(), CommonMethods.DF_9), CommonMethods.convertDateUK(CommonMethods.DF_9, tvEndTime.getText().toString().trim(), CommonMethods.DF_9));
                            mCallback.onSaved(tvStartTime.getText().toString().trim(), tvEndTime.getText().toString().trim());
                        }
                        dismiss();
                    } else {
                        errorMessage(getString(R.string.earlier_start_time), null, false);
                    }
                } else {
                    errorMessage("Select " + (tvStartTime.getText().toString().trim().isEmpty() ? "Start" : "End") + " time ", null, false);
                }
                break;
        }
    }

    private void openTimePicker(boolean isStartTime) {
        new TimePickerDialog(requireContext(), R.style.DatePickerTheme, (view, hourOfDay, minute) -> {
            Calendar calInstance = Calendar.getInstance();
            calInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calInstance.set(Calendar.MINUTE, minute);
//            DateFormat timeFormat = CommonMethods.getDateFormat(CommonMethods.DF_9);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            if (isStartTime) {
                tvStartTime.setText(timeFormat.format(calInstance.getTime()));
            } else {
                tvEndTime.setText(timeFormat.format(calInstance.getTime()));
            }
        },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), true).show();
    }

    @Override
    protected void init() {
        if (null != getArguments()) {
            isOverTime = getArguments().getBoolean("isOvertime");
        }

        /* set title */
        tvTitle.setText(getString(isOverTime ? R.string.add_overtime : R.string.add_break));
    }


    public void setCallback(AddTimeInterface mCallback) {
        this.mCallback = mCallback;
    }

}
