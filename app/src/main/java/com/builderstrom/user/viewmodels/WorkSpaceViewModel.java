package com.builderstrom.user.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.database.databaseModels.Domain;
import com.builderstrom.user.data.repositories.WorkSpaceRepository;
import com.builderstrom.user.data.repositoryCallbacks.AccessWorkSpace;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.WorkspaceModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@SuppressLint("StaticFieldLeak")
public class WorkSpaceViewModel extends BaseViewModel {

    public MutableLiveData<Integer> redirectLD;
    private final String TAG = WorkSpaceViewModel.class.getName();
    public WorkSpaceRepository repository;

    public WorkSpaceViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkSpaceRepository(application);
        redirectLD = new MutableLiveData<>();
    }

    public void accessWorkSpace(String domain) {
        setLogs(TAG, "BEGIN ", "APP_VERSION:" + CommonMethods.getAppVersion(getApplication()));
        setLogs(TAG, "BEGIN ", "baseURL: " + domain.toUpperCase());

        mPrefs.setBaseSite(domain);
        isLoadingLD.postValue(true);
        repository.accessWorkSpace(domain, CommonMethods.getDeviceID(getApplication()), new AccessWorkSpace() {
            @Override
            public void onWorkSpaceAccess(String domainName, WorkspaceModel workspaceModel) {
                updateDomains(domainName, workspaceModel);
            }

            @Override
            public void onNetworkError() {
                offlineDomainAccess(domain);
            }

            @Override
            public void onError(ErrorModel error) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(error);
            }
        });
    }

    /* Database operations */
    private void updateDomains(String baseSite, WorkspaceModel model) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler domainHandler = new Handler(Looper.getMainLooper());
        service.execute(() -> {
            mPrefs.setAuthToken(model.getAuthToken());
            mPrefs.setSiteId(model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
            mPrefs.setWorkSpaceLogo(model.getData().getLogo());
            Domain domain = mDatabase.getDomain(baseSite);

            domainHandler.post(() -> {
                if (domain != null) {
                    mDatabase.updateDomain(baseSite, model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
                } else {
                    mDatabase.insertDomain(baseSite, model.getData().getSiteId() == null ? "" : model.getData().getSiteId());
                }
                redirectToLogin();
            });

        });

     /*   new AsyncTask<Void, Void, Domain>() {
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
        }.execute();*/
    }

    private void offlineDomainAccess(String clientURL) {
        try {
            ExecutorService service = Executors.newSingleThreadExecutor();
            Handler domainHandler = new Handler(Looper.getMainLooper());
            service.execute(() -> {
                Domain domain = mDatabase.getDomain(clientURL);

                domainHandler.post(() -> {
                    if (domain != null && domain.getSiteID() != null && !domain.getSiteID().isEmpty()) {
                        mPrefs.setSiteId(domain.getSiteID());
                        mPrefs.setBaseSite(clientURL);
                        redirectToLogin();
                    } else {
                        isLoadingLD.postValue(false);
                        mPrefs.setBaseSite("");
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API, "Workspace not found"));
                    }
                });

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


      /*  try {
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
        }*/
    }

    private void redirectToLogin() {
        if (!mPrefs.getUserId().isEmpty() && !mPrefs.getSiteId().isEmpty()) {
            ExecutorService service = Executors.newSingleThreadExecutor();
            Handler domainHandler = new Handler(Looper.getMainLooper());
            service.execute(() -> {
                boolean alreadyLogin = mDatabase.getLoginPin(mPrefs.getUserId(), mPrefs.getSiteId());
                domainHandler.post(() -> {
                    isLoadingLD.postValue(false);
                    redirectLD.postValue(alreadyLogin ? 1 : 2);
                });
            });
        } else {
            isLoadingLD.postValue(false);
            redirectLD.postValue(2);
        }

      /*  if (!mPrefs.getUserId().isEmpty() && !mPrefs.getSiteId().isEmpty()) {
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
        }*/
    }

}
