package com.builderstrom.user.repository.database.databaseModels;

public class ReportProblem {
    public static final String TABLE_NAME = "support";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_EMAIL = "user_email";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_DATA = "logged_files";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_ITEM_DATA + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String userID;
    private String userEmail;
    private String description;
    private String logged_files;
    private String timestamp;

    public ReportProblem() {
    }

    public ReportProblem(int id, String userID, String userEmail, String description, String logged_files, String timestamp) {
        this.id = id;
        this.userID = userID;
        this.userEmail = userEmail;
        this.description = description;
        this.logged_files = logged_files;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogged_files() {
        return logged_files;
    }

    public void setLogged_files(String logged_files) {
        this.logged_files = logged_files;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
