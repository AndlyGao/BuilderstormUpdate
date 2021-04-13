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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.ChangeCredentialViewModel;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPinActivity extends BaseActivity {
    @BindView(R.id.edLoginPin) EditText edLoginPin;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.ivLogo) ImageView ivLogo;
    @BindView(R.id.tvSlash) View tvSlash;
    @BindView(R.id.tvForgotCredentials) View tvForgotCredentials;
    @BindView(R.id.tvChangeWorkspace) View tvChangeWorkspace;
    @BindView(R.id.tvLogin) View tvLogin;
    private ChangeCredentialViewModel viewModel;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login_pin;
    }

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(ChangeCredentialViewModel.class);
        observeViewModel();
        /* Forgot password view*/
        tvSlash.setVisibility(View.GONE);
        tvForgotCredentials.setVisibility(View.GONE);
        tvChangeWorkspace.setVisibility(View.GONE);
        tvLogin.setVisibility(View.GONE);
        btnLogin.setText(getString(R.string.create_pin));
        setActionDoneKeyboard();
        if (isInternetAvailable()) {
            GlideApp.with(this).load(viewModel.mPrefs.getWorkSpaceLogo())
                    .apply(new RequestOptions().error(R.drawable.ic_app_logo)).into(ivLogo);
        } else {
            ivLogo.setImageResource(R.drawable.ic_app_logo);
        }
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, loading -> {
            if (null != loading) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.changePinSuccessLD.observe(this, changeSuccess -> {
            if (null != changeSuccess && changeSuccess) {
                startActivity(new Intent(this, DashBoardActivity.class));
                finish();
            }
        });

        viewModel.errorModelLD.observe(this, model -> {
            if (model != null) {
                handleErrorModel(model);
            }
        });
    }

    @OnClick(R.id.btnLogin)
    public void onClick() {
        methodPinSet();
    }

    private void setActionDoneKeyboard() {
        edLoginPin.setOnEditorActionListener((textView, i, keyEvent) -> {
            methodPinSet();
            return false;
        });
    }

    private void methodPinSet() {
        if (edLoginPin.getText().toString().isEmpty()) {
            edLoginPin.setError("required field");
        } else if (edLoginPin.length() < 6) {
            edLoginPin.setError("Pin must contains at least 6 characters.");
        } else if (edLoginPin.length() > 8) {
            edLoginPin.setError("Pin cannot exceed more than 8 characters.");
        } else {
            CommonMethods.hideKeyboard(this);
            viewModel.setPinApi(edLoginPin.getText().toString().trim());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, WorkSpaceActivity.class);
            startActivity(intent);
            finish();
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

}


