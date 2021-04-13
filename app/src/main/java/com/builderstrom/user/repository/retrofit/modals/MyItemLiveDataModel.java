package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class MyItemLiveDataModel {
    private List<PojoMyItem> myItems = null;
    private boolean isOfflineStatus = false;


    public MyItemLiveDataModel(List<PojoMyItem> myItems, boolean isOfflineStatus) {
        this.myItems = myItems;
        this.isOfflineStatus = isOfflineStatus;
    }

    public List<PojoMyItem> getMyItems() {
        return myItems;
    }

    public void setMyItems(List<PojoMyItem> myItems) {
        this.myItems = myItems;
    }

    public boolean isOfflineStatus() {
        return isOfflineStatus;
    }

    public void setOfflineStatus(boolean offlineStatus) {
        isOfflineStatus = offlineStatus;
    }
}
