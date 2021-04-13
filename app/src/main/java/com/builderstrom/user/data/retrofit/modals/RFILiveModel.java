package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class RFILiveModel {
    private List<Rfi> rfiList;
    private List<Rfitotal> locks;
    private int[] offlineLocks;

    public RFILiveModel(List<Rfi> rfiList, List<Rfitotal> locks) {
        this.rfiList = rfiList;
        this.locks = locks;
        this.offlineLocks = null;
    }

    public RFILiveModel(List<Rfi> rfiList, int[] offlineLocks) {
        this.rfiList = rfiList;
        this.offlineLocks = offlineLocks;
        this.locks = null;
    }

    public List<Rfi> getRfiList() {
        return rfiList;
    }

    public void setRfiList(List<Rfi> rfiList) {
        this.rfiList = rfiList;
    }

    public List<Rfitotal> getLocks() {
        return locks;
    }

    public void setLocks(List<Rfitotal> locks) {
        this.locks = locks;
    }

    public int[] getOfflineLocks() {
        return offlineLocks;
    }

    public void setOfflineLocks(int[] offlineLocks) {
        this.offlineLocks = offlineLocks;
    }
}
