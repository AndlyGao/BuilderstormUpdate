package com.builderstrom.user.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.builderstrom.user.views.offlineToOnline.SyncCompleteDatabase;
import com.builderstrom.user.utils.BuilderStormApplication;

public class DbSyncSchedulerTask extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        if (isNothingToSync()) {
            jobFinished(params, true);
        } else {
            new SyncCompleteDatabase(getApplicationContext()).executeSyncing();
        }
        return true;
    }

    private boolean isNothingToSync() {
        return BuilderStormApplication.mLocalDatabase.anySignRecord()
                && BuilderStormApplication.mLocalDatabase.noDrawingsToSync()
                && BuilderStormApplication.mLocalDatabase.noGalleryToSync()
                && BuilderStormApplication.mLocalDatabase.noDiaryToSync()
                && BuilderStormApplication.mLocalDatabase.noRfiToSync()
                && BuilderStormApplication.mLocalDatabase.noSnagToSync()
                && BuilderStormApplication.mLocalDatabase.noDiDocToSync();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
