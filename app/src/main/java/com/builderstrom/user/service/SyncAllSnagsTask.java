package com.builderstrom.user.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.PojoAttachment;
import com.builderstrom.user.data.retrofit.modals.Snag;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SyncAllSnagsTask extends JobService {

    private List<Snag> listToSync;
    private JobParameters params;
    private boolean isWorking = false;
    private DownloadManager mDownloadManager;
    private List<String> syncedList = new ArrayList<>();
    private String projectId = "";


    @Override
    public boolean onStartJob(JobParameters params) {
        if (!isWorking) {
            this.params = params;
            this.isWorking = true;
            String jobString = params.getExtras().getString("data");
            listToSync = new Gson().fromJson(jobString, new TypeToken<List<Snag>>() {
            }.getType());
            if (listToSync != null && !listToSync.isEmpty()) {
                projectId = listToSync.get(0).getProjectId();
                updateSyncedList();
            }
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
            CommonMethods.displayToast(getApplicationContext(), "Syncing Started");
            handleSyncing();
        }


        return true;
    }

    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(BuilderStormApplication.mLocalDatabase.getSyncSnagIdList(BuilderStormApplication.mPrefs.getSiteId(),
                BuilderStormApplication.mPrefs.getUserId(), projectId));
        Log.e("synced List",new Gson().toJson(syncedList));

    }

    @SuppressLint("StaticFieldLeak")
    private void handleSyncing() {
        if (null != listToSync && !listToSync.isEmpty() && isWorking) {
            if (syncedList.contains(listToSync.get(0).getId())) {
                listToSync.remove(0);
                handleSyncing();
            } else {
                new AsyncTask<Void, Void, Long>() {
                    @Override
                    protected Long doInBackground(Void... voids) {
                        Snag snag = listToSync.get(0);
                        downloadAllFiles(snag);
                        return BuilderStormApplication.mLocalDatabase.insertSnag(
                                BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(), snag.getProjectId(),
                                snag.getId(), snag.getRequestedFrom(), new Gson().toJson(snag.getToUsers()),
                                new Gson().toJson(snag.getCcUsers()), snag.getLocationId(),
                                snag.getPackageId(), snag.getDefect_details(), snag.getDueDate(),
                                new Gson().toJson(snag), snag.getSnag_status(),
                                new Gson().toJson(snag.getMark_as()));
                    }

                    @Override
                    protected void onPostExecute(Long aLong) {
                        super.onPostExecute(aLong);
                        if (aLong != null) {
                            listToSync.remove(0);
                        }
                        handleSyncing();
                    }
                }.execute();
            }
        } else {
            jobFinished(params, false);
            callBroadCast();
        }

    }

    private void downloadAllFiles(Snag snag) {
        if (null != snag.getAttachments()) {
            for (PojoAttachment fileModel : snag.getAttachments()) {
                String downloadUrl = CommonMethods.decodeUrl(fileModel.getUrl().isEmpty() ?
                        fileModel.getFile_url() : fileModel.getUrl());
                if (!downloadUrl.isEmpty()) {
                    CommonMethods.download(getDownloadManager(), downloadUrl, fileModel.getOriginal_name(),
                            getApplication(), "");
                }
            }
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        isWorking = false;
        listToSync = null;
        this.params = null;
        return true;
    }

    private void callBroadCast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public DownloadManager getDownloadManager() {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mDownloadManager;
    }



}
