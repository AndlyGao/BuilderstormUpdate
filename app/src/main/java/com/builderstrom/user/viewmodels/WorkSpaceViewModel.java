package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.database.databaseModels.Domain;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.WorkspaceModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class WorkSpaceViewModel extends BaseViewModel {

    public MutableLiveData<Integer> redirectLD;
    private String TAG = WorkSpaceViewModel.class.getName();

    public WorkSpaceViewModel(@NonNull Application application) {
        super(application);
        redirectLD = new MutableLiveData<>();
    }

    public void accessWorkSpace(String domain) {
        setLogs(TAG, "BEGIN ", "APP_VERSION:" + CommonMethods.getAppVersion(getApplication()));
        setLogs(TAG, "BEGIN ", "baseURL: " + domain.toUpperCase());


        mPrefs.setBaseSite(domain);
        isLoadingLD.postValue(true);
//        RestClient.getWorkspaceClient().workspace(domain, CommonMethods.getDeviceID(getApplication())).enqueue(
        RestClient.createService().workspace(domain, CommonMethods.getDeviceID(getApplication())).enqueue(
                new Callback<WorkspaceModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WorkspaceModel> call, @NonNull Response<WorkspaceModel> response) {
                        setLogs(TAG, "callAuthenticateAPI", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                updateDomains(domain, response.body());
                            } else {
                                isLoadingLD.postValue(false);
                                mPrefs.setBaseSite("");
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            isLoadingLD.postValue(false);
                            mPrefs.setBaseSite("");
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WorkspaceModel> call, @NonNull Throwable t) {
                        setLogs(TAG, "callAuthenticateAPI", t.getLocalizedMessage());
                        if (CommonMethods.isNetworkError(t)) {
                            offlineDomainAccess(domain);
                        } else {
                            isLoadingLD.postValue(false);
                            mPrefs.setBaseSite("");
                            errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));

                        }
                    }
                });
    }

    /* Database operations */
    private void updateDomains(String baseSite, WorkspaceModel model) {
        new AsyncTask<Void, Void, Domain>() {
            @Override
            protected Domain doInBackground(Void... voids) {
                mPrefs.setAuthToken(model.getAuthToken());
                mPrefs.setSiteId(model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
                mPrefs.setWorkSpaceLogo(model.getData().getLogo());
                return mDatabase.getDomain(baseSite);
            }

            @Override
            protected void onPostExecute(Domain localDomain) {
                super.onPostExecute(localDomain);
                if (localDomain != null) {
                    mDatabase.updateDomain(baseSite, model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
                } else {
                    mDatabase.insertDomain(baseSite, model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
                }
                redirectToLogin();
            }
        }.execute();
    }

    private void offlineDomainAccess(String clientURL) {
        try {
            new AsyncTask<Void, Void, Domain>() {
                @Override
                protected Domain doInBackground(Void... voids) {
                    return mDatabase.getDomain(clientURL);
                }

                @Override
                protected void onPostExecute(Domain localDomain) {
                    super.onPostExecute(localDomain);
                    setLogs(TAG, " offlineDomainAccess", "Data from LocalDB :" + new Gson().toJson(localDomain));
                    if (localDomain != null && localDomain.getSiteID() != null && !localDomain.getSiteID().isEmpty()) {
                        mPrefs.setSiteId(localDomain.getSiteID());
                        mPrefs.setBaseSite(clientURL);
                        redirectToLogin();
                    } else {
                        isLoadingLD.postValue(false);
                        mPrefs.setBaseSite("");
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API, "Workspace not found"));
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectToLogin() {
        if (!mPrefs.getUserId().isEmpty() && !mPrefs.getSiteId().isEmpty()) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    setLogs(TAG, "REDIRECT TO LOGIN METHOD", "USER_ID:  " + mPrefs.getUserId() + "  ------- SITE_ID:" + mPrefs.getSiteId());
                    return mDatabase.getLoginPin(mPrefs.getUserId(), mPrefs.getSiteId());
                }

                @Override
                protected void onPostExecute(Boolean alreadyLogin) {
                    super.onPostExecute(alreadyLogin);
                    setLogs(TAG, "ASYNC TASK POST EXECUTE", "USER REDIRECT ON:" + (alreadyLogin ? "PinLoginActivity" : "LoginActivity"));
                    isLoadingLD.postValue(false);
                    redirectLD.postValue(alreadyLogin ? 1 : 2);
                }
            }.execute();
        } else {
            isLoadingLD.postValue(false);
            redirectLD.postValue(2);
        }
    }

}
