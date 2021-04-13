package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectNote {

    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments = null;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public class Attachment {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("path")
        @Expose
        private String path;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
