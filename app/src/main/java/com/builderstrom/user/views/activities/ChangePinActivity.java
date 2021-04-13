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
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ChangeCredentialViewModel;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePinActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edit_old_pin) EditText edOldPin;
    @BindView(R.id.edit_new_pin) EditText edNewPin;
    @BindView(R.id.edit_confirm_pin) EditText edConfirmPin;
    private ChangeCredentialViewModel viewModel;

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loading -> {
            if (null != loading) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.changePinSuccessLD.observe(this, changeSuccess -> {
            if (null != changeSuccess && changeSuccess) {
//                finish();
                toolbarFinish();
            }
        });

        viewModel.errorModelLD.observe(this, model -> {
            if (model != null) {
                handleErrorModel(model);
            }
        });
    }

    @OnClick(R.id.btn_change_pin)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pin:
                callAPI();
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_pin;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ChangeCredentialViewModel.class);
        observeViewModel();
        initToolbar();
        setActionDoneKeyboard();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle("Change Pin");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> toolbarFinish());
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    @Override
    public void onBackPressed() {
        toolbarFinish();
//        if (isTaskRoot()) {
//            startActivity(new Intent(ChangePinActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

    private void callAPI() {
        edOldPin.setError(null);
        edNewPin.setError(null);
        edConfirmPin.setError(null);
        if (edOldPin.getText().toString().isEmpty()) {
            edOldPin.setError("required field");
        } else if (edNewPin.getText().toString().isEmpty()) {
            edNewPin.setError("required field");
        } else if (edConfirmPin.getText().toString().isEmpty()) {
            edConfirmPin.setError("required field");
        } else if (edNewPin.length() < 6 && edConfirmPin.length() < 6) {
            edNewPin.setError("Pin must contains at least 6 characters.");
            edConfirmPin.setError("Pin must contains at least 6 characters.");
        } else if (edNewPin.getText().toString().trim().equals(edConfirmPin.getText().toString().trim())) {
            CommonMethods.hideKeyboard(this);
            viewModel.changePinApi(edOldPin.getText().toString().trim(), edNewPin.getText().toString().trim());
        } else {
            edNewPin.setError("pin mismatch");
            edConfirmPin.setError("pin mismatch");
        }
    }

    private void setActionDoneKeyboard() {
        edConfirmPin.setOnEditorActionListener((textView, i, keyEvent) -> {
            callAPI();
            return false;
        });
    }

}
