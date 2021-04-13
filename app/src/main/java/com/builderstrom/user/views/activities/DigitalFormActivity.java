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

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.DigitalFormModel;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;
import com.builderstrom.user.data.retrofit.modals.PojoMyItem;
import com.builderstrom.user.data.retrofit.modals.RowFormModel;
import com.builderstrom.user.data.retrofit.modals.TemplateData;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.DigitalDocumentVM;
import com.builderstrom.user.views.adapters.TestRowFormAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DigitalFormActivity extends BaseActivity {
    private static final String TAG = DigitalFormActivity.class.getSimpleName();
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.ivAppLogo)
    ImageView ivAppLogo;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.rvDocument)
    RecyclerView rvDocument;
    @BindView(R.id.ivFooter)
    ImageView ivFooter;
    @BindView(R.id.switchTabular)
    Switch switchTabular;
    @BindView(R.id.linearLayout2)
    ConstraintLayout mConstraintLayout;
    ViewGroup.LayoutParams params;

    private int width;
    private int screenWidth;
    private Integer rowPosition = -1, columnPosition = -1;
    private Integer projectDocumentId;
    private Integer customDocumentId;
    private Integer rowId;
    private boolean isStagedDocs = false;
    private boolean isTabular = false;
    private String assigned_user = "";
    private String reccuringType = "";
    private String issue_Id = "";
    private String templateId = "";
    private DigitalDocumentVM viewModel;
    private TestRowFormAdapter mAdapter;
    private List<TemplateData> dataList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private PermissionUtils permissionUtils;
    private PojoMyItem myItem;

    private void screenMeasurement() {
        params = mConstraintLayout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        params.width = width;
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        mConstraintLayout.setLayoutParams(params);
        screenWidth = (int) ((width * 1.8));
    }

    private void getBundleData() {
        if (getIntent().hasExtra("data")) {
            myItem = getIntent().getParcelableExtra("data");
            reccuringType = myItem.getRecurrence_type();
            templateId = myItem.getTemplateId();
            issue_Id = myItem.getIssue() != null ? myItem.getIssue() : "";
            customDocumentId = Integer.parseInt(myItem.getDoc_id() != null ? myItem.getDoc_id() : "0");
        } else if (getIntent().hasExtra("TemplateId")) {
            templateId = getIntent().getStringExtra("TemplateId");
            customDocumentId = getIntent().hasExtra("CustomDocId") ? getIntent().getIntExtra("CustomDocId", 0) : 0;
            projectDocumentId = getIntent().hasExtra("ProDocId") ? getIntent().getIntExtra("ProDocId", 0) : 0;
        }

        viewModel.getDocumentUsers();
        if (templateId != null && !templateId.isEmpty())
            viewModel.getRawFormAPI(templateId, customDocumentId, issue_Id);

    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.docHeaderLD.observe(this, docHeader -> {
            if (null != docHeader) {
                GlideApp.with(getApplicationContext())
                        .load(CommonMethods.decodeUrl(docHeader.getTemplate_header()))
                        .apply(new RequestOptions().error(R.drawable.ic_app_logo)).into(ivAppLogo);
            }
        });

        viewModel.digitalFormLD.observe(this, templateData -> {
            if (templateData != null) {
                showForm(templateData);
            }
        });

        viewModel.submitFormLD.observe(this, isSubmit -> {
            if (isSubmit != null && isSubmit) {
                callBroadcast();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (null != errorModel) {
                handleErrorModel(errorModel);
            }
        });

        viewModel.rowID.observe(this, rowID -> rowId = rowID);

        viewModel.docHeaderLD.observe(this, docHeader -> {
            if (null != docHeader) {
                if (docHeader.getTemplate_header() != null && !docHeader.getTemplate_header().isEmpty()) {
                    GlideApp.with(getApplicationContext())
                            .load(CommonMethods.decodeUrl(docHeader.getTemplate_header()))
                            .apply(new RequestOptions().error(R.drawable.ic_app_logo)).into(ivAppLogo);
                }
                if (docHeader.getTemplate_footer() != null && !docHeader.getTemplate_footer().isEmpty()) {
                    GlideApp.with(getApplicationContext())
                            .load(CommonMethods.decodeUrl(docHeader.getTemplate_footer()))
                            .apply(new RequestOptions().error(R.drawable.ic_app_logo)).into(ivFooter);
                }
            }
        });

        viewModel.isStagedLD.observe(this, aBoolean -> {
            if (aBoolean != null) {
                isStagedDocs = aBoolean;
            }
        });

        viewModel.usersLD.observe(this, itemUsersModels -> {
            if (itemUsersModels != null)
                usersList.addAll(itemUsersModels);
        });
    }

    private void showForm(List<TemplateData> templateData) {
        dataList.clear();
        for (TemplateData rowData : templateData) {
            List<RowFormModel> list = new ArrayList<>();
            for (DigitalFormModel.Rowcolumn column : rowData.getRowColumns()) {
                RowFormModel model = new RowFormModel();
                DigitalFormModel.ColumnData cData = column.getColumnData();
                if (cData != null) {
                    /** Implementation of multiplication-dropdown on 03/06/2020*/
                    if (cData.getType() != null && cData.getType().equalsIgnoreCase("multiplication-dropdown")) {
                        multiDropDown(column, model, list);
                    } else {
                        model.setRowID(column.getRowId());
                        model.setColumnID(column.getId());

                        if (cData.getLabel() != null) {
                            model.setLabel(cData.getLabel());
                        }

                        if (cData.getHeadingType() != null) {
                            model.setHeading_type(cData.getHeadingType());
                        }

                        if (cData.getIsRequired() != null) {
                            model.setIsRequired(cData.getIsRequired().toString());
                        }

                        if (cData.getType() != null) {
                            model.setType(cData.getType());
                        }
                        /** Change in to dropdown and input markup file at 11/09/2020 */
                        if (cData.getValue() != null) {
                            if (cData.getValue().isEmpty() && cData.getDefaultAnswer() != null) {
                                model.setValue(cData.getDefaultAnswer());
                            } else {
                                model.setValue(cData.getValue());
                            }

                            if (cData.getType() != null && !model.getType().equalsIgnoreCase("input_markup_file") && !model.getType().equalsIgnoreCase("dropdown")) {
                                model.setAnswerString(cData.getValue());
                            } else if (cData.getType() != null && model.getType().equalsIgnoreCase("dropdown") && cData.getOptions() != null) {
                                model.setAnswerString(getOptionsString(cData.getOptions(), cData.getValue()));
                            }
                        } else if (cData.getDefaultAnswer() != null) {
                            model.setValue(cData.getDefaultAnswer());
                            model.setAnswerString(cData.getDefaultAnswer());
                        }

                        if (column.getEditableColumn() != null) {
                            model.setEditableColumn(column.getEditableColumn());
                        }

                        if (cData.getOptions() != null) {
                            List<MetaOptions> options = new ArrayList<>();
                            for (DigitalFormModel.Option optionString : cData.getOptions()) {
                                options.add(new MetaOptions(optionString.getId(), optionString.getName(), optionString.isSelected()));
                            }
                            model.setOptions(options);
                        }

                        if (model.getType() != null && !model.getType().isEmpty() && isSupportedFormats(model.getType())) {
                            list.add(model);
                        }
                    }
                }
            }
            rowData.setFormModelList(list);
        }
        dataList.addAll(templateData);
        Log.e("form list", new Gson().toJson(dataList));
        mAdapter.notifyDataSetChanged();
        btnSubmit.setVisibility(dataList.isEmpty() ? View.GONE : View.VISIBLE);
        switchTabular.setVisibility(dataList.isEmpty() ? View.GONE : View.VISIBLE);
        tvNoData.setVisibility(dataList.isEmpty() ? View.VISIBLE : View.GONE);
        switchTabular.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isTabular = true;
                params.width = width * 2;
            } else {
                isTabular = false;
                params.width = width;
            }
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            mConstraintLayout.setLayoutParams(params);
            mAdapter = new TestRowFormAdapter(DigitalFormActivity.this, dataList, isTabular, screenWidth);
            rvDocument.setAdapter(mAdapter);
        });
    }

    private void multiDropDown(DigitalFormModel.Rowcolumn cData, RowFormModel model, List<RowFormModel> list) {
        for (int i = 1; i <= 3; i++) {
            model = new RowFormModel();
            model.setRowID(cData.getRowId());
            model.setColumnID(cData.getId() + "_" + i);

            if (i == 1) {
                if (cData.getColumnData().getLabel() != null) {
                    model.setLabel(cData.getColumnData().getLabel());
                }

                if (cData.getColumnData().getType() != null) {
                    model.setType(cData.getColumnData().getType());
                }

                if (cData.getColumnData().getOptions() != null) {
                    List<MetaOptions> options = new ArrayList<>();
                    for (DigitalFormModel.Option optionString : cData.getColumnData().getOptions()) {
                        options.add(new MetaOptions(optionString.getId(), optionString.getName(), optionString.isSelected()));
                    }
                    model.setOptions(options);
                }

                if (cData.getColumnData().getValue1() != null) {
                    model.setValue(cData.getColumnData().getValue1());
                    model.setAnswerString(cData.getColumnData().getValue1());
                }

            } else if (i == 2) {
                if (cData.getColumnData().getLabel2() != null) {
                    model.setLabel(cData.getColumnData().getLabel2());
                }

                if (cData.getColumnData().getType() != null) {
                    model.setType(cData.getColumnData().getType());
                }

                if (cData.getColumnData().getOptions2() != null) {
                    List<MetaOptions> options = new ArrayList<>();
                    for (DigitalFormModel.Option optionString : cData.getColumnData().getOptions2()) {
                        options.add(new MetaOptions(optionString.getId(), optionString.getName(), optionString.isSelected()));
                    }
                    model.setOptions(options);
                }

                if (cData.getColumnData().getValue2() != null) {
                    model.setValue(cData.getColumnData().getValue2());
                    model.setAnswerString(cData.getColumnData().getValue2());
                }
            } else if (i == 3) {
                if (cData.getColumnData().getLabel3() != null) {
                    model.setLabel(cData.getColumnData().getLabel3());
                }

                if (cData.getColumnData().getType() != null) {
                    model.setType("multi_drop_text");
                }

                if (cData.getColumnData().getColorNumber() != null && !cData.getColumnData().getColorNumber().isEmpty()) {
                    model.setColorValue(cData.getColumnData().getColorNumber());
                }

                if (cData.getColumnData().getColorCodes() != null && !cData.getColumnData().getColorCodes().isEmpty()) {
                    model.setColor(cData.getColumnData().getColorCodes());
                }

                if (cData.getColumnData().getResult() != null) {
                    model.setValue(cData.getColumnData().getResult());
                    model.setAnswerString(cData.getColumnData().getResult());
                }
            }

            if (cData.getColumnData().getHeadingType() != null) {
                model.setHeading_type(cData.getColumnData().getHeadingType());
            }

            if (cData.getColumnData().getIsRequired() != null) {
                model.setIsRequired(cData.getColumnData().getIsRequired().toString());
            }

//            if (cData.getColumnData().getType() != null) {
//                model.setType(cData.getColumnData().getType());
//            }


            if (cData.getEditableColumn() != null) {
                model.setEditableColumn(cData.getEditableColumn());
            }


//            list.add(model);

            if (model.getType() != null && !model.getType().isEmpty() && isSupportedFormats(model.getType())) {
                list.add(model);
            }

        }
    }

    private String getOptionsString(List<DigitalFormModel.Option> options, String value) {
        for (DigitalFormModel.Option option : options) {
            if (option.getId() != null && option.getId().equalsIgnoreCase(value)) {
                return option.getName();
            }
        }
        return "";
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
        CommonMethods.hideKeyboard(this);
        if (null != mAdapter && viewModel.isProperFieldMain(dataList))
            if (isStagedDocs) {
                final Dialog dialog = new Dialog(this, R.style.DialogTheme);
                dialog.setContentView(R.layout.dialog_digital_staged);
                EditText etSpinner = dialog.findViewById(R.id.etSpinner);
                etSpinner.setOnClickListener(v -> popupUserList(etSpinner));
                Button btnOk = dialog.findViewById(R.id.btnSave);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                btnOk.setOnClickListener(v -> {
                    if (assigned_user != null && !assigned_user.isEmpty()) {
                        callSubmitAPI();
                        dialog.dismiss();
                    } else {
                        etSpinner.setError("");
                        CommonMethods.displayToast(this, "Please select a user");
                    }
                });
                btnCancel.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            } else {
                callSubmitAPI();
            }
    }

    private void popupUserList(EditText etSpinner) {
        if (usersList != null && !usersList.isEmpty()) {
            ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, usersList);
            ListPopupWindow popupWindow = new ListPopupWindow(this);
            popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setAnchorView(etSpinner);
            popupWindow.setAdapter(spinnerAdapter);
            popupWindow.setOnItemClickListener((parent, view, position, id) -> {
                if (!usersList.get(position).getUserId().equalsIgnoreCase("0")) {
                    etSpinner.setText(usersList.get(position).getName());
                    assigned_user = usersList.get(position).getUserId();
                    popupWindow.dismiss();
                }
            });
            popupWindow.show();
        }
    }

    private void callSubmitAPI() {
        viewModel.submitDigitalForm(reccuringType, rowId, assigned_user, myItem, templateId, projectDocumentId, customDocumentId,
                viewModel.getMetaValuesList(dataList, myItem));
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(DigitalFormActivity.this);
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
        if (requestCode == CommonMethods.FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uriFile = data.getData();
                if (uriFile != null) {
                    String fileLocation = CommonMethods.getFilePathFromURI(this, uriFile);
                    File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(this, uriFile));
                    CommonMethods.copyFile(new File(fileLocation), destinationFile);
                    if (rowPosition != -1 && columnPosition != -1) {
                        List<String> fileList = new ArrayList<>();
                        try {
                            String fileName = CommonMethods.getFileName(this, uriFile);
                            if (!fileName.substring(fileName.lastIndexOf(".")).isEmpty()) {
                                fileList.add(fileLocation);
                            }
                        } catch (Exception e) {
                            errorMessage("File not supported", null, false);
                        } finally {
                            setAdapterDataByPosition(fileList);
                        }
                    }
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_digital_document_form;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(DigitalDocumentVM.class);
        observeViewModel();
        screenMeasurement();
        /* Stopping scrolling */
        rvDocument.stopScroll();
        rvDocument.stopNestedScroll();
        mAdapter = new TestRowFormAdapter(this, dataList, isTabular, screenWidth);
        rvDocument.setAdapter(mAdapter);
        getBundleData();

    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    public void openExplorerOnclick(Integer rowPosition, Integer columnPosition) {
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
        Log.e("positions", "" + rowPosition + " -----> " + columnPosition);
        checkPermission();
    }

    public void setAdapterDataByPosition(List<String> fileList) {
        if (dataList.get(rowPosition).getFormModelList() != null) {
            dataList.get(rowPosition).getFormModelList().get(columnPosition).setMetaUploadFiles(fileList);
        }
        mAdapter.notifyDataSetChanged();
    }

    private boolean isSupportedFormats(String type) {
        return !type.equalsIgnoreCase("interactive_file")
                && !type.equalsIgnoreCase("add_from_builderstorm");
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(DigitalFormActivity.this, DashBoardActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
        CommonMethods.hideKeyboard(this);
    }
}
