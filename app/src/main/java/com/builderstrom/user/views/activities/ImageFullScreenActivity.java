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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.customViews.CustomImageViewZoom;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class ImageFullScreenActivity extends BaseActivity {
    private static final int STORAGE_REQUEST = 111;
    @BindView(R.id.imageView) CustomImageViewZoom imageView;
    private String imageUrl = "";
    private PermissionUtils permissionUtils;
    private Bitmap imageBitmap = null;

    @Override
    protected void init() {
        if (getIntent().hasExtra("imageBitmap")) {
            byte[] byteArray = getIntent().getByteArrayExtra("imageBitmap");
            imageBitmap = null;
            imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            initializeView();
            setDataInViewObjects();

        } else if (getIntent().hasExtra("fileName")) {
            String imageName = getIntent().getStringExtra("fileName");
            File test = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + imageName);
            imageBitmap = BitmapFactory.decodeFile(test.getAbsolutePath());
            initializeView();
            setDataInViewObjects();
        } else {
            if (getIntent().hasExtra("imageUrl") || getIntent().hasExtra("filePath")) {

                if (getIntent().hasExtra("imageUrl")) {
                    imageUrl = getIntent().getStringExtra("imageUrl");
                    imageBitmap = null;
                }

                if (getIntent().hasExtra("filePath")) {
                    File outPutFile = new File(getIntent().getStringExtra("filePath"));
                    imageBitmap = BitmapFactory.decodeFile(outPutFile.getAbsolutePath());
                    imageUrl = "";
                }
                initializeView();
                setDataInViewObjects();
            } else {
                errorMessage("", R.string.something_went_wrong, false);
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_image_full_screen;
    }

    public void initializeView() {
        if (CommonMethods.hasLollipop()) {
            imageView.setTransitionName("profile");
        }

        permissionUtils = new PermissionUtils(this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {

            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
            }
        });
    }

    @OnClick(R.id.ivBack)
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }
    
    private void setDataInViewObjects() {
        if (imageUrl.isEmpty() && imageBitmap == null) {
            setResult(RESULT_OK);
            errorMessage("", R.string.image_not_found, false);
            finish();
        } else if (imageUrl.isEmpty()) {
            imageView.setImageBitmap(imageBitmap);
        } else {
            GlideApp.with(this).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    Log.e(ImageFullScreenActivity.class.getSimpleName(), "LOad failure");
                }
            });
//            new loadPrescriptionImage().execute(imageUrl);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        supportFinishAfterTransition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_REQUEST) {
            permissionUtils.verifyResults(requestCode, grantResults);
        }
    }


//    @SuppressLint("StaticFieldLeak")
//    private class loadPrescriptionImage extends AsyncTask<String, Void, Bitmap> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgress();
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            URL url;
//            Bitmap bitmap;
//            try {
//                url = new URL(strings[0]);
//                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (Exception e) {
//                e.printStackTrace();
//                bitmap = null;
//            }
//            return bitmap;
//        }
//
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            dismissProgress();
//            if (bitmap == null) {
//                errorMessage("", R.string.something_went_wrong, false);
//                setResult(RESULT_OK);
//                ImageFullScreenActivity.this.finish();
//            } else {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }

}
