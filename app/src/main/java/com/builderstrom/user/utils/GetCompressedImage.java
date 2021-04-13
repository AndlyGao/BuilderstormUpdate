package com.builderstrom.user.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.provider.MediaStore;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class GetCompressedImage extends AsyncTask<String, Void, File> {

    private Activity mActivity;
    private SampledImageAsyncResp mSampledImageAsyncResp = null;
    private ProgressDialog mProgressDialog;
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());

    public GetCompressedImage(Fragment fragment) {
        mActivity = fragment.getActivity();
        mSampledImageAsyncResp = (SampledImageAsyncResp) fragment;
    }

    public GetCompressedImage(Activity activity) {
        mActivity = activity;
        mSampledImageAsyncResp = (SampledImageAsyncResp) activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("processing");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected File doInBackground(String... params) {
        try {
            String picturePath = params[0];
            return getCompressed(mActivity, picturePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            String picturePath = params[0];
//            String imageDirectory = params[1];
//            boolean isGalleryImage = Boolean.valueOf(params[2]);
//            int reqImageWidth = Integer.parseInt(params[3]);
//            if (picturePath != null) {
//                ExifInterface exif = new ExifInterface(picturePath);
//                int orientation = exif.getAttributeInt(
//                        ExifInterface.TAG_ORIENTATION, 1);
//                final BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(picturePath, options);
//                options.inSampleSize = calculateInSampleSize(options, reqImageWidth, reqImageWidth);
//                options.inJustDecodeBounds = false;
//                Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath, options);
//                if (orientation == 6) {
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
//                            imageBitmap.getWidth(), imageBitmap.getHeight(),
//                            matrix, true);
//                } else if (orientation == 8) {
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(270);
//                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
//                            imageBitmap.getWidth(), imageBitmap.getHeight(),
//                            matrix, true);
//                } else if (orientation == 3) {
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(180);
//                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
//                            imageBitmap.getWidth(), imageBitmap.getHeight(),
//                            matrix, true);
//                }
//                if (imageBitmap != null) {
//                    return getImageFile(imageBitmap, picturePath, imageDirectory, isGalleryImage);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        mProgressDialog.dismiss();
        if (mSampledImageAsyncResp != null) {
            mSampledImageAsyncResp.onSampledImageAsyncPostExecute(file);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private File getImageFile(Bitmap bmp, String picturePath, String imageDirectory,
                              boolean isGalleryImage) {
        try {
            OutputStream fOut = null;
            File file;
            if (isGalleryImage) {
                file = CommonMethods.setUpImageFile(imageDirectory);
            } else {
                file = new File(picturePath);
            }
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
                    file.getAbsolutePath(), file.getName(),
                    file.getName());
            return file;
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface SampledImageAsyncResp {
        void onSampledImageAsyncPostExecute(File file);
    }


    public static File getCompressed(Context context, String path) throws IOException {

        if (context == null)
            throw new NullPointerException("Context must not be null.");

        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            //fall back
            cacheDir = context.getCacheDir();

        String rootDir = cacheDir.getAbsolutePath() + "/BSCompressor";
        File root = new File(rootDir);

        //Create ImageCompressor folder if it doesnt already exists.
        if (!root.exists())
            root.mkdirs();

        ExifInterface exif = new ExifInterface(path);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);


        //decode and resize the original bitmap from @param path.
        Bitmap bitmap = decodeImageFromFiles(path, 400, 400);


        /* setup rotation */
        if (orientation == 6) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == 8) {
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (orientation == 3) {
            Matrix matrix = new Matrix();
            matrix.postRotate(180);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        //create placeholder for the compressed image file
        File compressed = new File(root, SDF.format(new Date()) + ".jpg" /*Your desired format*/);

        //convert the decoded bitmap to stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        /*
        Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
        java.io.FileOutputStream can help us do just That!
         */
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }


}
