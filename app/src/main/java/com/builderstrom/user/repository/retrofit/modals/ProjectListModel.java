package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectListModel extends BaseApiModel {
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

        @SerializedName("projects")
        @Expose
        private List<ProjectDetails> projects = null;
        @SerializedName("project_statuses")
        @Expose
        private List<String> projectStatuses = null;
        @SerializedName("project_stages")
        @Expose
        private List<String> projectStages = null;

        public List<ProjectDetails> getProjects() {
            return projects;
        }

        public void setProjects(List<ProjectDetails> projects) {
            this.projects = projects;
        }

        public List<String> getProjectStatuses() {
            return projectStatuses;
        }

        public void setProjectStatuses(List<String> projectStatuses) {
            this.projectStatuses = projectStatuses;
        }

        public List<String> getProjectStages() {
            return projectStages;
        }

        public void setProjectStages(List<String> projectStages) {
            this.projectStages = projectStages;
        }

    }
}
