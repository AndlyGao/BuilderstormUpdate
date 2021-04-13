package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.database.databaseModels.Categories;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.CategoryModel;
import com.builderstrom.user.repository.retrofit.modals.CompanyComment;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.CompanyStatus;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.PojoCompanyCommentModel;
import com.builderstrom.user.repository.retrofit.modals.PojoCompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.PojoCompanyStatus;
import com.builderstrom.user.repository.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.service.SyncAllCompanyDocsTask;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class CompanyViewModel extends BaseViewModel {

    private static String TAG = CompanyViewModel.class.getName();
    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Boolean> syncVisibilityLD;
    public MutableLiveData<List<CompanyDocument>> companyDocListLD;
    public MutableLiveData<Boolean> notifyAdapterLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<CompanyStatus>> companyStatusLD;
    public MutableLiveData<List<CatListing>> companyCategoryLD;
    public MutableLiveData<Boolean> addedSuccessLD;
    public MutableLiveData<Boolean> trackStatusLD;
    public MutableLiveData<Boolean> favStatusLD;
    public MutableLiveData<List<CompanyComment>> commentsLD;
    public MutableLiveData<String> successAddCommentLD;
    public MutableLiveData<Boolean> successDeleteCompanyDocLD;
    private List<CompanyDocument> listToSync = new ArrayList<>();

    private List<String> syncedList = new ArrayList<>();

    public CompanyViewModel(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        syncVisibilityLD = new MutableLiveData<>();
        companyDocListLD = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        notifyAdapterLD = new MutableLiveData<>();
        companyStatusLD = new MutableLiveData<>();
        companyCategoryLD = new MutableLiveData<>();
        addedSuccessLD = new MutableLiveData<>();
        trackStatusLD = new MutableLiveData<>();
        favStatusLD = new MutableLiveData<>();
        commentsLD = new MutableLiveData<>();
        successAddCommentLD = new MutableLiveData<>();
        successDeleteCompanyDocLD = new MutableLiveData<>();
    }

    private void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(mDatabase.getSyncedCompanyDocs(mPrefs.getSiteId(), mPrefs.getUserId()));
    }

    public void getCompanyDocList(String categoryId, Integer offset, Integer limit) {
        updateSyncedList();
        isRefreshingLD.postValue(true);
        RestClient.createService().apiCompanyDocs(categoryId, null, null,
                null, null, null, null, null, null, null,
                offset, limit).enqueue(new Callback<PojoCompanyDocument>() {
            @Override
            public void onResponse(@NonNull Call<PojoCompanyDocument> call, @NonNull Response<PojoCompanyDocument> response) {
                CommonMethods.setLogs(TAG, "getCompanyDocList", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        if (null != response.body().getAllDocuments()) {
                            for (CompanyDocument doc : response.body().getAllDocuments()) {
                                if (syncedList.contains(doc.getId())) {
                                    updateCompanyDocOnListing(doc);
                                    doc.setDocSynced(true);
                                }

                            }
                        }
                        syncVisibilityLD.postValue(true);
                        companyDocListLD.postValue(response.body().getAllDocuments() == null ? new ArrayList<>() : response.body().getAllDocuments());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isRefreshingLD.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<PojoCompanyDocument> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "getCompanyDocList", t.getLocalizedMessage());
                if (CommonMethods.isNetworkError(t)) {
                    getOfflineCompanyDocsList(categoryId);
                } else {
                    isRefreshingLD.setValue(false);
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }

            }
        });
    }

    private void updateCompanyDocOnListing(CompanyDocument doc) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (doc.getFilename() != null && !doc.getFilename().isEmpty()) {
                    downloadFile(CommonMethods.decodeUrl(doc.getFilelocation()), doc.getFilename(), false);
                }
                return mDatabase.updateCompanyDocFromOnline(mPrefs.getSiteId(), mPrefs.getUserId(), doc);
            }
        }.execute();
    }

    public void getCompanyStatusList() {
        RestClient.createService().apiCompanyStatuses().enqueue(new Callback<PojoCompanyStatus>() {
            @Override
            public void onResponse(@NonNull Call<PojoCompanyStatus> call, @NonNull Response<PojoCompanyStatus> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        mPrefs.addCompanyStatuses(response.body().getData());
                        companyStatusLD.postValue(response.body().getData() == null ?
                                new ArrayList<>() : response.body().getData());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCompanyStatus> call, @NonNull Throwable t) {
                if (CommonMethods.isNetworkError(t)) {
                    companyStatusLD.postValue(mPrefs.getCompanyStatuses());
                }
            }
        });
    }

    public void getCompanyCategories() {
        RestClient.createService().apiCompanyCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        if (response.body().getData() != null) {
                            companyCategoryLD.postValue(response.body().getData());
                            addCategoriesToDatabase(response.body().getData(), DataNames.COMPANY_CATEGORIES);
                        } else {
                            companyCategoryLD.postValue(new ArrayList<>());
                        }
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_EXCEPTION, e, null, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                getOfflineCategories(DataNames.COMPANY_CATEGORIES);
            }
        });
    }

    public void addCompanyDocument(String title, String category, String categoryTitle,
                                   String revision, String parentId,
                                   String signed, String pinnedComment, String statusId,
                                   String regionalDoc, String fileLocation, String docNumber) {
        isLoadingLD.postValue(true);
        CompanyDocument document = new CompanyDocument();
        document.setTitle(title);
        document.setCategory(category);
        document.setDoc_status(statusId);
        document.setCat_title(categoryTitle);
        document.setRevision(revision);
        document.setParent_id(parentId);
        document.setRegional_doc(regionalDoc);
        document.setSigned_doc(signed);
        document.setPinComment(pinnedComment);
        document.setUploadername(mPrefs.getUserName());
        document.setFilename(CommonMethods.getFileNameFromPath(fileLocation));
        document.setFilelocation(fileLocation);
        document.setDocument_no(docNumber);
        document.setCreated_on(CommonMethods.getCurrentDate(CommonMethods.DF_1));


        RestClient.createService().apiAddCompanyDocument(CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString("0"),
                CommonMethods.createPartFromString(category),
                CommonMethods.createPartFromString(revision),
                CommonMethods.createPartFromString(parentId),
                CommonMethods.createPartFromString("0"),
                CommonMethods.createPartFromString(signed),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(pinnedComment),
                CommonMethods.createPartFromString(statusId),
                CommonMethods.createPartFromString(regionalDoc),
                CommonMethods.createPartFromString(docNumber),
                CommonMethods.isFileNullOrEmpty(fileLocation) ? null :
                        CommonMethods.createPartFromFile("document", fileLocation))
                .enqueue(new Callback<PojoNewSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                        isLoadingLD.postValue(false);
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                                addedSuccessLD.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }

                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                        if (CommonMethods.isNetworkError(t)) {
                            insertCompanyDocToDb("0", category, new Gson().toJson(document));
                        } else {
                            isLoadingLD.postValue(false);
                            errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));

                        }
                    }
                });

    }

    public void editCompanyDocument(String documentId, String title, String category, /*String categoryTitle,*/
                                    String revision, String parentId,
                                    String signed, String pinnedComment, String statusId,
                                    String regionalDoc, String fileLocation, String docNumber) {
        isLoadingLD.postValue(true);
        if (CommonMethods.isNetworkAvailable(getApplication())) {
            RestClient.createService().apiEditCompanyDocument(
                    CommonMethods.createPartFromString("0"),
                    CommonMethods.createPartFromString(documentId),
                    CommonMethods.createPartFromString(title),
                    CommonMethods.createPartFromString("0"),
                    CommonMethods.createPartFromString(category),
                    CommonMethods.createPartFromString(revision),
                    CommonMethods.createPartFromString(parentId),
                    CommonMethods.createPartFromString("0"),
                    CommonMethods.createPartFromString(signed),
                    CommonMethods.createPartFromString(""),
                    CommonMethods.createPartFromString(""),
                    CommonMethods.createPartFromString(pinnedComment),
                    CommonMethods.createPartFromString(statusId),
                    CommonMethods.createPartFromString(regionalDoc),
                    CommonMethods.createPartFromString(docNumber),
                    CommonMethods.isFileNullOrEmpty(fileLocation) ? null :
                            CommonMethods.createPartFromFile("document", fileLocation))
                    .enqueue(new Callback<PojoNewSuccess>() {
                        @Override
                        public void onResponse(@NonNull Call<PojoNewSuccess> call,
                                               @NonNull Response<PojoNewSuccess> response) {
                            isLoadingLD.postValue(false);
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                                    addedSuccessLD.postValue(true);
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

    }

    public void deleteCompanyDocument(String docId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiDeleteCompanyDoc(docId).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        successDeleteCompanyDocLD.postValue(true);
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

    public void trackDocument(String docId, String docTitle) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiTrackCompanyDoc(docId, "", docTitle).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        trackStatusLD.postValue(true);
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

    public void favoriteDocument(String docId, String docTitle) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiFavoriteCompanyDoc(docId, "", docTitle).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        favStatusLD.postValue(true);
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

    public void getAllComments(String documentId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetAllCommentsCompanyDoc(documentId).enqueue(new Callback<PojoCompanyCommentModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoCompanyCommentModel> call,
                                   @NonNull Response<PojoCompanyCommentModel> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        commentsLD.postValue(response.body().getAllComments());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCompanyCommentModel> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addComment(String docId, String docTitle, String comment) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAddCommentCompanyDoc(docId, "", docTitle, comment).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        successAddCommentLD.postValue(response.body().getMessage());
                        getAllComments(docId);
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

    /* Database Operations */

    private void getOfflineCompanyDocsList(String categoryId) {
        new AsyncTask<Void, Void, List<CompanyDocument>>() {
            @Override
            protected List<CompanyDocument> doInBackground(Void... voids) {
                return mDatabase.getCompanyDocuments(mPrefs.getSiteId(), mPrefs.getUserId(), categoryId);
            }

            @Override
            protected void onPostExecute(List<CompanyDocument> companyDocuments) {
                super.onPostExecute(companyDocuments);
                isRefreshingLD.postValue(false);
                syncVisibilityLD.postValue(false);
                companyDocListLD.postValue(companyDocuments);
            }
        }.execute();
    }

    private void insertCompanyDocToDb(String rowId, String category, String project) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.insertCompanyDocs(rowId, mPrefs.getSiteId(), mPrefs.getUserId(),
                        category, project);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (aLong != null) {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                            getApplication().getString(rowId.equals("0") ? R.string.success_document_added
                                    : R.string.success_document_synced)));
                }
                if (rowId.equals("0")) {
                    addedSuccessLD.postValue(true);
                } else {
                    notifyAdapterLD.postValue(true);
                }
            }
        }.execute();
    }

    public void syncCompanyDocument(CompanyDocument document) {
        if (isInternetAvailable() && document.getId() != null) {
            if (!document.isDocSynced()) {
                isLoadingLD.postValue(true);
                document.setDocSynced(true);
                if (!document.getFilename().isEmpty()) {
                    downloadFile(document.getFilelocation(), document.getFilename(), true);
                }
                insertCompanyDocToDb(document.getId(), document.getCategory(), new Gson().toJson(document));
            } else {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.doc_already_synced)));
            }
        }
    }

    public void syncAllProjectDocuments(List<CompanyDocument> docList) {
        updateSyncedList();
        getListToSync(docList);
        if (!listToSync.isEmpty()) {
            if (isAlreadyScheduleComDocsJob()) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Already syncing"));
            } else {
                PersistableBundle dataBundle = new PersistableBundle();
                dataBundle.putString("data", new Gson().toJson(listToSync));
                getScheduler().schedule(new JobInfo.Builder(DataNames.SYNC_ALL_COMPANY_DOCS_SERVICE_ID,
                        new ComponentName(getApplication(), SyncAllCompanyDocsTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(dataBundle)
                        .build());
            }
        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Nothing to sync"));
        }
    }

    private void getListToSync(List<CompanyDocument> allDocs) {
        listToSync.clear();
        for (CompanyDocument doc : allDocs) {
            if (!syncedList.contains(doc.getId())) {
                listToSync.add(doc);
            }
        }
    }

    private void addCategoriesToDatabase(List<CatListing> catListings, String section) {
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

    private void getOfflineCategories(String section) {
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
        companyCategoryLD.postValue(catList);
    }
}
