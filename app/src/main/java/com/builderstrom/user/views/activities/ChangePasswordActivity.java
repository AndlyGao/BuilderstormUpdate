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

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edOldPassword) EditText edOldPassword;
    @BindView(R.id.edNewPassword) EditText edNewPassword;
    @BindView(R.id.edConfirmPassword) EditText edConfirmPassword;
    private ChangeCredentialViewModel viewModel;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ChangeCredentialViewModel.class);
        observeViewModel();
        initToolbar();
        setActionDoneKeyboard();
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loading -> {
            if (null != loading) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.changePasswordSuccessLD.observe(this, changeSuccess -> {
            if (null != changeSuccess && changeSuccess) {
                startActivity(new Intent(this, DashBoardActivity.class));
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

    @OnClick(R.id.btnPassword)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPassword:
                callAPI();
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_password;
    }

    private void initToolbar() {
        toolbar.setTitle(R.string.change_password);
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
//            startActivity(new Intent(ChangePasswordActivity.this, DashBoardActivity.class));
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//        CommonMethods.hideKeyboard(this);
    }

    private void callAPI() {
        edOldPassword.setError(null);
        edNewPassword.setError(null);
        edConfirmPassword.setError(null);
        if (edOldPassword.getText().toString().isEmpty()) {
            edOldPassword.setError("required field");
        } else if (edNewPassword.getText().toString().isEmpty()) {
            edNewPassword.setError("required field");
        } else if (edConfirmPassword.getText().toString().isEmpty()) {
            edConfirmPassword.setError("required field");
        } else if (edNewPassword.getText().toString().trim().equals(edConfirmPassword.getText().toString().trim())) {
            CommonMethods.hideKeyboard(this);
            viewModel.apiChangePassword(edOldPassword.getText().toString().trim(), edNewPassword.getText().toString().trim());
        } else {
            edNewPassword.setError("password mismatch");
            edConfirmPassword.setError("password mismatch");
        }
    }

    private void setActionDoneKeyboard() {
        edConfirmPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            callAPI();
            return false;
        });
    }

}
