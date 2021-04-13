package com.builderstrom.user.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.repository.retrofit.modals.GeneralSettingsDatum;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.NotificationIconModel;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static java.util.jar.Pack200.Packer.ERROR;

/**
 * Created by anil_singhania on 26/09/2018.
 */
public class CommonMethods {
    public static final int PHOTO_REQUEST_CODE = 1213;
    public static final int FILE_REQUEST_CODE = 1214;
    public static final int PHOTO_REQUEST_UPDATE = 1215;
    public static final int PICK_FILE_RESULT_PDF = 1216;
    public static final int PICK_FILE_RESULT_DWG = 1217;
    public static final int PICK_FILE_RESULT_OTHER = 1220;
    /* Common Date Formats String */
    public static final String DF_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DF_2 = "yyyy-MM-dd";
    public static final String DF_3 = "dd MMMM yyyy HH:mm";
    public static final String DF_4 = "dd MMMM yyyy";
    public static final String DF_5 = "dd MMM yyyy";
    public static final String DF_6 = "dd/MM/yyyy HH:mm";
    public static final String DF_7 = "dd/MM/yyyy";
    public static final String DF_8 = "dd-MM-yyyy";
    public static final String DF_9 = "HH:mm";
    public static final String DF_10 = "EEE MM/dd/yyyy";
    public static final String DF_11 = "EEE dd/MM/yyyy";
    public static final String DF_12 = "dd-M-yyyy";
    public static final String DF_13 = "EEEE dd/MM";
    public static final String DF_14 = "dd MMMM, yyyy HH:mm";
    public static final String DF_15 = "yyyy-MM-dd HH:mm";
    public static final String DF_16 = "dd-MMM-yyyy HH:mm";
    public static final String DF_17 = "EEE";

    private static final String TAG = CommonMethods.class.getSimpleName();
    private static final String LOCAL_STORAGE_BASE_PATH_FOR_MEDIA = Environment.getExternalStorageDirectory() + "/" + "Builderstorm";
    public static final String LOCAL_STORAGE_BASE_PATH_FOR_POSTED_IMAGES =
            LOCAL_STORAGE_BASE_PATH_FOR_MEDIA + "/Post/Images/";
    public static final String LOCAL_STORAGE_BASE_PATH_FOR_USER_PHOTOS =
            LOCAL_STORAGE_BASE_PATH_FOR_MEDIA + "/User/Photos/";
    /* file prefix and suffix */
    private static final String JPEG_FILE_PREFIX = "";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    /**
     * Method to refrain user from entering emojis
     */
    public static InputFilter EMOJI_FILTER = (source, start, end, dest, dStart, dEnd) -> {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));
            if (type == Character.SURROGATE) {
                return "";
            }
        }
        return null;
    };


    /* Utils Functions */
    private static String UK_TIME_ZONE = "WET";
    private static int THUMBNAIL_SIZE = 7;

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void displayToast(Context ctx, String message) {
        if (ctx != null) {
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isInvalidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    /**
     * Method to close the soft keypad
     */

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This method check the network availability in the device weather its from
     * mobile data or Wi-Fi or any other medium
     *
     * @param context context of the current Activity
     * @return TRUE, if Internet connection is available, FALSE otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static String networkType(Context context) {
        String networktype = "Not Available";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            networktype = info.getTypeName();
        }
        return networktype;
    }

    public static boolean isOfflineEntry(String rowId) {
        return rowId == null || rowId.equals("0");
    }

    public static Uri getUriFromFileName(Context context, String name) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + name);
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.builderstrom.user.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Uri getUriFromName(Context context, String name) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + name);
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.builderstrom.user.fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Bitmap getBitmapFromName(Context context, String name) {
        Uri localUri = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + name);
        if (Build.VERSION.SDK_INT >= 24) {
            localUri = FileProvider.getUriForFile(context, "com.builderstrom.user.fileprovider",
                    file);
        } else {
            localUri = Uri.fromFile(file);
        }
        Bitmap result = null;
        try {
            result = MediaStore.Images.Media.getBitmap(context.getContentResolver(), localUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static SpannableStringBuilder spannedText(String formatText, int start, int end) {
        if (null != formatText && !formatText.isEmpty()) {
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
            ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssBuilder;
        }
        return null;
    }

    public static SpannableStringBuilder coloredSpannedText(String formatText, int start, int end) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
//        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssBuilder;
    }

    public static SpannableStringBuilder coloredSpannedText(String formatText, int end) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssBuilder;
    }

    public static SpannableStringBuilder coloredSpannedText(String formatText, int bStart, int bEnd, int CEnd) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, bEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), bEnd, CEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssBuilder;
    }


    public static SpannableStringBuilder coloredSpannedTextMultiples(String formatText, int firstTextLength, int secondTextLength) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, firstTextLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), formatText.length() - secondTextLength,
                formatText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), formatText.length() - secondTextLength,
                formatText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssBuilder;
    }

    public static SpannableStringBuilder coloredSpannedTextMultiple(String formatText, int firstTextLength, int secondTextLength) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(formatText);
        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, firstTextLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), 0, firstTextLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), formatText.length() - secondTextLength,
                formatText.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), formatText.length() - secondTextLength,
                formatText.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssBuilder;
    }


    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static byte[] BitMapTobyte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Bitmap ByteToBitMap(byte[] bytesArray) {
        try {
            return BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static BitmapDrawable convertBitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }


    /**
     * Get byte array from bitmap
     *
     * @param bitmap image bitmap
     * @return byte[]
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Get version name of the current app
     */
    public static String getAppVersion(Context context) {
        String versionName;
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "1.0.0.0";
        }
        return versionName;
    }

    public static String getDeviceID(Context mContext) {
        return hasMarshmallow() ? CommonMethods.getMACAddress_6_0() : CommonMethods.getMACAddress(
                mContext);
    }

    /**
     * returns Wifi MAC address for devices having OS below 6.0
     */
    @SuppressLint("HardwareIds")
    private static String getMACAddress(Context context) {
        WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        return wifiInf.getMacAddress();
    }

    /**
     * returns Wifi MAC address for devices having OS Android 6.0
     */
    private static String getMACAddress_6_0() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            return "02:00:00:00:00:00";
        }
        return "02:00:00:00:00:00";
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static File createSignatureFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static Bitmap getScaleBitmap(int scale, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = scale;
        int targetH = scale;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        //        mImageView.setImageBitmap(bitmap);
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }

    public static void download(String downloadUrl, String description, Context context, String
            fileExtension) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String fileName = timeStamp + fileExtension;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setDescription(
                description);   //appears the same in Notification bar while downloading
        request.setTitle(fileName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    /* Date conversions */
    public static String getCurrentDate(String reqFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(reqFormat, Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
        return dateFormat.format(new Date());
    }

    public static Date getTodayDate(String reqFormat) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(reqFormat, Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
        return dateFormat.parse(dateFormat.format(new Date()));
    }

    public static DateFormat getDateFormat(String format) {

        DateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
        return dateFormat;
    }

    public static Boolean isStartBeforeEndTime(String startTime, String endTime) {
        DateFormat startFormat = getDateFormat(DF_9);
        DateFormat endFormat = getDateFormat(DF_9);
        try {
            Date startDate = startFormat.parse(startTime);
            Date endDate = endFormat.parse(endTime);
            return startDate.before(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Boolean compareDates(String startDate, String endDate) {
        DateFormat startFormat = getDateFormat(DF_2);
        DateFormat endFormat = getDateFormat(DF_2);
        try {
            Date sDate = startFormat.parse(startDate);
            Date eDate = endFormat.parse(endDate);
            return sDate.before(eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCalenderDate(String reqFormat, Date date) {
        return new SimpleDateFormat(reqFormat, Locale.US).format(date);
    }

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static boolean downloadFile(Context context) {
        if (isNetworkAvailable(context)) {
            if (CommonMethods.hasMarshmallow()) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        CommonMethods.displayToast(context, context.getResources().getString(
                                R.string.storage_permission_required));
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                0);
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                0);
                    }
                } else {
                    return true;
//                    download(manager, downloadUrl, description, context, fileExtension);
                }
            } else {
                return true;
//                download(manager, downloadUrl, description, context, fileExtension);
            }
            return false;
        } else {
            displayToast(context,
                    context.getResources().getString(R.string.no_internet_connection));
            return false;
        }

    }

    public static String download(DownloadManager manager, String downloadUrl, String description,
                                  Context context, String fileExtension) {
        String urlDecode = URLDecoder.decode(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDecode));
        request.setDescription(description);   //appears the same in Notification bar while downloading
        request.setTitle(description);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, description);
        manager.enqueue(request);
        boolean downloading = true;
        try {
            DownloadManager.Query query = new DownloadManager.Query();
            Cursor cursorDownloadFile;
            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
            while (downloading) {
                cursorDownloadFile = manager.query(query);
                if (cursorDownloadFile.moveToFirst()) {
                    int status = cursorDownloadFile.getInt(cursorDownloadFile.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                        if (context != null) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(DataNames.INTENT_SINGLE_FILE_DOWNLOAD));
                        }
                        break;
                    }
                    if (status == DownloadManager.STATUS_FAILED) {
                        downloading = false;
                        break;
                    }
                    cursorDownloadFile.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), description);
        return file.getName();
    }

    public static String download(DownloadManager manager, String downloadUrl, String description) {
        String urlDecode = URLDecoder.decode(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDecode));
        request.setDescription(description);   //appears the same in Notification bar while downloading
        request.setTitle(description);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, description);
        manager.enqueue(request);
        return description;
    }

    public static void shareToGmail(String email, String subject, String content, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        context.startActivity(emailIntent);
    }

    @NonNull
    public static RequestBody createPartFromString(String description) {
        return RequestBody.create(MultipartBody.FORM, description);
    }

    @NonNull
    public static MultipartBody.Part createPartFromFile(String partName, String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static void copyFile(File SourceLocation, File DestinationLocation) {
        if (SourceLocation.exists()) {
            try {
                InputStream in = new FileInputStream(SourceLocation);
                OutputStream out = new FileOutputStream(DestinationLocation);
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v(TAG, "Copy file successful.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.v(TAG, "Copy file failed. Source file missing.");
        }
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        String fileName = getFileName(context, contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getFilesDir() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getFileNameFromPath(String filePath) {
        try {
            if (!filePath.isEmpty()) {
                String[] filarray = filePath.split("/");
                return filarray[filarray.length - 1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFileNameFromURL(String filePath) {
        try {
            if (!filePath.isEmpty()) {
                String filename = filePath.replace("}", "").replace("{", "").replace("\"", "");
                String[] filarray = filename.split(":");
                return filarray[filarray.length - 1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IoUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* date conversion methods */
    public static String convertDate(String iFormat, String inputDate, String oFormat) {
        if (null != inputDate && !inputDate.isEmpty()) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(iFormat, Locale.UK);
            SimpleDateFormat outputFormat = new SimpleDateFormat(oFormat, Locale.UK);
            inputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
            outputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
            String str = null;
            try {
                Date date = inputFormat.parse(inputDate);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }
        return null;
    }

    public static String convertDateUK(String iFormat, String inputDate, String oFormat) {
        if (null != inputDate && !inputDate.isEmpty()) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(iFormat, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(oFormat, Locale.UK);
//            inputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
            outputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
            String str = null;
            try {
                Date date = inputFormat.parse(inputDate);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }
        return null;
    }

    public static Date convertToDate(String dueDateString) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        inputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
        try {
            date = inputFormat.parse(dueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date convertToDateNoTime(String dueDateString) {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        inputFormat.setTimeZone(TimeZone.getTimeZone(UK_TIME_ZONE));
        try {
            date = inputFormat.parse(dueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCommaSeparatedString(List<?> objectList) {
        return objectList.toString().replace(", ", ",").replaceAll("[\\[.\\]]", "");
    }

    public static List<String> getListFromCommaString(String commaString) {
        try {
            if (!commaString.isEmpty()) {
                String[] stringArray = commaString.split(",");
                for (int i = 0; i < stringArray.length; i++) {
                    stringArray[i] = stringArray[i].trim().replaceAll("[\\[.\\].\"]", "");
                }
                return Arrays.asList(stringArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static RectF calculateRectOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight());
    }

    public static RectF calculateRectInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new RectF(location[0], location[1], location[0] + view.getMeasuredWidth(),
                location[1] + view.getMeasuredHeight());
    }

    public static ArrayList<Integer> getAllIcons() {
        Integer[] icons =
                {R.mipmap.page, R.mipmap.aac, R.mipmap.ai, R.mipmap.aiff,
                        R.mipmap.avi, R.mipmap.bmp, R.mipmap.cpp, R.mipmap.cpp,
                        R.mipmap.css, R.mipmap.csv, R.mipmap.dat, R.mipmap.dmg,
                        R.mipmap.doc, R.mipmap.docx, R.mipmap.dotx, R.drawable.ic_dwg_blue,
                        R.mipmap.dxf, R.mipmap.eps, R.mipmap.exe, R.mipmap.flv,
                        R.mipmap.gif, R.mipmap.gz, R.mipmap.h, R.mipmap.hpp,
                        R.mipmap.html, R.mipmap.ic_ics, R.mipmap.ifc, R.mipmap.iso,
                        R.mipmap.java, R.mipmap.key, R.mipmap.mid, R.mipmap.mp3,
                        R.mipmap.mp4, R.mipmap.odf, R.mipmap.ods, R.mipmap.odt,
                        R.mipmap.otp, R.mipmap.ots, R.mipmap.ott, R.drawable.ic_pdf_red,
                        R.mipmap.psd, R.mipmap.php, R.mipmap.ppt, R.mipmap.py,
                        R.mipmap.qt, R.mipmap.rar, R.mipmap.rb, R.mipmap.rtf,
                        R.mipmap.rvt, R.mipmap.sql, R.mipmap.tga, R.mipmap.tgz,
                        R.mipmap.tiff, R.mipmap.txt, R.mipmap.wav, R.mipmap.xls,
                        R.mipmap.xlsx, R.mipmap.xml, R.mipmap.yml, R.mipmap.zip,
                        R.mipmap.jpg, R.mipmap.jpeg, R.mipmap.png};
        return new ArrayList<>(Arrays.asList(icons));
    }

    public static ArrayList<String> getAllExtensions() {
        String[] name = {".page", ".aac", ".ai", ".aiff",
                ".avi", ".bmp", ".c", ".cpp", ".css", ".csv", ".dat", ".dmg",
                ".doc", ".docx", ".dotx", ".dwg", ".dxf", ".eps", ".exe", ".flv",
                ".gif", ".gz", ".h", ".hpp", ".html", ".ics", ".ifc", ".iso",
                ".java", ".key", ".mid", ".mp3", ".mp4", ".odf", ".ods", ".odt",
                ".otp", ".ots", ".ott", ".pdf", ".psd", ".php", ".ppt", ".py",
                ".qt", ".rar", ".rb", ".rtf", ".rvt", ".sql", ".tga", ".tgz",
                ".tiff", ".txt", ".wav", ".xls", ".xlsx", ".xml", ".yml", ".zip", ".jpg", ".jpeg", ".png"};
        return new ArrayList<>(Arrays.asList(name));
    }

    public static int getFileImageFromName(String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!extension.isEmpty() && CommonMethods.getAllExtensions().contains(extension)) {
                int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                return CommonMethods.getAllIcons().get(extensionIndex);
            } else {
                return R.drawable.ic_otf;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.ic_otf;
        }
    }

    public static boolean isPreviewAvailable(String fileName) {
        try {
            Log.e("preview file name", fileName);
            String extension = fileName.substring(fileName.lastIndexOf("."));
            if (!extension.isEmpty()) {
                switch (extension.toLowerCase()) {
                    case ".png":
                    case ".jpg":
                    case ".jpeg":
                    case ".pdf":
                    case ".dwg":
                        return true;
                    default:
                        return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isImageUrl(String name) {
        String extension = name.substring(name.lastIndexOf("."));
        return extension.toLowerCase().equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg");
    }

    public static boolean isDWG(String name) {
        String extension = name.substring(name.lastIndexOf("."));
        return !extension.isEmpty() && extension.toLowerCase().equalsIgnoreCase(".dwg");
    }

    public static void checkVisiblePermission(String permission, View view) {
        if (null != permission) {
            view.setVisibility(permission.equalsIgnoreCase("on") ? View.VISIBLE : View.GONE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static boolean isPermissionGranted(String permission) {
        if (null != permission) {
            return permission.equalsIgnoreCase("on");
        } else {
            return false;
        }
    }

    public static boolean isFileNullOrEmpty(String fileLocation) {
        return fileLocation == null || fileLocation.isEmpty();
    }

    /* distance between locations */
    public static String distance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        distance = Math.sqrt(distance);
        return String.valueOf(distance);
    }

    public static Double distanceProject(double lat1, double lat2, double lon1, double lon2) {
        try {
            double distance = Math.round(((Math.acos(
                    Math.sin((lat1 * Math.PI / 180)) * Math.sin((lat2 * Math.PI / 180)) + Math.cos(
                            (lat1 * Math.PI / 180)) * Math.cos((lat2 * Math.PI / 180)) * Math.cos(
                            ((lon2 - lon1) * Math.PI / 180)))) * 180 / Math.PI) * 60 * 1.1515 * 1.609344);
            DecimalFormat df = new DecimalFormat("#.###");
            return Double.valueOf(df.format(distance));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

    public static float locationDistance(Location userLocation, Location projectLocation) {
        float distance = userLocation.distanceTo(projectLocation);
        return distance;
    }

    public static void downloadFilePermission(String downloadUrl, String description, Context context) {
        if (isNetworkAvailable(context)) {
            if (CommonMethods.hasMarshmallow()) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        CommonMethods.displayToast(context, context.getResources().getString(
                                R.string.storage_permission_required));

                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                0);
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                0);
                    }
                } else {
                    downloadWithPermission(downloadUrl, description, context);
                }
            } else {
                downloadWithPermission(downloadUrl, description, context);
            }
        } else {
            displayToast(context, context.getString(R.string.no_internet_connection));
        }
    }

    public static void downloadWithPermission(String downloadUrl, String description, Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setDescription(
                description);   //appears the same in Notification bar while downloading
        request.setTitle(description);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, description);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    public static boolean isFileDownloaded(String fileName) {
        File extStore = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File file = new File(extStore.getAbsolutePath() + "/" + fileName);
        return file.exists();
    }

    public static boolean isNetworkError(Throwable throwable) {
        return throwable instanceof UnknownHostException;
    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
//        Log.e("Hash password", password);
        return password;
    }

    public static String filteredMetaDataString(List<LocalMetaValues> metaData) {
        List<LocalMetaValues> filteredList = new ArrayList<>();
        for (LocalMetaValues value : metaData) {
            if (!value.getAttachment()) {
                filteredList.add(value);
            }
        }
        return new Gson().toJson(filteredList);
    }

    public static File setUpImageFile(String directory) throws IOException {
        File imageFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File storageDir = new File(directory);
            if (null != storageDir) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
            imageFile = File.createTempFile("IMG_"
                            + System.currentTimeMillis() + "_",
                    ".jpg", storageDir);
        }
        return imageFile;
    }

    public static boolean isNotEmptyArray(String docArray) {
        return !docArray.isEmpty() && !docArray.equals("[]");
    }

    public static String getRetrofitException(Throwable t) {
        if (t instanceof SocketTimeoutException) {
            return "Time Out Exception";
        } else {
            return t.getLocalizedMessage();
        }
    }

    public static void setTextAppearance(Context context, int resId, TextView tv) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            tv.setTextAppearance(context, resId);
        } else {
            tv.setTextAppearance(resId);
        }
    }

    public static List<DashBoardMenuModel> getMenuList() {
        List<DashBoardMenuModel> menuModelList = new ArrayList<>();
        menuModelList.add(new DashBoardMenuModel(0, R.drawable.icon_signin, "Sign In", "signinginout", "#42B24C", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(1, R.drawable.ic_drawings, "Drawings", "drawings", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(2, R.drawable.ic_camera, "Photo Gallery", "photo_Gallery", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(3, R.drawable.ic_book, "Daily Diary", "daily_diaries", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(4, R.drawable.ic_rfi, "RFIS", "rfi", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(5, R.drawable.ic_snag, "Snag List", "defect_report", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(6, R.drawable.ic_cube, "Digital Documents", "custom_documents", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(7, R.drawable.ic_menu_time_sheet, "TimeSheets", "timesheets_new", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(8, R.drawable.ic_company_doc, "Company Documents", "cloud_storage", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(9, R.drawable.ic_project_doc, "Project Documents", "documents", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(10, R.drawable.ic_todo, "TO DO(s)", "todos", "#428bca", "", "", ""));
        menuModelList.add(new DashBoardMenuModel(11, R.drawable.ic_proj_management, "Project Management", "projects", "#428bca", "", "", ""));
        return menuModelList;
    }

    public static boolean isArrayEmpty(int[] array) {
        for (int count : array) {
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInValidUrl(String workspace) {
        return workspace.contains(".") && workspace.lastIndexOf(".") == (workspace.length() - 1);
    }

    /**
     * Check External memory
     */
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Calculate Free(Available) internal memory Size of Device
     */
    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    /**
     * Calculate Total internal memory Size of Device
     */
    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    /**
     * Calculate Free(Available) external memory Size of Device
     */
    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    /**
     * Calculate Total external memory Size of Device
     */
    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return formatSize(totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    private static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static String readableFileSize() {
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            availableSpace = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        else
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

        if (availableSpace <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(availableSpace) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(
                availableSpace / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static boolean isLocationEnable(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gps_enabled && network_enabled;
    }

    public static List<DashBoardMenuModel> getTimeSheetMenu() {
        List<DashBoardMenuModel> menuList = new ArrayList<>();
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_OVERVIEW, R.drawable.ic_calendar, "Overview", "Overview"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_WORK_TIME, R.drawable.ic_worktime, "Worktime", "Worktime"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_TRAVEL, R.drawable.ic_travel, "Travel", "Travel"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_BREAKS, R.drawable.ic_breaks, "Breaks", "Breaks"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_EXPENSES, R.drawable.ic_expenses, "Expenses", "Expenses"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_PRICE_WORK, R.drawable.ic_pound, "Pricework", "Pricework"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_HOLIDAY, R.drawable.ic_holiday, "Holiday", "Holiday"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_SICK, R.drawable.ic_sick, "Sick", "Sick"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_NOTES, R.drawable.ic_menu_time_sheet, "Notes", "Notes"));
        menuList.add(new DashBoardMenuModel(DataNames.TIME_SHEET_OTHER, R.drawable.ic_other, "Other", "Other"));
        return menuList;
    }

    public static List<String> getOtherOptionList() {
        List<String> otherOptionList = new ArrayList<>();
        otherOptionList.add("Bonus overtime $25");
        otherOptionList.add("Electrical Installation  $200.50");
        otherOptionList.add("DV $20");
        otherOptionList.add("test $50");
        otherOptionList.add("Test Meta $5");
        otherOptionList.add("Bonus overtime 23 $2500");
        otherOptionList.add("Bonus overtime 3 $251");
        return otherOptionList;
    }

    public static boolean isEmptyList(String breakTimeList) {
        return breakTimeList.isEmpty() || breakTimeList.equalsIgnoreCase("[]");
    }

    public static String[] getWholeWeekDays(String inputDate) {
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat(DF_2).parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days[i] = getDateFormat(DF_11).format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.e("Week Data", new Gson().toJson(days));
        return days;
    }

    public static List<String> getWholeWeekList(String inputDate) {
        List<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat(DF_2).parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

//        String[] days = new String[7];
        for (int i = 0; i < 7; i++) {
            days.add(getDateFormat(DF_11).format(calendar.getTime()));
//            days[i] = getDateFormat(DF_11).format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.e("Week Data", new Gson().toJson(days));
        return days;
    }

    public static String weeklySwitch(String date, boolean status) {
        DateFormat format = getDateFormat(DF_8);
        DateFormat sdf = getDateFormat(DF_8);
        Calendar calendar = Calendar.getInstance();
        Date parseDate = null;
        try {
            parseDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parseDate);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (status) {
            calendar.add(Calendar.DAY_OF_MONTH, 7);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        }
        return format.format(calendar.getTime());
    }

    public static String daySwitch(String date, boolean status) {
        DateFormat format = getDateFormat(DF_13);
        DateFormat sdf = getDateFormat(DF_13);
        Calendar calendar = Calendar.getInstance();
        Date parseDate = null;
        try {
            parseDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parseDate);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (status) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return format.format(calendar.getTime());
    }

    public static String daySwitch(String date, boolean status, String iFormat, String oFormat) {
        DateFormat toFormat = getDateFormat(oFormat);
        DateFormat fromFormat = getDateFormat(iFormat);
        Calendar calendar = Calendar.getInstance();
        Date parseDate = null;
        try {
            parseDate = fromFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parseDate);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (status) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return toFormat.format(calendar.getTime());
    }

    public static void setLogs(String tag, String methodName, String message) {
        ClientLogger.d(tag, "------------------------------------------------------------------------");
        ClientLogger.d(tag, String.format("---------------------------- ON  %s ----------------------------",
                methodName));
        ClientLogger.d(tag, message);
    }

    public static Integer getTimeInMinutes(String startTime, String endTime) {
        try {
            Date endDate = getDateFormat(DF_9).parse(endTime);
            Date startDate = getDateFormat(DF_9).parse(startTime);
            long difference = endDate.getTime() - startDate.getTime();

            return Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(difference)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer convertMinutesToHours(Long minutes) {
        try {
            return Long.valueOf(TimeUnit.MINUTES.toHours(minutes)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }
    public static Integer convertHoursToMinutes(Integer hours) {
        try {
            return Long.valueOf(TimeUnit.HOURS.toMinutes(hours)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static String convertMinToHours(Integer minutes) {
        if (minutes >= 0) {
            int hours = minutes / 60; //since both are ints, you get an int
            int mins = minutes % 60;
            return (hours < 10 ? "0" + hours : hours) + ":" + (mins < 10 ? "0" + mins : mins);
        } else {
            return "00:00";
        }
    }

    public static long daysBetween(Date one, Date two) {
        long difference = (one.getTime() - two.getTime()) / 86400000;
        return Math.abs(difference);
    }

    /**
     * Method for displaying Logs. Set "isDebuggerOn" to false if you do not
     * want to display Logs else set it to true
     *
     * @param tag     Tag of the Log
     * @param message message to be displayed in Log
     * @author anilsinghania 08/08/2016
     */
    public static void myLog(String tag, String message) {
        boolean isDebuggerOn = true;
        if (isDebuggerOn) {
            try {
                Log.d(tag, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for displaying data on web browser
     *
     * @param context application/activity context
     * @param url     url to be displayed in browser
     * @author anilsinghania 09/08/2016
     */
    public static void openUrl(Context context, String url) {
        if (isNetworkAvailable(context)) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            displayToast(context, context.getResources().getString(R.string.no_internet_connection));
        }
    }

    public static String getVideoThumbNail(String url) {
        String videoId = getVideoId(url);
        return "https://img.youtube.com/vi/" + videoId + "/0.jpg";
//        return "http://img.youtube.com/vi/" + videoId + "/default.jpg";
    }

    public static String getVideoId(String url) {
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        String videoId = null;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }

    public static boolean validRecurringCondition(String completedDate, String recurrenceType) {
        long days;
        if (completedDate != null && !completedDate.isEmpty() && recurrenceType != null && !recurrenceType.isEmpty()) {
            DateFormat commonFormat = CommonMethods.getDateFormat(CommonMethods.DF_5);
            try {
                days = CommonMethods.daysBetween(CommonMethods.getTodayDate(CommonMethods.DF_5),
                        commonFormat.parse(completedDate));
//                0=none, 1=Daily, 2=Weekly, 3=Monthly, 4=Yearly
                switch (recurrenceType) {
                    case "1":
                        return days >= 1;
                    case "2":
                        return days >= 7;
                    case "3":
                        return days >= 30;
                    default:
                        return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static List<NotificationIconModel> getNotificationIcon() {
        List<NotificationIconModel> model = new ArrayList<>();
        model.add(new NotificationIconModel(R.drawable.icon_signin_black, "Sign In"));
        model.add(new NotificationIconModel(R.drawable.ic_drawings_black, "Drawing"));
        model.add(new NotificationIconModel(R.drawable.ic_camera_black, "Photo Gallery"));
        model.add(new NotificationIconModel(R.drawable.ic_book_black, "Daily Diary"));
        model.add(new NotificationIconModel(R.drawable.ic_rfi_black, "RFI"));
        model.add(new NotificationIconModel(R.drawable.ic_snag_black, "Snag List"));
        model.add(new NotificationIconModel(R.drawable.ic_cube_black, "Digital Documents"));
        model.add(new NotificationIconModel(R.drawable.ic_menu_time_sheet_black, "TimeSheets"));
        model.add(new NotificationIconModel(R.drawable.ic_company_doc_black, "Cloud Storage"));
        model.add(new NotificationIconModel(R.drawable.ic_project_doc_black, "Document"));
        model.add(new NotificationIconModel(R.drawable.ic_todo_black, "TO DO(s)"));
        model.add(new NotificationIconModel(R.drawable.ic_allocation_black, "Allocation"));
        model.add(new NotificationIconModel(R.drawable.ic_assetmanager_black, "Asset manager"));
        model.add(new NotificationIconModel(R.drawable.ic_fleetmanager_black, "Fleet manager"));
        model.add(new NotificationIconModel(R.drawable.ic_holiday_black, "Holiday"));
        return model;
    }

    public static String timesheetViewType(String type) {
        String viewType;
        switch (type) {
            case "Holiday":
                viewType = "2";
                break;
            case "Sick":
                viewType = "3";
                break;
            case "Pricework":
            case "Travel":
            case "Worktime":
                viewType = "4";
                break;
            default:
                viewType = "0";
                break;
        }
        return viewType;
    }

    public static String timeDifference(String startTime, String finishTime) {
        long diffInHours = 0;
        DateFormat startFormat = getDateFormat(DF_9);
        DateFormat endFormat = getDateFormat(DF_9);
        try {
            Date startDate = startFormat.parse(startTime);
            Date endDate = endFormat.parse(finishTime);
            diffInHours = endDate.getTime() - startDate.getTime();
//            diffInHours = startDate.getTime() - endDate.getTime();
            diffInHours = TimeUnit.MILLISECONDS.toMinutes(diffInHours);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long hours = diffInHours / 60;
        long minutes = diffInHours % 60;
        return String.format(Locale.getDefault(), "%d.%02d", hours, minutes);
    }


    public static long timeDifferenceMins(String startTime, String finishTime) {
        long diffInHours = 0;
        DateFormat startFormat = getDateFormat(DF_9);
        DateFormat endFormat = getDateFormat(DF_9);
        try {
            Date startDate = startFormat.parse(startTime);
            Date endDate = endFormat.parse(finishTime);
            diffInHours = endDate.getTime() - startDate.getTime();
            diffInHours = TimeUnit.MILLISECONDS.toMinutes(diffInHours);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffInHours;
    }


    public static String convertMinToHours(long minutes) {
        long hours = TimeUnit.MINUTES.toHours(minutes);
        long mins = minutes % 60;
        String hourString = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minString = mins < 10 ? "0" + mins : String.valueOf(mins);
        return String.format(Locale.getDefault(), "%s:%s", hourString, minString);
//        return hours == 0 && mins == 0 ? "00:00" : String.format(Locale.getDefault(), "%2d:%2d", hours, mins);

    }

    public static String getSWHours(long minutes) {
        long hours = TimeUnit.MINUTES.toHours(minutes);
        return String.format(Locale.getDefault(), "%d", hours);

    }

    public static Integer getHoursToMin(Integer hour) {
        try {
            if (hour != null) {
                Long longValue = Long.valueOf(hour);
                long minutes = TimeUnit.HOURS.toMinutes(longValue);
                return (int) minutes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String[] getTodayWorkingHours(List<GeneralSettingsDatum> generalSettingsData) {
        String[] time = null;
        String currentDay = getDateFormat(DF_17).format(new Date());
        switch (currentDay.toLowerCase()) {
            case "sun":
                time = findWorkTime(generalSettingsData, DataNames.WH_SUN_ST, DataNames.WH_SUN_ED);
                break;
            case "mon":
                time = findWorkTime(generalSettingsData, DataNames.WH_MON_ST, DataNames.WH_MON_ED);
                break;
            case "tue":
                time = findWorkTime(generalSettingsData, DataNames.WH_TUE_ST, DataNames.WH_TUE_ED);
                break;
            case "wed":
                time = findWorkTime(generalSettingsData, DataNames.WH_WED_ST, DataNames.WH_WED_ED);
                break;
            case "thu":
                time = findWorkTime(generalSettingsData, DataNames.WH_THU_ST, DataNames.WH_THU_ED);
                break;
            case "fri":
                time = findWorkTime(generalSettingsData, DataNames.WH_FRI_ST, DataNames.WH_FRI_ED);
                break;
            case "sat":
                time = findWorkTime(generalSettingsData, DataNames.WH_SAT_ST, DataNames.WH_SAT_ED);
                break;
        }
        Log.e("time array", new Gson().toJson(time));
        return time;

    }

    private static String[] findWorkTime(List<GeneralSettingsDatum> gs, String whSt, String whEnd) {
        String[] time = new String[2];
        for (GeneralSettingsDatum data : gs) {
            if (data.getName().equalsIgnoreCase(whSt)) {
                time[0] = data.getValue();
            }
            if (data.getName().equalsIgnoreCase(whEnd)) {
                time[1] = data.getValue();
            }
        }
        return time;
    }
}

