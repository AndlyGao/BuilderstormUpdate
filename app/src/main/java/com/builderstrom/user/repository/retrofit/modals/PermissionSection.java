package com.builderstrom.user.repository.retrofit.modals;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionSection {

    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("value")
    @Expose
    private String value;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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
        return section;
    }


}