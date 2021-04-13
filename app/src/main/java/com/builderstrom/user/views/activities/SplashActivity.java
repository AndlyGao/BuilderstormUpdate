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
import android.os.Build;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.ClientLogger;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity initialize the user access permission
 * and redirect user to appropriate screen.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getName();
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnLearnMore) Button btnLearnMore;
    private PermissionUtils permissionUtils;

    @Override
    protected void init() {
        if (!BuilderStormApplication.mPrefs.getBaseSite().isEmpty()) {
            startActivity(new Intent(SplashActivity.this, BuilderStormApplication.mPrefs.isLogin() ?
                    DashBoardActivity.class : (isUserExists() ? PinLoginActivity.class : LoginActivity.class)));
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
            finish();
        } else {
            checkPermission();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    private void initView() {
        ClientLogger.d("", "------------------------------------------------------------------------");
        ClientLogger.d(TAG, "---------------------------- WELCOME TO BUILDERSTORM ----------------------------");
        ClientLogger.d(TAG, "Device API Level:- " + Build.VERSION.SDK_INT);
        ClientLogger.d(TAG, "Device:- " + Build.DEVICE);
        ClientLogger.d(TAG, "Model:- " + Build.MODEL);
        ClientLogger.d(TAG, "MANUFACTURER:- " + Build.MANUFACTURER);
        ClientLogger.d(TAG, "PRODUCT:- " + Build.PRODUCT);
        ClientLogger.d(TAG, "PRODUCT:- " + Build.PRODUCT);
        ClientLogger.d(TAG, "Internal storage Available:- " + CommonMethods.getAvailableInternalMemorySize());
        ClientLogger.d(TAG, "Internal storage Total:- " + CommonMethods.getTotalInternalMemorySize());
        ClientLogger.d(TAG, "External storage Available:- " + CommonMethods.getAvailableExternalMemorySize());
        ClientLogger.d(TAG, "External storage Total:- " + CommonMethods.getTotalExternalMemorySize());
        ClientLogger.d(TAG, String.format("Network Type : %s", CommonMethods.networkType(SplashActivity.this)));
        ClientLogger.d(TAG, String.format("Network Status : %s", CommonMethods.isNetworkConnected(SplashActivity.this) ? "Connected" : "Disconnected"));
        ClientLogger.d(TAG, String.format("User Location: %s", CommonMethods.isLocationEnable(SplashActivity.this) ? "Enable" : "Disabled"));
    }

    private boolean isUserExists() {
        return BuilderStormApplication.mPrefs.getUserId() != null
                && !BuilderStormApplication.mPrefs.getUserId().isEmpty()
                && !BuilderStormApplication.mPrefs.getUserId().equals("0");
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(SplashActivity.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                ClientLogger.init(getApplicationContext(), ClientLogger.MICROLOG_4_ANDROID_LOGGER);
                initView();
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
                initView();
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

    @OnClick({R.id.btnLogin, R.id.btnLearnMore})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                startActivity(new Intent(getApplicationContext(), WorkSpaceActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                finish();
                break;
            case R.id.btnLearnMore:
                CommonMethods.openUrl(this, DataNames.DEMO_URL);
                break;
        }
    }

}
