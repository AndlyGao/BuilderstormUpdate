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

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.customViews.CallProgressWheel;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class SimpleBaseActivity extends AppCompatActivity {
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        this.setFinishOnTouchOutside(false);
        setContentView(getLayoutID());
        mUnBinder = ButterKnife.bind(this);
    }

    protected abstract int getLayoutID();

    protected abstract void init();

    public void showProgress() {
        CallProgressWheel.showLoadingDialog(this, "Loading...");
    }

    public void dismissProgress() {
        CallProgressWheel.dismissLoadingDialog();
    }

    public void errorMessage(String message, @Nullable Integer errorId, boolean isFailure) {
        CommonMethods.displayToast(this, message);
    }

    public boolean isInternetAvailable() {
        return CommonMethods.isNetworkAvailable(this);
    }

    @Override
    protected void onDestroy() {
        mUnBinder.unbind();
        super.onDestroy();
    }

}
