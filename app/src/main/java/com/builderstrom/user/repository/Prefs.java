package com.builderstrom.user.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.builderstrom.user.repository.database.databaseModels.GeneralSetting;
import com.builderstrom.user.repository.retrofit.modals.CompanyStatus;
import com.builderstrom.user.repository.retrofit.modals.CurrentLocation;
import com.builderstrom.user.repository.retrofit.modals.DocumentStatus;
import com.builderstrom.user.repository.retrofit.modals.GeneralSettingsDatum;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DEVIL
 */

public class Prefs {
    public static final String PREFS_TODO_CATEGORIES = "todo_categories";
    private static final String BASE_SITE = "base_site";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String PREFS_SITE_ID = "site_id";
    private static final String PREFS_USER_ID = "user_id";
    private static final String PREFS_PROJECT_ID = "project_id";
    private static final String PREFS_PROJECT_NAME = "project_name";
    private static final String PREFS_SELECTED_PROJECT = "selected_project";
    private static final String PREFS_USER_NAME = "user_name";
    private static final String PREFS_USER_EMAIL = "user_email";
    private static final String PREFS_USER_IMAGE = "user_profile_image";
    private static final String PREFS_CURRENT_LOCATION = "current_location";
    private static final String PREF_IS_PROJECT_SIGN_IN = "is_project_sign";
    private static final String PREF_IS_LOG_IN = "is_login";
    private static final String PREF_IS_UPDATE_DIALOG = "is_update_dialog";
    private static final String PREFS_USER_CATEGORIES = "user_company_categories";
    private static final String PREFS_USER_STATUSES = "user_company_statuses";
    private static final String PREFS_DOCS_STATUSES = "user_document_statuses";
    private static final String PREFS_GENERAL_SETTING = "user_general_settings";
    private static final String PREFS_DIGI_DOCS_CATEGORIES = "digital_document_categories";
    private static final String PREFS_DIGI_DOCS_USERS = "digital_document_users";
    private static final String PREFS_TO_DO_CATEGORIES = "to_do_categories";
    private static final String PREFS_APP_VERSION = "application_version_code";
    private static final String PREFS_APP_LINK = "application_link";
    private static final String PREFS_WORKSPACE_LOGO = "workspace_logo";
    private static final String PREFS_VERSION_DES = "version_description";
    private static final String TAG = "Prefs";
    private static final String PREFS_WORKSPACE_DETAIL = "workspace_detail";
    private static final String PREFS_CATEGORY_FILTER = "todo_category_filter";
    private static final String PREFS_USER_FILTER = "todo_user_filter";
    private static final String PREFS_STATUS_FILTER = "todo_status_filter";
    private static final String PREFS_USER_TYPE = "todo_user_type";

    private static Prefs singleton = null;

    private static SharedPreferences preferences;

    private static SharedPreferences.Editor editor;

    private Prefs(Context context) {
        preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static Prefs with(Context context) {
        if (singleton == null) {
            singleton = new Builder(context).build();
        }
        return singleton;
    }

    public String getWorkSpaceLogo() {
        return preferences.contains(PREFS_WORKSPACE_LOGO) ?
                preferences.getString(PREFS_WORKSPACE_LOGO, "") : "";
    }

    public void setWorkSpaceLogo(String workSpaceLogo) {
        if (null != workSpaceLogo) {
            editor.putString(PREFS_WORKSPACE_LOGO, workSpaceLogo).apply();
        }
    }

    /* important id's */

    public String getVersionDescription() {
        return preferences.contains(PREFS_VERSION_DES) ?
                preferences.getString(PREFS_VERSION_DES, "") : "";
    }

    public void setVersionDescription(String versionDescription) {
        if (null != versionDescription) {
            editor.putString(PREFS_VERSION_DES, versionDescription).apply();
        }
    }

    public void setIsLogin(Boolean isLogin) {
        if (null != isLogin) {
            editor.putBoolean(PREF_IS_LOG_IN, isLogin).apply();
        }
    }

    public Boolean isLogin() {
        return preferences.getBoolean(PREF_IS_LOG_IN, false);
    }

    public void setIsDialogVisible(Boolean isLogin) {
        if (null != isLogin) {
            editor.putBoolean(PREF_IS_UPDATE_DIALOG, isLogin).apply();
        }
    }

    public Boolean isDialogVisible() {
        return preferences.getBoolean(PREF_IS_UPDATE_DIALOG, true);
    }

    public String getBaseSite() {
        return preferences.getString(BASE_SITE, "");
    }

    public void setBaseSite(String baseSite) {
        if (null != baseSite) {
            editor.putString(BASE_SITE, baseSite).apply();
        }

    }

    public String getSiteId() {
        return preferences.getString(PREFS_SITE_ID, "0");
    }

    public void setSiteId(String siteId) {
        if (siteId != null) {
            editor.putString(PREFS_SITE_ID, siteId).apply();
        }
    }

    public String getAuthToken() {
        return preferences.getString(ACCESS_TOKEN, "0");
    }

    public void setAuthToken(String token) {
        if (token != null) {
            editor.putString(ACCESS_TOKEN, token).apply();
        }
    }

    public String getUserId() {
        return preferences.getString(PREFS_USER_ID, "0");
    }

    public void setUserId(String userId) {
        if (userId != null) {
            editor.putString(PREFS_USER_ID, userId).apply();
        }
    }

    public String getProjectId() {
        return preferences.getString(PREFS_PROJECT_ID, "0");
    }

    public void setProjectId(String projectId) {
        if (projectId != null) {
            editor.putString(PREFS_PROJECT_ID, projectId).apply();
        }
    }

    public String getProjectName() {
        return preferences.getString(PREFS_PROJECT_NAME, "0");
    }

    public void setProjectName(String projectName) {
        if (projectName != null) {
            editor.putString(PREFS_PROJECT_NAME, projectName).apply();
        }
    }

    public String getUserName() {
        return preferences.getString(PREFS_USER_NAME, "");
    }

    public void setUserName(String userName) {
        if (userName != null) {
            editor.putString(PREFS_USER_NAME, userName).apply();
        }
    }

    public String getUserEmail() {
        return preferences.getString(PREFS_USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        if (userEmail != null) {
            editor.putString(PREFS_USER_EMAIL, userEmail).apply();
        }
    }

    public String getProfileImage() {
        return preferences.getString(PREFS_USER_IMAGE, "");
    }

    public void setProfileImage(String userImage) {
        if (userImage != null) {
            editor.putString(PREFS_USER_IMAGE, userImage).apply();
        }
    }

    public Boolean isProjectSignIn() {
        return preferences.getBoolean(PREF_IS_PROJECT_SIGN_IN, false);
    }

    public void saveProjectSign(Boolean isSignedIn) {
        if (null != isSignedIn) {
            editor.putBoolean(PREF_IS_PROJECT_SIGN_IN, isSignedIn).apply();
        }
    }

    public void saveSelectedProject(ProjectDetails project) {
        if (null != project) {
            editor.putString(PREFS_SELECTED_PROJECT, new Gson().toJson(project)).apply();
        }
    }

    public ProjectDetails getSelectedProject() {
        if (preferences.contains(PREFS_SELECTED_PROJECT)) {
            return new Gson().fromJson(preferences.getString(PREFS_SELECTED_PROJECT, ""), ProjectDetails.class);
        } else return null;
    }


    public String getAppVersion() {
        return preferences.contains(PREFS_APP_VERSION) ?
                preferences.getString(PREFS_APP_VERSION, "") : "";
    }

    public void setAppVersion(String appVersion) {
        if (null != appVersion) {
            editor.putString(PREFS_APP_VERSION, appVersion).apply();
        }
    }

    public String getTodoUserType() {
        return preferences.contains(PREFS_USER_TYPE) ?
                preferences.getString(PREFS_USER_TYPE, "") : "";
    }

    public void setTodoUserType(String userType) {
        if (null != userType) {
            editor.putString(PREFS_USER_TYPE, userType).apply();
        }
    }

    public String getAppLink() {
        return preferences.contains(PREFS_APP_LINK) ?
                preferences.getString(PREFS_APP_LINK, "") : "";
    }

    public void setAppLink(String appLink) {
        if (null != appLink) {
            editor.putString(PREFS_APP_LINK, appLink).apply();
        }
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void addGeneralSetting(List<GeneralSettingsDatum> generalSettings) {
        if (null != generalSettings) {
            editor.putString(PREFS_GENERAL_SETTING, new Gson().toJson(generalSettings)).apply();
        }
    }

    public List<GeneralSettingsDatum> getGeneralSetting() {
        if (preferences.contains(PREFS_GENERAL_SETTING)) {
            return new Gson().fromJson(preferences.getString(PREFS_GENERAL_SETTING, ""),
                    new TypeToken<List<GeneralSettingsDatum>>() {
                    }.getType());
        } else return new ArrayList<>();
    }

    public void addDocumentStatuses(List<DocumentStatus> documentStatuses) {
        if (null != documentStatuses) {
            editor.putString(PREFS_DOCS_STATUSES, new Gson().toJson(documentStatuses)).apply();
        }
    }

    public List<DocumentStatus> getDocumentStatuses() {
        if (preferences.contains(PREFS_DOCS_STATUSES)) {
            return new Gson().fromJson(preferences.getString(PREFS_DOCS_STATUSES, ""),
                    new TypeToken<List<DocumentStatus>>() {
                    }.getType());
        } else return new ArrayList<>();
    }

    public void addCompanyStatuses(List<CompanyStatus> companyStatuses) {
        if (null != companyStatuses) {
            editor.putString(PREFS_USER_STATUSES, new Gson().toJson(companyStatuses)).apply();
        }
    }

    public List<CompanyStatus> getCompanyStatuses() {
        if (preferences.contains(PREFS_USER_STATUSES)) {
            return new Gson().fromJson(preferences.getString(PREFS_USER_STATUSES, ""),
                    new TypeToken<List<CompanyStatus>>() {
                    }.getType());
        } else return new ArrayList<>();
    }

    /* companies listing */

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void logout() {
        String siteID = getSiteId();
        String userID = getUserId();
        String authToken = getAuthToken();
        String baseSite = preferences.getString(BASE_SITE, "");
        String baseLogo = preferences.getString(PREFS_WORKSPACE_LOGO, "");
        editor.clear();
        editor.putString(PREFS_SITE_ID, siteID);
        editor.putString(PREFS_USER_ID, userID);
        editor.putString(ACCESS_TOKEN, authToken);
        editor.putString(BASE_SITE, baseSite);
        editor.putString(PREFS_WORKSPACE_LOGO, baseLogo);
        editor.apply();
    }

    public void onDBUpgrade() {
        editor.clear();
        editor.apply();
    }

    public void saveCurrentLocation(CurrentLocation location) {
        editor.putString(PREFS_CURRENT_LOCATION, new Gson().toJson(location)).apply();
    }

    public CurrentLocation getCurrentLocation() {
        return new Gson().fromJson(preferences.getString(PREFS_CURRENT_LOCATION, ""),
                CurrentLocation.class);
    }

    /* current locations */

    private static class Builder {

        private final Context context;

        Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        public Prefs build() {
            return new Prefs(context);
        }
    }

}