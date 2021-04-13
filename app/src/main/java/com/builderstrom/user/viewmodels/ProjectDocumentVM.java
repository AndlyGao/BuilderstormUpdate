package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.data.database.databaseModels.Categories;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.CatListing;
import com.builderstrom.user.data.retrofit.modals.CategoryModel;
import com.builderstrom.user.data.retrofit.modals.DocumentStatus;
import com.builderstrom.user.data.retrofit.modals.DoumentStatusModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PDCommentModel;
import com.builderstrom.user.data.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.data.retrofit.modals.PojoCommon;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.ProjectDocumentModel;
import com.builderstrom.user.service.SyncAllProjectDocsTask;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ProjectDocumentVM extends BaseViewModel {

    private static final String TAG = ProjectDocumentVM.class.getName();

    public MutableLiveData<String> actionSuccess;
    public MutableLiveData<List<PDocsDataModel>> projectDocLiveList;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<PDCommentModel.Comment>> DocCommentLiveList;
    public MutableLiveData<List<CatListing>> categoryLiveList;
    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> isSyncing;
    public MutableLiveData<Boolean> isDeleted;
    public MutableLiveData<Boolean> notifyAdapterLD;
    public MutableLiveData<Boolean> syncAllVisibilityLD;
    public MutableLiveData<List<DocumentStatus>> documentStatusLD;
    private List<String> syncedList = new ArrayList<>();
    private List<PDocsDataModel> listToSync = new ArrayList<>();

    public ProjectDocumentVM(@NonNull Application application) {
        super(application);
        actionSuccess = new MutableLiveData<>();
        projectDocLiveList = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        DocCommentLiveList = new MutableLiveData<>();
        categoryLiveList = new MutableLiveData<>();
        notifyAdapterLD = new MutableLiveData<>();
        isSuccess = new MutableLiveData<>();
        isSyncing = new MutableLiveData<>();
        isDeleted = new MutableLiveData<>();
        syncAllVisibilityLD = new MutableLiveData<>();
        documentStatusLD = new MutableLiveData<>();
    }

    public void projectDocuments(String categoryId, Integer offset, Integer limit) {
        updateSyncedList();
        isLoadingLD.postValue(true);
        RestClient.createService().apiProjectDocument(mPrefs.getProjectId(), categoryId, offset, limit).enqueue(new Callback<ProjectDocumentModel>() {
            @Override
            public void onResponse(@NonNull Call<ProjectDocumentModel> call, @NonNull Response<ProjectDocumentModel> response) {
                CommonMethods.setLogs(TAG, "apiProjectDocument", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        if (null != response.body().getAllfile()) {
                            for (PDocsDataModel docs : response.body().getAllfile()) {
                                if (syncedList.contains(String.valueOf(docs.getUid()))) {
                                    docs.setSynced(true);
                                    updateDBDocList(docs);
                                }
                            }
                        }
                        syncAllVisibilityLD.postValue(true);
                        projectDocLiveList.postValue(response.body().getAllfile() == null ? new ArrayList<>() : response.body().getAllfile());

                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<ProjectDocumentModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "apiProjectDocument", t.getLocalizedMessage());
                getOfflineDocsList(categoryId);
            }
        });
    }

    private void getOfflineDocsList(String categoryId) {
        new AsyncTask<Void, Void, List<PDocsDataModel>>() {
            @Override
            protected List<PDocsDataModel> doInBackground(Void... voids) {
                return mDatabase.getProjectDocuments(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId(), categoryId);
            }

            @Override
            protected void onPostExecute(List<PDocsDataModel> documents) {
                super.onPostExecute(documents);
                isLoadingLD.postValue(false);
                syncAllVisibilityLD.postValue(false);
                projectDocLiveList.postValue(documents);
            }
        }.execute();
    }

    public void projectDocCategories() {
        isLoadingLD.postValue(true);
        RestClient.createService().apiProjectDocCategory(mPrefs.getProjectId()).enqueue(new Callback<CategoryModel>() {

            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                CommonMethods.setLogs(TAG, "proDocumentCategories", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        if (response.body().getData() == null || response.body().getData().isEmpty()) {
                            categoryLiveList.postValue(new ArrayList<>());
                        } else {
                            saveCategoriesDB(response.body().getData(), DataNames.DOCUMENT_CATEGORIES);
                            categoryLiveList.postValue(response.body().getData());
                        }

                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                getOfflineCatList(DataNames.DOCUMENT_CATEGORIES);
            }
        });
    }

    public void getDocsStatusList() {
        RestClient.createService().apiDocumentStatuses().enqueue(new Callback<DoumentStatusModel>() {
            @Override
            public void onResponse(@NonNull Call<DoumentStatusModel> call, @NonNull Response<DoumentStatusModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        mPrefs.addDocumentStatuses(response.body().getData());
                        documentStatusLD.postValue(response.body().getData() == null ?
                                new ArrayList<>() : response.body().getData());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DoumentStatusModel> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    documentStatusLD.postValue(mPrefs.getDocumentStatuses());
                }

            }
        });
    }

    private void getOfflineCatList(String section) {
        List<CatListing> catList = new ArrayList<>();
        try {
            Categories response = mDatabase.getCategoryBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section);
            if (response != null && response.getItemData() != null && !response.getItemData().isEmpty()) {
                catList = new Gson().fromJson(response.getItemData(), new TypeToken<List<CatListing>>() {
                }.getType());
            } else {
                Categories commonResponse = mDatabase.getAllCategory(mPrefs.getSiteId(), mPrefs.getUserId());
                if (commonResponse != null && commonResponse.getItemData() != null && !commonResponse.getItemData().isEmpty()) {
                    catList = new Gson().fromJson(commonResponse.getItemData(), new TypeToken<List<CatListing>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoadingLD.postValue(false);
        categoryLiveList.postValue(catList);

    }

    public void projectDocsComments(String docsId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiPDComments(docsId).enqueue(new Callback<PDCommentModel>() {
            @Override
            public void onResponse(@NonNull Call<PDCommentModel> call, @NonNull Response<PDCommentModel> response) {
                CommonMethods.setLogs(TAG, "projectDocsComments", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        DocCommentLiveList.postValue(response.body().getComments() != null ? response.body().getComments() : new ArrayList<>());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PDCommentModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "projectDocsComments", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });

    }

    public void addComment(String docsId, String comment) {
        isLoadingLD.postValue(true);
        RestClient.createService().addPDComment(docsId, comment).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                CommonMethods.setLogs(TAG, "addDocumentComment", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "addDocumentComment", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void actionOnDocument(String action, Integer documentId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiDocumentAction(action, mPrefs.getProjectId(), documentId).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                CommonMethods.setLogs(TAG, "Action on document", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Action on document", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void composeDocument(PDocsDataModel dataModel) {
        if (dataModel.getUid() == 0 && dataModel.getTableRowId() == null) {
            addDocument(dataModel);
        } else {
            if (dataModel.getUid()!=0) {
                editDocument(dataModel);
            } else {
                isLoadingLD.postValue(true);
                updateDocumentOffline(dataModel);
            }
        }
    }

    private void addDocument(PDocsDataModel dataModel) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAddDocument(
                mPrefs.getProjectId(), CommonMethods.createPartFromString(dataModel.getTitle()),
                CommonMethods.createPartFromString(dataModel.getCategoryUid() == null ? "" : dataModel.getCategoryUid()),
                CommonMethods.createPartFromString(dataModel.getStatusID() == null ? "" : dataModel.getStatusID()),
                CommonMethods.createPartFromString(dataModel.getRevision()),
                CommonMethods.createPartFromString(dataModel.getSignedDoc()),
                CommonMethods.createPartFromString(dataModel.getNote()),
                CommonMethods.createPartFromString(dataModel.getPincomment()),
                CommonMethods.isFileNullOrEmpty(dataModel.getFile()) ? null : CommonMethods.createPartFromFile("document", dataModel.getFile())
        ).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                CommonMethods.setLogs(TAG, "Add  Document", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Add  Document", t.getLocalizedMessage());
                dataModel.setSynced(false);
                addOfflineData(dataModel);
            }
        });
    }

    private void editDocument(PDocsDataModel dataModel) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiEditDocument(mPrefs.getProjectId(),
                CommonMethods.createPartFromString(String.valueOf(dataModel.getUid())),
                CommonMethods.createPartFromString(dataModel.getTitle()),
                CommonMethods.createPartFromString(dataModel.getCategoryUid() != null ? dataModel.getCategoryUid() : ""),
                CommonMethods.createPartFromString(dataModel.getStatusID()),
                CommonMethods.createPartFromString(dataModel.getRevision()),
                CommonMethods.createPartFromString(dataModel.getSignedDoc()),
                CommonMethods.createPartFromString(dataModel.getNote()),
                CommonMethods.createPartFromString(dataModel.getPincomment()),
                CommonMethods.isFileNullOrEmpty(dataModel.getFile()) ? null : CommonMethods.createPartFromFile("document", dataModel.getFile())
        ).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                CommonMethods.setLogs(TAG, "Edit document", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Edit document", t.getLocalizedMessage());
//                dataModel.setEdited(true);
//                updateSyncedDocument(dataModel);
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    private void addOfflineData(PDocsDataModel dataModel) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.insertProjectDocs(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId(), dataModel.getCategoryUid(), "0", new Gson().toJson(dataModel));
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                if (aLong != null) {
                    isLoadingLD.postValue(false);
                    isSuccess.postValue(true);
                }
            }
        }.execute();
    }

    public void deleteProjectDocument(String docId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiDeleteProjectDoc(docId).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        isDeleted.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }

                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });


    }

    private void saveCategoriesDB(List<CatListing> catListings, String section) {
        try {
            new AsyncTask<Void, Void, Categories>() {
                @Override
                protected Categories doInBackground(Void... voids) {
                    return mDatabase.getCategoryBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section);
                }

                @Override
                protected void onPostExecute(Categories categories) {
                    super.onPostExecute(categories);
                    if (categories != null) {
                        mDatabase.updateCategoryList(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(catListings));
                    } else {
                        mDatabase.insertCategory(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(catListings));
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(mDatabase.getDocsIdList(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId()));
    }

    public void syncDocs(PDocsDataModel docs) {
        isSyncing.postValue(true);
        docs.setSynced(true);
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                String downloadUrl = CommonMethods.decodeUrl(docs.getUrlFile());
                if (!downloadUrl.isEmpty()) {
                    downloadFile(downloadUrl, docs.getOriginalName(), false);
                }
                return mDatabase.insertProjectDocs(mPrefs.getSiteId(),
                        mPrefs.getUserId(), mPrefs.getProjectId(), docs.getCategoryUid(),
                        String.valueOf(docs.getUid()), new Gson().toJson(docs));
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                notifyAdapterLD.postValue(true);
                isSyncing.postValue(false);
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, aLong != null ?
                        "Document synced successfully" : "Operation failed"));
            }
        }.execute();
    }

    public void syncAllProjectDocuments(List<PDocsDataModel> allDocs) {
        updateSyncedList();
        getListToSync(allDocs);
        if (!listToSync.isEmpty()) {
            /* Sync list */
            if (isAlreadySchedulePDocsJob()) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Already syncing"));
            } else {
                PersistableBundle dataBundle = new PersistableBundle();
                dataBundle.putString("data", new Gson().toJson(listToSync));
                getScheduler().schedule(new JobInfo.Builder(DataNames.SYNC_ALL_PROJECT_DOCS_SERVICE_ID,
                        new ComponentName(getApplication(), SyncAllProjectDocsTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(dataBundle)
                        .build());
            }
        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Nothing to sync"));
        }

    }

    private void getListToSync(List<PDocsDataModel> allDocs) {
        listToSync.clear();
        for (PDocsDataModel doc : allDocs) {
            if (!syncedList.contains(String.valueOf(doc.getUid()))) {
                listToSync.add(doc);
            }
        }
    }

    private void updateDBDocList(PDocsDataModel docs) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                String downloadUrl = CommonMethods.decodeUrl(docs.getUrlFile());
                if (!downloadUrl.isEmpty()) {
                    downloadFile(downloadUrl, docs.getOriginalName(), false);
                }
                return mDatabase.updateProjectDocFromOnline(mPrefs.getSiteId(), mPrefs.getUserId(), docs);
            }
        }.execute();
    }

    private void updateDocumentOffline(PDocsDataModel docs) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updateProjectDocOffline(mPrefs.getSiteId(), mPrefs.getUserId(),mPrefs.getProjectId(), docs.getTableRowId(), docs);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (aLong != null) {
                    isSuccess.postValue(true);
                }
            }
        }.execute();
    }

    private void updateSyncedDocument(PDocsDataModel docs) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updateProjectDocFromOnline(mPrefs.getSiteId(), mPrefs.getUserId(), docs);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (aLong != null) {
                    isSuccess.postValue(true);
                }
            }
        }.execute();
    }

}


