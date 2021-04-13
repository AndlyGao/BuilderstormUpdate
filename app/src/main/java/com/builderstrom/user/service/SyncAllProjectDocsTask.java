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
import com.builderstrom.user.data.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SyncAllProjectDocsTask extends JobService {
    private List<PDocsDataModel> listToSync;
    private List<String> syncedList = new ArrayList<>();
    private JobParameters params;
    private boolean isWorking = false;
    private DownloadManager mDownloadManager;
    private String projectId = "";

    @Override
    public boolean onStartJob(JobParameters params) {
        if (!isWorking) {
            /* start all snag syncing*/
            this.params = params;
            this.isWorking = true;
            String jobString = params.getExtras().getString("data");
            listToSync = new Gson().fromJson(jobString, new TypeToken<List<PDocsDataModel>>() {
            }.getType());
            if (listToSync != null && !listToSync.isEmpty()) {
                projectId = listToSync.get(0).getProjectUid();
                updateSyncedList();
            }
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
            CommonMethods.displayToast(getApplicationContext(), "Syncing Started");
            handleSyncing();
        }

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void handleSyncing() {
        if (null != listToSync && !listToSync.isEmpty() && isWorking) {
            if (syncedList.contains(String.valueOf(listToSync.get(0).getUid()))) {
                listToSync.remove(0);
                handleSyncing();
            } else {
                new AsyncTask<Void, Void, Long>() {
                    @Override
                    protected Long doInBackground(Void... voids) {
                        PDocsDataModel doc = listToSync.get(0);
                        doc.setSynced(true);
                        downloadsAllFiles(doc);
                        return BuilderStormApplication.mLocalDatabase.insertProjectDocs(
                                BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(), projectId,
                                doc.getCategoryUid(), String.valueOf(doc.getUid()),
                                new Gson().toJson(doc));
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

    private void downloadsAllFiles(PDocsDataModel doc) {
        String downloadUrl = CommonMethods.decodeUrl(doc.getUrlFile());
        if (!downloadUrl.isEmpty()) {
            CommonMethods.download(getDownloadManager(), downloadUrl, doc.getOriginalName(),
                    getApplication(), "");
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

    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(BuilderStormApplication.mLocalDatabase.getDocsIdList(BuilderStormApplication.mPrefs.getSiteId(),
                BuilderStormApplication.mPrefs.getUserId(), projectId));
        Log.e("synced List", new Gson().toJson(syncedList));
    }
}
