package com.builderstrom.user.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.retrofit.modals.NotificationListModel;

import java.util.List;

public class ShortDocsViewModel extends BaseViewModel {
    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<NotificationListModel>> documentLiveData;

    public ShortDocsViewModel(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        isSuccess = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        documentLiveData = new MutableLiveData<>();
    }

    public void getAllDocument() {
        isRefreshingLD.postValue(true);
//        RestClient.createService().getRecentDocuments(mPrefs.getProjectId()).enqueue(new Callback<NotificationDataModel>() {
//            @Override
//            public void onResponse(@NonNull Call<NotificationDataModel> call, @NonNull Response<NotificationDataModel> response) {
//                isRefreshingLD.postValue(false);
//                try {
//                    setLogs(DiaryViewModel.class.getName(), "getAllNotification", new Gson().toJson(response));
//                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
//                        offsetLD.postValue(response.body().getDatalimit() != null ? response.body().getDatalimit().getOffset() : 0);
//                        documentLiveData.postValue(response.body().getData());
//                    } else {
//                        handleErrorBody(response.errorBody());
//                    }
//                } catch (Exception e) {
//                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<NotificationDataModel> call, @NonNull Throwable t) {
//                setLogs(DiaryViewModel.class.getName(), "getAllNotification", t.getLocalizedMessage());
//                isRefreshingLD.postValue(false);
//                documentLiveData.postValue(new ArrayList<>());
//                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
//            }
//        });
    }

}
