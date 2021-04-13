package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.AddDiaryModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class SupportViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> submitReportLD;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        submitReportLD = new MutableLiveData<>();
    }


    public void sendReport(String description, List<String> parts) {

        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (String path : parts) {
            filesPart.add(CommonMethods.createPartFromFile("logs" + parts.indexOf(path), path));
        }

        isLoadingLD.postValue(true);
        RestClient.createService().sendReport(CommonMethods.createPartFromString(mPrefs.getUserEmail()),
                CommonMethods.createPartFromString(description),
                filesPart).enqueue(new Callback<AddDiaryModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDiaryModel> call, @NonNull Response<AddDiaryModel> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "reported successfully"));
                        submitReportLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDiaryModel> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    insertReportInDB(mPrefs.getUserEmail(), mPrefs.getUserId(),
                            description, new Gson().toJson(parts));
                } else {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                }
            }
        });
    }

    private void insertReportInDB(String userEmail, String userID, String description, String offlineListData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertReport(userEmail, userID, description, offlineListData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                /* global sync here*/
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                        "Report has been submitted successfully"));
                submitReportLD.postValue(true);
            }
        }.execute();
    }
}
