package com.builderstrom.user.repository.database.databaseModels;

public class GeneralSetting {
    public static final String TABLE_NAME = "GeneralSettings";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SETTING = "general_setting_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT NOT NULL UNIQUE,"
                    + COLUMN_SETTING + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String generalSetting;
    private String timestamp;

    public GeneralSetting(String generalSetting) {
        this.generalSetting = generalSetting;
    }

    public GeneralSetting(int id, String siteID, String userID, String generalSetting, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.generalSetting = generalSetting;
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

    public String getGeneralSetting() {
        return generalSetting;
    }

    public void setGeneralSetting(String generalSetting) {
        this.generalSetting = generalSetting;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
