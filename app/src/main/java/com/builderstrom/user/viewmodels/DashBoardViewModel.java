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
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.CategoryModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.repository.retrofit.modals.DoumentStatusModel;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.PMNotesModel;
import com.builderstrom.user.repository.retrofit.modals.PojoCompanyStatus;
import com.builderstrom.user.repository.retrofit.modals.PojoSuccessCommon;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.repository.retrofit.modals.ProjectsUpdateModel;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.repository.retrofit.modals.UserModel;
import com.builderstrom.user.repository.retrofit.modals.UserProjectModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class DashBoardViewModel extends BaseViewModel {
    public MutableLiveData<List<ProjectDetails>> projectsLD;
    public MutableLiveData<String> dropDownText;
    public MutableLiveData<Boolean> isLogOut;
    private String TAG = DashBoardViewModel.class.getName();

    public DashBoardViewModel(@NonNull Application application) {
        super(application);
        projectsLD = new MutableLiveData<>();
        isLogOut = new MutableLiveData<>();
        dropDownText = new MutableLiveData<>();

    }


    public void syncAllProjects() {
        RestClient.createService().getUserAssignedProjects().enqueue(new Callback<ProjectsUpdateModel>() {
            @Override public void onResponse(@NonNull Call<ProjectsUpdateModel> call,
                                             @NonNull Response<ProjectsUpdateModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        UserProjectModel projectModel = response.body().getData();
                        if (null != projectModel.getUser_projects()) {
                            updateSavedProjects(projectModel.getUser_projects());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override public void onFailure(@NonNull Call<ProjectsUpdateModel> call, @NonNull Throwable t) {

            }
        });
    }

    public void accessAllProjects(boolean isUpdating, String notification_projectId) {
        new AsyncTask<Void, Void, List<ProjectDetails>>() {
            @Override
            protected List<ProjectDetails> doInBackground(Void... voids) {
                return mDatabase.getUserProjectList(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(List<ProjectDetails> projects) {
                super.onPostExecute(projects);
                projectsLD.postValue(projects);
                if (projects != null && !projects.isEmpty()) {
                    if (notification_projectId != null) {
                        if (!isProjectContained(projects, notification_projectId)) {
                            mPrefs.setProjectId(projects.get(0).getUid());
                            mPrefs.setProjectName(projects.get(0).getTitle());
                            mPrefs.saveSelectedProject(projects.get(0));
                            dropDownText.postValue(projects.get(0).getTitle());
                        }
                    } else {
                        if (!isContainedInList(projects)) {
                            mPrefs.setProjectId(projects.get(0).getUid());
                            mPrefs.setProjectName(projects.get(0).getTitle());
                            mPrefs.saveSelectedProject(projects.get(0));
                            dropDownText.postValue(projects.get(0).getTitle());
                        } else {
                            if (!isUpdating) {
                                mPrefs.setProjectId(projects.get(0).getUid());
                                mPrefs.setProjectName(projects.get(0).getTitle());
                                mPrefs.saveSelectedProject(projects.get(0));
                                dropDownText.postValue(projects.get(0).getTitle());
                            }
                        }
                    }
                } else {
                    mPrefs.setProjectId("0");
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.no_project_available)));
                }
                if (!isUpdating) {
                    /* save All Lists and add first Fragment*/
                    if (isInternetAvailable()) {
                        getAllToUsers();
                        getAllMetaData(DataNames.META_DIARY_SECTION);
                        getAllMetaData(DataNames.META_DRAWING_SECTION);
                        getAllMetaData(DataNames.META_GALLERY_SECTION);
                        getAllMetaData(DataNames.META_RFI_SECTION);
                        updateCompanyCategories();
                        updateDocumentCategories();
                        updateToDoCategories();
                        updateCompanyStatuses();
                        updateDocsStatus();
                        updateDigitalDocCategories();
                        updateDigitalDocUsers();
                    }
                }
            }
        }.execute();
    }

    private boolean isContainedInList(List<ProjectDetails> projectList) {
        for (ProjectDetails project : projectList) {
            if (project.getUid().equalsIgnoreCase(mPrefs.getProjectId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isProjectContained(List<ProjectDetails> projectList, String notification_projectId) {
        for (ProjectDetails project : projectList) {
            if (project.getUid().equalsIgnoreCase(notification_projectId)) {
                mPrefs.setProjectId(project.getUid());
                mPrefs.setProjectName(project.getTitle());
                dropDownText.postValue(project.getTitle());
                return true;
            }
        }
        return false;
    }

    private void getAllToUsers() {
        RestClient.createService().apiAllUsers(
                mPrefs.getProjectId(), "0", "").enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                try {
                    CommonMethods.setLogs(TAG, "Get Users", new Gson().toJson(response));
                    if (null != response.body()) {
                        addUsersToDatabase(response.body().getAllUsers(), "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Get Users", t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    private void getAllMetaData(String section) {
        RestClient.createService().getDiaryAPIMetaData(mPrefs.getProjectId(), section).enqueue(new Callback<DiaryMetaDataModel>() {
            @Override
            public void onResponse(@NonNull Call<DiaryMetaDataModel> call, @NonNull Response<DiaryMetaDataModel> response) {
                if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                    addMetaDataToDb(section, new Gson().toJson(response.body().getFields()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiaryMetaDataModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Get Users", t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    private void updateCompanyCategories() {
        RestClient.createService().apiCompanyCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        addCategoriesToDatabase(response.body().getData(), DataNames.COMPANY_CATEGORIES);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateDocumentCategories() {
        RestClient.createService().apiProjectDocCategory(mPrefs.getProjectId()).enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        addCategoriesToDatabase(response.body().getData(), DataNames.DOCUMENT_CATEGORIES);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateToDoCategories() {
        RestClient.createService().apiGetToDoCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        addCategoriesToDatabase(response.body().getData(), DataNames.TODO_CATEGORIES);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateCompanyStatuses() {
        RestClient.createService().apiCompanyStatuses().enqueue(new Callback<PojoCompanyStatus>() {
            @Override
            public void onResponse(@NonNull Call<PojoCompanyStatus> call, @NonNull Response<PojoCompanyStatus> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        mPrefs.addCompanyStatuses(response.body().getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCompanyStatus> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateDocsStatus() {
        RestClient.createService().apiDocumentStatuses().enqueue(new Callback<DoumentStatusModel>() {
            @Override
            public void onResponse(@NonNull Call<DoumentStatusModel> call, @NonNull Response<DoumentStatusModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        mPrefs.addDocumentStatuses(response.body().getData());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DoumentStatusModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateDigitalDocCategories() {
        RestClient.createService().apiDigitalDocCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        /* save in database */
                        addCategoriesToDatabase(response.body().getData(), DataNames.DIGITAL_CATEGORIES);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void updateDigitalDocUsers() {
        RestClient.createService().apiDigitalItemUsers().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                setLogs(RFIViewModel.class.getName(), "getDigitalUsers", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /** update users in local database */
                        updateUsersToDatabase(response.body().getAllUsers(), DataNames.DIGI_DOCS_USERS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                setLogs(DashBoardViewModel.class.getName(), "getDigitalUsers", t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    /* Database Operations*/
    private void addUsersToDatabase(List<User> users, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateUsersList(mPrefs.getSiteId(), mPrefs.getUserId(),
                            section, new Gson().toJson(users));
                } else {
                    return mDatabase.insertUser(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), section, new Gson().toJson(users));
                }
            }
        }.execute();
    }

    private void addCategoriesToDatabase(List<CatListing> category, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getCategoryBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateCategoryList(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(category));
                } else {
                    return mDatabase.insertCategory(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(category));
                }
            }
        }.execute();
    }

    private void addMetaDataToDb(String section, String fields) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null == mDatabase.getMetaDataBySection(
                        mPrefs.getSiteId(), mPrefs.getProjectId(), section)) {
                    return mDatabase.insertMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getUserId(), mPrefs.getProjectId(),
                            mPrefs.getUserName(), section, fields);
                } else {
                    return mDatabase.updateMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getProjectId(), section, fields);
                }

            }


        }.execute();
    }


    public void logOutUser() {
        RestClient.createService().apiLogout().enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        updateLoginInfo();
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                updateLoginInfo();
            }
        });
    }

    private void updateLoginInfo() {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updateLogOutStatus(mPrefs.getSiteId(), mPrefs.getUserId(),
                        firebaseToken, 0);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                if (id != null) {
                    isLogOut.postValue(true);
                }
            }
        }.execute();
    }

    private void updateUsersToDatabase(List<User> users, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateUsersList(mPrefs.getSiteId(), mPrefs.getUserId(),
                            section, new Gson().toJson(users));
                } else {
                    return mDatabase.insertUser(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), section, new Gson().toJson(users));
                }
            }
        }.execute();
    }

}


//    public void accessAllProjects(boolean isUpdating, String notification_projectId) {
//        new AsyncTask<Void, Void, List<ProjectDetails>>() {
//            @Override
//            protected List<ProjectDetails> doInBackground(Void... voids) {
//                return mDatabase.getUserProjectList(mPrefs.getSiteId(), mPrefs.getUserId());
//            }
//
//            @Override
//            protected void onPostExecute(List<ProjectDetails> projects) {
//                super.onPostExecute(projects);
//                projectsLD.postValue(projects);
//                if (projects != null && !projects.isEmpty()) {
//                    if (!isContainedInList(projects)) {
//                        mPrefs.setProjectId(projects.get(0).getUid());
//                        dropDownText.postValue(projects.get(0).getTitle());
//                    } else {
//                        if (!isUpdating) {
//                            mPrefs.setProjectId(projects.get(0).getUid());
//                            dropDownText.postValue(projects.get(0).getTitle());
//                        }
//                    }
//                } else {
//                    mPrefs.setProjectId("0");
//                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.no_project_available)));
//                }
//                if (!isUpdating) {
//                    /* save All Lists and add first Fragment*/
//                    if (isInternetAvailable()) {
//                        getAllToUsers();
//                        getAllMetaData(DataNames.META_DIARY_SECTION);
//                        getAllMetaData(DataNames.META_DRAWING_SECTION);
//                        getAllMetaData(DataNames.META_GALLERY_SECTION);
//                        getAllMetaData(DataNames.META_RFI_SECTION);
//                        updateCompanyCategories();
//                        updateCompanyStatuses();
//                        updateDocsStatus();
//                        updateDigitalDocCategories();
//                        updateDigitalDocUsers();
//                    }
//                }
//            }
//        }.execute();
//    }