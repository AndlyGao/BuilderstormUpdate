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
import com.builderstrom.user.data.database.databaseModels.Categories;
import com.builderstrom.user.data.database.databaseModels.Users;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.Attachment;
import com.builderstrom.user.data.retrofit.modals.CatListing;
import com.builderstrom.user.data.retrofit.modals.CategoryModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.PojoSuccessCommon;
import com.builderstrom.user.data.retrofit.modals.PojoToDo;
import com.builderstrom.user.data.retrofit.modals.PojoToDoListResp;
import com.builderstrom.user.data.retrofit.modals.ToDoComments;
import com.builderstrom.user.data.retrofit.modals.ToDoCommentsModel;
import com.builderstrom.user.data.retrofit.modals.ToDoFilterModel;
import com.builderstrom.user.data.retrofit.modals.ToDoListDataModel;
import com.builderstrom.user.data.retrofit.modals.ToDoType;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.data.retrofit.modals.UserModel;
import com.builderstrom.user.service.SyncAllToDoTask;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.customViews.textChips.chip.ChipInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ToDoViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Boolean> isOfflineViewLD;
    public MutableLiveData<ToDoListDataModel> todoListLD;
    public MutableLiveData<List<ToDoType>> toDoTypeLD;
    public MutableLiveData<List<CatListing>> categoriesLD;
    public MutableLiveData<Boolean> addToDoSuccessLD;
    public MutableLiveData<Boolean> notifyAdapterLD;
    public MutableLiveData<Boolean> updateStatusLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<ToDoComments>> commentsLD;
    private List<String> syncedList = new ArrayList<>();
    private List<PojoToDo> listToSync = new ArrayList<>();

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        isOfflineViewLD = new MutableLiveData<>();
        todoListLD = new MutableLiveData<>();
        addToDoSuccessLD = new MutableLiveData<>();
        toDoTypeLD = new MutableLiveData<>();
        categoriesLD = new MutableLiveData<>();
        notifyAdapterLD = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        updateStatusLD = new MutableLiveData<>();
        commentsLD = new MutableLiveData<>();
    }

    public void updateSyncedList() {
        syncedList.clear();
        syncedList.addAll(mDatabase.getSyncedToDos(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId()));
    }

    public void getUsers() {
        RestClient.createService().apiAllUsers(null, "0", DataNames.TO_DO_SECTION_USERS).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /** update users in local database */
                        updateUsersToDatabase(response.body().getAllUsers(), DataNames.TO_DO_SECTION_USERS);
                        usersLD.postValue(response.body().getAllUsers());
                    } else {
                        usersLD.postValue(new ArrayList<>());
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                getUsersFromDB();
            }
        });
    }

    public void getToDoList(int offset, Integer limit, ToDoFilterModel filterModel) {
        updateSyncedList();
        isRefreshingLD.postValue(true);
        RestClient.createService().apiGetToDoList(mPrefs.getProjectId(),
                filterModel.getStatusId(), filterModel.getCategoryId(), filterModel.getUserId(),
                filterModel.getStartDate().isEmpty() ? "" : filterModel.getStartDate() + "-" + filterModel.getEndDate(), offset,
                limit).enqueue(new Callback<PojoToDoListResp>() {
            @Override
            public void onResponse(@NonNull Call<PojoToDoListResp> call, @NonNull Response<PojoToDoListResp> response) {
                isRefreshingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        isOfflineViewLD.postValue(false);
                        if (response.body().getData().getToDos() != null) {
                            for (PojoToDo todo : response.body().getData().getToDos()) {
                                todo.setSynced(syncedList.contains(String.valueOf(todo.getId())));
                                if (syncedList.contains(String.valueOf(todo.getId()))) {
                                    updateSavedToDo(todo);
                                }
                            }
                        }
                        toDoTypeLD.postValue(response.body().getData().getToDoTotals());
                        todoListLD.postValue(new ToDoListDataModel(response.body().getData().getToDos(), false));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (
                        Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<PojoToDoListResp> call, @NonNull Throwable t) {
                getOfflineToDoList();
            }
        });
    }

    private void updateSavedToDo(PojoToDo todo) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (todo.getAttachments() != null && !todo.getAttachments().isEmpty()) {
                    for (Attachment attachment : todo.getAttachments()) {
                        downloadFile(attachment.getFileUrl(), attachment.getFileName(), false);
                    }
                }

                return mDatabase.updateToDoItem(mPrefs.getSiteId(), mPrefs.getUserId(), todo);
            }
        }.execute();
    }

    public void getToDoCategories() {
        RestClient.createService().apiGetToDoCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (response.body().getData() != null) {
                            addCategoriesToDatabase(response.body().getData(), DataNames.TODO_CATEGORIES);
                            categoriesLD.postValue(response.body().getData());
                        } else {
                            categoriesLD.postValue(new ArrayList<>());
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                getOfflineCategories(DataNames.TODO_CATEGORIES);
            }
        });
    }

    public void addToDo(PojoToDo toDo) {
        isLoadingLD.postValue(true);
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        if (toDo.getAttachments() != null) {
            if (isInternetAvailable()) {
                for (Attachment attach : toDo.getAttachments()) {
                    filesPart.add(CommonMethods.createPartFromFile("files", attach.getFileUrl()));
                }
            }
        }
        RestClient.createService().apiAddToDo(CommonMethods.createPartFromString(mPrefs.getProjectId()),
                CommonMethods.createPartFromString(toDo.getTitle()),
                CommonMethods.createPartFromString(String.valueOf(toDo.getCategory())),
                CommonMethods.createPartFromString(toDo.getDescription()),
                CommonMethods.createPartFromString(toDo.getDueDate()),
                CommonMethods.createPartFromString(toDo.getStrToUsers()),
                CommonMethods.createPartFromString(toDo.getStrCcUsers()),
                CommonMethods.createPartFromString(""),
                filesPart).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        addToDoSuccessLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                addToDoToOffline(toDo, false);
            }
        });
    }

    public void editToDo(PojoToDo toDo) {
        isLoadingLD.postValue(true);
        String image_id = "";
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        if (toDo.getAttachments() != null) {
            if (toDo.getAttachments().size() > 0) {
                if (isInternetAvailable()) {
                    for (Attachment attach : toDo.getAttachments()) {
                        if (attach.isOffline()) {
                            image_id = toDo.getImgae_Id();
                            filesPart.add(CommonMethods.createPartFromFile("files", attach.getFileUrl()));
                        }
                    }
                }
            } else {
                image_id = toDo.getImgae_Id();
            }
        }

        RestClient.createService().apiEditToDo(
                CommonMethods.createPartFromString(String.valueOf(toDo.getId())),
                CommonMethods.createPartFromString(String.valueOf(toDo.getProjectId())),
                CommonMethods.createPartFromString(toDo.getTitle()),
                CommonMethods.createPartFromString(String.valueOf(toDo.getCategory())),
                CommonMethods.createPartFromString(toDo.getDescription()),
                CommonMethods.createPartFromString(toDo.getDueDate()),
                CommonMethods.createPartFromString(toDo.getStrToUsers()),
                CommonMethods.createPartFromString(toDo.getStrCcUsers()),
                CommonMethods.createPartFromString(image_id),
                filesPart.isEmpty() ? null : filesPart).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        addToDoSuccessLD.postValue(true);
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

    private void addToDoToOffline(PojoToDo toDo, boolean isSyncing) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (isSyncing) {
                    if (toDo.getAttachments() != null && !toDo.getAttachments().isEmpty()) {
                        for (Attachment attachment : toDo.getAttachments()) {
                            if (!attachment.isOffline()) {
                                downloadFile(attachment.getFileUrl(), attachment.getFileName(), false);
                            }
                        }
                    }
                }

                return mDatabase.insertToDoItem(mPrefs.getSiteId(), mPrefs.getUserId(),
                        String.valueOf(toDo.getProjectId()), toDo.getId() == null ? "0" :
                                String.valueOf(toDo.getId()), String.valueOf(toDo.getCategory()),
                        new Gson().toJson(toDo));
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (aLong != null) {
                    updateSyncedList();
                    notifyAdapterLD.postValue(true);
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                            getApplication().getString(isSyncing ? R.string.success_document_synced
                                    : R.string.success_data_saved)));
                    addToDoSuccessLD.postValue(true);
                }
            }
        }.execute();
    }

    public void syncDocument(PojoToDo toDoItem) {
        if (!syncedList.contains(String.valueOf(toDoItem.getId()))) {
            isLoadingLD.postValue(true);
            toDoItem.setSelected(false);
            addToDoToOffline(toDoItem, true);
        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                    getApplication().getString(R.string.doc_already_synced)));
        }
    }

    public void getAllComments(String toDoId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetToDoComments(toDoId).enqueue(new Callback<ToDoCommentsModel>() {
            @Override
            public void onResponse(@NonNull Call<ToDoCommentsModel> call, @NonNull Response<ToDoCommentsModel> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        commentsLD.postValue(response.body().getData());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ToDoCommentsModel> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addToDoComment(String toDoId, String comment) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAddToDoComment(toDoId, comment).enqueue(new Callback<PojoSuccessCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {

                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        getAllComments(toDoId);
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    isLoadingLD.postValue(false);
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void markTodo(Integer toDoId, Integer status, PojoToDo toDo) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiMarkToDo(toDoId, status, CommonMethods.getCurrentDate(CommonMethods.DF_1)).
                enqueue(new Callback<PojoNewSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                        isLoadingLD.postValue(false);
                        try {
                            if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                                updateStatusLD.postValue(true);
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
//                isLoadingLD.postValue(false);
//                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
                        markTodoOffline(toDoId, status, toDo);
                    }
                });
    }


    /* DataBase Operations */
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

    private void getUsersFromDB() {
        List<User> allUserList = new ArrayList<>();
        try {
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), DataNames.TO_DO_SECTION_USERS);
            if (userResponse != null && userResponse.getUserData() != null && !userResponse.getUserData().isEmpty()) {
                allUserList = new Gson().fromJson(userResponse.getUserData(), new TypeToken<List<User>>() {
                }.getType());
            } else {
                Users commonResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), "");
                if (commonResponse != null && commonResponse.getUserData() != null && !commonResponse.getUserData().isEmpty()) {
                    allUserList = new Gson().fromJson(commonResponse.getUserData(), new TypeToken<List<User>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersLD.postValue(allUserList);
    }

    private void getOfflineToDoList() {
        new AsyncTask<Void, Void, List<PojoToDo>>() {
            @Override
            protected List<PojoToDo> doInBackground(Void... voids) {
                return mDatabase.getAllToDos(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId());
            }

            @Override
            protected void onPostExecute(List<PojoToDo> pojoToDos) {
                super.onPostExecute(pojoToDos);
                offsetLD.postValue(0);
                isRefreshingLD.postValue(false);
                isOfflineViewLD.postValue(true);
                if (pojoToDos != null) {
                    toDoTypeLD.postValue(getToDoStatuses(pojoToDos));
                    todoListLD.postValue(new ToDoListDataModel(pojoToDos, true));
                }
            }
        }.execute();

    }

    private List<ToDoType> getToDoStatuses(List<PojoToDo> pojoToDos) {
        HashMap<String, Integer> typeMap = new HashMap<>();
        typeMap.put(DataNames.TODO_NORMAL, 0);
        typeMap.put(DataNames.TODO_WARNING, 0);
        typeMap.put(DataNames.TODO_OVERDUE, 0);
        for (PojoToDo toDo : pojoToDos) {
            switch (toDo.getStatus()) {
                case 0:
                    break;
                case 1:
                    Integer normal = typeMap.get(DataNames.TODO_NORMAL);
                    typeMap.put(DataNames.TODO_NORMAL, normal == null ? 1 : normal + 1);
                    break;
                case 2:
                    Integer warning = typeMap.get(DataNames.TODO_WARNING);
                    typeMap.put(DataNames.TODO_WARNING, warning == null ? 1 : warning + 1);
                    break;
                case 3:
                    Integer overdue = typeMap.get(DataNames.TODO_OVERDUE);
                    typeMap.put(DataNames.TODO_OVERDUE, overdue == null ? 1 : overdue + 1);
                    break;
            }
        }
        List<ToDoType> typeList = new ArrayList<>();
        for (String key : typeMap.keySet()) {
            typeList.add(new ToDoType(key, typeMap.get(key)));
        }
        return typeList;
    }

    public List<ChipInfo> setChipInfo(List<User> toUsers) {
        List<ChipInfo> chipInfos = new ArrayList<>();

        if (toUsers != null && !toUsers.isEmpty()) {
            for (User user : toUsers) {
                chipInfos.add(new ChipInfo(user.getName(), user));
            }
        }
        return chipInfos;
    }

    public void syncAllToDos(List<PojoToDo> docList) {
        updateSyncedList();
        getListToSync(docList);
        if (!listToSync.isEmpty()) {
            if (isAlreadyScheduleToDoJob()) {
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Already syncing"));
            } else {
                PersistableBundle dataBundle = new PersistableBundle();
                dataBundle.putString("data", new Gson().toJson(listToSync));
                getScheduler().schedule(new JobInfo.Builder(DataNames.SYNC_ALL_TO_DO_SERVICE_ID,
                        new ComponentName(getApplication(), SyncAllToDoTask.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(dataBundle)
                        .build());
            }
        } else {
            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Nothing to sync"));
        }
    }

    private void getListToSync(List<PojoToDo> allDocs) {
        listToSync.clear();
        for (PojoToDo doc : allDocs) {
            if (!syncedList.contains(String.valueOf(doc.getId()))) {
                listToSync.add(doc);
            }
        }
    }

    private void markTodoOffline(Integer todoId, Integer status, PojoToDo toDo) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return mDatabase.isToDoItemResolved(todoId);
            }

            @Override
            protected void onPostExecute(Boolean aLong) {
                super.onPostExecute(aLong);
                if (aLong != null
                ) {
                    isLoadingLD.postValue(false);
                    toDo.setCompletedOn(status == 1 ? CommonMethods.getCurrentDate(CommonMethods.DF_1) : "");
                    toDo.setStatus(status);
                    mDatabase.updateToDoItem(mPrefs.getSiteId(), mPrefs.getUserId(), toDo);
                    if (aLong) {
                        mDatabase.insertToDoResolvedStatus(mPrefs.getSiteId(), mPrefs.getUserId(), todoId, status, CommonMethods.getCurrentDate(CommonMethods.DF_1));
                    } else {
                        mDatabase.updateToDoResolvedStatus(mPrefs.getSiteId(), mPrefs.getUserId(), todoId, status, CommonMethods.getCurrentDate(CommonMethods.DF_1));
                    }
                    updateStatusLD.postValue(true);
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, status == 0 ? "To do has been marked as incomplete" : "To do has been marked as complete"));
                }

            }
        }.execute();
    }

    public void markOfflineEntry(PojoToDo pojoToDo) {
        isLoadingLD.postValue(true);
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.updateToDoOffline(mPrefs.getSiteId(), mPrefs.getUserId(), pojoToDo);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (aLong != null) {
                    updateStatusLD.postValue(true);
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, pojoToDo.getStatus() == 0 ? "To do has been marked as incomplete" : "To do has been marked as complete"));
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
        categoriesLD.postValue(catList);
    }
}
