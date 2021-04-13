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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.PinLoginViewModel;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity login user with pin and
 * store response of APIs in local DB
 */
public class PinLoginActivity extends BaseActivity {
    @BindView(R.id.ivLogo) ImageView ivLogo;
    @BindView(R.id.edLoginPin) EditText edLoginPin;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.tvForgotCredentials) TextView tvForgotCredentials;
    @BindView(R.id.tvChangeWorkspace) TextView tvChangeWorkspace;
    @BindView(R.id.tvLogin) TextView tvLogin;
    PinLoginViewModel viewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login_pin;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(PinLoginViewModel.class);
        observedViewModel();
        btnLogin.setText(getString(R.string.login));
        underlineText(tvForgotCredentials);
        underlineText(tvChangeWorkspace);
        underlineText(tvLogin);
        setActionDoneKeyboard();
        if (isInternetAvailable()) {
            GlideApp.with(getApplicationContext())
                    .load(viewModel.mPrefs.getWorkSpaceLogo())
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
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                finish();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (null != errorModel) {
                if (errorModel.getError_Type() == DataNames.TYPE_ERROR_API) {
                    new AlertDialog.Builder(PinLoginActivity.this)
                            .setMessage(errorModel.getMessage())
                            .setNegativeButton("Okay", null)
                            .create().show();
                } else {
                    errorMessage(errorModel.getMessage(), null, false);
                }
            }
        });
    }

    private void underlineText(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setTextColor(ContextCompat.getColor(PinLoginActivity.this, android.R.color.holo_blue_light));
        textView.setVisibility(View.VISIBLE);
    }

    private void setActionDoneKeyboard() {
        edLoginPin.setOnEditorActionListener((textView, i, keyEvent) -> {
            loginUser();
            return false;
        });
    }

    private void loginUser() {
        if (edLoginPin.getText().toString().isEmpty()) {
            edLoginPin.setError(getString(R.string.required_field));
        } else if (edLoginPin.length() > 8) {
            edLoginPin.setError(getString(R.string.pin_length));
        } else if (!BuilderStormApplication.mPrefs.getBaseSite().isEmpty()) {
            CommonMethods.hideKeyboard(this);
            viewModel.pinLogin(edLoginPin.getText().toString().trim());
        } else {
            startActivity(new Intent(PinLoginActivity.this, WorkSpaceActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent backPressedIntent = new Intent(getApplicationContext(), WorkSpaceActivity.class);
        startActivity(backPressedIntent);
        finish();
    }

    @Override
    public void errorMessage(String message, @Nullable Integer errorId, boolean isFailure) {
        if (isFailure) {
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton("Okay", null)
                    .create().show();
        } else {
            super.errorMessage(message, errorId, false);
        }
    }

    @OnClick({R.id.btnLogin, R.id.tvForgotCredentials, R.id.tvLogin, R.id.tvChangeWorkspace})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                loginUser();
                break;
            case R.id.tvForgotCredentials:
                Intent forgotPinIntent = new Intent(PinLoginActivity.this, ForgotCredentialActivity.class);
                forgotPinIntent.putExtra("isPassword", false);
                startActivity(forgotPinIntent);
                break;
            case R.id.tvLogin:
                startActivity(new Intent(PinLoginActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.tvChangeWorkspace:
                onBackPressed();
                break;
        }
    }

}


