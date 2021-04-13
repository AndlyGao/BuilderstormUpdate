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

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.AddAttachModel;
import com.builderstrom.user.data.retrofit.modals.DiaryData;
import com.builderstrom.user.data.retrofit.modals.DiaryLabourModel;
import com.builderstrom.user.data.retrofit.modals.DiaryManLabour;
import com.builderstrom.user.data.retrofit.modals.GeneralSettingsDatum;
import com.builderstrom.user.data.retrofit.modals.MetaDataField;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;
import com.builderstrom.user.data.retrofit.modals.MetaValues;
import com.builderstrom.user.data.retrofit.modals.PojoCostCode;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.FileUtils;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.DiaryViewModel;
import com.builderstrom.user.views.adapters.AddAttachmentAdapter;
import com.builderstrom.user.views.adapters.AddDiaryLabourAdapter;
import com.builderstrom.user.views.adapters.metaDataAdapters.MetaDataListAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class AddDiary extends BaseActivity {
    @BindView(R.id.include)
    Toolbar toolbar;
    @BindView(R.id.llGlobal)
    LinearLayout llGlobal;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.btnUploadRfi)
    Button btnUploadFiles;
    @BindView(R.id.btnLabour)
    Button btnLabour;
    @BindView(R.id.rvAddDiary)
    RecyclerView rvAddDiary;
    @BindView(R.id.rvSiteLabour)
    RecyclerView rvSiteLabour;
    @BindView(R.id.rvMetaData)
    RecyclerView rvMetaData;
    boolean isStartFinish = false;
    private boolean isEditMode = false;
    private Integer metaPosition = -1;
    private Calendar calendar = Calendar.getInstance();
    private DiaryData modelData;
    private PermissionUtils permissionUtils;
    private MetaDataListAdapter metaAdapter;
    private AddDiaryLabourAdapter labourAdapter;
    private DiaryViewModel viewModel;
    private List<AddAttachModel> dairyFileList = new ArrayList<>();
    private List<List<MetaValues>> editMetaValues = new ArrayList<>();
    private List<MetaDataField> metaDataList = new ArrayList<>();
    private List<DiaryLabourModel> labourDataList = new ArrayList<>();
    private List<String> labourTypeList = new ArrayList<>();
    private List<String> payTypeList = new ArrayList<>();
    private List<PojoCostCode> costCodeList = new ArrayList<>();
    private boolean isLabourEnable = false;
    private String[] timeArr = null;
    private Float sWorkHours = null;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_diary;
    }

    @Override
    protected void init() {
        getBundleData();
        initView();
        notifyAdaptor(dairyFileList);
        callMetaData();
    }

    private void getBundleData() {
        if (getIntent().hasExtra("DiaryData")) {
            isEditMode = true;
            String dairyData = getIntent().getStringExtra("DiaryData");
            modelData = new Gson().fromJson(dairyData, new TypeToken<DiaryData>() {
            }.getType());
            if (isInternetAvailable()) editMetaValues.addAll(modelData.getCustom_field_data());
            etTitle.setText(modelData.getTitle());
            etDate.setText(isInternetAvailable() ? CommonMethods.convertDate(CommonMethods.DF_1, modelData.getCreatedOn(), CommonMethods.DF_8) : modelData.getCreatedOn());
            etDescription.setText(modelData.getDescription());
            dairyFileList = modelData.getAttachments();
            notifyAdaptor(dairyFileList);
        }
    }

    private void callMetaData() {
        viewModel.getDiaryAPIMetaData();
    }

    private void initView() {
        viewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
        observeViewModel();
        /* set Up Toolbar */
        toolbar.setTitle(isEditMode ? "Edit Daily Diary" : "Add Daily Diary");
        toolbar.setNavigationOnClickListener(view -> toolbarFinish());
        accessGeneralSetting();

        /* setUp Metadata Adapter */
        metaAdapter = new MetaDataListAdapter(this, metaDataList);
        metaAdapter.setCallback(position -> {
            metaPosition = position;
            checkPermission();
        });
        rvMetaData.setAdapter(metaAdapter);
        btnLabour.setVisibility(isLabourEnable ? View.VISIBLE : View.GONE);
        if (null != userPermissions)
            CommonMethods.checkVisiblePermission(userPermissions.getDailyDiaries().getUploadFile(), btnUploadFiles);


        /* update labour data at start in add/edit diary */
        labourDataList.clear();
        if (viewModel.isInternetAvailable()) {
            viewModel.getManHoursEntries(isEditMode ? modelData.getId() : null);
        } else {
            if (isEditMode) {
                if (modelData != null && modelData.getDiaryManHours() != null) {
                    for (DiaryManLabour labor : modelData.getDiaryManHours()) {
                        DiaryLabourModel model = new DiaryLabourModel();
                        model.setLabourType(labor.getLabel());
                        model.setCostCode(labor.getCostCode());
                        model.setStartTime(labor.getStartTime());
                        model.setFinishTime(labor.getEndTime());
                        model.setNoSite(labor.getWorkHours());
                        model.setPayType(labor.getPayType());
                        model.setId(labor.getId());
                        if (labor.getSwh() != null && !labor.getSwh().isEmpty()) {
                            sWorkHours = Float.parseFloat(labor.getSwh());
                        }
                        labourDataList.add(model);
                    }
                }
                labourAdapter.notifyDataSetChanged();
            }
        }

    }

    private void accessGeneralSetting() {
        boolean isLabourManual = false;
        boolean isPayEnable = false;
        boolean isPayManual = false;
        boolean isCostEnable = false;
        labourTypeList.clear();
        payTypeList.clear();
        costCodeList.clear();
        /* add cost code of specified project*/
        if ((null != viewModel.mPrefs.getSelectedProject())) {
            if (viewModel.mPrefs.getSelectedProject().getCostCodes() != null) {
                costCodeList.addAll(viewModel.mPrefs.getSelectedProject().getCostCodes());
            }
            if (viewModel.mPrefs.getSelectedProject().getCostTrackLabour() != null) {
                isCostEnable = viewModel.mPrefs.getSelectedProject().getCostTrackLabour();
            }
        }

        List<GeneralSettingsDatum> generalSettingsData = viewModel.mPrefs.getGeneralSetting();
        timeArr = CommonMethods.getTodayWorkingHours(generalSettingsData);
        if (generalSettingsData != null && !generalSettingsData.isEmpty()) {

            for (GeneralSettingsDatum data : generalSettingsData) {
                if (data.getName().equalsIgnoreCase("LABOUR_TYPE")) {
                    String indexValue = data.getValue();
                    if (indexValue.equals("1")) {
                        isLabourEnable = true;
                        isLabourManual = false;
                    } else if (indexValue.equals("2")) {
                        isLabourEnable = true;
                        isLabourManual = true;
                    }
                }

                if (data.getName().equalsIgnoreCase("LABOUR_TYPE_VALUE")) {
                    String labourType = data.getValue();
                    if (labourType != null && !labourType.isEmpty()) {
                        String[] labourArray = labourType.split(",");
                        labourTypeList.addAll(Arrays.asList(labourArray));
                    }
                }

                if (data.getName().equalsIgnoreCase("PAY_TYPE")) {
                    switch (data.getValue()) {
                        case "0":
                            isPayEnable = false;
                            isPayManual = false;
                            break;
                        case "1":
                            isPayEnable = true;
                            isPayManual = false;
                            break;
                        case "2":
                            isPayEnable = true;
                            isPayManual = true;
                            break;
                    }
                }

                if (data.getName().equalsIgnoreCase("PAY_TYPE_VALUE")) {
                    String payType = data.getValue();
                    if (payType != null && !payType.isEmpty()) {
                        String[] payArray = payType.split(",");
                        payTypeList.addAll(Arrays.asList(payArray));
                    }
                }

                if (data.getName().equalsIgnoreCase("SET_START_AND_FINISH_TIME")) {
                    String SFvalue = data.getValue();
                    if (SFvalue != null && !SFvalue.isEmpty()) {
                        isStartFinish = SFvalue.equals("1");
                    }
                }
            }
            /* setUp labour Adapter */
            labourAdapter = new AddDiaryLabourAdapter(this, labourDataList, labourTypeList,
                    payTypeList, costCodeList, isLabourEnable, isPayEnable, isCostEnable, isLabourManual, isPayManual, isStartFinish, viewModel);
            rvSiteLabour.setAdapter(labourAdapter);
        }
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, aBoolean -> {
            if (aBoolean) showProgress();
            else dismissProgress();
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (errorModel != null) {
                handleErrorModel(errorModel);
            }
        });

        viewModel.isSuccess.observe(this, aBoolean -> {
            if (null != aBoolean) {
                callBroadcast();
            }
        });

        viewModel.dialogLD.observe(this, liveData -> {
            if (null != liveData) {
                showConfirmDialog(liveData);
            }
        });

        viewModel.metaLiveData.observe(this, metaFiledLD -> {
            if (null != metaFiledLD) {
                successMetaData(metaFiledLD);
            }
        });
        viewModel.manHoursLD.observe(this, diaryManLabours -> {
            labourDataList.clear();
            if (diaryManLabours != null) {
                for (DiaryManLabour labor : diaryManLabours) {
                    DiaryLabourModel model = new DiaryLabourModel();
                    model.setLabourType(labor.getLabel());
                    model.setCostCode(labor.getCostCode());
                    model.setStartTime(labor.getStartTime());
                    model.setFinishTime(labor.getEndTime());
                    model.setNoSite(labor.getWorkHours());
                    model.setPayType(labor.getPayType());
                    model.setId(labor.getId());
                    if (labor.getSwh() != null && !labor.getSwh().isEmpty()) {
                        sWorkHours = Float.parseFloat(labor.getSwh());
                    }
                    labourDataList.add(model);
                }
            }
            labourAdapter.notifyDataSetChanged();
        });

    }

    @OnClick({R.id.etDate, R.id.btnUploadRfi, R.id.btnSave, R.id.btnLabour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLabour:
                labourDataList.add(new DiaryLabourModel());
                labourAdapter.notifyDataSetChanged();
                break;
            case R.id.etDate:
                CommonMethods.hideKeyboard(AddDiary.this, etDate);
                DatePickerDialog pickerDialog = new DatePickerDialog(AddDiary.this, R.style.DatePickerTheme,
                        (pickerView, year, month, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            etDate.setText(new SimpleDateFormat(CommonMethods.DF_8, Locale.US).format(calendar.getTime()));
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();
                break;
            case R.id.btnUploadRfi:
                metaPosition = -1;
                checkPermission();
                break;
            case R.id.btnSave:
                clearAllErrors();
                if (isValidForm()) {
                    if (metaAdapter.isProperFilled()) {
                        /* for online files */
                        List<String> filesList = new ArrayList<>();
                        for (AddAttachModel model : dairyFileList) {
                            if (model.getFilePath() != null) {
                                filesList.add(model.getFilePath());
                            }
                        }
                        /* filtered labour list */
                        List<DiaryLabourModel> filterLabourList = new ArrayList<>();
                        for (DiaryLabourModel model : labourDataList) {
                            if (model.getNoSite() != null) {
                                if (isStartFinish) {
                                    if (model.getStartTime() != null && !model.getStartTime().isEmpty()
                                            && model.getFinishTime() != null && !model.getFinishTime().isEmpty()) {
                                        filterLabourList.add(model);
                                    }
                                } else {
                                    filterLabourList.add(model);
                                }
                            }
                        }

                        /* Add Diary or edit diary*/
                        if (isEditMode) {
                            /* edit diary case*/
                            if (isInternetAvailable()) {
                                viewModel.addDiary(modelData.getUid(), etTitle.getText().toString().trim(),
                                        etDescription.getText().toString().trim(),
                                        etDate.getText().toString().trim(), CommonMethods.getCurrentDate(CommonMethods.DF_1), filesList,
                                        metaAdapter.getMetaValuesList(), new Gson().toJson(metaAdapter.getMetaValuesView()),
                                        filterLabourList);
                            } else {
                                /* for now ignored case*/
                                viewModel.updateProjectDiary(modelData.getId(), etTitle.getText().toString().trim(),
                                        etDescription.getText().toString().trim(),
                                        CommonMethods.getCurrentDate(CommonMethods.DF_1),
                                        new Gson().toJson(filesList), filterLabourList);
                            }
                        } else {
                            /* add diary case*/
                            viewModel.addDiary("", etTitle.getText().toString().trim(),
                                    etDescription.getText().toString().trim(),
                                    etDate.getText().toString().trim(),
                                    CommonMethods.getCurrentDate(CommonMethods.DF_1), filesList,
                                    metaAdapter.getMetaValuesList(),
                                    new Gson().toJson(metaAdapter.getMetaValuesView()), filterLabourList);
                        }
                    }
                }
                break;
        }

    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(AddDiary.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
            }
        });
        permissionUtils.checkPermissions();
    }

    private boolean isValidForm() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError("Required Field");
            errorMessage("Title must not be empty.", null, false);
            return false;
        } else if (etDate.getText().toString().trim().isEmpty()) {
            errorMessage("Date must not be empty.", null, false);
            etDate.setError("Required Field");
            return false;
        } else if (etDescription.getText().toString().trim().isEmpty()) {
            errorMessage("Description must not be empty.", null, false);
            etDescription.setError("Required Field");
            return false;
        } else {
            return true;
        }
    }

    private void clearAllErrors() {
        etTitle.setError(null);
        etDate.setError(null);
        etDescription.setError(null);
    }

    private void showConfirmDialog(String files) {
        Dialog dialog = new Dialog(AddDiary.this);
        dialog.setContentView(R.layout.dialog_gallery_operations);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(String.format("You have already a diary for '%s'. Do you want to also add a new one?", etDate.getText().toString()));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            viewModel.insertDiaryInDB(etTitle.getText().toString().trim(), etDescription.getText().toString().trim(),
                    etDate.getText().toString().trim(), files, new Gson().toJson(metaAdapter.getMetaValuesList()),
                    new Gson().toJson(metaAdapter.getMetaValuesView()), labourDataList);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> {
            dialog.dismiss();
            callBroadcast();
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uriFile = data.getData();
                if (uriFile != null) {
//                    String fileLocation = CommonMethods.getFilePathFromURI(AddDiary.this, uriFile);
                    String fileLocation = FileUtils.getPath(AddDiary.this, uriFile);
                    File sourceFile = new File(fileLocation);
                    File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CommonMethods.getFileName(AddDiary.this, uriFile));
                    CommonMethods.copyFile(sourceFile, destinationFile);
                    if (metaPosition == -1) {
                        AddAttachModel model = new AddAttachModel();
                        model.setFilePath(fileLocation);
                        model.setFileurl("");
                        model.setName(CommonMethods.getFileName(AddDiary.this, uriFile));
                        if (CommonMethods.isImageUrl(model.getName())) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFile);
                                model.setImageBitmap(bitmap);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        dairyFileList.add(model);
                        notifyAdaptor(dairyFileList);
                    } else {
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

    private void notifyAdaptor(List<AddAttachModel> dairyFileList) {
        AddAttachmentAdapter mAdapter = new AddAttachmentAdapter(AddDiary.this, dairyFileList);
        rvAddDiary.setAdapter(mAdapter);
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        finish();
        toolbarFinish();
    }

    private void successMetaData(List<MetaDataField> response) {
        metaDataList.clear();
        if (!isInternetAvailable() && isEditMode) {

        } else {
            if (null != response && !response.isEmpty()) {
                for (MetaDataField metaModel : response) {
                    if (metaModel.getFieldType().equalsIgnoreCase("select")) {
                        if ((metaModel.getOptions() != null && !metaModel.getOptions().isEmpty())) {
                            metaDataList.add(metaModel);
                        }
                    } else {
                        metaDataList.add(metaModel);
                    }
                }
            }
            if (isEditMode) {
                for (List<MetaValues> preValues : editMetaValues) {
                    for (MetaDataField data : metaDataList) {
                        MetaValues firstMetaValue = preValues.get(0);

                        if (firstMetaValue.getCustom_field_id().equals(data.getId())) {
                            switch (firstMetaValue.getField_type()) {
                                case "textarea":
                                case "text":
                                case "date":
                                case "blankfiller":
                                case "signature":
                                    data.setAnswerString(firstMetaValue.getValue());
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
                                        if (firstMetaValue.getValue().equals(option.getOptionName())) {
                                            option.setSelected(true);
                                        }
                                    }
                                    data.setAnswerString(firstMetaValue.getCheck_input());
                                    break;
                                case "radio":
                                    for (MetaOptions option : data.getOptions()) {
                                        if (firstMetaValue.getValue().equals(option.getOptionName())) {
                                            option.setSelected(true);
                                        }
                                    }
                                    break;
                                case "file":
                                    List<String> serverFiles = new ArrayList<>();
                                    serverFiles.add(firstMetaValue.getValue());
                                    data.setMetaServerFiles(serverFiles);
                                    break;
                            }
                        }
                    }
                }
            }
        }
        metaAdapter.notifyDataSetChanged();
        llGlobal.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(AddDiary.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

    public String[] getTimeArr() {
        return timeArr;
    }

    public Float getsWorkHours() {
        return sWorkHours;
    }
}
