package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class ApkModel {
    @SerializedName("latest_mobile_apk_version")
    @Expose
    private String apkVersion = "";
    @SerializedName("mobile_apk_filepath")
    @Expose
    private String apkPath = "";
    @SerializedName("mobile_apk_description")
    @Expose
    private String description = "";

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
