package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.data.database.databaseModels.DiaryMetaDataDb;
import com.builderstrom.user.data.database.databaseModels.Gallery;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.DeleteGalleryChildImageModel;
import com.builderstrom.user.data.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.GalleryComment;
import com.builderstrom.user.data.retrofit.modals.GalleryData;
import com.builderstrom.user.data.retrofit.modals.GalleryModel;
import com.builderstrom.user.data.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.data.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.data.retrofit.modals.MetaDataField;
import com.builderstrom.user.data.retrofit.modals.PhotosDataModel;
import com.builderstrom.user.data.retrofit.modals.PojoAddGallery;
import com.builderstrom.user.data.retrofit.modals.PojoGalleryComment;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class ProjectPhotosVM extends BaseViewModel {

    private static final String TAG = ProjectPhotosVM.class.getName();

    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<Boolean> isOfflineAdapterLD;
    public MutableLiveData<PhotosDataModel> allGalleriesListLD;
    public MutableLiveData<Boolean> successAddGalleryLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<MetaDataField>> metaDataLD;
    public MutableLiveData<Integer> deleteGallerySuccessLD;
    public MutableLiveData<List<GalleryComment>> imageCommentsLD;
    public MutableLiveData<Boolean> successCommentAddedLD;
    public MutableLiveData<DeleteGalleryChildImageModel> successDeleteGalleryImageLD;

    public ProjectPhotosVM(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        isOfflineAdapterLD = new MutableLiveData<>();
        allGalleriesListLD = new MutableLiveData<>();
        successAddGalleryLD = new MutableLiveData<>();
        metaDataLD = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        deleteGallerySuccessLD = new MutableLiveData<>();
        imageCommentsLD = new MutableLiveData<>();
        successCommentAddedLD = new MutableLiveData<>();
        successDeleteGalleryImageLD = new MutableLiveData<>();
    }

    public void callGalleryAPI(Integer offset, Integer limit) {
        isRefreshingLD.postValue(true);
        RestClient.createService().apiGetAllProjectPhotos(mPrefs.getProjectId(), offset, limit).enqueue(new Callback<GalleryModel>() {
            @Override
            public void onResponse(@NonNull Call<GalleryModel> call, @NonNull Response<GalleryModel> response) {
                setLogs(TAG, "callGalleryAPI", new Gson().toJson(response));
                isRefreshingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        if (offset == 0) {
                            isOfflineAdapterLD.postValue(false);
                        }
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        allGalleriesListLD.postValue(new PhotosDataModel(response.body().getData()
                                == null ? new ArrayList<>() : response.body().getData(), false));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GalleryModel> call, @NonNull Throwable t) {
                setLogs(TAG, "callGalleryAPI", t.getLocalizedMessage());
                getOfflineGalleries();
            }
        });
    }

    public void apiAddGallery(String title, String latitude, String longitude, List<String> files, List<LocalMetaValues> metaData) {
        isUploadingLD.postValue(true);
        List<MultipartBody.Part> galleryFiles = new ArrayList<>();

        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                galleryFiles.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String filePath : files) {
            galleryFiles.add(CommonMethods.createPartFromFile("galleryFiles" + files.indexOf(filePath), filePath));
        }

        RestClient.createService().apiAddGallery(CommonMethods.createPartFromString(mPrefs.getProjectId()),
                null, CommonMethods.createPartFromString(title), CommonMethods.createPartFromString(latitude),
                CommonMethods.createPartFromString(longitude),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                galleryFiles).enqueue(new Callback<PojoAddGallery>() {
            @Override
            public void onResponse(@NonNull Call<PojoAddGallery> call, @NonNull Response<PojoAddGallery> response) {
                setLogs(TAG, "addGalleryMultipartAPI", new Gson().toJson(response));
                isUploadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        successAddGalleryLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoAddGallery> call, @NonNull Throwable t) {
                setLogs(TAG, "addGalleryMultipartAPI", t.getLocalizedMessage());
                insertGalleryToDb(title, metaData, files);
            }
        });
    }

    public void apiUpdateGallery(String title, String galleryID, String latitude,
                                 String longitude, String rowID, List<String> files,
                                 List<LocalMetaValues> metaData) {
        isUploadingLD.postValue(true);

        List<MultipartBody.Part> galleryFiles = new ArrayList<>();

        /* gallery files */
        for (String filePath : files) {
            galleryFiles.add(CommonMethods.createPartFromFile("galleryFiles" + files.indexOf(filePath), filePath));
        }
        /* metaData files */
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                galleryFiles.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        RestClient.createService().apiAddGallery(CommonMethods.createPartFromString(mPrefs.getProjectId()),
                CommonMethods.createPartFromString(galleryID), CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(latitude),
                CommonMethods.createPartFromString(longitude),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                galleryFiles).enqueue(new Callback<PojoAddGallery>() {
            @Override
            public void onResponse(@NonNull Call<PojoAddGallery> call, @NonNull Response<PojoAddGallery> response) {
                setLogs(TAG, "updateGalleryMultipartAPI", new Gson().toJson(response));
                isUploadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        successAddGalleryLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<PojoAddGallery> call, @NonNull Throwable t) {
                setLogs(TAG, "updateGalleryMultipartAPI", t.getLocalizedMessage());
                if (null != rowID && !rowID.isEmpty()) {
                    updateLocalGallery(title, rowID, files);
                }
            }
        });
    }

    public void getGalleryMetaData() {
        isLoadingLD.postValue(true);
        RestClient.createService().getDiaryAPIMetaData(mPrefs.getProjectId(), DataNames.META_GALLERY_SECTION).enqueue(
                new Callback<DiaryMetaDataModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DiaryMetaDataModel> call, @NonNull Response<DiaryMetaDataModel> response) {
                        setLogs(TAG, "getGalleryMetaData", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                                insertGalleryMetaData(response.body().getFields());
                            } else {
                                isLoadingLD.postValue(true);
                                handleErrorBody(response.errorBody());
                            }
                        } catch (IOException e) {
                            isLoadingLD.postValue(true);
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DiaryMetaDataModel> call, @NonNull Throwable t) {
                        setLogs(TAG, "getGalleryMetaData", t.getLocalizedMessage());
                        updateFromDataBase();

                    }
                });
    }

    public void deleteGalleryAPI(String galleryID, int adapterPosition) {
        isLoadingLD.postValue(true);
        RestClient.createService().deleteGallery(galleryID).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "deleteGalleryAPI", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        deleteGallerySuccessLD.postValue(adapterPosition);
                    } else handleErrorBody(response.errorBody());

                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(TAG, "deleteGalleryAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                if (CommonMethods.isNetworkError(t)) {
                    if (!isInternetAvailable()) {
                        mDatabase.deleteGallery(galleryID);
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                                getApplication().getString(R.string.success_gallery_delete)));
                        deleteGallerySuccessLD.postValue(adapterPosition);
                    }
                } else {
                    errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));

                }

            }
        });
    }

    public void deleteGalleryImageAPI(String image_id, Integer prevPosition, List<GalleryPicModel> picModel) {
        isLoadingLD.postValue(true);
        RestClient.createService().deleteGalleryImage(image_id).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "deleteGalleryImageAPI", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        successDeleteGalleryImageLD.postValue(new DeleteGalleryChildImageModel(prevPosition, picModel));
                    } else {
                        successDeleteGalleryImageLD.postValue(null);
                        handleErrorBody(response.errorBody());

                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(TAG, "deleteGalleryImageAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                successDeleteGalleryImageLD.postValue(null);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void getAllImageComments(String imageId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetImageAllComments(imageId).enqueue(new Callback<PojoGalleryComment>() {
            @Override
            public void onResponse(@NonNull Call<PojoGalleryComment> call,
                                   @NonNull Response<PojoGalleryComment> response) {
                setLogs(TAG, "getAllImageComments", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        imageCommentsLD.postValue(response.body().getAllComments());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoGalleryComment> call, @NonNull Throwable t) {
                setLogs(TAG, "getAllImageComments", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void imageCommentAPI(String imageID, String comment) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiAddImageComment(imageID, comment).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "imageCommentAPI", new Gson().toJson(response));
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                        successCommentAddedLD.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(TAG, "imageCommentAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* ******************* Database operations ******************** */

    private void getOfflineGalleries() {
        new AsyncTask<Void, Void, List<Gallery>>() {
            @Override
            protected List<Gallery> doInBackground(Void... voids) {
                try {
                    return mDatabase.getProjectGallery(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Gallery> galleries) {
                super.onPostExecute(galleries);
                List<GalleryData> dataList = new ArrayList<>();
                if (galleries != null) {
                    for (Gallery gallery : galleries) {
                        List<GalleryPicModel> itemPicsList = new ArrayList<>();
                        List<String> galleryImageList = new Gson().fromJson(gallery.getImages(), new TypeToken<List<String>>() {
                        }.getType());
                        for (String filePath : galleryImageList) {
                            GalleryPicModel model = new GalleryPicModel();
                            model.setImagePath(filePath);
                            model.setClient(true);
                            itemPicsList.add(model);
                        }
                        GalleryData mGalleryData = new GalleryData();
                        mGalleryData.setTitle(gallery.getGalleryTitle());
                        mGalleryData.setRowId(String.valueOf(gallery.getId()));
                        mGalleryData.setPics(itemPicsList);
                        dataList.add(mGalleryData);
                    }
                }

                isRefreshingLD.postValue(false);
                isOfflineAdapterLD.postValue(true);
                allGalleriesListLD.postValue(new PhotosDataModel(dataList, true));

            }
        }.execute();

    }

    private void insertGalleryToDb(String title, List<LocalMetaValues> metaData, List<String> files) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertProjectGallery(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), "0", title,
                            mPrefs.getCurrentLocation() == null ? "0.00" : mPrefs.getCurrentLocation().getLatitude(),
                            mPrefs.getCurrentLocation() == null ? "0.00" : mPrefs.getCurrentLocation().getLongitude(),
                            new Gson().toJson(metaData), new Gson().toJson(files));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isUploadingLD.postValue(false);
                globalSyncService();
                if (aLong != null) {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS,
                            getApplication().getString(R.string.success_added_gallery)));
                }
                successAddGalleryLD.postValue(true);
            }
        }.execute();

    }

    private void updateLocalGallery(String title, String rowID, List<String> files) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.updateProjectGallery(title, rowID, new Gson().toJson(files));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isUploadingLD.postValue(false);
                if (aLong != null) {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getApplication().getString(R.string.success_added_gallery)));
                }
                successAddGalleryLD.postValue(true);
            }
        }.execute();
    }

    private void insertGalleryMetaData(List<MetaDataField> fields) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getMetaDataBySection(mPrefs.getSiteId(),
                        mPrefs.getProjectId(), DataNames.META_GALLERY_SECTION)) {
                    return mDatabase.updateMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getProjectId(), DataNames.META_GALLERY_SECTION,
                            new Gson().toJson(fields));
                } else {
                    return mDatabase.insertMetaDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), mPrefs.getUserName(), DataNames.META_GALLERY_SECTION,
                            new Gson().toJson(fields));
                }
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                updateFromDataBase();
            }
        }.execute();
    }

    private void updateFromDataBase() {
        DiaryMetaDataDb dbMetaData = mDatabase.getMetaDataBySection(mPrefs.getSiteId(),
                mPrefs.getProjectId(), DataNames.META_GALLERY_SECTION);
        List<MetaDataField> localList;
        if (null != dbMetaData) {
            localList = new Gson().fromJson(dbMetaData.getMetaData(), new TypeToken<List<MetaDataField>>() {
            }.getType());
        } else localList = new ArrayList<>();
        isLoadingLD.postValue(false);
        metaDataLD.postValue(localList == null ? new ArrayList<>() : localList);
    }

}
