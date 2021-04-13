package com.builderstrom.user.repository.retrofit.modals;

public class SiteAccessSignData {

    private ProjectDetails projectdetails;
    private String project_title;
    private Integer signinid;
    private String date;
    private String time;
    private String myLat;
    private String myLong;

    public ProjectDetails getProjectdetails() {
        return projectdetails;
    }

    public void setProjectdetails(ProjectDetails projectdetails) {
        this.projectdetails = projectdetails;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public Integer getSigninid() {
        return signinid;
    }

    public void setSigninid(Integer signinid) {
        this.signinid = signinid;
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

    public String getMyLat() {
        return myLat;
    }

    public void setMyLat(String myLat) {
        this.myLat = myLat;
    }

    public String getMyLong() {
        return myLong;
    }

    public void setMyLong(String myLong) {
        this.myLong = myLong;
    }

}
