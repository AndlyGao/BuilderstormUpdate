package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class DigiDocLiveModel {
    private boolean isOffline = false;
    private List<DigitalDoc> docList = null;

    public DigiDocLiveModel(boolean isOffline, List<DigitalDoc> docList) {
        this.isOffline = isOffline;
        this.docList = docList;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public List<DigitalDoc> getDocList() {
        return docList;
    }

    public void setDocList(List<DigitalDoc> docList) {
        this.docList = docList;
    }
}
