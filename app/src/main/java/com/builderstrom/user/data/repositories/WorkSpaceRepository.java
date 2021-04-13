package com.builderstrom.user.data.repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.builderstrom.user.data.repositoryCallbacks.AccessWorkSpace;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.WorkspaceModel;
import com.builderstrom.user.utils.CommonMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkSpaceRepository extends BaseDataRepository {

    public WorkSpaceRepository(Application application) {
        super(application);
    }


    public void accessWorkSpace(String domain, String deviceId, AccessWorkSpace action) {
        restClient.workspace(domain, deviceId).enqueue(new Callback<WorkspaceModel>() {
            @Override
            public void onResponse(@NonNull Call<WorkspaceModel> call, @NonNull Response<WorkspaceModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        action.onWorkSpaceAccess(domain, response.body());
                    } else {
                        mPrefs.setBaseSite("");
                        action.onError(handleErrorBody(response.errorBody()));
                    }
                } catch (Exception e) {
                    mPrefs.setBaseSite("");
                    action.onError(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkspaceModel> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    action.onNetworkError();
                } else {
                    mPrefs.setBaseSite("");
                    action.onError(new ErrorModel(t, t.getLocalizedMessage()));

                }
            }
        });

    }


}
