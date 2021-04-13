package com.builderstrom.user.repository.database.databaseModels;

public class DbTimeSheetTask {

    public static final String TABLE_NAME = "time_sheet_task";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_TASK_LIST = "project_tasks";
    public static final String COLUMN_ITEMS_LIST = "project_items";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_PROJECT_ID + " TEXT,"
            + COLUMN_TASK_LIST + " TEXT,"
            + COLUMN_ITEMS_LIST + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private int id;
    private String siteID;
    private String userID;
    private String projectID;
    private String projectTasks;
    private String projectItems;
    private String timestamp;

    public DbTimeSheetTask() {

    }

    public DbTimeSheetTask(int id, String siteID, String userID, String projectID,
                           String projectTasks, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.projectTasks = projectTasks;
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

    public String getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(String projectTasks) {
        this.projectTasks = projectTasks;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(String projectItems) {
        this.projectItems = projectItems;
    }
}