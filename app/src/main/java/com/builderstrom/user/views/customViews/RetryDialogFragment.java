package com.builderstrom.user.views.customViews;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.views.dialogFragments.BaseDialogFragment;
import com.builderstrom.user.views.viewInterfaces.ConfirmRetryCallback;

public class RetryDialogFragment extends BaseDialogFragment {

    private String message = "";
    private TextView tvMessage, tvTitle;
    private ConfirmRetryCallback mCallback;

    public static RetryDialogFragment newInstance(String message) {
        RetryDialogFragment dialogFragment = new RetryDialogFragment();
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
        return R.layout.dialog_retry_failure;
    }

    @Override
    protected void bindViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvMessage = view.findViewById(R.id.tvMessage);
        view.findViewById(R.id.btnRetry).setOnClickListener(v -> {
            if (mCallback != null) mCallback.onConfirmClicked();
            dismiss();
        });

        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (mCallback != null) mCallback.onCancelClicked();
            dismiss();
        });
    }

    @Override
    protected void init() {
        getDialog().setCancelable(false);
        if (getArguments() != null) {
            message = getArguments().getString("message");
        }
        setInitialView();
    }

    private void setInitialView() {
        tvTitle.setText("Failure!");
        tvMessage.setText(message);
    }


    public void setCallback(ConfirmRetryCallback mCallback) {
        this.mCallback = mCallback;
    }
}
