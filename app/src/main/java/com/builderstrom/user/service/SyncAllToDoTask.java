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

import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.Attachment;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.PojoToDo;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SyncAllToDoTask extends JobService {
    private List<PojoToDo> listToSync;
    private List<String> syncedList = new ArrayList<>();
    private JobParameters params;
    private boolean isWorking = false;
    private String projectId = "";
    private DownloadManager mDownloadManager;

    @Override
    public boolean onStartJob(JobParameters params) {
        if (!isWorking) {
            this.params = params;
            this.isWorking = true;
            String jobString = params.getExtras().getString("data");
            listToSync = new Gson().fromJson(jobString, new TypeToken<List<PojoToDo>>() {
            }.getType());
            if (listToSync != null && !listToSync.isEmpty()) {
                updateSyncedList();
                projectId = String.valueOf(listToSync.get(0).getProjectId());
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
            if (syncedList.contains(String.valueOf(listToSync.get(0).getId()))) {
                listToSync.remove(0);
                handleSyncing();
            } else {
                new AsyncTask<Void, Void, Long>() {
                    @Override
                    protected Long doInBackground(Void... voids) {
                        PojoToDo doc = listToSync.get(0);
                        doc.setSynced(true);
                        doc.setSelected(false);
                        downloadsAllFiles(doc);
                        return BuilderStormApplication.mLocalDatabase.insertToDoItem(
                                BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(),
                                String.valueOf(doc.getProjectId()), String.valueOf(doc.getId()),
                                String.valueOf(doc.getCategory()), new Gson().toJson(doc));
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

    private void downloadsAllFiles(PojoToDo doc) {
        if (doc.getAttachments() != null && !doc.getAttachments().isEmpty()) {
            for (Attachment attachment : doc.getAttachments()) {
                if (!attachment.getFileName().isEmpty()) {
                    CommonMethods.download(getDownloadManager(), attachment.getFileUrl(),
                            attachment.getFileName(), getApplication(), "");
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


    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(BuilderStormApplication.mLocalDatabase.getSyncedToDos(
                BuilderStormApplication.mPrefs.getSiteId(),
                BuilderStormApplication.mPrefs.getUserId(), projectId));
        Log.e("synced To Do List", new Gson().toJson(syncedList));
    }


}
