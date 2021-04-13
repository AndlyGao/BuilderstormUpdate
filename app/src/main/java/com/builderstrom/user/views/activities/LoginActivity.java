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

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.CredentialLoginVM;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity login user with credentials and
 * store response of APIs in local DB
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.tvForgotPassword) TextView tvForgotPassword;
    @BindView(R.id.tvChangeWorkspace) TextView tvChangeWorkspace;
    @BindView(R.id.tvLogin) TextView tvLogin;
    @BindView(R.id.ivLogo) ImageView ivLogo;
    private CredentialLoginVM viewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(CredentialLoginVM.class);
        observedViewModel();
        underlineText(tvForgotPassword);
        underlineText(tvChangeWorkspace);
        underlineText(tvLogin);
        setActionDoneKeyboard();
        tvLogin.setVisibility(viewModel.isUserExists() ? View.VISIBLE : View.INVISIBLE);

        if (isInternetAvailable()) {
            GlideApp.with(getApplicationContext())
                    .load(BuilderStormApplication.mPrefs.getWorkSpaceLogo())
                    .apply(new RequestOptions().error(R.drawable.app_logo_big).placeholder(R.drawable.app_logo_big))
                    .into(ivLogo);
        } else {
            ivLogo.setImageResource(R.drawable.ic_app_logo);
        }
    }

    private void observedViewModel() {
        viewModel.isLoadingLD.observe(this, aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.isPinSet.observe(this, aBoolean -> {
            if (null != aBoolean) {
                startActivity(new Intent(getApplicationContext(), aBoolean ? DashBoardActivity.class : SetPinActivity.class));
                finish();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (null != errorModel) {
                if (errorModel.getError_Type() == DataNames.TYPE_ERROR_API) {
                    Log.e("error msg", errorModel.getMessage());
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage(errorModel.getMessage())
                            .setNegativeButton(R.string.ok, null)
                            .create().show();
                } else {
                    errorMessage(errorModel.getMessage(), null, false);
                }
            }
        });
    }

    private void underlineText(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setTextColor(ContextCompat.getColor(LoginActivity.this, android.R.color.holo_blue_light));
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, WorkSpaceActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.tvForgotPassword, R.id.tvChangeWorkspace, R.id.tvLogin, R.id.btnLogin})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgotPassword:
                Intent forgotPinIntent = new Intent(LoginActivity.this, ForgotCredentialActivity.class);
                forgotPinIntent.putExtra("isPassword", true);
                startActivity(forgotPinIntent);
                break;
            case R.id.tvChangeWorkspace:
                startActivity(new Intent(LoginActivity.this, WorkSpaceActivity.class));
                finish();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(LoginActivity.this, PinLoginActivity.class));
                finish();
                break;
            case R.id.btnLogin:
                loginUser();
                break;
        }
    }

    private void setActionDoneKeyboard() {
        etPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            loginUser();
            return false;
        });
    }

    private void loginUser() {
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError(getString(R.string.required_field));
        } else if (CommonMethods.isInvalidEmail(etUsername.getText().toString().trim())) {
            etUsername.setError(getString(R.string.valid_mail));
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.required_field));
        } else if (!BuilderStormApplication.mPrefs.getBaseSite().isEmpty()) {
            viewModel.credentialLogin(etUsername.getText().toString(), etPassword.getText().toString());
        } else {
            startActivity(new Intent(LoginActivity.this, WorkSpaceActivity.class));
            finish();
        }
    }

}
