package com.builderstrom.user.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;

import com.builderstrom.user.utils.CommonMethods;

public class DownloadTask extends JobService {
    private JobParameters params;
    private DownloadManager mDownloadManager;

    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        downloadFile();
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadFile() {
        if (params != null) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String file_url=params.getExtras().getString("link");
                    String file_name=params.getExtras().getString("name");
                    return CommonMethods.download(getDownloadManager(), file_url, file_name,getApplicationContext(),"");
                }

                @Override
                protected void onPostExecute(String aVoid) {
                    super.onPostExecute(aVoid);
                    onStopJob(params);
                }
            }.execute();
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public DownloadManager getDownloadManager() {
        if (mDownloadManager == null) {
            mDownloadManager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return mDownloadManager;
    }
}
