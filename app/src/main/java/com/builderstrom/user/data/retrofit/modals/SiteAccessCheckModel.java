package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteAccessCheckModel extends BaseApiModel {

    private SignInCheckData data;

    public SignInCheckData getData() {
        return data;
    }

    public class SignInCheckData {
        private ApkModel APK;
        private ProjectHours projectHours;
        private ProjectDetails projectdetails;
        private String project_title = "";
        private String signinid = "";
        private String date = "";
        private String time = "";
        private List<ProjectDetails> all_assigned_projects = null;
        @SerializedName("permissions")
        @Expose
        private LoginModel.Permissions permissions;
        @SerializedName("permission_section")
        @Expose
        private List<PermissionSection> permissionSection = null;
        @SerializedName("generalSettingsData")
        @Expose
        private List<GeneralSettingsDatum> generalSettingsData = null;

        public ApkModel getAPK() {
            return APK;
        }

        public ProjectHours getProjectHours() {
            return projectHours;
        }

        public ProjectDetails getProjectDetails() {
            return projectdetails;
        }

        public String getProjectTitle() {
            return project_title;
        }

        public String getSigninId() {
            return signinid;
        }

        public List<ProjectDetails> getProjects() {
            return all_assigned_projects;
        }

        public LoginModel.Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(LoginModel.Permissions permissions) {
            this.permissions = permissions;
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

        public List<PermissionSection> getPermissionSection() {
            return permissionSection;
        }

        public void setPermissionSection(List<PermissionSection> permissionSection) {
            this.permissionSection = permissionSection;
        }

        public List<GeneralSettingsDatum> getGeneralSettingsData() {
            return generalSettingsData;
        }

        public void setGeneralSettingsData(List<GeneralSettingsDatum> generalSettingsData) {
            this.generalSettingsData = generalSettingsData;
        }
    }

}
