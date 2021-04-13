package com.builderstrom.user.data.retrofit.modals;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class TimeModel {
    private String startTime = "";
    private String endTime = "";

    public TimeModel() {
    }

    public TimeModel(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @NonNull
    @Override
    public String toString() {
        return TextUtils.concat(startTime, " : ", endTime).toString();
    }
}
