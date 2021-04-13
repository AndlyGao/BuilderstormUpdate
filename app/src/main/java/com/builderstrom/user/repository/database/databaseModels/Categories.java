package com.builderstrom.user.repository.database.databaseModels;

public class Categories {

    public static final String TABLE_NAME = "CATEGORIES";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SECTION_NAME = "section";
    public static final String COLUMN_ITEM_DATA = "category_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_SECTION_NAME + " TEXT,"
            + COLUMN_ITEM_DATA + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";
    private int id;
    private String siteID;
    private String userID;
    private String section;
    private String itemData;
    private String timestamp;

    public Categories() {
    }

    public Categories(String itemData) {
        this.itemData = itemData;
    }

    public Categories(int id, String siteID, String userID, String section, String itemData, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.section = section;
        this.itemData = itemData;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}