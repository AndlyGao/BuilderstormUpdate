package com.builderstrom.user.repository.database.databaseModels;

public class Drawings {
    public static final String TABLE_NAME = "drawings";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_ROW_ID = "project_item_id";
    public static final String COLUMN_SYNC = "is_sync";
    public static final String COLUMN_ITEM_DATA = "project_item_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_PROJECT_ID + " TEXT,"
                    + COLUMN_ROW_ID + " TEXT,"
                    + COLUMN_SYNC + " INTEGER DEFAULT 0,"
                    + COLUMN_ITEM_DATA + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String projectID;
    private String rowID;
    private boolean sync;
    private String drawingItem;
    private String timestamp;

    public Drawings() {
    }

    public Drawings(int id, String siteID, String userID, String projectID, String rowID, boolean sync, String drawingItem, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.projectID = projectID;
        this.rowID = rowID;
        this.sync = sync;
        this.drawingItem = drawingItem;
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

    public String getRowID() {
        return rowID;
    }

    public void setRowID(String rowID) {
        this.rowID = rowID;
    }

    public boolean getSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getDrawingItem() {
        return drawingItem;
    }

    public void setDrawingItem(String drawingItem) {
        this.drawingItem = drawingItem;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
