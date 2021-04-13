package com.builderstrom.user.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.builderstrom.user.R;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

public class PermissionUtils {
    public static final int CAMERA_REQUEST = 222;
    public static final int STORAGE_REQUEST = 111;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 333;

    private Activity activity;
    private OnCameraAndStorageGrantedListener listener;
    private Fragment fragment;

    public PermissionUtils(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public PermissionUtils(Activity context) {
        activity = context;
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.CAMERA)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    CommonMethods.displayToast(activity, activity.getResources().getString(R.string.camera_permission_required));

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST);

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQUEST);
                }
            } else {

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        CommonMethods.displayToast(activity, activity.getResources().getString(R.string.storage_permission_required));

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                STORAGE_REQUEST);
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_REQUEST);
                    }
                } else {
                    listener.onPermissionsGranted();
                }
            }
        } else {
            listener.onPermissionsGranted();
        }
    }

    public void setListener(OnCameraAndStorageGrantedListener listener) {
        this.listener = listener;
    }

    public void verifyResults(int requestCode, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                listener.onPermissionRefused(activity.getResources().getString(R.string.camera_permission_denied));
            } else {
                checkPermissions();
            }
        } else if (requestCode == STORAGE_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                listener.onPermissionRefused(activity.getResources().getString(R.string.storage_permission_denied));
            } else {
                listener.onPermissionsGranted();
            }
        }

    }

    public boolean isPermissionGrantedForLocation() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }


    public void requestPermissionForLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlertDialog(activity.getString(R.string.location_permission_needed),
                    (dialog, which) -> ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE), null);

        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestFragmentPermissionForLocation() {
        if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showAlertDialog(fragment.getString(R.string.location_permission_needed),
                    (dialog, which) -> fragment.requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE), null);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }


    }


    /* storage permissions */

    public boolean isPermissionGrantedForExtStorage() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    public void requestPermissionForExtStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showAlertDialog(activity.getString(R.string.storage_permission_needed),
                    (dialog, which) -> ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_REQUEST), null);

        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST);
        }
    }

    public void requestFragmentPermissionForExtStorage() {
        if (fragment.shouldShowRequestPermissionRationale(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showAlertDialog(fragment.getString(R.string.storage_permission_needed),
                    (dialog, which) -> fragment.requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_REQUEST), null);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST);
        }
    }


    private void showAlertDialog(String message, DialogInterface.OnClickListener okListener,
                                 DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(activity.getString(R.string.ok), okListener)
                .setNegativeButton(activity.getString(R.string.cancel), cancelListener)
                .create()
                .show();
    }

}
