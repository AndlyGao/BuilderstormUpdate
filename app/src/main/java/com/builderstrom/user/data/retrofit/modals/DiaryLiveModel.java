package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class DiaryLiveModel {
    private List<DiaryData> diaryList;
    private Boolean isOffline;

    public DiaryLiveModel(List<DiaryData> diaryList, Boolean isOffline) {
        this.diaryList = diaryList;
        this.isOffline = isOffline;
    }

    public List<DiaryData> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<DiaryData> diaryList) {
        this.diaryList = diaryList;
    }

    public Boolean getOffline() {
        return isOffline;
    }

    public void setOffline(Boolean offline) {
        isOffline = offline;
    }
}
