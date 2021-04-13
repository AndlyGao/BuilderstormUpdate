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
package com.builderstrom.user.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.AddAttachModel;
import com.builderstrom.user.data.retrofit.modals.GalleryData;
import com.builderstrom.user.data.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.data.retrofit.modals.MetaDataField;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;
import com.builderstrom.user.data.retrofit.modals.MetaValues;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.builderstrom.user.views.adapters.AddGalleryImageAdapter;
import com.builderstrom.user.views.adapters.metaDataAdapters.MetaDataListAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class AddGalleryActivity extends BaseActivity {
    public List<AddAttachModel> dairyFileList = new ArrayList<>();
    public List<GalleryPicModel> imagesList = new ArrayList<>();
    @BindView(R.id.include) Toolbar toolbar;
    @BindView(R.id.llGlobal) LinearLayout llGlobal;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.btnUploadFiles) Button btnUploadFiles;
    @BindView(R.id.rvAddGallery) RecyclerView rvAddGallery;
    @BindView(R.id.rvMetaData) RecyclerView rvMetaData;
    private Integer metaPosition = -1;
    private String galleryID = "", rowID = "";
    private boolean isEditMode = false;
    private PermissionUtils permissionUtils;
    private ProjectPhotosVM viewModel;
    private MetaDataListAdapter metaAdapter;
    private File outPutFile;
    private List<List<MetaValues>> editMetaValues = new ArrayList<>();
    private List<MetaDataField> metaDataList = new ArrayList<>();
    private List<String> base64ImageList = new ArrayList<>();

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.isUploadingLD.observe(this, loader -> {
            if (loader != null) {
                if (loader) showProgress("Uploading gallery...");
                else dismissProgress();
            }
        });

        viewModel.successAddGalleryLD.observe(this, successAdd -> {
            if (successAdd != null && successAdd) {
                callBroadcast();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (null != errorModel && !isDestroyed()) {
                handleErrorModel(errorModel);
            }
        });

        viewModel.metaDataLD.observe(this, metaDataFields -> {
            if (metaDataFields != null) {
                updateMetaDataView(metaDataFields);
            }
        });

        viewModel.successDeleteGalleryImageLD.observe(this, model -> {
            if (model != null) {
                if (model.getGroupPosition() < imagesList.size()) {
                    imagesList.remove(model.getGroupPosition());
                    notifyAdaptor();
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_gallery;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ProjectPhotosVM.class);
        observeViewModel();
        getBundleData();
        initView();
        /* access metadata*/
        viewModel.getGalleryMetaData();
    }

    private void initView() {
        toolbar.setTitle(isEditMode ? "Edit Gallery" : "Add Gallery");
        toolbar.setNavigationOnClickListener(view -> {
            activityFinish();
        });
        metaAdapter = new MetaDataListAdapter(this, metaDataList);
        metaAdapter.setCallback(position -> {
            metaPosition = position;
            checkPermission();
        });
        rvMetaData.setAdapter(metaAdapter);
        notifyAdaptor();
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        if (userPermissions != null)
            CommonMethods.checkVisiblePermission(userPermissions.getPhotoGallery().getUploadPhoto(), btnUploadFiles);
    }

    private void getBundleData() {
        if (getIntent().hasExtra("isEdit")) {
            isEditMode = true;
            imagesList.clear();
            GalleryData gallery = new Gson().fromJson(getIntent().getStringExtra("data"), GalleryData.class);
            galleryID = gallery.getGalleryId() == null ? "" : gallery.getGalleryId();
            etTitle.setText(gallery.getTitle());
            rowID = gallery.getRowId();
            imagesList.addAll(gallery.getPics());
            notifyAdaptor();
            if (null != gallery.getCustom_field_data()) {
                editMetaValues.addAll(gallery.getCustom_field_data());
            }
        }
    }

    @OnClick({R.id.btnUploadFiles, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUploadFiles:
                CommonMethods.hideKeyboard(getApplicationContext(), etTitle);
                Intent intent = new Intent(getApplicationContext(), GetPhotoActivity.class);
                startActivityForResult(intent, CommonMethods.PHOTO_REQUEST_CODE);
                break;

            case R.id.btnSave:
                etTitle.setError(null);
                if (etTitle.getText().toString().trim().isEmpty()) {
                    etTitle.setError("required field");
                } else if (imagesList.isEmpty()) {
                    errorMessage("Please select atleast one image", null, false);
                } else {
                    CommonMethods.hideKeyboard(getApplicationContext(), etTitle);
                    if (isEditMode) {
                        callUpdateGalleryMethod(etTitle.getText().toString().trim(), galleryID, rowID);
                    } else {
                        callAddGalleryMethod(etTitle.getText().toString().trim());
                    }
                }
                break;
        }
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(AddGalleryActivity.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
                finish();
            }
        });
        permissionUtils.checkPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case CommonMethods.FILE_REQUEST_CODE:
                    Uri uriFile = data.getData();
                    if (uriFile != null) {
                        String fileLocation = CommonMethods.getFilePathFromURI(AddGalleryActivity.this, uriFile);
                        File sourceFile = new File(fileLocation);
                        File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(AddGalleryActivity.this, uriFile));
                        CommonMethods.copyFile(sourceFile, destinationFile);
                        if (metaPosition == -1) {
                            AddAttachModel model = new AddAttachModel();
                            model.setFilePath(fileLocation);
                            model.setFileurl("");
                            model.setName(CommonMethods.getFileName(AddGalleryActivity.this, uriFile));
                            if (CommonMethods.isImageUrl(CommonMethods.getFileName(AddGalleryActivity.this, uriFile))) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFile);
                                    model.setImageBitmap(bitmap);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            dairyFileList.add(model);
//                                notifyAdaptor();
                        } else {
                            List<String> fileList = new ArrayList<>();
                            try {
                                String fileName = CommonMethods.getFileName(this, uriFile);
                                if (!fileName.substring(fileName.lastIndexOf(".")).isEmpty()) {
                                    fileList.add(fileLocation);
                                }
                            } catch (Exception e) {
                                errorMessage("File not supported", null, false);
                            } finally {
                                metaDataList.get(metaPosition).setMetaUploadFiles(fileList);
                                metaAdapter.notifyItemChanged(metaPosition);
                            }
                        }
                    }
                    break;

                case CommonMethods.PHOTO_REQUEST_CODE:
                    if (null != data.getStringExtra("filePath")) {
                        outPutFile = new File(data.getStringExtra("filePath"));
                        Bitmap imageBitmap = CommonMethods.getScaleBitmap(300, outPutFile.getAbsolutePath());
                        GalleryPicModel model = new GalleryPicModel();
                        model.setImagePath(outPutFile.getAbsolutePath());
                        model.setImageBitmap(imageBitmap);
                        model.setClient(true);
                        imagesList.add(model);
                        notifyAdaptor();
                    } else {
                        errorMessage("File doesn't exist on device.", null, false);
                    }
                    break;
                case CommonMethods.PHOTO_REQUEST_UPDATE:
                    outPutFile = new File(data.getStringExtra("filePath"));
                    Bitmap imageBitmapUpdate = CommonMethods.getScaleBitmap(300, outPutFile.getAbsolutePath());
                    GalleryPicModel modelUpdate = new GalleryPicModel();
                    modelUpdate.setImageBitmap(imageBitmapUpdate);
                    modelUpdate.setImagePath(outPutFile.getAbsolutePath());
                    modelUpdate.setClient(true);
                    imagesList.add(modelUpdate);
                    notifyAdaptor();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.STORAGE_REQUEST:
            case PermissionUtils.CAMERA_REQUEST:
                permissionUtils.verifyResults(requestCode, grantResults);
                break;
        }
    }

    private void notifyAdaptor() {
        AddGalleryImageAdapter mImageAdapter = new AddGalleryImageAdapter(this, imagesList, viewModel);
        rvAddGallery.setAdapter(mImageAdapter);
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        activityFinish();
    }

    private void updateMetaDataView(List<MetaDataField> response) {
        metaDataList.clear();
        if (null != response && !response.isEmpty()) {
            for (MetaDataField metaModel : response) {
                if (metaModel.getFieldType().equalsIgnoreCase("select") || metaModel.getFieldType().equalsIgnoreCase("checkbox") || metaModel.getFieldType().equalsIgnoreCase("checkbox+input")) {
                    if ((metaModel.getOptions() != null && !metaModel.getOptions().isEmpty())) {
                        metaDataList.add(metaModel);
                    }
                } else {
                    metaDataList.add(metaModel);
                }
            }
        }

        /* for edit diary with pre filled data*/
        if (isEditMode) {
            for (List<MetaValues> preValues : editMetaValues) {
                for (MetaDataField data : metaDataList) {
                    if (preValues.get(0).getCustom_field_id().equals(data.getId())) {
                        switch (preValues.get(0).getField_type()) {
                            case "textarea":
                            case "text":
                            case "date":
                            case "blankfiller":
                            case "signature":
                                data.setAnswerString(preValues.get(0).getValue());
                                break;
                            case "select":
                            case "checkbox":
                                for (MetaValues optionValue : preValues) {
                                    for (MetaOptions option : data.getOptions()) {
                                        if (option.getOptionName().equals(optionValue.getValue())) {
                                            option.setSelected(true);
                                        }
                                    }
                                }
                                break;
                            case "checkbox+input":
                                for (MetaValues optionValue : preValues) {
                                    for (MetaOptions option : data.getOptions()) {
                                        if (option.getOptionName().equals(optionValue.getValue())) {
                                            option.setSelected(true);
                                            option.setAnswerString(optionValue.getCheck_input());
                                        }
                                    }
                                }
                                break;
                            case "select+input":
                                for (MetaOptions option : data.getOptions()) {
                                    if (preValues.get(0).getValue().equals(option.getOptionName())) {
                                        option.setSelected(true);
                                    }
                                }
                                data.setAnswerString(preValues.get(0).getCheck_input());
                                break;
                            case "radio":
                                for (MetaOptions option : data.getOptions()) {
                                    if (preValues.get(0).getValue().equals(option.getOptionName())) {
                                        option.setSelected(true);
                                    }
                                }
                                break;
                            case "file":
                                List<String> serverFiles = new ArrayList<>();
                                serverFiles.add(preValues.get(0).getValue());
                                data.setMetaServerFiles(serverFiles);
                                break;
                        }
                    }
                }
            }
        }
        metaAdapter.notifyDataSetChanged();
        llGlobal.setVisibility(View.VISIBLE);
    }

    private void callAddGalleryMethod(String title) {
        base64ImageList.clear();
        for (GalleryPicModel model : imagesList) {
            base64ImageList.add(model.getImagePath());
        }
        if (!base64ImageList.isEmpty() && metaAdapter.isProperFilled()) {
            viewModel.apiAddGallery(title, viewModel.mPrefs.getCurrentLocation() == null ?
                            "0.00" : viewModel.mPrefs.getCurrentLocation().getLatitude(),
                    viewModel.mPrefs.getCurrentLocation() == null ? "0.00" :
                            viewModel.mPrefs.getCurrentLocation().getLongitude(),
                    base64ImageList, metaAdapter.getMetaValuesList());
        }
    }

    private void callUpdateGalleryMethod(String title, String galleryID, String rowID) {
        base64ImageList.clear();
        for (GalleryPicModel model : imagesList) {
            if (model.isClient()) {
                base64ImageList.add(model.getImagePath());
            }
        }
        if (metaAdapter.isProperFilled()) {
            viewModel.apiUpdateGallery(title, galleryID,
                    viewModel.mPrefs.getCurrentLocation() == null ? "0.00" :
                            viewModel.mPrefs.getCurrentLocation().getLatitude(),
                    viewModel.mPrefs.getCurrentLocation() == null ? "0.00" :
                            viewModel.mPrefs.getCurrentLocation().getLongitude(), rowID,
                    base64ImageList, metaAdapter.getMetaValuesList());
        }
    }

    @Override
    public void onBackPressed() {
        activityFinish();
    }

    private void activityFinish() {
        if (isTaskRoot()) {
            startActivity(new Intent(AddGalleryActivity.this, DashBoardActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
        CommonMethods.hideKeyboard(this);
    }

}

