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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.WorkSpaceViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity authenticate the site
 */
public class WorkSpaceActivity extends BaseActivity {
    @BindView(R.id.etWorkspace) AutoCompleteTextView etWorkspace;
    @BindView(R.id.tvTermsCondition) TextView tvTermsCondition;
    private WorkSpaceViewModel viewModel;

    @Override
    protected void init() {
        viewModel = new ViewModelProvider(this).get(WorkSpaceViewModel.class);
        observeViewModel();
        customTextView(tvTermsCondition);
        setActionDoneKeyboard();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_workspace;
    }

    @OnClick({R.id.btnSubmit})
    public void onClick() {
        accessWorkspace();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(WorkSpaceActivity.this)
                .setTitle(R.string.close_app)
                .setMessage(R.string.msg_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, id) -> WorkSpaceActivity.this.finish())
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel()).show();
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(this, aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.redirectLD.observe(this, redirectInteger -> {
            if (redirectInteger != null) {
                startActivity(new Intent(getApplicationContext(), redirectInteger == 1 ?
                        PinLoginActivity.class : LoginActivity.class));
                finish();
            }
        });

        viewModel.errorModelLD.observe(this, errorModel -> {
            if (errorModel != null) {
                handleErrorModel(errorModel);
            }
        });
    }

    private void accessWorkspace() {
        if (etWorkspace.getText().toString().isEmpty()) {
            etWorkspace.setError(getString(R.string.workspace_name));
        } else if (!etWorkspace.getText().toString().isEmpty() &&
                CommonMethods.isInValidUrl(etWorkspace.getText().toString().trim())) {
            errorMessage(getString(R.string.invalid_name), null, false);
        } else if (!isContainSpecial(etWorkspace.getText().toString())) {
            CommonMethods.displayToast(WorkSpaceActivity.this, getString(R.string.special_characters));
        } else {
            viewModel.accessWorkSpace(validWorkSpace(etWorkspace.getText().toString().trim()));
        }
    }

    private String validWorkSpace(String url) {
        return url.replace("Https://", "").replace("Http://", "").replace(".builderstorm.com", "");
    }

    private boolean isContainSpecial(String site) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\._\\-]*");
        Matcher matcher = pattern.matcher(site);
        return matcher.matches();
    }

    private void setActionDoneKeyboard() {
        etWorkspace.setOnEditorActionListener((textView, i, keyEvent) -> {
            accessWorkspace();
            return false;
        });
    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(view.getText());
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                CommonMethods.openUrl(WorkSpaceActivity.this, DataNames.TERM_CONDITION_URL);
            }
        }, 0, 16, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                CommonMethods.openUrl(WorkSpaceActivity.this, DataNames.PRIVACY_POLICY_URL);
            }
        }, 21, view.getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt);
    }

}




