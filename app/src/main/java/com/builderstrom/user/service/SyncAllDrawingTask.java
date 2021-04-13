package com.builderstrom.user.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.Datum;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SyncAllDrawingTask extends JobService {
    private List<Datum> listToSync;
    private JobParameters params;
    private boolean isWorking = false;
    private DownloadManager mDownloadManager;

    @Override
    public boolean onStartJob(JobParameters params) {
        if (!isWorking) {
            this.params = params;
            isWorking = true;
            String jobString = params.getExtras().getString("data");
            listToSync = new Gson().fromJson(jobString, new TypeToken<List<Datum>>() {
            }.getType());
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
            new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    Datum data = listToSync.get(0);
                    if (data.getPdflocation() != null && !data.getPdflocation().isEmpty()) {
                        CommonMethods.download(getDownloadManager(), data.getPdflocation(), data.getPdfname(), getApplicationContext(), "");
                    }
                    if (null != data.getDwglocation() && !data.getDwglocation().isEmpty()) {
                        CommonMethods.download(getDownloadManager(), data.getDwglocation(), data.getDwgname(), getApplicationContext(), "");
                    }
                    return BuilderStormApplication.mLocalDatabase.insertDrawing(BuilderStormApplication.mPrefs.getSiteId(),
                            BuilderStormApplication.mPrefs.getUserId(),
                            BuilderStormApplication.mPrefs.getProjectId(), data.getId(), 1, new Gson().toJson(data));
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
        } else {
            jobFinished(params, false);
            callBroadCast();
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
