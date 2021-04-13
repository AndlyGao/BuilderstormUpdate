package com.builderstrom.user.views.dialogFragments;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvMessage) TextView tvMessage;
    @BindView(R.id.tvNew) TextView tvNew;
    @BindView(R.id.tvDescription) TextView tvDescription;

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_notification;
    }

    @Override
    protected void bindViews(View view) {

    }

    @OnClick({R.id.btnConfirm, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!BuilderStormApplication.mPrefs.getAppLink().isEmpty()) {
                    CommonMethods.downloadFilePermission(BuilderStormApplication.mPrefs.getAppLink(),
                            "builderstorm.apk", getActivity());
                    dismiss();
                }
                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }

    @Override
    protected void init() {
        if (null != getDialog()) {
            getDialog().setCancelable(false);
        }
        setInitialView();
    }

    private void setInitialView() {
        tvTitle.setText(getString(R.string.update));
        tvMessage.setText(getString(R.string.app_new_version_available));
        tvDescription.setText(!BuilderStormApplication.mPrefs.getVersionDescription().isEmpty() ?
                BuilderStormApplication.mPrefs.getVersionDescription() : "Performance improvement");
        underlineText(tvNew);

    }


    private void underlineText(TextView textView) {
        if (null != getActivity()) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary_text_color));
            textView.setVisibility(View.VISIBLE);
        }
    }
}
