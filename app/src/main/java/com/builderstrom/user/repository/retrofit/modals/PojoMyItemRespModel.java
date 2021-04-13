package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoMyItemRespModel extends BaseApiModel {

    private List<PojoMyItem> data = null;

    public List<PojoMyItem> getData() {
        return data;
    }
}
