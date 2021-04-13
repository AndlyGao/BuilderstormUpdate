/**
 * Copyright (C) 2018 iDEA foundation pvt.,Ltd
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.BuildConfig;
import com.builderstrom.user.R;
import com.builderstrom.user.data.database.databaseModels.GeneralSetting;
import com.builderstrom.user.data.database.databaseModels.Permission;
import com.builderstrom.user.data.database.databaseModels.ShortcutCategories;
import com.builderstrom.user.data.database.databaseModels.SignedProject;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.GeneralSettingsDatum;
import com.builderstrom.user.data.retrofit.modals.LoginModel;
import com.builderstrom.user.data.retrofit.modals.PermissionSection;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.data.retrofit.modals.ProjectSignModel;
import com.builderstrom.user.data.retrofit.modals.RecentCategory;
import com.builderstrom.user.data.retrofit.modals.RecentCategoryModel;
import com.builderstrom.user.data.retrofit.modals.SiteAccessCheckModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ProjectSignViewModel extends BaseViewModel {

    private static final String TAG = ProjectSignViewModel.class.getName();
    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<List<DashBoardMenuModel>> homeProjectsListLD;
    public MutableLiveData<List<RecentCategory>> recentCategoryList;
    public MutableLiveData<LoginModel.Permissions> permissionListLD;
    public MutableLiveData<ProjectSignModel> onlineConfirmDialogLD;
    public MutableLiveData<Boolean> notifyGridAdapterLD;
    public MutableLiveData<ProjectDetails> offlineSignInDialogLD;
    public MutableLiveData<SignedProject> offlineSignOutDialogLD;
    public MutableLiveData<Boolean> updateAppLD;

    List<DashBoardMenuModel> menuModelList = new ArrayList<>();

    private List<PermissionSection> permissions_section = new ArrayList<>();

    public ProjectSignViewModel(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        homeProjectsListLD = new MutableLiveData<>();
        recentCategoryList = new MutableLiveData<>();
        onlineConfirmDialogLD = new MutableLiveData<>();
        notifyGridAdapterLD = new MutableLiveData<>();
        offlineSignInDialogLD = new MutableLiveData<>();
        offlineSignOutDialogLD = new MutableLiveData<>();
        permissionListLD = new MutableLiveData<>();
        updateAppLD = new MutableLiveData<>();
    }

    public void projectStatusRefresh(String date) {
        isRefreshingLD.postValue(true);
        RestClient.createService().projectSignCheck(date).enqueue(new Callback<SiteAccessCheckModel>() {
            @Override
            public void onResponse(@NonNull Call<SiteAccessCheckModel> call, @NonNull Response<SiteAccessCheckModel> response) {
                isRefreshingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        clearProjectSignedInfo();
                        if (null != response.body().getData().getAPK()) {
                            mPrefs.setAppVersion(response.body().getData().getAPK().getApkVersion());
                            mPrefs.setAppLink(response.body().getData().getAPK().getApkPath());
                            mPrefs.setVersionDescription(response.body().getData().getAPK().getDescription());
                        }
                        mPrefs.saveProjectSign(response.body().isSuccess());
                        if (null != response.body().getData().getProjectDetails()) {
                            insertCheckDetailInDB(response.body().getData());
                        }

                        if (null != response.body().getData().getProjects()) {
                            updateSavedProjects(response.body().getData().getProjects());
                        }

                        if (null != response.body().getData().getGeneralSettingsData()) {
                            fetchGeneralSetting(response.body().getData().getGeneralSettingsData());
                        }

                        if (null != response.body().getData().getPermissions() && null != response.body().getData().getPermissionSection()) {
                            fetchPermission(response.body().getData().getPermissionSection(), response.body().getData().getPermissions());
                        }

                        /* update adapter */
                        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(
                                new Intent(DataNames.INTENT_ACTION_UPDATE_NOTIFICATION)
                                        .putExtra("KEY_FLAG", true));
                        notifyGridAdapterLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SiteAccessCheckModel> call, @NonNull Throwable t) {
                isRefreshingLD.postValue(false);
                if (CommonMethods.isNetworkError(t)) {
                    accessSignedProject();
                    accessAllSections();
                    accessGeneralSetting();
                } else {
                    CommonMethods.setLogs(TAG, "projectSignCheck", t.getLocalizedMessage());
                    errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                }
            }
        });
    }


    public void projectSign(String lat, String lng) {
        isLoadingLD.postValue(true);
        RestClient.createService().projectSignIn(mPrefs.isProjectSignIn() ? 1 : 0, lat, lng,
                CommonMethods.getCurrentDate(CommonMethods.DF_2),
                CommonMethods.getCurrentDate(CommonMethods.DF_9)).enqueue(new Callback<ProjectSignModel>() {
            @Override
            public void onResponse(@NonNull Call<ProjectSignModel> call, @NonNull Response<ProjectSignModel> response) {
                CommonMethods.setLogs(TAG, "project Sign", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (response.body().isSuccess()) {
                            onlineConfirmDialogLD.postValue(response.body());
                        } else {
                            mPrefs.saveProjectSign(false);
                            clearProjectSignedInfo();
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProjectSignModel> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    accessOfflineProjects(lat, lng);
                } else {
                    isLoadingLD.postValue(false);
                    CommonMethods.setLogs(TAG, "project Sign", t.getLocalizedMessage());
                }
            }
        });
    }

    private void accessSignedProject() {
        try {
            SignedProject signedProject = mDatabase.getSignedProject(mPrefs.getSiteId(), mPrefs.getUserId());
            mPrefs.saveProjectSign(null != signedProject);
            LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(new Intent(DataNames.INTENT_ACTION_UPDATE_NOTIFICATION).putExtra("KEY_FLAG", true));
            notifyGridAdapterLD.postValue(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void accessGeneralSetting() {
        new AsyncTask<Void, Void, GeneralSetting>() {

            @Override
            protected GeneralSetting doInBackground(Void... voids) {
                return mDatabase.getGeneralSetting(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(GeneralSetting setting) {
                super.onPostExecute(setting);
                // Access projects from local database
                if (setting != null) {
                    mPrefs.addGeneralSetting(new Gson().fromJson(setting.getGeneralSetting(), new TypeToken<List<GeneralSettingsDatum>>() {}.getType()));
                }
            }
        }.execute();


        try {

            mDatabase.getGeneralSetting(mPrefs.getSiteId(), mPrefs.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void projectSignConfirm(String projectID, @Nullable String overtimeList, @Nullable String selectedIdList, @Nullable String standardBreakList) {
        isLoadingLD.postValue(true);
        RestClient.createService().projectSignConfirm(projectID, mPrefs.isProjectSignIn() ? 1 : 0,
                mPrefs.getCurrentLocation().getLatitude(), mPrefs.getCurrentLocation().getLongitude(),
                CommonMethods.getCurrentDate(CommonMethods.DF_2), CommonMethods.getCurrentDate(CommonMethods.DF_9),
                overtimeList, selectedIdList, standardBreakList).enqueue(new Callback<SiteAccessCheckModel>() {
            @Override
            public void onResponse(@NonNull Call<SiteAccessCheckModel> call, @NonNull Response<SiteAccessCheckModel> response) {
                isLoadingLD.postValue(false);
                CommonMethods.setLogs(TAG, "projectSignConfirm", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        mPrefs.saveProjectSign(response.body().isSuccess());
                        if (response.body().isSuccess()) {
                            insertCheckDetailInDB(response.body().getData());
                        } else {
                            clearProjectSignedInfo();
                        }
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                } finally {
                    notifyGridAdapterLD.postValue(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SiteAccessCheckModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "projectSignConfirm", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /**
     * Offline database Operations
     */
    /* getting all user Access permission for sections*/
    public void accessAllSections() {
        isRefreshingLD.postValue(true);
        new AsyncTask<Void, Void, Permission>() {

            @Override
            protected Permission doInBackground(Void... voids) {
                return mDatabase.getPermission(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(Permission mPermission) {
                super.onPostExecute(mPermission);
                // Access projects from local database
                if (mPermission != null) {
                    permissionListLD.postValue(new Gson().fromJson(mPermission.getUserPermission(), new TypeToken<LoginModel.Permissions>() {
                    }.getType()));
                    permissions_section.clear();
                    permissions_section.addAll(new Gson().fromJson(mPermission.getPermissionSection(), new TypeToken<List<PermissionSection>>() {
                    }.getType()));
                    generateList(permissions_section);
                } else {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "No Projects Found"));
                }
            }
        }.execute();
    }

    /**
     * function added on 23/6/2020 to call API of custom categories.
     */
    public void callShortcutCategories() {
        RestClient.createService().getRecentCategories(mPrefs.getProjectId()).enqueue(new Callback<RecentCategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<RecentCategoryModel> call, @NonNull Response<RecentCategoryModel> response) {
                CommonMethods.setLogs(TAG, "Recent categories", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (response.body().isSuccess()) {
                            saveShortcutCatDB(response.body().getData());
                            recentCategoryList.postValue(response.body().getData());
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecentCategoryModel> call, @NonNull Throwable t) {
                getOfflineShortCat();
                CommonMethods.setLogs(TAG, "Recent categories", t.getLocalizedMessage());
            }
        });
    }

    /**
     * function added on 23/6/2020 to add custom categories in Home menu tiles.
     */
    public void generateCategoryList(List<RecentCategory> recentCategories) {
        if (recentCategories != null && !recentCategories.isEmpty()) {
            isRefreshingLD.postValue(true);
            int index = 100;
            menuModelList.clear();
            for (int i = 0; i < recentCategories.size(); i++) {
                RecentCategory model = recentCategories.get(i);
                menuModelList.add(new DashBoardMenuModel(index, R.drawable.ic_cube, model.getCategoryTitle(), "", model.getColorCode(), model.getCategoryId(), model.getCategorySection(), String.valueOf(model.getDocumentsPending())));
            }
            generateList(permissions_section);
        }
    }

    private void generateList(List<PermissionSection> permissionsSection) {
        List<DashBoardMenuModel> gridList = new ArrayList<>();
        if (!permissionsSection.isEmpty()) {
            gridList.clear();
            for (DashBoardMenuModel model : CommonMethods.getMenuList()) {
                if (permissionsSection.toString().contains(model.getValue())) {
                    for (PermissionSection section : permissionsSection) {
                        if (!section.getValue().equalsIgnoreCase("none") && section.getSection().equals(model.getValue())) {
                            gridList.add(model);
                            if (section.getSection().equalsIgnoreCase("todos")) {
                                mPrefs.setTodoUserType(section.getValue());
                            }
                            break;
                        }
                    }
                }
            }
            /**
             * modification on 23/6/2020 to add custom categories in Home menu tiles
             */
            gridList.addAll(menuModelList);
            Collections.sort(gridList, (o1, o2) -> o1.getAction_index().compareTo(o2.getAction_index()));
            homeProjectsListLD.postValue(gridList);
            isRefreshingLD.postValue(false);
        }
    }

    private void accessOfflineProjects(String lat, String lng) {
        Location userLocation = new Location("user");
        userLocation.setLatitude(Double.parseDouble(lat));
        userLocation.setLongitude(Double.parseDouble(lng));

        if (mPrefs.isProjectSignIn()) {
            SignedProject signedProject = mDatabase.getSignedProject(mPrefs.getSiteId(), mPrefs.getUserId());
            isLoadingLD.postValue(false);
            if (signedProject != null) {
                offlineSignOutDialogLD.postValue(signedProject);
            } else {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "No project found"));
            }
        } else {
            List<ProjectDetails> userProjectList = mDatabase.getUserProjectList(mPrefs.getSiteId(), mPrefs.getUserId());
            List<ProjectDetails> accessProjectList = new ArrayList<>();

            if (userProjectList != null) {
                for (ProjectDetails project : userProjectList) {
                    if (project.getLat() != null && !project.getLat().isEmpty() &&
                            project.getLang() != null && !project.getLang().isEmpty() &&
                            project.getShowfor_mobile_siteaccess().equalsIgnoreCase("1")) {
                        project.setProjectStartTime(CommonMethods.getCurrentDate(CommonMethods.DF_9));
                        project.setProjectEndTime(CommonMethods.getCurrentDate(CommonMethods.DF_2));
                        /** Calculate Distance from user to site*/
                        Location projectLocation = new Location("project");
                        projectLocation.setLatitude(Double.parseDouble(project.getLat()));
                        projectLocation.setLongitude(Double.parseDouble(project.getLang()));
                        project.setDistance(CommonMethods.locationDistance(userLocation, projectLocation));
                        accessProjectList.add(project);
                    }
                }
                /** Sort projects w.r.t distance using latitude & longitude */
                Collections.sort(accessProjectList, (s1, s2) -> Float.compare(s1.getDistance(), s2.getDistance()));
                isLoadingLD.postValue(false);
                /* nearest project */
                if (!accessProjectList.isEmpty()) {
                    offlineSignInDialogLD.postValue(accessProjectList.get(0));
                    CommonMethods.setLogs(TAG, "After sorting", new Gson().toJson(accessProjectList));
                } else {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.no_project_available)));
                }
            }
        }
    }

    public void offlineSignIn(ProjectDetails model) {
        mPrefs.saveProjectSign(true);
        insertDetailInLocalDBOffline(model);
    }

    public void offlineSignOut(String idList, String breakList, String overtimeList) {
        mPrefs.saveProjectSign(false);
        /* update for syncing */
        List<SignedProject> projects = mDatabase.getSignedProjects(mPrefs.getSiteId(), mPrefs.getUserId());

        for (SignedProject project : projects) {
            if (null == project.getUserEndTime() || project.getUserEndTime().isEmpty()) {
                updateProjectDetails(project.getProjectID(), breakList, overtimeList, idList);
            }
        }
        isLoadingLD.postValue(false);
        notifyGridAdapterLD.postValue(true);
    }

    private void updateProjectDetails(String projectId, String breaks, String overtimes, String idList) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updateSignedProject(mPrefs.getSiteId(), mPrefs.getUserId(),
                        projectId, 1, CommonMethods.getCurrentDate(CommonMethods.DF_1), breaks, overtimes, idList,
                        mPrefs.getCurrentLocation().getLatitude(), mPrefs.getCurrentLocation().getLongitude());
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                globalSyncService();
            }
        }.execute();
    }

    private void insertDetailInLocalDBOffline(ProjectDetails model) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.insertSignedProject(mPrefs.getSiteId(), mPrefs.getUserId(), model.getUid(),
                        CommonMethods.getCurrentDate(CommonMethods.DF_15), "",
                        0, new Gson().toJson(model),
                        mPrefs.getCurrentLocation().getLatitude(), mPrefs.getCurrentLocation().getLongitude(),
                        "", "");
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                notifyGridAdapterLD.postValue(true);
            }
        }.execute();
    }

    private void insertCheckDetailInDB(SiteAccessCheckModel.SignInCheckData data) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.insertSignedProject(mPrefs.getSiteId(), mPrefs.getUserId(), data.getProjectDetails().getUid(),
                        data.getDate() != null && !data.getDate().isEmpty() ? data.getDate() + " " + data.getTime() : CommonMethods.getCurrentDate(CommonMethods.DF_15),
                        "", 0, new Gson().toJson(data.getProjectDetails()),
                        data.getProjectDetails().getLat(), data.getProjectDetails().getLang(),
                        "", "");
            }
        }.execute();
    }

    private void clearProjectSignedInfo() {
        mDatabase.deleteSignInformation(mPrefs.getSiteId(), mPrefs.getUserId());
    }


    private void fetchPermission(List<PermissionSection> permissionSection, LoginModel.Permissions permissions) {
        new AsyncTask<Void, Void, Permission>() {
            @Override
            protected Permission doInBackground(Void... voids) {
                return mDatabase.getPermission(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(Permission permission) {
                super.onPostExecute(permission);
                // Insert/Update User-Site Permission in local database
                try {
                    if (permission != null) {
                        mDatabase.updatePermission(mPrefs.getSiteId(),
                                mPrefs.getUserId(), new Gson().toJson(permissions),
                                new Gson().toJson(permissionSection));
                    } else {
                        mDatabase.insertPermission(
                                mPrefs.getSiteId(),
                                mPrefs.getUserId(),
                                new Gson().toJson(permission),
                                new Gson().toJson(permissionSection)
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    accessAllSections();
                }
            }
        }.execute();

    }

    private void fetchGeneralSetting(List<GeneralSettingsDatum> settingsDatumList) {
        new AsyncTask<Void, Void, GeneralSetting>() {
            @Override
            protected GeneralSetting doInBackground(Void... voids) {
                return mDatabase.getGeneralSetting(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(GeneralSetting setting) {
                super.onPostExecute(setting);
                try {
                    if (setting != null) {
                        mDatabase.updateGeneralSetting(mPrefs.getSiteId(),
                                mPrefs.getUserId(), new Gson().toJson(settingsDatumList));
                    } else {
                        mDatabase.insertGeneralSetting(
                                mPrefs.getSiteId(),
                                mPrefs.getUserId(), new Gson().toJson(settingsDatumList));
                    }
                    mPrefs.addGeneralSetting(settingsDatumList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void checkForVersionUpdate() {
        if (isInternetAvailable()) {
            new AsyncTask<Void, String, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String newVersion = null;
                    try {
                        newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getApplication().getPackageName() + "&hl=en")
                                .timeout(30000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                                .select(".hAyfc .htlgb")
                                .get(7)
                                .ownText();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return newVersion;
                }

                @Override
                protected void onPostExecute(String onlineVersion) {
                    super.onPostExecute(onlineVersion);
                    if (onlineVersion != null && !onlineVersion.isEmpty() && mPrefs.isDialogVisible() &&
                            !BuildConfig.VERSION_NAME.equalsIgnoreCase(onlineVersion)) {
                        updateAppLD.postValue(true);
                    } else {
                        updateAppLD.postValue(false);
                    }
                }
            }.execute();
        }
    }

    /**
     * Save Shortcut categories into DATABASE
     */
    private void saveShortcutCatDB(List<RecentCategory> ShortcatList) {
        try {
            new AsyncTask<Void, Void, ShortcutCategories>() {
                @Override
                protected ShortcutCategories doInBackground(Void... voids) {
                    return mDatabase.getShortcutCat(mPrefs.getSiteId(), mPrefs.getUserId());
                }

                @Override
                protected void onPostExecute(ShortcutCategories categories) {
                    super.onPostExecute(categories);
                    if (categories != null) {
                        mDatabase.updateShortcutCat(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), new Gson().toJson(ShortcatList));
                    } else {
                        mDatabase.insertShortcutCat(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), new Gson().toJson(ShortcatList));
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOfflineShortCat() {
        List<RecentCategory> catList = new ArrayList<>();
        try {
            ShortcutCategories response = mDatabase.getShortcutCat(mPrefs.getSiteId(), mPrefs.getUserId());
            if (response != null && response.getItemData() != null && !response.getItemData().isEmpty()) {
                catList = new Gson().fromJson(response.getItemData(), new TypeToken<List<RecentCategory>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        recentCategoryList.postValue(catList);
    }
}
