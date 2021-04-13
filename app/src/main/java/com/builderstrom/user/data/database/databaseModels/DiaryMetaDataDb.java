package com.builderstrom.user.data.database.databaseModels;

public class DiaryMetaDataDb {

    public static final String TABLE_NAME = "diarymetadata";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER = "username";
    public static final String COLUMN_SECTION_NAME = "sectionname";
    public static final String COLUMN_ITEM_DATA = "diary_meta_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_PROJECT_ID + " TEXT,"
                    + COLUMN_USER + " TEXT,"
                    + COLUMN_SECTION_NAME + " TEXT,"
                    + COLUMN_ITEM_DATA + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String projectID;
    private String username;
    private String sectionName;
    private String metaData;
    private String timestamp;

    public DiaryMetaDataDb() {

    }

    public DiaryMetaDataDb(int id, String siteID, String userID, String projectID, String sectionName, String username, String metaData, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.sectionName = sectionName;
        this.username = username;
        this.metaData = metaData;
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

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return sectionName;
    }
}
