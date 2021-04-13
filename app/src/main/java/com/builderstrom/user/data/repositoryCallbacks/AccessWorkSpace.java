package com.builderstrom.user.data.repositoryCallbacks;

import com.builderstrom.user.data.retrofit.modals.WorkspaceModel;


public interface AccessWorkSpace extends RepoCallBack {
    void onWorkSpaceAccess(String domainName, WorkspaceModel workspaceModel);
}

