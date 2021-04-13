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
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.DocumentStatus;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.repository.retrofit.modals.Snag;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddDocumentActivity extends BaseActivity {
    @BindView(R.id.include)
    Toolbar toolbar;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etStatus)
    EditText etStatus;
    @BindView(R.id.etCategories)
    EditText etCategories;
    @BindView(R.id.etRevision)
    EditText etRevision;
    @BindView(R.id.etNotes)
    EditText etNotes;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.etUploadFile)
    EditText etUploadFile;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.llGlobal)
    LinearLayout llGlobal;
    @BindView(R.id.cbSigned)
    CheckBox cbSigned;
    private ProjectDocumentVM viewModel;
    private int isSigned = 0;
    Integer tableRowId = null;
    private boolean isEdit = false;
    private boolean isSynced = false;
    private PDocsDataModel dataModel;
    private PermissionUtils permissionUtils;
    private List<CatListing> docCatList = new ArrayList<>();
    private List<DocumentStatus> statusList = new ArrayList<>();
    private String fileLocation, categoryId = "", statusId = "", fileName = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_document;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ProjectDocumentVM.class);
        observeViewModel();
        /* setup toolbar */
        toolbar.setNavigationOnClickListener(view -> AddDocumentActivity.this.toolbarFinish());
        getBundleData();
        cbSigned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSigned = isChecked ? 1 : 0;
        });
    }

    private void getBundleData() {
        if (getIntent() != null) {
            docCatList = getIntent().getParcelableArrayListExtra("PDocsCategories");
            statusList = getIntent().getParcelableArrayListExtra("PDocsStatus");

            if (getIntent().hasExtra("EDIT_DOCS")) {
                toolbar.setTitle("Edit Document");
                isEdit = true;
//                dataModel = getIntent().getParcelableExtra("EDIT_DOCS");
                dataModel = new Gson().fromJson(getIntent().getStringExtra("EDIT_DOCS"), new TypeToken<PDocsDataModel>() {
                }.getType());
                setBundleData(dataModel);
            } else {
                toolbar.setTitle("Add Document");
            }
        } else {
            toolbar.setTitle("Add Document");
        }
    }

    private void setBundleData(PDocsDataModel dataModel) {
        etTitle.setText(dataModel.getTitle() != null ? dataModel.getTitle() : "");
        etRevision.setText(dataModel.getRevision() != null ? dataModel.getRevision() : "");
        etNotes.setText(dataModel.getNote() != null ? dataModel.getNote() : "");
        etComment.setText(dataModel.getPinnedcomment() != null ? dataModel.getPinnedcomment().getComment() != null ? dataModel.getPinnedcomment().getComment() : "" : "");
        if (dataModel.getPincomment() != null && !dataModel.getPincomment().isEmpty()) {
            etComment.setText(dataModel.getPincomment());
        }
        etUploadFile.setText(dataModel.getOriginalName() != null ? dataModel.getOriginalName() : "");
        cbSigned.setChecked(dataModel.getSignedDoc() != null && dataModel.getSignedDoc().equals("1"));
        isSigned = dataModel.getSignedDoc() != null ? (dataModel.getSignedDoc().equals("1") ? 1 : 0) : 0;
        try {
            if (dataModel.getUid() == 0) fileLocation = dataModel.getFile();
            fileName = dataModel.getOriginalName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        isSynced = dataModel.isSynced();
        tableRowId = dataModel.getTableRowId();

        if (dataModel.getCategoryUid() != null && !dataModel.getCategoryUid().equals("0")) {
            if (!docCatList.isEmpty()) {
                for (CatListing category : docCatList) {
                    if (category.getId() != null && category.getId().equals(dataModel.getCategoryUid())) {
                        etCategories.setText(category.getTitle().trim());
                        categoryId = dataModel.getCategoryUid();
                        break;
                    }
                }
            }
        }
        if (dataModel.getDocStatus() != null && !dataModel.getDocStatus().equals("0")) {
            if (!statusList.isEmpty()) {
                for (DocumentStatus status : statusList) {
                    if (status.getId() != null && status.getId().equals(dataModel.getDocStatus())) {
                        etStatus.setText(status.getTitle().trim());
                        statusId = status.getId();
                        break;
                    }
                }
            }
        }
    }

    @OnClick({R.id.etUploadFile, R.id.btnSave, R.id.etCategories, R.id.etStatus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etCategories:
                showCategoryDropdown();
                break;
            case R.id.etStatus:
                openStatusPopUp(etStatus);
                break;
            case R.id.etUploadFile:
                checkPermission();
                break;
            case R.id.btnSave:
                if (isDataFilled()) {
                    PDocsDataModel pDocsDataModel = new PDocsDataModel(
                            dataModel != null ? dataModel.getUid() : 0,
                            etTitle.getText().toString().trim(),
                            categoryId, etCategories.getText().toString().trim(), statusId,
                            etRevision.getText().toString().trim(),
                            String.valueOf(isSigned),
                            etNotes.getText().toString().trim(),
                            etComment.getText().toString().trim(),
                            fileLocation, fileName,
                            CommonMethods.getCurrentDate(CommonMethods.DF_1),
                            BuilderStormApplication.mPrefs.getUserName(), isSynced, tableRowId
                    );
                    viewModel.composeDocument(pDocsDataModel);
                }
                break;
        }
    }

    private boolean isDataFilled() {
        clearAllErrors();
        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("required field");
            return false;
        } else if (etCategories.getText().toString().trim().isEmpty()) {
            etCategories.setError("required field");
            return false;
        } else if (etRevision.getText().toString().trim().isEmpty()) {
            etRevision.setError("required field");
            return false;
        } /*else if (etNotes.getText().toString().trim().isEmpty()) {
            etNotes.setError("required field");
            return false;
        } else if (etComment.getText().toString().trim().isEmpty()) {
            etComment.setError("required field");
            return false;
        }*/ else if (etUploadFile.getText().toString().trim().isEmpty()) {
            CommonMethods.displayToast(this, "You should upload a file.");
            return false;
        }
        return true;
    }

    private void clearAllErrors() {
        etTitle.setError(null);
        etCategories.setError(null);
        etRevision.setError(null);
        etNotes.setError(null);
        etComment.setError(null);
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        viewModel.isSuccess.observe(this, aBoolean -> {
            if (aBoolean) {
                errorMessage("Your document have been successfully uploaded.", null, false);
                callBroadcast();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (null != errorModel) {
                handleErrorModel(errorModel);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    fileLocation = CommonMethods.getFilePathFromURI(this, data.getData());
                    fileName = CommonMethods.getFileNameFromPath(fileLocation);
                    File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, data.getData()));
                    CommonMethods.copyFile(new File(fileLocation), destinationFile);
                    etUploadFile.setText(CommonMethods.getFileName(this, data.getData()));
                }
            }
        }
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        finish();
        AddDocumentActivity.this.toolbarFinish();
    }

    /* pop ups and drop downs */
    private void showCategoryDropdown() {
        if (null != docCatList && !docCatList.isEmpty()) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(this);
            listPopupWindow.setAdapter(
                    new ArrayAdapter<>(this, R.layout.row_dropdown, R.id.tvDropDown, docCatList));
            listPopupWindow.setAnchorView(etCategories);
            listPopupWindow.setModal(true);
            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                etCategories.setText(docCatList.get(position).getTitle());
                categoryId = docCatList.get(position).getId();
                listPopupWindow.dismiss();
            });
            listPopupWindow.show();
        } else {
            errorMessage("No Category Found", null, false);
        }
    }

    private void openStatusPopUp(EditText etStatus) {
        CommonMethods.hideKeyboard(this, etStatus);
        if (null != statusList && !statusList.isEmpty()) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(this);
            listPopupWindow.setAdapter(new ArrayAdapter<>(this,
                    R.layout.row_dropdown, R.id.tvDropDown, statusList));
            listPopupWindow.setAnchorView(this.etStatus);
            listPopupWindow.setModal(true);
            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                this.etStatus.setText(statusList.get(position).getTitle());
                statusId = statusList.get(position).getId();
                listPopupWindow.dismiss();
            });
            listPopupWindow.show();
        } else {
            errorMessage("No status available", null, false);
        }
    }

    private void checkPermission() {
        if (permissionUtils == null) {
            permissionUtils = new PermissionUtils(this);
        }

        if (permissionUtils.isPermissionGrantedForExtStorage()) {
            openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
        } else {
            permissionUtils.requestFragmentPermissionForExtStorage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
            } else {
                errorMessage(getString(R.string.storage_permission_needed), null, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AddDocumentActivity.this.toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(AddDocumentActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

}
