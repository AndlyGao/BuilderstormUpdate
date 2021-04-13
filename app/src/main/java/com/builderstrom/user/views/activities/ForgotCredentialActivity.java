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

import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.ChangeCredentialViewModel;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotCredentialActivity extends BaseActivity {
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.ivLogo) ImageView ivLogo;
    private Boolean isForgetPassword = false;
    private ChangeCredentialViewModel viewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_forgot_pin;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ChangeCredentialViewModel.class);
        observeViewModel();
        /* Bundle data */
        if (getIntent().hasExtra("isPassword")) {
            isForgetPassword = getIntent().getBooleanExtra("isPassword", false);
        } else isForgetPassword = false;

        if (isInternetAvailable()) {
            GlideApp.with(getApplicationContext())
                    .load(BuilderStormApplication.mPrefs.getWorkSpaceLogo())
                    .apply(new RequestOptions().error(R.drawable.app_logo_big).placeholder(R.drawable.app_logo_big))
                    .into(ivLogo);
        } else {
            ivLogo.setImageResource(R.drawable.ic_app_logo);
        }
        /* ime option */
        etEmail.setOnEditorActionListener((textView, i, keyEvent) -> {
            forgetClick();
            return false;
        });
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loading -> {
            if (null != loading) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.forgotCredSuccessLD.observe(this, forgotCred -> {
            if (null != forgotCred && forgotCred) {
                etEmail.setText(null);
                finish();
            }
        });

        viewModel.errorModelLD.observe(this, model -> {
            if (model != null) {
                handleErrorModel(model);
            }
        });
    }

    @OnClick(R.id.btnForgot)
    public void onClick() {
        forgetClick();
    }

    private void forgetClick() {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.required_field));
        } else if (CommonMethods.isInvalidEmail(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.enter_valid_email));
        } else {
            if (isInternetAvailable()) {
                if (isForgetPassword) {
                    viewModel.apiForgotPassword(etEmail.getText().toString().trim());
                } else {
                    viewModel.apiForgotPin(etEmail.getText().toString().trim());
                }
            } else {
                errorMessage(getString(R.string.no_internet_connection), null, false);
            }
        }
    }

}



