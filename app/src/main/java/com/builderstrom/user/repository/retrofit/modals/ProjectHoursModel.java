package com.builderstrom.user.repository.retrofit.modals;

public class ProjectHoursModel {
    private String id = "";
    private String project_id = "";
    private String project_day = "";
    private String visible = "";
    private String start_time = "";
    private String end_time = "";
    private String date_updated = "";

    public String getId() {
        return id;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getProject_day() {
        return project_day;
    }

    public String getVisible() {
        return visible;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDate_updated() {
        return date_updated;
    }
}
