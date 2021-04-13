package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.database.databaseModels.Users;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoCommon;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.data.retrofit.modals.UserModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ShareViewModel extends BaseViewModel {
    private static String TAG = ShareViewModel.class.getName();
    public MutableLiveData<Boolean> isSuccess;

    public ShareViewModel(@NonNull Application application) {
        super(application);
        isSuccess = new MutableLiveData<>();
    }

    public void issueDigitalDocument(MyItemModel model) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiIssueDocument(model.getProjectId(), model.getUsers(),
                model.getDocId(), model.getIssueId(), model.getRefId(), model.getCompletedFor(),
                model.getIsCompleted()).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        updateIssuedDocument(model.getRefId(), model.getDocId());
                        isSuccess.postValue(true);
                    } else handleErrorBody(response.errorBody());
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                insertDataInDatabase(model);
            }
        });
    }

    private void insertDataInDatabase(MyItemModel model) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertShareDocument(mPrefs.getSiteId(), mPrefs.getUserId(), new Gson().toJson(model));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                updateIssuedDocument(model.getRefId(), model.getDocId());
                isSuccess.postValue(true);
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, aLong != null ?
                        "Document issued successfully" : "Operation failed"));
            }
        }.execute();
    }

    public void shareDocument(String docsId, String userList) {
        isLoadingLD.postValue(true);
        RestClient.createService().addPDShare(docsId, BuilderStormApplication.mPrefs.getProjectId(), userList).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                CommonMethods.setLogs(TAG, "Share Document", new Gson().toJson(response));
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
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "ShareViewModel", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void shareCompanyDoc(String documentId, String documentTitle, String userList) {
        isLoadingLD.postValue(true);

        RestClient.createService().apiShareCompanyDoc(documentId, 1, "",
                documentTitle, userList).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                CommonMethods.setLogs(TAG, "Share Company Document", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.setValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Share Company Document", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void shareRFI(String docsId, String toUsers, String ccUsers) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiShareRFI(docsId, mPrefs.getProjectId(), toUsers, ccUsers)
                .enqueue(new Callback<PojoCommon>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                        CommonMethods.setLogs(TAG, "Share Document", new Gson().toJson(response));
                        isLoadingLD.postValue(false);
                        try {
                            if (response.isSuccessful() && null != response.body()) {
                                isSuccess.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                        CommonMethods.setLogs(TAG, "updateTimeSheet", t.getLocalizedMessage());
                        isLoadingLD.postValue(false);
                        errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                    }
                });
    }

    public void shareSnag(String docsId, String toUsers, String ccUsers) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiShareSnag(docsId, mPrefs.getProjectId(), toUsers, ccUsers)
                .enqueue(new Callback<PojoCommon>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                        CommonMethods.setLogs(TAG, "Share Snag", new Gson().toJson(response));
                        isLoadingLD.postValue(false);
                        try {
                            if (response.isSuccessful() && null != response.body()) {
                                isSuccess.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                        CommonMethods.setLogs(TimeSheetViewModel.class.getName(), "updateTimeSheet", t.getLocalizedMessage());
                        isLoadingLD.postValue(false);
                        errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                    }
                });
    }

    public void getShareUsers(String section) {
        RestClient.createService().apiAllUsers(mPrefs.getProjectId(), "0", section).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /** update users in local database */
                        updateUsersToDatabase(response.body().getAllUsers(), section);
                        usersLD.postValue(response.body().getAllUsers());
                    } else {
                        usersLD.postValue(new ArrayList<>());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    getOfflineUsersList(section);
                }
            }
        });
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


    private void getOfflineUsersList(String section) {
        List<User> allUserList = new ArrayList<>();
        try {
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section);
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
        usersLD.postValue(allUserList);
    }

    private void updateIssuedDocument(String recType, String templateId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return mDatabase.getMyItemRowId(mPrefs.getSiteId(), mPrefs.getProjectId(), templateId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String columnId) {
                super.onPostExecute(columnId);
                if (null != columnId) {
                    mDatabase.deleteDDTemplateRow(Integer.parseInt(columnId));
//                    if (!recType.isEmpty() && !recType.equals("0")) {
//                        mDatabase.updateCompleteDate(mPrefs.getSiteId(), mPrefs.getUserId(), Integer.parseInt(columnId));
//                    } else {
//                        mDatabase.deleteDDTemplateRow(Integer.parseInt(columnId));
//                    }
                }
            }
        }.execute();
    }


}
