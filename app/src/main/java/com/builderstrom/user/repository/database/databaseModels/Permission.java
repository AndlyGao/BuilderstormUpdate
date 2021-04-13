package com.builderstrom.user.repository.database.databaseModels;

public class Permission {
    public static final String TABLE_NAME = "permission";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_PERMISSION = "user_permission";
    public static final String COLUMN_PERMISSION_SECTION = "permission_section";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT NOT NULL UNIQUE,"
                    + COLUMN_USER_PERMISSION + " TEXT,"
                    + COLUMN_PERMISSION_SECTION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String siteID;
    private String userID;
    private String userPermission;
    private String permissionSection;
    private String timestamp;

    public Permission(int id, String siteID, String userID, String userPermission, String permissionSection, String timestamp) {
        this.id = id;
        this.siteID = siteID;
        this.userID = userID;
        this.userPermission = userPermission;
        this.permissionSection = permissionSection;
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

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getPermissionSection() {
        return permissionSection;
    }

    public void setPermissionSection(String permissionSection) {
        this.permissionSection = permissionSection;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
