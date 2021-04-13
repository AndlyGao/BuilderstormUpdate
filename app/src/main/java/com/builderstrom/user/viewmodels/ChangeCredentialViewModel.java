package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoSuccessCommon;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ChangeCredentialViewModel extends BaseViewModel {

    private static final String TAG = ChangeCredentialViewModel.class.getName();
    public MutableLiveData<Boolean> changePinSuccessLD;
    public MutableLiveData<Boolean> changePasswordSuccessLD;
    public MutableLiveData<Boolean> forgotCredSuccessLD;


    public ChangeCredentialViewModel(@NonNull Application application) {
        super(application);
        changePinSuccessLD = new MutableLiveData<>();
        changePasswordSuccessLD = new MutableLiveData<>();
        forgotCredSuccessLD = new MutableLiveData<>();
    }

    /* create new pin */
    public void setPinApi(String pin) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiCreatePin(pin, CommonMethods.getDeviceID(getApplication()),
                DataNames.DEVICE_TYPE).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                CommonMethods.setLogs(TAG, "setPinAPI", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        updatePinInDb(pin, response.body().getMessage());
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "setPinAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* change pin */
    public void changePinApi(String oldPin, String newPin) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiPinChange(oldPin, newPin, CommonMethods.getDeviceID(getApplication()),
                DataNames.DEVICE_TYPE).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                CommonMethods.setLogs(TAG, "changePin", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        updatePinInDb(newPin, response.body().getMessage());
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "changePin", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* change password */
    public void apiChangePassword(String oldPassword, String newPassword) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiChangePassword(oldPassword, newPassword).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                CommonMethods.setLogs(TAG, "change Password", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        updatePasswordInDb(newPassword, response.body().getMessage());
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "change Password", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* forgot pin*/
    public void apiForgotPin(String emailId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiForgotPin(emailId, DataNames.DEVICE_TYPE, CommonMethods.getDeviceID(getApplication()))
                .enqueue(new Callback<PojoSuccessCommon>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                        CommonMethods.setLogs(TAG, "forgotPinAPI", new Gson().toJson(response));
                        isLoadingLD.postValue(false);
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                                forgotCredSuccessLD.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                        isLoadingLD.postValue(false);
                        CommonMethods.setLogs(TAG, "forgotPinAPI", t.getLocalizedMessage());
                        errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                    }
                });
    }

    /* forgot password*/
    public void apiForgotPassword(String emailId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiForgotPassword(emailId,
                DataNames.DEVICE_TYPE, CommonMethods.getDeviceID(getApplication())).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                CommonMethods.setLogs(TAG, "forgotPassword", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        forgotCredSuccessLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "forgotPassword", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });


    }

    /* Database operations */

    private void updatePinInDb(String pin, String message) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updatePin(CommonMethods.convertPassMd5(pin), mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, message));
                changePinSuccessLD.postValue(true);
            }
        }.execute();
    }

    private void updatePasswordInDb(String newPassword, String message) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updatePassword(newPassword, mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, message));
                changePasswordSuccessLD.postValue(true);
            }
        }.execute();
    }



}
