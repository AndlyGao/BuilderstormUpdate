package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.database.databaseModels.DbSnag;
import com.builderstrom.user.repository.database.databaseModels.Users;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.AddMoreComment;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.PojoAttachment;
import com.builderstrom.user.repository.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.repository.retrofit.modals.PojoSnagCommentModel;
import com.builderstrom.user.repository.retrofit.modals.PojoSnagListModel;
import com.builderstrom.user.repository.retrofit.modals.Snag;
import com.builderstrom.user.repository.retrofit.modals.SnagComment;
import com.builderstrom.user.repository.retrofit.modals.SnagDataModel;
import com.builderstrom.user.repository.retrofit.modals.SnagLocksModel;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.repository.retrofit.modals.UserModel;
import com.builderstrom.user.service.SyncAllSnagsTask;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class SnagViewModel extends BaseViewModel {

    private static String TAG = SnagViewModel.class.getName();

    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Boolean> isOfflineSnagAdapterLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<SnagLocksModel> updateLocksLD;
    public MutableLiveData<SnagDataModel> dataModelLD;
    public MutableLiveData<Boolean> updateSnagLD;
    public MutableLiveData<Boolean> snagAddedLD;
    public MutableLiveData<List<SnagComment>> snagCommentsLD;
    public MutableLiveData<Boolean> snagCommentAddedLD;
    public MutableLiveData<Boolean> notifySnagAdapterLD;
    public MutableLiveData<Boolean> allSyncedStatusLd;

    private List<String> syncedList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private List<Snag> listToSync = new ArrayList<>();
    private int[] offlineLocks = {0, 0, 0, 0, 0, 0};


    public SnagViewModel(@NonNull Application application) {
        super(application);

        isRefreshingLD = new MutableLiveData<>();
        isOfflineSnagAdapterLD = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        updateLocksLD = new MutableLiveData<>();
        dataModelLD = new MutableLiveData<>();
        updateSnagLD = new MutableLiveData<>();
        snagAddedLD = new MutableLiveData<>();
        notifySnagAdapterLD = new MutableLiveData<>();
        snagCommentsLD = new MutableLiveData<>();
        snagCommentAddedLD = new MutableLiveData<>();
        allSyncedStatusLd = new MutableLiveData<>();
    }

    public void getSnagList(Integer offset, int limit) {
        isRefreshingLD.postValue(true);
        RestClient.createService().apiGetAllSnags(mPrefs.getProjectId(), offset, limit).enqueue(new Callback<PojoSnagListModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoSnagListModel> call, @NonNull Response<PojoSnagListModel> response) {
                isRefreshingLD.postValue(false);
                setLogs(TAG, "getSnagList", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        if (offset <= 0) {
                            isOfflineSnagAdapterLD.postValue(false);
                        }
                        for (Snag snag : response.body().getData().getSnags()) {
                            if (syncedList.contains(snag.getId())) {
                                updateSyncedSnag(snag);
                                snag.setSynced(true);
                            }
                        }

                        updateLocksLD.postValue(new SnagLocksModel(response.body().getData().getSnagTotals(), null));
                        dataModelLD.postValue(new SnagDataModel(
                                response.body().getData().getSnags() == null ? new ArrayList<>()
                                        : response.body().getData().getSnags(), false));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<PojoSnagListModel> call, @NonNull Throwable t) {
                setLogs(TAG, "getSnagList", t.getLocalizedMessage());
                getOfflineSnagLists();
            }
        });
    }


    public void updateSnagMark(String snagId, Integer markPosition) {
        isRefreshingLD.postValue(true);
        RestClient.createService().apiUpdateSnagMark(snagId, markPosition).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshingLD.postValue(false);
                setLogs(TAG, "updateSnagMark", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        updateSnagLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));

                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshingLD.postValue(false);
                setLogs(TAG, "updateSnagMark", t.getLocalizedMessage());
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addSnag(String title, String location, String packNo, String toUsers, String ccUsers, String dueDate,
                        String submittedTime, List<String> parts) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (String filepath : parts) {
            filesPart.add(CommonMethods.createPartFromFile("diaryfiles" + parts.indexOf(filepath), filepath));
        }

        isLoadingLD.postValue(true);
        RestClient.createService().apiAddSnag(mPrefs.getProjectId(),
                CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(location),
                CommonMethods.createPartFromString(packNo),
                CommonMethods.createPartFromString(toUsers),
                CommonMethods.createPartFromString(ccUsers),
                CommonMethods.createPartFromString(dueDate),
                CommonMethods.createPartFromString(submittedTime), filesPart).enqueue(
                new Callback<AddMoreComment>() {
                    @Override
                    public void onResponse(@NonNull Call<AddMoreComment> call, @NonNull Response<AddMoreComment> response) {
                        setLogs(TAG, "addSnag", new Gson().toJson(response));
                        isLoadingLD.postValue(false);
                        try {
                            if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                                snagAddedLD.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddMoreComment> call, @NonNull Throwable t) {
                        setLogs(TAG, "addSnag", t.getLocalizedMessage());
                        insertSnagToDb(toUsers, ccUsers, location, packNo, title, dueDate, new Gson().toJson(parts));
                    }
                }
        );
    }

    public void getAllToUsers() {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAllUsers(mPrefs.getProjectId(), "0",
                DataNames.SNAG_SECTION_USERS).enqueue(new Callback<UserModel>() {

            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                setLogs(TAG, "getAllToUsers", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /** update users in local database */
                        updateUsersToDatabase(response.body().getAllUsers(), DataNames.SNAG_SECTION_USERS);
                        usersLD.postValue(response.body().getAllUsers());
                    } else {
                        usersLD.postValue(new ArrayList<>());
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    usersLD.postValue(new ArrayList<>());
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                setLogs(TAG, "getAllToUsers", t.getLocalizedMessage());
                getUsersFromDB();
            }
        });
    }

    public void getSnagComments(String snagId) {

        isLoadingLD.postValue(true);
        RestClient.createService().apiGetSnagComments(snagId).enqueue(new Callback<PojoSnagCommentModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoSnagCommentModel> call, @NonNull Response<PojoSnagCommentModel> response) {
                setLogs(TAG, "getSnagComments", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        snagCommentsLD.postValue(response.body().getComments());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));

                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSnagCommentModel> call, @NonNull Throwable t) {
                setLogs(TAG, "getSnagComments", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addComment(String snagId, String comment) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAddSnagComment(snagId, comment).enqueue(new Callback<AddMoreComment>() {
            @Override
            public void onResponse(@NonNull Call<AddMoreComment> call, @NonNull Response<AddMoreComment> response) {
                setLogs(TAG, "addComment", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        snagCommentAddedLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddMoreComment> call, @NonNull Throwable t) {
                setLogs(TAG, "addComment", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* Database Operations */

    public void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(mDatabase.getSyncSnagIdList(mPrefs.getSiteId(), mPrefs.getUserId(),
                mPrefs.getProjectId()));

    }

    public void updateUsers() {
        usersList.clear();
        Users commonResponse;
        try {
            commonResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(),
                    mPrefs.getUserId(), DataNames.RFI_SECTION_USERS);
        } catch (Exception e) {
            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
            commonResponse = null;
        }
        if (null != commonResponse) {
            usersList.addAll(new Gson().fromJson(commonResponse.getUserData(), new TypeToken<List<User>>() {
            }.getType()));
        }
    }

    private void getOfflineSnagLists() {
        new AsyncTask<Void, Void, List<DbSnag>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<DbSnag> doInBackground(Void... voids) {
                return mDatabase.getDbSnagList(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId());
            }

            @Override
            protected void onPostExecute(List<DbSnag> dbSnags) {
                super.onPostExecute(dbSnags);
                isOfflineSnagAdapterLD.postValue(true);
                List<Snag> snagList = new ArrayList<>();
                clearArray();
                if (dbSnags != null) {
                    for (DbSnag dbSnag : dbSnags) {
                        if (dbSnag.getRowId().equals("-1")) {
                            Snag snag = new Snag();
                            snag.setId(dbSnag.getRowId());
                            snag.setProjectSiteId(dbSnag.getSiteId());
                            snag.setUser_id(dbSnag.getUserId());
                            snag.setRequested_from(dbSnag.getUserName());
                            snag.setProject_id(dbSnag.getProjectId());
                            snag.setLocationId(dbSnag.getLocation());
                            snag.setPackageId(dbSnag.getPackageNo());
                            snag.setDueDate(dbSnag.getDueDate());
                            snag.setCreated_on(dbSnag.getTimeStamp());
                            snag.setDefect_details(dbSnag.getDescription());
                            snag.setSnag_status(dbSnag.getStatus());
                            snag.setCanMark(1);
                            List<Integer> statusList = new Gson().fromJson(dbSnag.getMarkStatus(), new TypeToken<List<Integer>>() {
                            }.getType());
                            snag.setMark_as(statusList == null ? new ArrayList<>() : statusList);
                            snag.setOfflineFiles(dbSnag.getItemData());
                            List<User> ccUsersList = new ArrayList<>();
                            if (!dbSnag.getCcUsers().isEmpty()) {
                                for (String userId : dbSnag.getCcUsers().split(",")) {
                                    for (User user : usersList) {
                                        if (user.getUserId().equals(userId)) {
                                            ccUsersList.add(user);
                                            break;
                                        }
                                    }
                                }
                            }

                            List<User> toUsersList = new ArrayList<>();
                            if (!dbSnag.getToUsers().isEmpty()) {
                                for (String userId : dbSnag.getToUsers().split(",")) {
                                    for (User user : usersList) {
                                        if (user.getUserId().equals(userId)) {
                                            toUsersList.add(user);
                                            break;
                                        }
                                    }
                                }
                            }
                            snag.setCcUsers(ccUsersList);
                            snag.setToUsers(toUsersList);
                            setUpShowStatus(snag);
                            snagList.add(snag);
                        } else {
                            Snag snag = new Gson().fromJson(dbSnag.getItemData(), Snag.class);
                            setUpShowStatus(snag);
                            snagList.add(snag);
                        }
                    }
                }
                isRefreshingLD.postValue(false);
                updateLocksLD.postValue(new SnagLocksModel(null, offlineLocks));
                dataModelLD.postValue(new SnagDataModel(snagList, false));
            }
        }.execute();
    }

    private void setUpShowStatus(Snag snag) {
        snag.setSelected(false);
        Date dueDate = CommonMethods.convertToDateNoTime(snag.getDueDate());
        Date currentDate = new Date();
        switch (snag.getSnag_status()) {
            case "2":
                if (dueDate.after(currentDate)) {
                    offlineLocks[2] = offlineLocks[2] + 1;
                    snag.setShow_status(2);
                } else {
                    offlineLocks[3] = offlineLocks[3] + 1;
                    snag.setShow_status(5);
                }
                break;
            case "0":
            case "4":
                if (snag.getSnag_status().equals("0") || snag.getSnag_status().equals("4")) {
                    if (dueDate.after(currentDate)) {
                        offlineLocks[4] = offlineLocks[4] + 1;
                        snag.setShow_status(0);
                    } else {
                        offlineLocks[5] = offlineLocks[5] + 1;
                        snag.setShow_status(4);
                    }
                }
                break;
            case "1":
                offlineLocks[1] = offlineLocks[1] + 1;
                snag.setShow_status(1);
                break;
            case "3":
                offlineLocks[0] = offlineLocks[0] + 1;
                snag.setShow_status(3);
                break;
        }
    }

    private void clearArray() {
        offlineLocks[0] = 0;
        offlineLocks[1] = 0;
        offlineLocks[2] = 0;
        offlineLocks[3] = 0;
        offlineLocks[4] = 0;
        offlineLocks[5] = 0;
    }

    private void insertSnagToDb(String toUsers, String ccUsers, String location, String packNo, String title, String dueDate, String files) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                List<Integer> markStatusList = new ArrayList<>();
                markStatusList.add(0);
                return mDatabase.insertSnag(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId(), "-1", mPrefs.getUserName(), toUsers, ccUsers,
                        location, packNo, title, dueDate, files, "0", new Gson().toJson(markStatusList));
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                globalSyncService();
                snagAddedLD.postValue(true);
            }
        }.execute();
    }


    private void updateUsersToDatabase(List<User> users, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateUsersList(mPrefs.getSiteId(), mPrefs.getUserId(),
                            section, new Gson().toJson(users));
                } else {
                    return mDatabase.insertUser(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), section, new Gson().toJson(users));
                }
            }
        }.execute();
    }


    private void getUsersFromDB() {
        List<User> allUserList = new ArrayList<>();
        try {
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), DataNames.SNAG_SECTION_USERS);
            if (userResponse != null && userResponse.getUserData() != null && !userResponse.getUserData().isEmpty()) {
                allUserList = new Gson().fromJson(userResponse.getUserData(), new TypeToken<List<User>>() {
                }.getType());
            } else {
                Users commonResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), "");
                if (commonResponse != null && commonResponse.getUserData() != null && !commonResponse.getUserData().isEmpty()) {
                    allUserList = new Gson().fromJson(commonResponse.getUserData(), new TypeToken<List<User>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoadingLD.postValue(false);
        usersLD.postValue(allUserList);
    }

    public void syncSnagToDataBase(Snag snag) {
        if (syncedList.contains(snag.getId())) {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Snag Already synced"));

        } else {
            snag.setSynced(true);
            snag.setSelected(false);
            new AsyncTask<Void, Void, Long>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isLoadingLD.postValue(true);
                }

                @Override
                protected Long doInBackground(Void... voids) {
                    if (null != snag.getAttachments()) {
                        for (PojoAttachment fileModel : snag.getAttachments()) {
                            String downloadUrl = CommonMethods.decodeUrl(fileModel.getUrl().isEmpty() ?
                                    fileModel.getFile_url() : fileModel.getUrl());
                            if (!downloadUrl.isEmpty()) {
                                downloadFile(downloadUrl, fileModel.getOriginal_name(), false);
                            }
                        }
                    }
                    return mDatabase.insertSnag(mPrefs.getSiteId(), mPrefs.getUserId(),/* mPrefs.getProjectId()*/ snag.getProjectId(),
                            snag.getId(), snag.getRequestedFrom(), new Gson().toJson(snag.getToUsers()),
                            new Gson().toJson(snag.getCcUsers()), snag.getLocationId(), snag.getPackageId(),
                            snag.getDefect_details(), snag.getDueDate(), new Gson().toJson(snag), snag.getSnag_status(),
                            new Gson().toJson(snag.getMark_as()));


                }

                @Override
                protected void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);
                    isLoadingLD.postValue(false);
                    updateSyncedList();
                    if (null != aLong) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                getApplication().getString(R.string.success_snag_sync)));
                        notifySnagAdapterLD.postValue(true);
                    }
                }
            }.execute();
        }


    }

    private void updateSyncedSnag(Snag snag) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != snag.getAttachments()) {
                    for (PojoAttachment fileModel : snag.getAttachments()) {
                        String downloadUrl = CommonMethods.decodeUrl(fileModel.getUrl().isEmpty() ?
                                fileModel.getFile_url() : fileModel.getUrl());
                        if (!downloadUrl.isEmpty()) {
                            downloadFile(downloadUrl, fileModel.getOriginal_name(), false);
                        }
                    }
                }
                return mDatabase.updateSnag(mPrefs.getSiteId(), mPrefs.getUserId(),
                        snag.getProjectId(), snag.getId(), new Gson().toJson(snag));

            }
        }.execute();

    }


    public void syncAllSnagList(List<Snag> snagList) {
        updateSyncedList();
        List<Snag> listToSync = getListToSync(snagList);
        if (!listToSync.isEmpty()) {
            /* Sync list */
            if (isAlreadyScheduleSnagJob()) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Already syncing"));
            } else {
                PersistableBundle dataBundle = new PersistableBundle();
                dataBundle.putString("data", new Gson().toJson(listToSync));
                getScheduler().schedule(new JobInfo.Builder(DataNames.SYNC_ALL_SNAG_SERVICE_ID,
                        new ComponentName(getApplication(), SyncAllSnagsTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(dataBundle)
                        .build());
            }


        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Nothing to sync"));
        }

    }

    private List<Snag> getListToSync(List<Snag> snagList) {
        listToSync.clear();
        for (Snag snag : snagList) {
            if (!syncedList.contains(snag.getId())) {
                listToSync.add(snag);
            }
        }
        return listToSync;
    }

    public void updateAllSyncedStatus(List<Snag> snagList) {
        boolean allSynced = !snagList.isEmpty();
        for (Snag snag : snagList) {
            if (!syncedList.contains(snag.getId())) {
                allSynced = false;
                break;
            }
        }
        allSyncedStatusLd.postValue(allSynced);
    }

}
