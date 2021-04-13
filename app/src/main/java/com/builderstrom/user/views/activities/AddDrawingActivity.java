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
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.MetaDataField;
import com.builderstrom.user.repository.retrofit.modals.MetaOptions;
import com.builderstrom.user.repository.retrofit.modals.MetaValues;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.DrawingMenuVM;
import com.builderstrom.user.views.adapters.metaDataAdapters.MetaDataListAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddDrawingActivity extends BaseActivity {
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.edit_pdf) EditText editPdf;
    @BindView(R.id.edit_dwg) EditText editDwg;
    @BindView(R.id.edit_oft) EditText editOtf;
    @BindView(R.id.edit_drawing_name) EditText editDrawingName;
    @BindView(R.id.edit_drawing_no) EditText editDrawingNo;
    @BindView(R.id.edit_revision_name) EditText editRevisionName;
    @BindView(R.id.img_delete_dwg) ImageView ivDeleteDwg;
    @BindView(R.id.img_delete_pdf) ImageView ivDeletePdf;
    @BindView(R.id.img_delete_oft) ImageView ivDeleteOft;
    @BindView(R.id.llMain) LinearLayout llMain;
    @BindView(R.id.rvMetaData) RecyclerView rvMetaData;
    @BindView(R.id.include2) Toolbar toolbar;
    private int metaPosition = -1;
    private String pdfFileLocation = "", dwgFileLocation = "", otfLocation = "";
    private boolean isPDF = false, isDWG = false, isOTF = false, isEditMode = false;
    private PermissionUtils permissionUtils;
    private MetaDataListAdapter metaAdapter;
    private DrawingMenuVM viewModel;
    private List<MetaDataField> metaDataList = new ArrayList<>();
    private List<List<MetaValues>> editMetaValues = new ArrayList<>();

    private void observedViewModel() {
        viewModel.isLoadingLD.observe(this, aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.errorModelLD.observe(this, model -> {
            if (model != null) {
                handleErrorModel(model);
            }
        });

        viewModel.metaDataList.observe(this, metaDataFields -> {
            if (null != metaDataFields) {
                updateMetaData(metaDataFields);
            }
        });

        viewModel.isSuccess.observe(this, aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) {
                    callBroadcast();
                }
            }
        });
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(AddDrawingActivity.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                if (metaPosition == -1) {
                    if (isPDF) {
                        openExplorer("application/pdf", CommonMethods.PICK_FILE_RESULT_PDF);
                    } else if (isDWG) {
                        openExplorerDWG();
//                        openExplorer("dwg/*", CommonMethods.PICK_FILE_RESULT_DWG);
                    } else if (isOTF) {
                        openExplorer("*/*", CommonMethods.PICK_FILE_RESULT_OTHER);
                    } else {
                        openExplorer("application/*", CommonMethods.PICK_FILE_RESULT_DWG);
                    }
                } else {
                    openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
                }
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
                finish();
            }
        });
        permissionUtils.checkPermissions();
    }

    @OnClick({R.id.img_delete_pdf, R.id.img_delete_dwg, R.id.img_delete_oft, R.id.edit_pdf,
            R.id.edit_dwg, R.id.edit_oft, R.id.llPdf, R.id.llDwg, R.id.llOtf, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_delete_pdf:
                if (editPdf.getText().toString().trim().isEmpty()) {
                    errorMessage("No file attached", null, false);
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.delete_content_pdf))
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                editPdf.setText(null);
                                pdfFileLocation = "";
                                setEditTextView();
                            })
                            .create().show();
                }
                break;
            case R.id.img_delete_dwg:
                if (editDwg.getText().toString().trim().isEmpty()) {
                    errorMessage("No file attached", null, false);
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.delete_content_dwg))
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                editDwg.setText(null);
                                dwgFileLocation = "";
                                setEditTextView();
                            })
                            .create().show();
                }
                break;
            case R.id.img_delete_oft:
                if (editOtf.getText().toString().trim().isEmpty()) {
                    errorMessage("No file attached", null, false);
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.delete_content_otf))
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                editOtf.setText(null);
                                otfLocation = "";
                                setEditTextView();
                            })
                            .create().show();
                }
                break;
            case R.id.llPdf:
            case R.id.edit_pdf:
                metaPosition = -1;
                isDWG = false;
                isPDF = true;
                isOTF = false;
                editPdf.setError(null);
                checkPermission();
                break;
            case R.id.llDwg:
            case R.id.edit_dwg:
                metaPosition = -1;
                isDWG = true;
                isPDF = false;
                isOTF = false;
                editDwg.setError(null);
                checkPermission();
                break;
            case R.id.llOtf:
            case R.id.edit_oft:
                metaPosition = -1;
                isDWG = false;
                isOTF = true;
                isPDF = false;
                editOtf.setError(null);
                checkPermission();
                break;
            case R.id.btnSave:
                clearAllErrors();
                String pdf_extension = editPdf.getText().toString().trim().isEmpty() ? ""
                        : (editPdf.getText().toString().trim()).contains(".pdf") ? editPdf.getText().toString().trim().substring(editPdf.getText().toString().trim().lastIndexOf(".")) : "";
                String dwg_extension = editDwg.getText().toString().trim().isEmpty() ? ""
                        : (editDwg.getText().toString().trim()).contains(".dwg") ? editDwg.getText().toString().trim().substring(editDwg.getText().toString().trim().lastIndexOf(".")) : "";

                if (editDrawingName.getText().toString().trim().equalsIgnoreCase("")) {
                    editDrawingName.setError("Please input Drawing Name");
                } else if (editDrawingNo.getText().toString().trim().equalsIgnoreCase("")) {
                    editDrawingNo.setError("Please input Drawing No");
                } else if (editRevisionName.getText().toString().trim().equalsIgnoreCase("")) {
                    editRevisionName.setError("Please input Revision Name");
                } else if (editPdf.getText().toString().equals("") && editDwg.getText().toString().equals("")) {
                    errorMessage("Please select either PDF or DWG file", null, false);
                } else if (!editPdf.getText().toString().isEmpty() && !pdf_extension.equalsIgnoreCase(".pdf")) {
                    editPdf.setError("Please select a PDF file");
                    errorMessage("Please select a PDF file", null, false);
                } else if (!editDwg.getText().toString().isEmpty() && !dwg_extension.equalsIgnoreCase(".dwg")) {
                    editDwg.setError("Please select a DWG file");
                    errorMessage("Please select a DWG file", null, false);
                } else {
                    if (metaAdapter.isProperFilled()) {
                        Datum datum = new Datum(
                                editPdf.getText().toString().trim(),
                                editDwg.getText().toString().trim(),
                                editOtf.getText().toString().trim(),
                                pdfFileLocation, dwgFileLocation, otfLocation,
                                editDrawingNo.getText().toString().trim(),
                                editDrawingName.getText().toString().trim(),
                                editRevisionName.getText().toString().trim(),
                                "0",
                                0,
                                BuilderStormApplication.mPrefs.getUserName(),
                                CommonMethods.getCurrentDate(CommonMethods.DF_1),
                                metaAdapter.getMetaValuesList());
                        viewModel.addDrawing(datum);
                    }
                }
                break;
        }
    }

    private void clearAllErrors() {
        editPdf.setError(null);
        editDwg.setError(null);
        editOtf.setError(null);
        editDrawingName.setError(null);
        editDrawingNo.setError(null);
        editRevisionName.setError(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_drawing;
    }

    @Override
    protected void init() {
        if (getIntent().hasExtra("data")) {
            isEditMode = true;
            Datum datum = getIntent().getParcelableExtra("data");
            if (datum != null) {
                editMetaValues.addAll(datum.getCustom_field_data());
            }
        }

        viewModel = new ViewModelProvider(this).get(DrawingMenuVM.class);
        observedViewModel();
        viewModel.getMetaData();
        /* setup toolbar */
        toolbar.setTitle("Add Drawing");
        toolbar.setNavigationOnClickListener(view -> toolbarFinish());
        /* set up adapter */
        metaAdapter = new MetaDataListAdapter(this, metaDataList);
        metaAdapter.setCallback(position -> {
            metaPosition = position;
            checkPermission();
        });
        rvMetaData.setAdapter(metaAdapter);
        setEditTextView();
        llMain.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommonMethods.PICK_FILE_RESULT_DWG:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        String dwgFileName = CommonMethods.getFileName(this, data.getData());
                        Log.e("dwgFile", dwgFileName);
                        if (CommonMethods.isDWG(dwgFileName)) {
                            dwgFileLocation = CommonMethods.getFilePathFromURI(this, data.getData());
                            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, data.getData()));
                            CommonMethods.copyFile(new File(dwgFileLocation), destinationFile);
                            editDwg.setText(CommonMethods.getFileName(this, data.getData()));
                            setEditTextView();
                        } else {
                            viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, getString(R.string.unsuported_dwg_file)));
                        }

                    }
                }
                break;
            case CommonMethods.PICK_FILE_RESULT_PDF:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        pdfFileLocation = CommonMethods.getFilePathFromURI(this, data.getData());
                        File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, data.getData()));
                        CommonMethods.copyFile(new File(pdfFileLocation), destinationFile);
                        editPdf.setText(CommonMethods.getFileName(this, data.getData()));
                        setEditTextView();
                    }
                }
                break;
            case CommonMethods.PICK_FILE_RESULT_OTHER:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        otfLocation = CommonMethods.getFilePathFromURI(this, data.getData());
                        File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, data.getData()));
                        CommonMethods.copyFile(new File(otfLocation), destinationFile);
                        editOtf.setText(CommonMethods.getFileName(this, data.getData()));
                        setEditTextView();
                    }
                }
                break;

            case CommonMethods.FILE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uriFile = data.getData();
                    if (uriFile != null) {
                        String fileLocation = CommonMethods.getFilePathFromURI(this, uriFile);
                        File sourceFile = new File(fileLocation);
                        File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, uriFile));
                        CommonMethods.copyFile(sourceFile, destinationFile);
                        if (metaPosition != -1) {
                            List<String> fileList = new ArrayList<>();
                            try {
                                String fileName = CommonMethods.getFileName(this, uriFile);
                                if (null != fileName.substring(fileName.lastIndexOf("."))) {
                                    fileList.add(fileLocation);
                                }
                            } catch (Exception e) {
                                errorMessage("File not supported", null, false);
                            } finally {
                                metaDataList.get(metaPosition).setMetaServerFiles(null);
                                metaDataList.get(metaPosition).setMetaUploadFiles(fileList);
                                metaAdapter.notifyItemChanged(metaPosition);
                            }
                        }
                    }
                }
                break;
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

    private void setEditTextView() {
        ivDeletePdf.setVisibility(editPdf.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ivDeleteDwg.setVisibility(editDwg.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        ivDeleteOft.setVisibility(editOtf.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        toolbarFinish();

    }

    public void updateMetaData(List<MetaDataField> dbMetaList) {
        metaDataList.clear();
        for (MetaDataField metaModel : dbMetaList) {
            if (metaModel.getFieldType().equalsIgnoreCase("select")) {
                if ((metaModel.getOptions() != null && !metaModel.getOptions().isEmpty())) {
                    metaDataList.add(metaModel);
                }
            } else {
                metaDataList.add(metaModel);
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
                            case "signature":
                                data.setAnswerString(preValues.get(0).getValue());
                                break;

                        }
                    }
                }

            }
        }
        metaAdapter.notifyDataSetChanged();
        llMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(AddDrawingActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

}

