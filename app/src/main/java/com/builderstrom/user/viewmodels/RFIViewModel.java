package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.database.databaseModels.DbRFI;
import com.builderstrom.user.repository.database.databaseModels.DiaryMetaDataDb;
import com.builderstrom.user.repository.database.databaseModels.Users;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.AddRFIModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.MetaDataField;
import com.builderstrom.user.repository.retrofit.modals.PojoAttachment;
import com.builderstrom.user.repository.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.repository.retrofit.modals.PojoRfiComments;
import com.builderstrom.user.repository.retrofit.modals.RFIDataModel;
import com.builderstrom.user.repository.retrofit.modals.RFILiveModel;
import com.builderstrom.user.repository.retrofit.modals.Rfi;
import com.builderstrom.user.repository.retrofit.modals.RfiComment;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.repository.retrofit.modals.UserModel;
import com.builderstrom.user.service.SyncAllRfiTask;
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
public class RFIViewModel extends BaseViewModel {
    public MutableLiveData<RFILiveModel> rfiModelData;
    public MutableLiveData<Boolean> isOnlineAdapterLD;
    public MutableLiveData<List<RfiComment>> commentLiveData;
    public MutableLiveData<List<MetaDataField>> metaFieldLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> notifyRfiAdapterLd;
    public MutableLiveData<Boolean> allSyncedRfiLD;

    private List<String> syncedList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private int[] offlineLocks = {0, 0, 0, 0, 0, 0};
    private List<Rfi> listToSync = new ArrayList<>();

    public RFIViewModel(@NonNull Application application) {
        super(application);
        isSuccess = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        isOnlineAdapterLD = new MutableLiveData<>();
        rfiModelData = new MutableLiveData<>();
        commentLiveData = new MutableLiveData<>();
        metaFieldLD = new MutableLiveData<>();
        notifyRfiAdapterLd = new MutableLiveData<>();
        allSyncedRfiLD = new MutableLiveData<>();
    }

    public void getAllRFI(String date, Integer offset, Integer limit) {
        updateSyncedList();
        isLoadingLD.postValue(true);
        RestClient.createService().getRfiList(mPrefs.getProjectId(), date, offset, limit).enqueue(
                new Callback<RFIDataModel>() {
                    @Override
                    public void onResponse(@NonNull Call<RFIDataModel> call, @NonNull Response<RFIDataModel> response) {
                        setLogs(RFIViewModel.class.getName(), "getRFIList", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                if (offset <= 0) {
                                    isOnlineAdapterLD.postValue(true);
                                }
                                offsetLD.postValue(response.body().getDatalimit().getOffset());
                                if (null != response.body().getData().getRfis()) {
                                    for (Rfi rfi : response.body().getData().getRfis()) {
                                        if (syncedList.contains(rfi.getId())) {
                                            updateRFIDataBase(rfi);
                                            rfi.setSynced(true);
                                        }
                                    }
                                }
                                rfiModelData.postValue(new RFILiveModel(
                                        response.body().getData().getRfis() == null ?
                                                new ArrayList<>() : response.body().getData().getRfis(),
                                        response.body().getData().getRfitotals()));
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                        isLoadingLD.postValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<RFIDataModel> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getRFIList", t.getLocalizedMessage());
                        accessOfflineRFIs();
                    }
                });
    }

    private void updateRFIDataBase(Rfi rfi) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                /* downLoad allFiles*/
                for (PojoAttachment fileModel : rfi.getAttachments()) {
                    downloadFile(CommonMethods.decodeUrl(fileModel.getUrl().isEmpty()
                                    ? fileModel.getFile_url() : fileModel.getUrl()),
                            fileModel.getOriginal_name(), false);
                }
                return mDatabase.updateSyncRFI(mPrefs.getSiteId(),
                        mPrefs.getUserId(), mPrefs.getProjectId(), rfi.getId(),
                        rfi.getRequestBy(), "", "", rfi.getTitle(),
                        rfi.getDescription(), rfi.getCreatedOn(), rfi.getDuedate(),
                        new Gson().toJson(rfi), new Gson().toJson(rfi.getCustom_field_data()),
                        String.valueOf(rfi.getIsAnswered()), rfi.getStatus());
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
            }
        }.execute();
    }

    public void addAnswerToRfi(String answerString, String rfiId, List<String> files) {
        isLoadingLD.postValue(true);
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (files != null) {
            for (String filePath : files) {
                parts.add(CommonMethods.createPartFromFile("diaryFiles" + files.indexOf(filePath), filePath));
            }
        }

        RestClient.createService().apiAddRFIAnswer(rfiId, CommonMethods.createPartFromString(answerString), parts)
                .enqueue(new Callback<PojoNewSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                        isLoadingLD.setValue(false);
                        setLogs(RFIViewModel.class.getName(), "addAnswerToRfi", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                isSuccess.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getRFIList", t.getLocalizedMessage());
                        isLoadingLD.postValue(false);
                        errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                    }
                });
    }

    public void addRfiComment(String rfiId, String comment, List<String> filesList) {
        isLoadingLD.postValue(true);
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (String filePath : filesList) {
            partList.add(CommonMethods.createPartFromFile("diaryfiles" + filesList.indexOf(filePath), filePath));
        }

        RestClient.createService().apiAddRFIComment(
                CommonMethods.createPartFromString(rfiId),
                CommonMethods.createPartFromString(comment),
                partList).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(RFIViewModel.class.getName(), "addRfiComment", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(RFIViewModel.class.getName(), "addRfiComment", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void getAllComments(String rfiId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiRfiAllComments(rfiId).enqueue(new Callback<PojoRfiComments>() {
            @Override
            public void onResponse(@NonNull Call<PojoRfiComments> call, @NonNull Response<PojoRfiComments> response) {
                setLogs(RFIViewModel.class.getName(), "getAllComments", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        commentLiveData.postValue(response.body().getData());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoRfiComments> call, @NonNull Throwable t) {
                setLogs(RFIViewModel.class.getName(), "getAllComments", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void callAddRFI(String title, String dueDate, String description, String submittedTime,
                           String toUsers, String ccUsers, List<LocalMetaValues> metaData, List<String> parts) {

        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String filepath : parts) {
            filesPart.add(CommonMethods.createPartFromFile(String.format("photo[%s]", parts.indexOf(filepath)), filepath));
        }

        isLoadingLD.postValue(true);
        RestClient.createService().apiAddRfi(CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(mPrefs.getProjectId()),
                CommonMethods.createPartFromString(description),
                CommonMethods.createPartFromString(dueDate),
                CommonMethods.createPartFromString(submittedTime),
                CommonMethods.createPartFromString(toUsers),
                CommonMethods.createPartFromString(ccUsers),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                filesPart).enqueue(new Callback<AddRFIModel>() {
            @Override
            public void onResponse(@NonNull Call<AddRFIModel> call, @NonNull Response<AddRFIModel> response) {
                setLogs(RFIViewModel.class.getName(), "add_RFI_API", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<AddRFIModel> call, @NonNull Throwable t) {
                setLogs(RFIViewModel.class.getName(), "callAddRFI", t.getLocalizedMessage());
                addRfiToDataBase(title, toUsers, ccUsers, description, dueDate, parts, metaData);
            }
        });
    }

    public void getAllToUsers() {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAllUsers(mPrefs.getProjectId(), "0", DataNames.RFI_SECTION_USERS)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                        setLogs(RFIViewModel.class.getName(), "getAllRFIUsers", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                /** update users in local database */
                                updateUsersToDatabase(response.body().getAllUsers(), DataNames.RFI_SECTION_USERS);
                                usersLD.postValue(response.body().getAllUsers());
                            } else {
                                usersLD.postValue(new ArrayList<>());
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                        isLoadingLD.postValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getAllRFIUsers", t.getLocalizedMessage());
                        getUsersFromDB();
                    }
                });
    }

    public void getMetadata() {
        isLoadingLD.postValue(true);
        RestClient.createService().getDiaryAPIMetaData(mPrefs.getProjectId(), DataNames.META_RFI_SECTION).enqueue(
                new Callback<DiaryMetaDataModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DiaryMetaDataModel> call, @NonNull Response<DiaryMetaDataModel> response) {
                        setLogs(RFIViewModel.class.getName(), "getRFIMetadata", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                insertDiaryMetaData(response.body().getFields());
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                        isLoadingLD.postValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<DiaryMetaDataModel> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getRFIMetadata", t.getLocalizedMessage());
                        updateFromDataBase();
                        /*if (CommonMethods.isNetworkError(t)) {
                            updateFromDataBase();
                        } else {
                            isLoadingLD.postValue(false);
                            errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                        }*/
                    }
                });
    }

    /* Database Operations */
    private void accessOfflineRFIs() {
        new AsyncTask<Void, Void, List<DbRFI>>() {

            @Override
            protected List<DbRFI> doInBackground(Void... voids) {
                return mDatabase.getProjectRFIs(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId());
            }

            @Override
            protected void onPostExecute(List<DbRFI> dbRFIs) {
                super.onPostExecute(dbRFIs);
                List<Rfi> rfiList = new ArrayList<>();
                clearArray();
                isOnlineAdapterLD.postValue(false);

                if (null != dbRFIs) {
                    for (DbRFI rfi : dbRFIs) {
                        Rfi rfiObj;
                        if (CommonMethods.isOfflineEntry(rfi.getRowId())) {
                            rfiObj = new Rfi();
                            rfiObj.setTitle(rfi.getTitle());
                            rfiObj.setDescription(rfi.getDescription());
                            rfiObj.setUId(rfi.getUserID());
                            rfiObj.setRequestBy(rfi.getUsername());
                            rfiObj.setCreatedOn(rfi.getTimestamp());
                            rfiObj.setDuedate(rfi.getDate());
                            rfiObj.setPId(rfi.getProjectID());
                            rfiObj.setStatus(rfi.getStatus());
                            rfiObj.setIsAnswered(rfi.getIsAnswered());
                            rfiObj.setSynced(false);
                            rfiObj.setCan_answer(false);
                            rfiObj.setShowStatus(Integer.parseInt(rfi.getStatus()));
                            rfiObj.setOfflineFiles(rfi.getRfiData());
                            List<User> ccUsersList = new ArrayList<>();
                            if (!rfi.getCcUsers().isEmpty()) {
                                for (String userId : rfi.getCcUsers().split(",")) {
                                    for (User user : usersList) {
                                        if (user.getUserId().equals(userId)) {
                                            ccUsersList.add(user);
                                            break;
                                        }
                                    }
                                }
                            }
                            List<User> toUsersList = new ArrayList<>();
                            if (!rfi.getToUsers().isEmpty()) {
                                for (String userId : rfi.getToUsers().split(",")) {
                                    for (User user : usersList) {
                                        if (user.getUserId().equals(userId)) {
                                            toUsersList.add(user);
                                            break;
                                        }
                                    }
                                }
                            }
                            rfiObj.setCcusers(ccUsersList);
                            rfiObj.setTousers(toUsersList);

                        } else {
                            rfiObj = new Gson().fromJson(rfi.getRfiData(), Rfi.class);
                        }
                        /* update Locks Counts*/
                        setStatusLockCounts(rfiObj.getShowStatus(), rfiObj.getIsAnswered(), rfiObj.getDuedate());
                        rfiObj.setOpened(false);
                        rfiList.add(rfiObj);
                    }
                }

                /* update fragment for offline List and view */
                isLoadingLD.postValue(false);
                rfiModelData.postValue(new RFILiveModel(rfiList, offlineLocks));
            }
        }.execute();

    }

    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(mDatabase.getRfiIdList(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId()));
    }

    private void setStatusLockCounts(Integer status, Boolean isAnswered, String dueDateString) {
        Date dueDate = CommonMethods.convertToDate(dueDateString);
        Date currentDate = new Date();
        if (status == 3) {
            offlineLocks[0] = offlineLocks[0] + 1;
        } else if (status != 2) {
            if (isAnswered) {
                if (currentDate.before(dueDate)) {
                    if (status == 0) {
                        offlineLocks[1] = offlineLocks[1] + 1;
                    } else {
                        offlineLocks[2] = offlineLocks[2] + 1;
                    }
                } else {
                    if (status == 0) {
                        offlineLocks[1] = offlineLocks[1] + 1;
                    } else {
                        offlineLocks[3] = offlineLocks[3] + 1;
                    }
                }
            } else {
                if (currentDate.before(dueDate)) {
                    offlineLocks[4] = offlineLocks[4] + 1;
                } else {
                    offlineLocks[5] = offlineLocks[5] + 1;
                }
            }
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

    private void addRfiToDataBase(String title, String toUsers, String ccUsers, String description, String dueDate, List<String> parts, List<LocalMetaValues> metaData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                Long result = null;
                try {
                    result = mDatabase.insertRFI(
                            mPrefs.getSiteId(),
                            mPrefs.getUserId(),
                            mPrefs.getProjectId(), "0",
                            mPrefs.getUserName(),
                            toUsers, ccUsers, title, description,
                            CommonMethods.getCurrentDate(CommonMethods.DF_1),
                            dueDate, new Gson().toJson(parts), new Gson().toJson(metaData), "0", "0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                globalSyncService(); /* for global syncing of all periodically */
                isSuccess.postValue(true);
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
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), DataNames.RFI_SECTION_USERS);
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

    public void updateUsers() {
        try {
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), DataNames.RFI_SECTION_USERS);
            if (userResponse != null && userResponse.getUserData() != null && !userResponse.getUserData().isEmpty()) {
                usersList.addAll(new Gson().fromJson(userResponse.getUserData(), new TypeToken<List<User>>() {
                }.getType()));
            } else {
                Users commonResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), "");
                if (commonResponse != null && commonResponse.getUserData() != null && !commonResponse.getUserData().isEmpty()) {
                    usersList.addAll(new Gson().fromJson(commonResponse.getUserData(), new TypeToken<List<User>>() {
                    }.getType()));
                } else {
                    usersList = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertDiaryMetaData(List<MetaDataField> fields) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_RFI_SECTION)) {
                    return mDatabase.updateMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_RFI_SECTION, new Gson().toJson(fields));
                } else {
                    return mDatabase.insertMetaDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), mPrefs.getUserName(), DataNames.META_RFI_SECTION, new Gson().toJson(fields));
                }
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                updateFromDataBase();
            }
        }.execute();
    }

    private void updateFromDataBase() {
        DiaryMetaDataDb dbMetaData = mDatabase.getMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_RFI_SECTION);
        List<MetaDataField> localList;
        if (null != dbMetaData) {
            localList = new Gson().fromJson(dbMetaData.getMetaData(), new TypeToken<List<MetaDataField>>() {
            }.getType());
            if (localList == null) {
                localList = new ArrayList<>();
            }
        } else localList = new ArrayList<>();
        isLoadingLD.postValue(false);
        metaFieldLD.postValue(localList);
    }

    public void syncRfiToDatabase(Rfi rfi) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                for (PojoAttachment fileModel : rfi.getAttachments()) {
                    String fileLocation = CommonMethods.decodeUrl(fileModel.getUrl().isEmpty() ?
                            fileModel.getFile_url() : fileModel.getUrl());
                    if (!fileLocation.isEmpty()) {
                        downloadFile(fileLocation, fileModel.getOriginal_name(), true);
                    }

                }
                return mDatabase.insertRFI(mPrefs.getSiteId(),
                        mPrefs.getUserId(), rfi.getProjectId(), rfi.getId(),
                        rfi.getRequestBy(), "", "", rfi.getTitle(),
                        rfi.getDescription(), rfi.getCreatedOn(), rfi.getDuedate(),
                        new Gson().toJson(rfi), new Gson().toJson(rfi.getCustom_field_data()),
                        String.valueOf(rfi.getIsAnswered()), rfi.getStatus());
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                updateSyncedList();
                notifyRfiAdapterLd.postValue(true);
            }
        }.execute();

    }

    public void syncAllRfiList(List<Rfi> rfiList) {
        updateSyncedList();
        getListToSync(rfiList);
        if (!listToSync.isEmpty()) {
            /* Sync list */
            if (isAlreadyScheduleRfiJob()) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Already syncing"));
            } else {
                PersistableBundle dataBundle = new PersistableBundle();
                dataBundle.putString("data", new Gson().toJson(listToSync));
                getScheduler().schedule(new JobInfo.Builder(DataNames.SYNC_ALL_RFI_SERVICE_ID,
                        new ComponentName(getApplication(), SyncAllRfiTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(dataBundle)
                        .build());
            }
        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Nothing to sync"));
        }
    }

    private void getListToSync(List<Rfi> rfiList) {
        listToSync.clear();
        for (Rfi rfi : rfiList) {
            if (!syncedList.contains(rfi.getId())) {
                listToSync.add(rfi);
            }
        }
    }

    public void updateAllSyncRfiStatus(List<Rfi> rfiList) {
        boolean allSynced = !rfiList.isEmpty();
        for (Rfi rfi : rfiList) {
            if (!syncedList.contains(rfi.getId())) {
                allSynced = false;
                break;
            }
        }
        allSyncedRfiLD.postValue(allSynced);
    }
}
