package com.builderstrom.user.repository.retrofit.modals;


import androidx.annotation.NonNull;

public class HomeMenuModel {
    private String action = "";
    private int icon;
    private String title = "";
    private String value = "";

    public HomeMenuModel(String action, int icon, String title, String value) {
        this.action = action;
        this.icon = icon;
        this.title = title;
        this.value = value;
    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
