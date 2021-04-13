package com.builderstrom.user.repository.database.databaseModels;

public class Diary {

    public static final String TABLE_NAME = "diary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_USER = "username";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_META_DATA = "meta_data";
    public static final String COLUMN_META_DATA_VIEW = "meta_data_view";
    public static final String COLUMN_ITEM_DATA = "diary_item_data";
    public static final String COLUMN_LABOR_DATA = "diary_site_labor_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String ALTER_TABLE = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_LABOR_DATA + " TEXT;";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_PROJECT_ID + " TEXT,"
                    + COLUMN_USER + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_TIME + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_META_DATA + " TEXT,"
                    + COLUMN_META_DATA_VIEW + " TEXT,"
                    + COLUMN_ITEM_DATA + " TEXT,"
                    + COLUMN_LABOR_DATA + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String projectID;
    private String username;
    private String title;
    private String description;
    private String time;
    private String date;
    private String metaData;
    private String metaDataView;
    private String diaryData;
    private String siteLabor;
    private String timestamp;

    public Diary() {

    }

    public Diary(int id, String siteID, String userID, String projectID, String username, String title, String description, String time, String date, String diaryData, String siteLabor, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.username = username;
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.diaryData = diaryData;
        this.siteLabor = siteLabor;
        this.timestamp = timestamp;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiaryData() {
        return diaryData;
    }

    public void setDiaryData(String diaryData) {
        this.diaryData = diaryData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getMetaDataView() {
        return metaDataView;
    }

    public void setMetaDataView(String metaDataView) {
        this.metaDataView = metaDataView;
    }

    public String getSiteLabor() {
        return siteLabor;
    }

    public void setSiteLabor(String siteLabor) {
        this.siteLabor = siteLabor;
    }
}