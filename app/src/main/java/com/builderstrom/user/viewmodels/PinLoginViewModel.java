package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.data.database.databaseModels.Login;
import com.builderstrom.user.data.database.databaseModels.Permission;
import com.builderstrom.user.data.database.databaseModels.UserProjects;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.LoginModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.ClientLogger;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class PinLoginViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> isPinSet;

    private String TAG = PinLoginViewModel.class.getName();

    public PinLoginViewModel(@NonNull Application application) {
        super(application);
        isPinSet = new MutableLiveData<>();
        getFBToken();
    }

    public void pinLogin(String pin) {
        isLoadingLD.postValue(true);
        RestClient.createService().pinLogin(pin, CommonMethods.getDeviceID(getApplication()), DataNames.DEVICE_TYPE, firebaseToken).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {

                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        mPrefs.setAuthToken(response.body().getAuthToken());
                        loginSuccess(response.body().getData(), pin);
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "callAuthenticateAPI", t.getLocalizedMessage());
                accessLocalDB(pin);
                /* if (CommonMethods.isNetworkError(t)) {
                    accessLocalDB(pin);
                } else {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));

                }*/
            }
        });
    }

    private void loginSuccess(LoginModel.Data data, String pin) {
        mDatabase.clearUsers();
        mPrefs.setIsLogin(true);
        mPrefs.setUserId(String.valueOf(data.getUid()));
        mPrefs.setUserEmail(data.getEmail());
        mPrefs.setProfileImage(data.getImage());
        mPrefs.setUserName(data.getName() + " " + data.getLastname());
        fetchUserDetails(data, pin);

    }

    private void fetchUserDetails(LoginModel.Data response, String pin) {
        new AsyncTask<Void, Void, Login>() {

            @Override
            protected Login doInBackground(Void... voids) {
                return mDatabase.checkLoginPin(String.valueOf(response.getUid()), mPrefs.getSiteId());
            }

            @Override
            protected void onPostExecute(Login loginWithPin) {
                super.onPostExecute(loginWithPin);
                // Insert/Update pin in local database
                try {
                    if (loginWithPin != null) {
                        mDatabase.updateCredentials(true, "", "",
                                CommonMethods.convertPassMd5(pin),
                                mPrefs.getSiteId(), String.valueOf(response.getUid()),
                                response.getName() + " " + response.getLastname(),
                                response.getEmail(), response.getImage(), firebaseToken, 1);

                    } else {

                        mDatabase.insertCredentials(true, "", "",
                                CommonMethods.convertPassMd5(pin),
                                mPrefs.getSiteId(), String.valueOf(response.getUid()),
                                response.getName() + " " + response.getLastname(),
                                response.getEmail(), response.getImage(), firebaseToken, 1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (response.getProjects() != null && !response.getProjects().isEmpty()) {
                        fetchProjects(response);
                    } else if (response.getPermissions() != null) {
                        fetchPermission(response);
                    } else {
                        isLoadingLD.postValue(false);
                        isPinSet.postValue(true);
                    }
                }
            }
        }.execute();
    }

    private void fetchProjects(LoginModel.Data data) {
        new AsyncTask<Void, Void, UserProjects>() {
            @Override
            protected UserProjects doInBackground(Void... voids) {
                return mDatabase.getUserProjects(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(UserProjects projects) {
                super.onPostExecute(projects);
                // Insert/Update projects in local database
                try {
                    if (projects != null) {
                        mDatabase.updateProjects(mPrefs.getSiteId(), String.valueOf(data.getUid()), new Gson().toJson(data.getProjects()));
                    } else {
                        mDatabase.insertProjects(mPrefs.getSiteId(), String.valueOf(data.getUid()), new Gson().toJson(data.getProjects()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (data.getPermissions() != null) {
                        fetchPermission(data);
                    } else {
                        isLoadingLD.postValue(false);
                        isPinSet.postValue(true);
                    }
                }
            }
        }.execute();

    }

    private void fetchPermission(LoginModel.Data data) {
        new AsyncTask<Void, Void, Permission>() {
            @Override
            protected Permission doInBackground(Void... voids) {
                return mDatabase.getPermission(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(Permission permission) {
                super.onPostExecute(permission);
                // Insert/Update User-Site Permission in local database
                try {
                    if (permission != null) {
                        mDatabase.updatePermission(BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(),
                                new Gson().toJson(data.getPermissions()),
                                new Gson().toJson(data.getPermissionSection()));
                    } else {
                        mDatabase.insertPermission(
                                BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(),
                                new Gson().toJson(data.getPermissions()),
                                new Gson().toJson(data.getPermissionSection())
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isLoadingLD.postValue(false);
                    isPinSet.postValue(true);
                }
            }
        }.execute();
    }

    /**
     * User is exist in local database
     */
    public boolean isUserExists() {
        return mPrefs.getUserId() != null && !mPrefs.getUserId().isEmpty() && !mPrefs.getUserId().equals("0");
    }

    /**
     * Access user information from local database
     */
    private void accessLocalDB(String pin) {
        Login localLogin;
        try {
            localLogin = mDatabase.getLoginWithPin(CommonMethods.convertPassMd5(pin), mPrefs.getSiteId());

            if (localLogin != null) {
                ClientLogger.d(TAG, "Offline response :" + new Gson().toJson(localLogin));
                if (localLogin.getUserID() != null && !localLogin.getUserID().equals("")) {
                    mPrefs.setIsLogin(true);
                    mPrefs.setUserId(localLogin.getUserID());
                    mPrefs.setUserEmail(localLogin.getUserEmail());
                    mPrefs.setProfileImage(localLogin.getProfileImage());
                    mPrefs.setUserName(localLogin.getUserName());
                    isPinSet.postValue(true);
                } else {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API, "User not found"));
                }
            } else {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API,
                        getApplication().getString(R.string.error_incorrect_pin)));
            }
            isLoadingLD.postValue(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isLoadingLD.postValue(false);
        }
    }
}
