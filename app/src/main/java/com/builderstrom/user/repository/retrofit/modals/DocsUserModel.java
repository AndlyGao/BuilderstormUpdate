package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class DocsUserModel extends BaseApiModel {

    private List<ItemUsersModel> data = null;

    public List<ItemUsersModel> getData() {
        return data;
    }
}