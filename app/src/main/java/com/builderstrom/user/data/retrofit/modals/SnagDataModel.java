package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class SnagDataModel {
    private List<Snag> snagList;
    private boolean isOffline = false;

    public SnagDataModel(List<Snag> snagList, boolean isOffline) {
        this.snagList = snagList;
        this.isOffline = isOffline;
    }


    public List<Snag> getSnagList() {
        return snagList;
    }

    public boolean isOffline() {
        return isOffline;
    }
}
