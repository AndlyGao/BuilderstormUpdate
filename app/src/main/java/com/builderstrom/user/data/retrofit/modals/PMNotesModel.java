package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PMNotesModel extends BaseApiModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("project_notes")
        @Expose
        private List<ProjectNote> projectNotes = null;
        @SerializedName("site_url")
        @Expose
        private String siteUrl;

        public List<ProjectNote> getProjectNotes() {
            return projectNotes;
        }

        public void setProjectNotes(List<ProjectNote> projectNotes) {
            this.projectNotes = projectNotes;
        }

        public String getSiteUrl() {
            return siteUrl;
        }

        public void setSiteUrl(String siteUrl) {
            this.siteUrl = siteUrl;
        }

    }
}
