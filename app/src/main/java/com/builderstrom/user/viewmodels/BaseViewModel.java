package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.data.Prefs;
import com.builderstrom.user.data.database.DatabaseHelper;
import com.builderstrom.user.data.database.databaseModels.UserProjects;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoErrorModel;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.service.DbSyncSchedulerTask;
import com.builderstrom.user.service.DownloadTask;
import com.builderstrom.user.utils.ClientLogger;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public abstract class BaseViewModel extends AndroidViewModel {
    public Prefs mPrefs;
    public DatabaseHelper mDatabase;
    public MutableLiveData<Boolean> isLoadingLD;
    public MutableLiveData<Boolean> isUploadingLD;
    public MutableLiveData<ErrorModel> errorModelLD;
    public MutableLiveData<List<User>> usersLD;
    private JobScheduler scheduler;
    private static String TAG = BaseViewModel.class.getName();
    String firebaseToken = "";
    protected Gson gSon;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mPrefs = Prefs.with(application);
        mDatabase = DatabaseHelper.getDatabaseInstance(application);
        usersLD = new MutableLiveData<>();
        isLoadingLD = new MutableLiveData<>();
        isUploadingLD = new MutableLiveData<>();
        errorModelLD = new MutableLiveData<>();
        gSon = new Gson();
    }


    public boolean isInternetAvailable() {
        return CommonMethods.isNetworkAvailable(getApplication());
    }

    public void filePreview(String fileUrl, String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (CommonMethods.isImageUrl(fileName)) {
                Intent intent = new Intent(getApplication(), ImageFullScreenActivity.class);
                if (isInternetAvailable()) {
                    intent.putExtra("imageUrl", CommonMethods.decodeUrl(fileUrl));
                } else {
                    intent.putExtra("fileName", fileName);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent);
            } else if (extension.equalsIgnoreCase(".pdf")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(isInternetAvailable() ? Uri.parse(CommonMethods.decodeUrl(fileUrl))
                            : CommonMethods.getUriFromFileName(getApplication(), fileName), "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    getApplication().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // No application to view, ask to download one
                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(new Intent(DataNames.DIALOG_FLAG).putExtra("IS_PDF", true));
                }
            } else if (extension.equalsIgnoreCase(".dwg")) {
                boolean installed = CommonMethods.appInstalledOrNot(getApplication(), "com.gstarmc.android");
                if (installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent launchIntent = getApplication().getPackageManager().getLaunchIntentForPackage("com.gstarmc.android");
                    if (launchIntent != null) {
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    getApplication().startActivity(launchIntent);
                } else {
                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(new Intent(DataNames.DIALOG_FLAG).putExtra("IS_PDF", false));
                }
            } else {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "There is no preview available"));
            }
        }
    }

    void handleErrorBody(ResponseBody errorBody) throws IOException {
        Converter<ResponseBody, PojoErrorModel> errorConverter =
                RestClient.getRetrofitInstance().responseBodyConverter(PojoErrorModel.class, new Annotation[0]);
        PojoErrorModel error = errorConverter.convert(errorBody);
        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API, error.getMessage()));
    }

    protected void setLogs(String tag, String methodName, String message) {
        ClientLogger.d(tag, "------------------------------------------------------------------------");
        ClientLogger.d(tag, String.format("---------------------------- ON  %s ----------------------------", methodName));
        ClientLogger.d(tag, message);
    }

    public void globalSyncService() {
        if (isInternetAvailable()) {
            if (!alreadyScheduleSyncJob()) {
                scheduler.schedule(new JobInfo.Builder(DataNames.SYNC_SLOW_CONNECTION_SERVICE_ID,
                        new ComponentName(getApplication(), DbSyncSchedulerTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPeriodic(15 * 60 * 1000) // job reschedules after 20 minutes(15 minutes default time)
                        .build());
            }
        }
    }

    private boolean alreadyScheduleSyncJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_SLOW_CONNECTION_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyScheduleRfiJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_ALL_RFI_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyScheduleSnagJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_ALL_SNAG_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadySchedulePDocsJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_ALL_PROJECT_DOCS_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyScheduleComDocsJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_ALL_COMPANY_DOCS_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyScheduleToDoJob() {
        for (JobInfo jobInfo : getScheduler().getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_ALL_TO_DO_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    public void downloadFile(String fileLocation, String fileName, boolean showToast) {
        if (isInternetAvailable()) {
            if (!CommonMethods.isFileDownloaded(fileName)) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Downloading start..."));
                singleDownloadService(fileLocation, fileName);
            } else {
                if (showToast) {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "File Already Downloaded"));
                }
            }
        } else {
            if (showToast)
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.no_internet_connection)));
        }
    }

    private void singleDownloadService(String link, String fileName) {
        JobScheduler scheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        PersistableBundle dataBundle = new PersistableBundle();
        dataBundle.putString("link", link);
        dataBundle.putString("name", fileName);
        if (scheduler != null) {
            scheduler.schedule(new JobInfo.Builder(DataNames.SINGLE_DOWNLOAD_SERVICE_ID,
                    new ComponentName(getApplication(), DownloadTask.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(dataBundle)
                    .build());
        }

    }

    JobScheduler getScheduler() {
        if (scheduler == null) {
            scheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        return scheduler;
    }

    void getFBToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    // Get new Instance ID token
                    firebaseToken = task.getResult().getToken();
                    Log.e(TAG, firebaseToken);

                });
    }

    @SuppressLint("StaticFieldLeak")
    public void updateSavedProjects(List<ProjectDetails> allProjects) {
        new AsyncTask<Void, Void, UserProjects>() {
            @Override
            protected UserProjects doInBackground(Void... voids) {
                return mDatabase.getUserProjects(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(UserProjects projects) {
                super.onPostExecute(projects);
                // Insert or Update projects in local database
                try {
                    if (projects != null) {
                        mDatabase.updateProjects(mPrefs.getSiteId(), mPrefs.getUserId(), new Gson().toJson(allProjects));
                    } else {
                        mDatabase.insertProjects(mPrefs.getSiteId(), mPrefs.getUserId(), new Gson().toJson(allProjects));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    LocalBroadcastManager.getInstance(getApplication())
                            .sendBroadcast(new Intent(DataNames.INTENT_UPDATE_PROJECTS_LIST)
                                    .putExtra("KEY_FLAG", true));

                }
            }
        }.execute();
    }


}
