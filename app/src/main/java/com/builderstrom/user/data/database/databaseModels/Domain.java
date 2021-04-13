package com.builderstrom.user.data.database.databaseModels;

public class Domain {
    public static final String TABLE_NAME = "domain";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "domain_name";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SITE_ID + " TEXT NOT NULL UNIQUE,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String name;
    private String siteID;
    private String timestamp;

    public Domain() {
    }

    public Domain(int id, String name, String siteID, String timestamp) {
        this.id = id;
        this.name = name;
        this.siteID = siteID;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
