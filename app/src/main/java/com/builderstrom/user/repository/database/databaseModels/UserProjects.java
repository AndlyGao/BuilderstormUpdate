package com.builderstrom.user.repository.database.databaseModels;

public class UserProjects {
    public static final String TABLE_NAME = "user_projects";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECTS = "user_projects";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT NOT NULL UNIQUE,"
                    + COLUMN_PROJECTS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String userProjects;
    private String timestamp;

    public UserProjects() {
    }

    public UserProjects(int id, String siteID, String userID, String userProjects, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.userProjects = userProjects;
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

    public String getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(String userProjects) {
        this.userProjects = userProjects;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
