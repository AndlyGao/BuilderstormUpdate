package com.builderstrom.user.views.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.customViews.toolTip.Tooltip;
import com.builderstrom.user.utils.CommonMethods;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder mUnBinder;
    protected PermissionUtils permissionUtils;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        bindView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    protected abstract int getLayoutId();

    protected abstract void bindView(View view);

    protected abstract void init();


    @Override
    public void onDestroyView() {
        mUnBinder.unbind();
        super.onDestroyView();
    }

    public void showProgress() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProgress();
        }
    }

    public void dismissProgress() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).dismissProgress();
        }

    }

    public void errorMessage(String message, @Nullable Integer errorId, boolean isFailure) {
        if (getActivity() != null && isAdded()) {
            CommonMethods.displayToast(getActivity(), errorId != null ? getResources().getString(errorId) : message);
        }
    }

    void showTooltip(String messageString, ImageView ivCloseOver) {
        new Tooltip.Builder(ivCloseOver)
                .setText(messageString)
                .setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.theme_color))
                .setCornerRadius(10f)
                .setTextColor(ContextCompat.getColor(requireActivity(), android.R.color.white))
                .show();
    }

    public boolean isInternetAvailable() {
        return getActivity() != null && ((BaseActivity) getActivity()).isInternetAvailable();
    }

    void sendUserToAirplaneSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                errorMessage("Not able to set airplane mode", null, false);
            }
        }
    }

    void setSwipeRefreshView(SwipeRefreshLayout refreshView) {
        if (refreshView != null) {
            refreshView.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                    android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        }
    }

    public void globalSyncService() {
        if (null != getActivity()) {
            ((BaseActivity) getActivity()).globalSyncService();
        }
    }

    protected void setupDataSource(TextView tvDataSource, boolean isOffline) {
        if (null != tvDataSource && null != getActivity()) {
            tvDataSource.setVisibility(View.VISIBLE);
            tvDataSource.setSelected(isOffline);
            if (isInternetAvailable()) {
                tvDataSource.setText(getString(R.string.poor_connections_message));
            }
//                tvDataSource.setText(getString(isOffline ? R.string.offline_data : R.string.online_data));
        }
    }


    public void openExplorer(String fileType, int result) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileType);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), result);
    }

    public void hideKeyboard() {
        if (null != getContext() && null != getView()) {
            CommonMethods.hideKeyboard(getContext(), getView());
        }
    }

    void popOutFragment() {
        if (isAdded()) {
            hideKeyboard();
            getParentFragmentManager().popBackStackImmediate();
        }
    }


}
