package com.builderstrom.user.repository.database.databaseModels;

import android.text.TextUtils;

public class SignedProject {
    public static final String TABLE_NAME = "signed_projects";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    //    public static final String COLUMN_PROJECT_NAME = "project_name";
//    public static final String COLUMN_TIME_START = "project_start_time";
//    public static final String COLUMN_TIME_STOP = "project_stop_time";
//    public static final String COLUMN_DAY = "project_sign_day";
    public static final String COLUMN_TIME_SIGN_IN = "project_signin_time";
    public static final String COLUMN_TIME_SIGN_OUT = "project_signout_time";
    public static final String COLUMN_PROJECT_STATUS = "project_status";
    public static final String COLUMN_ITEM_DATA = "project_item_data";
    public static final String COLUMN_START_LATITUDE = "start_latitude";
    public static final String COLUMN_START_LONGITUDE = "start_longitude";
    public static final String COLUMN_END_LATITUDE = "end_latitude";
    public static final String COLUMN_END_LONGITUDE = "end_longitude";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    /* Extras */
    public static final String COLUMN_OVERTIMES = "overtime_array";
    //    public static final String COLUMN_ALLOW_BREAKS = "allow_break";
    public static final String COLUMN_STANDARD_BREAKS = "standard_breaks_array";
    public static final String COLUMN_SELECTED_BREAKS = "selected_breaks_id";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_PROJECT_ID + " TEXT,"
//                    + COLUMN_PROJECT_NAME + " TEXT,"
//                    + COLUMN_TIME_START + " TEXT,"
//                    + COLUMN_TIME_STOP + " TEXT,"
//                    + COLUMN_DAY + " TEXT,"
                    + COLUMN_TIME_SIGN_IN + " TEXT,"
                    + COLUMN_TIME_SIGN_OUT + " TEXT,"
                    + COLUMN_PROJECT_STATUS + " INTEGER DEFAULT 0,"
                    + COLUMN_ITEM_DATA + " TEXT,"
                    + COLUMN_START_LATITUDE + " TEXT,"
                    + COLUMN_START_LONGITUDE + " TEXT,"
                    + COLUMN_END_LATITUDE + " TEXT,"
                    + COLUMN_END_LONGITUDE + " TEXT,"
                    + COLUMN_OVERTIMES + " TEXT,"
//                    + COLUMN_ALLOW_BREAKS + " INTEGER,"
                    + COLUMN_STANDARD_BREAKS + " TEXT,"
                    + COLUMN_SELECTED_BREAKS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String projectID;
    private String projectName;
    private String projectStartTime;
    private String projectEndTime;
    private String projectSignDay;
    private String userStartTime;
    private String userEndTime;
    private Integer isSigned;
    private String projectData;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String timestamp;
    private String selected_breaks;
    private String overTime_array;
    private String allow_breaks;
    private String breaks_array;


    public SignedProject() {
    }


    public SignedProject(int id, String siteID, String userID, String projectID, String projectName, String projectStartTime, String projectEndTime, String userStartTime, String userEndTime, Integer isSigned, String projectData, String startLatitude, String startLongitude, String endLatitude, String endLongitude, String timestamp, Integer allow_overTime, Integer allow_breaks, String standard_breaks, String selectedId) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectStartTime = projectStartTime;
        this.projectEndTime = projectEndTime;
        this.userStartTime = userStartTime;
        this.userEndTime = userEndTime;
        this.isSigned = isSigned;
        this.projectData = projectData;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.timestamp = timestamp;
//        this.allow_breaks = allow_breaks;
//        this.allow_overTime = allow_overTime;
//        this.standard_breaks = standard_breaks;
//        this.selectedId = selectedId;
    }

    public SignedProject(int id, String siteID, String userID, String projectID, String userStartTime, String userEndTime, Integer isSigned, String projectData, String startLatitude, String startLongitude, String endLatitude, String endLongitude, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.userStartTime = userStartTime;
        this.userEndTime = userEndTime;
        this.isSigned = isSigned;
        this.projectData = projectData;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.timestamp = timestamp;
    }

    public SignedProject(int id, String siteID, String userID, String projectID, String userStartTime, String userEndTime, Integer isSigned, String projectData, String startLatitude, String startLongitude, String endLatitude, String endLongitude, String overTime_array, String breaks_array, String selected_breaks, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.userStartTime = userStartTime;
        this.userEndTime = userEndTime;
        this.isSigned = isSigned;
        this.projectData = projectData;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.overTime_array = overTime_array;
        this.breaks_array = breaks_array;
        this.selected_breaks = selected_breaks;
        this.timestamp = timestamp;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public String getProjectSignDay() {
        return projectSignDay;
    }

    public void setProjectSignDay(String projectSignDay) {
        this.projectSignDay = projectSignDay;
    }

    public String getUserStartTime() {
        return userStartTime;
    }

    public void setUserStartTime(String userStartTime) {
        this.userStartTime = userStartTime;
    }

    public String getUserEndTime() {
        return userEndTime;
    }

    public void setUserEndTime(String userEndTime) {
        this.userEndTime = userEndTime;
    }

    public Integer isSigned() {
        return isSigned;
    }

    public void setSigned(Integer signed) {
        isSigned = signed;
    }

    public String getProjectData() {
        return projectData;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getSelected_breaks() {
        return selected_breaks;
    }

    public void setSelected_breaks(String selected_breaks) {
        this.selected_breaks = selected_breaks;
    }

    public String getOverTime_array() {
        return overTime_array;
    }

    public void setOverTime_array(String overTime_array) {
        this.overTime_array = overTime_array;
    }

    public String getBreaks_array() {
        return breaks_array;
    }

    public void setBreaks_array(String breaks_array) {
        this.breaks_array = breaks_array;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return TextUtils.concat("start time-->", userStartTime, "<---End Time---->", userEndTime).toString();
    }
}
