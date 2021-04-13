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
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.MetaDataField;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.RFIViewModel;
import com.builderstrom.user.views.adapters.RfiChildAdapter;
import com.builderstrom.user.views.adapters.metaDataAdapters.MetaDataListAdapter;
import com.builderstrom.user.views.customViews.textChips.NachoTextView;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class AddRFIActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etTitleRFI)
    EditText etTitleRFI;
    @BindView(R.id.etDateRFI)
    EditText etDateRFI;
    @BindView(R.id.etDescriptionRFI)
    EditText etDescriptionRFI;
    @BindView(R.id.tvAutoUsersRFI)
    NachoTextView tvAutoUsersRFI;
    @BindView(R.id.tvAutoCCUsersRFI)
    NachoTextView tvAutoCCUsersRFI;
    @BindView(R.id.llMain)
    LinearLayout llMain;
    @BindView(R.id.rvAddRfi)
    RecyclerView rvAddRfi;
    @BindView(R.id.rvMetaData)
    RecyclerView rvMetaData;
    private int metaPosition = -1;
    private Calendar calendar = Calendar.getInstance();
    private PermissionUtils permissionUtils;
    private String selectedDueDate = "";
    private MetaDataListAdapter metaAdapter;
    private RFIViewModel viewModel;
    private List<MetaDataField> metaDataList = new ArrayList<>();
    private ArrayList<String> filePaths = new ArrayList<>();

    @Override
    protected void init() {
        /* toolbar */
        toolbar.setTitle("Add RFI");
        toolbar.setNavigationOnClickListener(view -> toolbarFinish());
        viewModel = new ViewModelProvider(this).get(RFIViewModel.class);
        metaAdapter = new MetaDataListAdapter(this, metaDataList);
        metaAdapter.setCallback(position -> {
            metaPosition = position;
            checkPermission();
        });
        rvMetaData.setAdapter(metaAdapter);
        observeViewModel();
        /* update to/cc users */
        viewModel.getAllToUsers();
        /* update Meta data */
        llMain.setVisibility(View.INVISIBLE);
        viewModel.getMetadata();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_rfi;
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

        viewModel.metaFieldLD.observe(this, metaDataFields -> {
            if (null != metaDataFields) {
                successMetaData(metaDataFields);
            }
        });

        viewModel.usersLD.observe(this, allUsers -> {
            if (null != allUsers) {
                updateUsersList(allUsers);
            }
        });
    }

    @OnClick({R.id.etDateRFI, R.id.btnUploadRfi, R.id.btnSaveRFI})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDateRFI:
                CommonMethods.hideKeyboard(this, etDateRFI);
                DatePickerDialog pickerDialog = new DatePickerDialog(AddRFIActivity.this,
                        R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker pickerView, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDueDate = new SimpleDateFormat(CommonMethods.DF_1, Locale.US).format(calendar.getTime());
                        etDateRFI.setText(CommonMethods.convertDate(CommonMethods.DF_1, selectedDueDate, CommonMethods.DF_7));
                    }
                }, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();
                break;
            case R.id.btnUploadRfi:
                metaPosition = -1;
                checkPermission();
                break;
            case R.id.btnSaveRFI:
                clearErrors();
                if (etTitleRFI.getText().toString().trim().equalsIgnoreCase("")) {
                    etTitleRFI.setError("Required Field");
                } else if (etDateRFI.getText().toString().trim().equalsIgnoreCase("")) {
                    etDateRFI.setError("Required Field");
                } else if (etDescriptionRFI.getText().toString().trim().equalsIgnoreCase("")) {
                    etDescriptionRFI.setError("Required Field");
                } else {
                    if (metaAdapter.isProperFilled()) {
                        viewModel.callAddRFI(etTitleRFI.getText().toString().trim(),
                                selectedDueDate, etDescriptionRFI.getText().toString().trim(),
                                CommonMethods.getCurrentDate(CommonMethods.DF_1),
                                CommonMethods.getCommaSeparatedString(tvAutoUsersRFI.getChipValues()),
                                CommonMethods.getCommaSeparatedString(tvAutoCCUsersRFI.getChipValues()),
                                metaAdapter.getMetaValuesList(), filePaths);
                    }
                }
                break;
        }
    }

    private void clearErrors() {
        tvAutoUsersRFI.setError(null);
        tvAutoCCUsersRFI.setError(null);
        etTitleRFI.setError(null);
        etDateRFI.setError(null);
        etDescriptionRFI.setError(null);
    }

    private void notifyAdaptor() {
        RfiChildAdapter mAdapter = new RfiChildAdapter(AddRFIActivity.this, filePaths);
        rvAddRfi.setAdapter(mAdapter);
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(AddRFIActivity.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                openExplorer("*/*", CommonMethods.PHOTO_REQUEST_CODE);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uriFile = data.getData();
                List<String> fileList = new ArrayList<>();
                try {
                    if (uriFile != null) {
                        String fileLocation = CommonMethods.getFilePathFromURI(AddRFIActivity.this, uriFile);
                        String fileName = CommonMethods.getFileName(this, uriFile);
                        if (null != fileName.substring(fileName.lastIndexOf("."))) {
                            if (metaPosition == -1) {
                                filePaths.add(fileLocation);
                            } else {
                                fileList.add(fileLocation);
                            }
                        }
                    }
                } catch (Exception e) {
                    errorMessage("File not supported", null, false);
                } finally {
                    if (metaPosition == -1) {
                        notifyAdaptor();
                    } else {
                        metaDataList.get(metaPosition).setMetaUploadFiles(fileList);
                        metaAdapter.notifyItemChanged(metaPosition);
                    }
                }
            }
        }
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        toolbarFinish();
//        finish();
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

    private void successMetaData(List<MetaDataField> response) {
        metaDataList.clear();
        for (MetaDataField metaModel : response) {
            if (metaModel.getFieldType().equalsIgnoreCase("select")) {
                if ((metaModel.getOptions() != null && !metaModel.getOptions().isEmpty())) {
                    metaDataList.add(metaModel);
                }
            } else {
                metaDataList.add(metaModel);
            }
        }
        metaAdapter.notifyDataSetChanged();
        llMain.setVisibility(View.VISIBLE);
    }

    private void updateUsersList(List<User> users) {
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, users);
        tvAutoUsersRFI.setThreshold(1);
        tvAutoUsersRFI.setAdapter(adapter);
        tvAutoCCUsersRFI.setThreshold(1);
        tvAutoCCUsersRFI.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(AddRFIActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

}








