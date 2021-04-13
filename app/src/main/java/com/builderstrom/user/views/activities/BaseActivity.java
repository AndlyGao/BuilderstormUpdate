/**
 * Copyright (C) 2018 iDEA foundation pvt.,Ltd
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.builderstrom.user.views.activities;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.service.DbSyncSchedulerTask;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.customViews.CallProgressWheel;
import com.builderstrom.user.views.customViews.RetryDialogFragment;
import com.builderstrom.user.views.viewInterfaces.ConfirmRetryCallback;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnBinder;
    private BroadcastReceiver notificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context && intent.hasExtra("IS_PDF")) {
                appInstallationDialog(intent.getBooleanExtra("IS_PDF", false));
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutID());
        mUnBinder = ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        mUnBinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(notificationBroadcast);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(notificationBroadcast, new IntentFilter(DataNames.DIALOG_FLAG));
    }

    protected abstract int getLayoutID();

    protected abstract void init();

    public void showProgress() {
        if (!CallProgressWheel.isDialogShowing()) {
            CallProgressWheel.showLoadingDialog(this, "Loading...");
        }
    }

    public void showProgress(String message) {
        if (!CallProgressWheel.isDialogShowing()) {
            CallProgressWheel.showLoadingDialog(this, message);
        }
    }

    public void dismissProgress() {
        if (CallProgressWheel.isDialogShowing()) {
            CallProgressWheel.dismissLoadingDialog();
        }
    }

    public void handleErrorModel(@NonNull ErrorModel model) {
        switch (model.getError_Type()) {
            case DataNames.TYPE_ERROR_API:
            case DataNames.TYPE_SYNCED_SUCCESS:
                errorMessage(model.getMessage(), null, false);
                break;
            case DataNames.TYPE_ERROR_EXCEPTION:
//                errorMessage(model.getException().getLocalizedMessage(), null, false);
                errorMessage(getString(R.string.retrofit_failure), null, false);
                break;
            case DataNames.TYPE_ERROR_FAILURE:
                errorMessage(CommonMethods.isNetworkError(model.getThrowable()) ?
//                                getString(R.string.no_internet_connection) : model.getMessage(),
                                getString(R.string.no_internet_connection) : getString(R.string.retrofit_failure),
                        null, false);
                break;
        }
    }

    public void errorMessage(String message, @Nullable @StringRes Integer errorId, boolean isFailure) {
        CommonMethods.displayToast(getApplicationContext(), errorId != null ? getResources().getString(errorId) : message);
    }

    public void showAlert(String message, ConfirmRetryCallback onCallback) {
        RetryDialogFragment dialogFragment = RetryDialogFragment.newInstance(message);
        dialogFragment.setCallback(onCallback);
        dialogFragment.show(getSupportFragmentManager(), "retry again");
    }

    public void openExplorer(String fileType, int result) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(fileType);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), result);
    }

    public void openExplorerDWG() {
//        String[] mimeTypes = {"application/acad", "image/vnd.dwg", "image/x-dwg"};
        String[] mimeTypes = {"application/octate", "application/acad", "application/x-acad",
                "application/autocad_dwg", "application/x-dwg", "application/dwg",
                "application/x-autocad", "drawing/dwg", "image/vnd.dwg", "image/x-dwg"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), CommonMethods.PICK_FILE_RESULT_DWG);
    }

    public void toolbarFinish() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, DashBoardActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
        CommonMethods.hideKeyboard(this);
//        finish();
    }

    public boolean isInternetAvailable() {
        return CommonMethods.isNetworkAvailable(this);
    }

    public void globalSyncService() {
        if (isInternetAvailable()) {
            JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (!alreadyScheduleSyncJob(scheduler)) {
                scheduler.schedule(new JobInfo
                        .Builder(DataNames.SYNC_SLOW_CONNECTION_SERVICE_ID, new ComponentName(this, DbSyncSchedulerTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPeriodic(15 * 60 * 1000) // job reschedules after 20 minutes(15 minutes default time)
                        .build());
            }
        }
    }

    private boolean alreadyScheduleSyncJob(JobScheduler scheduler) {
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_SLOW_CONNECTION_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public void appInstallationDialog(boolean isPDF) {
        new AlertDialog.Builder(this)
                .setTitle(isPDF ? "Preview PDF" : "Preview DWG")
                .setMessage(isPDF ? "To preview this PDF file a 3rd party application needs to be installed. Are you happy for this to be installed?" : "To preview this DWG file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                .setPositiveButton("‘Yes, Install", (dialog, which) -> {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse(isPDF ? "market://details?id=com.adobe.reader" : "market://details?id=com.gstarmc.android"));
                    marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(marketIntent);
                })
                .setNegativeButton("No, Thanks", null)
                .create().show();
    }

}
