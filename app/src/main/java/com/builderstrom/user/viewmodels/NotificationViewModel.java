package com.builderstrom.user.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.NotificationDataModel;
import com.builderstrom.user.repository.retrofit.modals.NotificationListModel;
import com.builderstrom.user.repository.retrofit.modals.PojoSuccessCommon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<NotificationListModel>> notificationLiveData;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        isSuccess = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        notificationLiveData = new MutableLiveData<>();
    }

    public void getAllNotification(Integer offset, Integer limit) {
        isRefreshingLD.postValue(true);
        RestClient.createService().getUserNotification(offset, limit).enqueue(new Callback<NotificationDataModel>() {
            @Override
            public void onResponse(@NonNull Call<NotificationDataModel> call, @NonNull Response<NotificationDataModel> response) {
                isRefreshingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "getAllNotification", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        offsetLD.postValue(response.body().getDatalimit() != null ? response.body().getDatalimit().getOffset() : 0);
                        notificationLiveData.postValue(response.body().getData());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationDataModel> call, @NonNull Throwable t) {
                setLogs(DiaryViewModel.class.getName(), "getAllNotification", t.getLocalizedMessage());
                isRefreshingLD.postValue(false);
                notificationLiveData.postValue(new ArrayList<>());
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void readNotification(int notificationId) {
        isLoadingLD.postValue(true);
        RestClient.createService().readNotification(notificationId).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "read Notification", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(response.body().getSuccess());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                setLogs(DiaryViewModel.class.getName(), "read Notification", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

}
