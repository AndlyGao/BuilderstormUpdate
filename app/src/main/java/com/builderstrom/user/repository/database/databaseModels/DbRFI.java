package com.builderstrom.user.repository.database.databaseModels;

public class DbRFI {

    public static final String TABLE_NAME = "rfi";
    public static final String COLUMN_ROW_ID = "row_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_USER = "username";
    public static final String COLUMN_TO_USERS = "tousers";
    public static final String COLUMN_CC_USERS = "ccusers";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_METADATA = "metadata";
    public static final String COLUMN_IS_ANSWERED = "is_Answered";
    public static final String COLUMN_ANSWERED_ADDED = "answered_added";
    public static final String COLUMN_ITEM_DATA = "rfi_item_data";
    public static final String COLUMN_ITEM_COMMENT = "comment";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_ROW_ID + " TEXT,"
            + COLUMN_PROJECT_ID + " TEXT,"
            + COLUMN_USER + " TEXT,"
            + COLUMN_TO_USERS + " TEXT,"
            + COLUMN_CC_USERS + " TEXT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_ITEM_COMMENT + " TEXT,"
            + COLUMN_TIME + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_ITEM_DATA + " TEXT,"
            + COLUMN_STATUS + " TEXT,"
            + COLUMN_METADATA + " TEXT,"
            + COLUMN_IS_ANSWERED + " TEXT,"
            + COLUMN_ANSWERED_ADDED + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private int id;
    private String rowId;
    private String siteID;
    private String userID;
    private String projectID;
    private String username;
    private String toUsers;
    private String ccUsers;
    private String comments;
    private String title;
    private String description;
    private String time;
    private String date;
    private String rfiData;
    private String timestamp;
    private Boolean isAnswered;
    private Boolean isAnsweredAdded;
    private String status;
    private String metaData;

    public DbRFI() {

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

    public String getToUsers() {
        return toUsers;
    }

    public void setToUsers(String toUsers) {
        this.toUsers = toUsers;
    }

    public String getCcUsers() {
        return ccUsers;
    }

    public void setCcUsers(String ccUsers) {
        this.ccUsers = ccUsers;
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

    public String getRfiData() {
        return rfiData;
    }

    public void setRfiData(String rfiData) {
        this.rfiData = rfiData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return rowId;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Boolean getAnsweredAdded() {
        return isAnsweredAdded;
    }

    public void setAnsweredAdded(Boolean answeredAdded) {
        isAnsweredAdded = answeredAdded;
    }
}
