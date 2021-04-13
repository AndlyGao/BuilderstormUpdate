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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.FileUtils;
import com.builderstrom.user.utils.GetCompressedImage;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class GetPhotoActivity extends BaseActivity implements GetCompressedImage.SampledImageAsyncResp {
    private static final int CAMERA_REQUEST = 222;
    private static final int STORAGE_REQUEST = 111;
    private static final int VIEW_IMAGE_REQUEST = 321;
    private static final int CAMERA_CODE = 2344;
    private static final int GALLERY_CODE = 2444;
    private static final int CROPPING_CODE = 2345;
    private PermissionUtils permissionUtils;
    private File outPutFile;
    private Uri mImageCaptureUri;
    private String imageUrl = "", previousScreen = "";
    private String filePath =/*  try {
            if (mImageCaptureUri != null) {
                Intent intent1 = new Intent();
                intent1.putExtra("filePath", filePath);
//                intent1.putExtra("filePath", compressImage(String.valueOf(mImageCaptureUri)));
                setResult(RESULT_OK, intent1);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/ "";
    //SDF to generate a unique name for our compress file.
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());

    @Override
    protected int getLayoutID() {
        return R.layout.activity_get_photo;
    }

    @Override
    protected void init() {
        if (getIntent().hasExtra("imageUrl")) {
            imageUrl = getIntent().getStringExtra("imageUrl");
        } else {
            imageUrl = "";
        }

        if (getIntent().hasExtra("previousScreen")) {
            previousScreen = getIntent().getStringExtra("previousScreen");
        } else {
            previousScreen = "";
        }

        try {
            outPutFile = CommonMethods.createImageFile(GetPhotoActivity.this);
//            outPutFile = CommonMethods.setUpImageFile(CommonMethods.LOCAL_STORAGE_BASE_PATH_FOR_USER_PHOTOS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        permissionUtils = new PermissionUtils(GetPhotoActivity.this);
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                selectImageOption();
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
                finish();
            }
        });
        permissionUtils.checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST:
            case CAMERA_REQUEST:
                permissionUtils.verifyResults(requestCode, grantResults);
                break;
        }
    }

    private void selectImageOption() {
        final CharSequence[] items;
        if (imageUrl.isEmpty() && previousScreen.isEmpty()) {
            items = new CharSequence[]{"Capture Photo", "Choose from Gallery", "Cancel"};
        } else {
            items = new CharSequence[]{"Capture Photo", "Choose from Gallery", "View Image", "Cancel"};
        }

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("")
                .setItems(items, (dialog, item) -> {
                    if (items[item].equals("Capture Photo")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(GetPhotoActivity.this.getPackageManager()) != null && outPutFile != null) {
                            mImageCaptureUri = FileProvider.getUriForFile(GetPhotoActivity.this, "com.builderstrom.user.fileprovider", outPutFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(takePictureIntent, CAMERA_CODE);
                        } else {
                            errorMessage("", R.string.something_went_wrong, false);
                            dialog.dismiss();
                            finish();
                        }
                    } else if (items[item].equals("Choose from Gallery")) {
//                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        i.setType("image/*");
                        startActivityForResult(i, GALLERY_CODE);
                    } else if (items[item].equals("View Image")) {
                        dialog.dismiss();
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

//    private void startChooseImageIntentForResult() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_CHOOSE_IMAGE);
//      }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_CODE:
                    filePath = outPutFile.getAbsolutePath();
                    galleryAddPic(filePath);
                    CroppingIMG(false);
                    break;
                case GALLERY_CODE:
                    mImageCaptureUri = data.getData();
                    filePath = FileUtils.getPath(this, mImageCaptureUri);
             /*       String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(mImageCaptureUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();*/
                    CroppingIMG(true);
                    break;

                case CROPPING_CODE:
//                    try {
//                        if (outPutFile.exists()) {
//                            Intent intent1 = new Intent();
//                            intent1.putExtra("filePath", outPutFile.getAbsolutePath());
//                            setResult(RESULT_OK, intent1);
//                            finish();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Error while saving image", Toast.LENGTH_SHORT).show();
//                            GetPhotoActivity.this.finish();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), "Error while saving image", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
                    break;

                case VIEW_IMAGE_REQUEST:
                    GetPhotoActivity.this.finish();
                    break;
            }
        } else {
            errorMessage("", R.string.something_went_wrong, false);
            finish();
        }
    }

    private void CroppingIMG(Boolean isGalleryImage) {
        new GetCompressedImage(this).execute(filePath);
      /*  try {
            if (mImageCaptureUri != null) {
                Intent intent1 = new Intent();
                intent1.putExtra("filePath", filePath);
//                intent1.putExtra("filePath", compressImage(String.valueOf(mImageCaptureUri)));
                setResult(RESULT_OK, intent1);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setType("image/*");
//        List<ResolveInfo> list = this.getPackageManager().queryIntentActivities(intent, 0);
//        int size = list.size();
//        if (size == 0) {
//            Toast.makeText(this, "Can't find image cropping app", Toast.LENGTH_SHORT).show();
//        } else {
//            intent.setData(mImageCaptureUri);
//            intent.putExtra("outputX", 512);
//            intent.putExtra("outputY", 512);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("scale", true);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
//            //intent.putExtra("return-data", true);
//            //Create output file here
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));
//            if (size > 0) {
//                Intent i = new Intent(intent);
//                ResolveInfo res = list.get(0);
//                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//                startActivityForResult(i, CROPPING_CODE);
//            }
//        }
    }

    /**
     * Add image to the gallery
     *
     * @param mCurrentPhotoPath path of the clicked image
     */
    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onSampledImageAsyncPostExecute(File file) {


        if (file != null) {
            try {
                Intent intent1 = new Intent();
                intent1.putExtra("filePath", file.getAbsolutePath());
                setResult(RESULT_OK, intent1);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}

