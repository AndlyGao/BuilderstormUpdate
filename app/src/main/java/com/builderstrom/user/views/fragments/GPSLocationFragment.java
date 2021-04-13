package com.builderstrom.user.views.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.builderstrom.user.repository.retrofit.modals.CurrentLocation;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

public abstract class GPSLocationFragment extends BaseFragment implements LocationListener {
    private static final int LOCATION_REQUEST_CODE = 299;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute
    public PermissionUtils permissionUtils;
    private Location currentLocation;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                errorMessage("Location Permission needed to sign in.", null, false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    CommonMethods.setLogs(TAG, "On Location Permission ", "Granted");
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    CommonMethods.setLogs(TAG, "On Location Permission ", "Cancelled");
                    errorMessage("Location permissions are required", null, false);
                    break;
                default:
                    break;
            }
        }
    }

    public PermissionUtils getPermissionUtils() {
        if (permissionUtils == null) {
            permissionUtils = new PermissionUtils(this);
        }
        return permissionUtils;
    }

    public abstract void onLocationReceived(Location location);

    protected boolean isAirplaneModeOff() {
        if (getActivity() != null) {
            return Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) == 0;
        } else {
            return true;
        }
    }

    void showAirplaneDialog() {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Airplane mode is on. Please turn it off first.")
                    .setPositiveButton("SETTINGS", (dialog, which) -> {
                        dialog.dismiss();
                        sendUserToAirplaneSettings();
                    })
                    .setNegativeButton("CANCEL", null)
                    .create().show();
        }
    }


    @SuppressLint("MissingPermission")
    void getLocation() {
        try {
            if (getPermissionUtils().isPermissionGrantedForLocation()) {
                LocationManager locationManager = getActivity() == null ? null : (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                boolean isGPSEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    showSettingsAlert();
                } else {
                    if (null != locationManager) {

                        if (isNetworkEnabled) {
                            Log.d("Network", "Network");
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            updateLocation();


                        } else if (isGPSEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateLocation();
                        }
                    }
                }
            } else {
                getPermissionUtils().requestFragmentPermissionForLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLocation() {
        BuilderStormApplication.mPrefs.saveCurrentLocation(new CurrentLocation(
                String.valueOf(currentLocation.getLatitude()),
                String.valueOf(currentLocation.getLongitude())));
        Log.e("location received", currentLocation.getLatitude() + "true" + currentLocation.getLongitude());
        onLocationReceived(currentLocation);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void showSettingsAlert() {
        if (null != getActivity()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS is settings")
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                    .setPositiveButton("Settings", (dialog, which) -> {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show();
        }
    }

}
