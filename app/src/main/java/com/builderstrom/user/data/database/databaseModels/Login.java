package com.builderstrom.user.data.database.databaseModels;

public class Login {
    public static final String TABLE_NAME = "login_pin";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PIN = "userpin";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_PROFILE_PIC = "profile_image";
    public static final String COLUMN_FIREBASE_TOKEN = "firebase_token";
    public static final String COLUMN_IS_LOGIN = "is_login";
    public static final String COLUMN_IS_OFFLINE_ACTION = "login_status";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_PIN + " TEXT,"
                    + COLUMN_SITE_ID + " TEXT,"
                    + COLUMN_USER_ID + " TEXT NOT NULL ,"
                    + COLUMN_USER_NAME + " TEXT,"
                    + COLUMN_USER_EMAIL + " TEXT,"
                    + COLUMN_PROFILE_PIC + " TEXT,"
                    + COLUMN_FIREBASE_TOKEN + " TEXT,"
                    + COLUMN_IS_LOGIN + " TEXT,"
                    + COLUMN_IS_OFFLINE_ACTION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String userID;
    private String userName;
    private String userEmail;
    private String profileImage;
    private String firebase_token;
    private Integer isLogin;
    private Integer offline_login;

    public Login() {
    }

    public Login(int id, String userID/*, String userName*/) {
        this.id = id;
        this.userID = userID;
    }

    public Login(int id, String userID, String userName, String userEmail, String profileImage) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.profileImage = profileImage;
    }

    public Login(String firebase_token,Integer isLogin) {
        this.firebase_token = firebase_token;
        this.isLogin = isLogin;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    public Integer getOffline_login() {
        return offline_login;
    }

    public void setOffline_login(Integer offline_login) {
        this.offline_login = offline_login;
    }
}

