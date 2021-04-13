package com.builderstrom.user.data.repositoryCallbacks;

import com.builderstrom.user.data.retrofit.modals.ErrorModel;

public interface RepoCallBack {

    void onNetworkError();

    void onError(ErrorModel error);

}
