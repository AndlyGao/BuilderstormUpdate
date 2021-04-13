package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RfiModel {

    @SerializedName("ccUsers")
    @Expose
    private String ccUsers;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("toUsers")
    @Expose
    private String toUsers;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("data")
    @Expose
    private List<RfiFileModel> fileData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<RfiFileModel> getFileData() {
        return fileData;
    }

    public void setFileData(List<RfiFileModel> fileData) {
        this.fileData = fileData;
    }


    public String getToUsers() {
        return toUsers;
    }

    public void setToUsers(String toUsers) {
        this.toUsers = toUsers;
    }


    public String getCcUsers() {
        return ccUsers;
    }

    public void setCcUsers(String ccUsers) {
        this.ccUsers = ccUsers;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
