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
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.SnagViewModel;
import com.builderstrom.user.views.adapters.RfiChildAdapter;
import com.builderstrom.user.views.customViews.textChips.NachoTextView;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class AddSnagActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvAutoUsers)
    NachoTextView tvAutoUsers;
    @BindView(R.id.tvAutoCCUsers)
    NachoTextView tvAutoCCUsers;
    @BindView(R.id.ll_Main)
    LinearLayout ll_Main;
    @BindView(R.id.etDate)
    EditText etDate;
    @BindView(R.id.etPackNo)
    EditText etPackNo;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.rvAdd)
    RecyclerView rvAttachment;
    private SnagViewModel viewModel;
    private PermissionUtils permissionUtils;
    private String selectedDate;
    private List<String> filePaths = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_snag;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(SnagViewModel.class);
        observeViewModel();
        initView();
        /* get all users*/
        viewModel.getAllToUsers();
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.snagAddedLD.observe(this, successAdd -> {
            if (successAdd != null && successAdd) {
                Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
                intent.putExtra("KEY_FLAG", 1);
                LocalBroadcastManager.getInstance(AddSnagActivity.this).sendBroadcast(intent);
//                finish();
                toolbarFinish();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (errorModel != null) {
                handleErrorModel(errorModel);
            }
        });

        viewModel.usersLD.observe(this, allUsers -> {
            if (null != allUsers) {
                ArrayAdapter<User> adapter = new ArrayAdapter<>(
                        AddSnagActivity.this, android.R.layout.select_dialog_item, allUsers);
                tvAutoUsers.setThreshold(1);
                tvAutoUsers.setAdapter(adapter);
                tvAutoCCUsers.setThreshold(1);
                tvAutoCCUsers.setAdapter(adapter);
                ll_Main.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        /* toolbar */
        toolbar.setTitle(R.string.add_snag_list_title);
        toolbar.setNavigationOnClickListener(view -> toolbarFinish());
        ll_Main.setVisibility(View.GONE);
    }

    @OnClick({R.id.etDate, R.id.btnUpload, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDate:
                openDatePicker();
                break;
            case R.id.btnUpload:
                checkPermission();
                break;
            case R.id.btnSave:
                saveSnagList();
                break;
        }
    }

    private void openDatePicker() {
        DatePickerDialog pickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, (view, year, month, dayOfMonth) -> {
            Calendar calendarInstance = Calendar.getInstance();
            calendarInstance.set(Calendar.YEAR, year);
            calendarInstance.set(Calendar.MONTH, month);
            calendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedDate = new SimpleDateFormat(CommonMethods.DF_2, Locale.US).format(calendarInstance.getTime());
            etDate.setText(CommonMethods.convertDate(CommonMethods.DF_2, selectedDate, CommonMethods.DF_7));
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
    }

    private void saveSnagList() {
        clearAllErrors();
        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Required Field");
        } else if (etPackNo.getText().toString().trim().isEmpty()) {
            etPackNo.setError("Required Field");
        } else if (etDate.getText().toString().trim().isEmpty()) {
            etDate.setError("Required Field");
        } else if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Required Field");
        } else {
            viewModel.addSnag(etDescription.getText().toString().trim(), etLocation.getText().toString().trim(),
                    etPackNo.getText().toString().trim(), CommonMethods.getCommaSeparatedString(tvAutoUsers.getChipValues()),
                    CommonMethods.getCommaSeparatedString(tvAutoCCUsers.getChipValues()), selectedDate,
                    CommonMethods.getCurrentDate(CommonMethods.DF_1), filePaths);
        }
    }

    private void clearAllErrors() {
        etDate.setError(null);
        etPackNo.setError(null);
        etDescription.setError(null);
        etLocation.setError(null);
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                openExplorer("*/*", CommonMethods.PHOTO_REQUEST_CODE);
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
            }
        });
        permissionUtils.checkPermissions();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uriFile = data.getData();
                try {
                    if (uriFile != null) {
                        String fileLocation = CommonMethods.getFilePathFromURI(AddSnagActivity.this, uriFile);
                        String fileName = CommonMethods.getFileName(this, uriFile);
                        if (null != fileName.substring(fileName.lastIndexOf("."))) {
                            filePaths.add(fileLocation);
                        }
                    }
                } catch (Exception e) {
                    errorMessage("File not supported", null, false);
                } finally {
                    notifyAdaptor();
                }
            }
        }
    }

    private void notifyAdaptor() {
        rvAttachment.setAdapter(new RfiChildAdapter(this, filePaths));
    }

    @Override
    public void onBackPressed() {
        toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(AddSnagActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }
}
