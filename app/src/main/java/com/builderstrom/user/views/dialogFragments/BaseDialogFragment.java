package com.builderstrom.user.views.dialogFragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.activities.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment {

    private Unbinder mUnBinder;
    protected PermissionUtils permissionUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        bindViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getDialog()) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    isFullScreenDialog() ? ViewGroup.LayoutParams.MATCH_PARENT :
                            ViewGroup.LayoutParams.WRAP_CONTENT);

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        init();
    }

    protected abstract boolean isFullScreenDialog();

    protected abstract int getLayoutId();

    protected abstract void bindViews(View view);

    protected abstract void init();

    @Override
    public void onDestroyView() {
        mUnBinder.unbind();
        super.onDestroyView();
    }


    public void showProgress() {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).showProgress();
        }
    }

    public void dismissProgress() {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }


    public void errorMessage(String message, @Nullable Integer errorId, boolean isFailure) {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).errorMessage(message, errorId, isFailure);
        }
    }

    public void openExplorer(String fileType, int result) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileType);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), result);
    }

    public void dismissWithHideKeyboard() {
        if (null != getContext() && null != getView()) {
            CommonMethods.hideKeyboard(getContext(), getView());
        }
        dismiss();
    }

    public void hideDialogKeyboard() {
        if (null != getContext() && null != getView()) {
            CommonMethods.hideKeyboard(getContext(), getView());
        }
    }

    public boolean isInternetAvailable() {
        return null != getActivity() && CommonMethods.isNetworkAvailable(getActivity());
    }

    public void globalSyncService() {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).globalSyncService();
        }
    }

    protected void openTimePicker(EditText editText) {
        new TimePickerDialog(requireContext(), R.style.DatePickerTheme,
                (view, hourOfDay, minute) -> {
                    Calendar calInstance = Calendar.getInstance();
                    calInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calInstance.set(Calendar.MINUTE, minute);
                    DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                    editText.setText(timeFormat.format(calInstance.getTime()));
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), true).show();
    }

    protected String checkNull(String text) {
        return text != null ? text : "";
    }

}
