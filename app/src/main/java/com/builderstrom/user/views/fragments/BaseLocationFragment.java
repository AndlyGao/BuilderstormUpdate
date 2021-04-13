package com.builderstrom.user.views.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.builderstrom.user.repository.retrofit.modals.CurrentLocation;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static android.content.ContentValues.TAG;

public abstract class BaseLocationFragment extends BaseFragment {
    private static final int LOCATION_REQUEST_CODE = 299;
    private static final long INTERVAL = 30 * 1000;
    private static final long FASTEST_INTERVAL = 5 * 1000;
    public PermissionUtils permissionUtils;
    private FusedLocationProviderClient providerClient;
    private LocationCallback mCallback;
    private LocationRequest mLocationRequest;
    private Location currentLocation;
    private boolean clicked = false;


    void callLocationClient(boolean btnClicked) {
        this.clicked = btnClicked;
        if (getActivity() != null) {
            if (providerClient == null) {
                providerClient = LocationServices.getFusedLocationProviderClient(getActivity());
            }

            showLocationConfirmPopup();

            /* location callback */
            mCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null) {
                        for (Location location : locationResult.getLocations()) {
                            currentLocation = location;
                        }
                        if (clicked && currentLocation != null) {
                            BuilderStormApplication.mPrefs.saveCurrentLocation(new CurrentLocation(
                                    String.valueOf(currentLocation.getLatitude()),
                                    String.valueOf(currentLocation.getLongitude())));
                            Log.e("location received", "true");
                            onLocationReceived(currentLocation);
                            if (providerClient != null) {
                                providerClient.removeLocationUpdates(mCallback);
                                clicked = false;
                            }
                        }

                    }
                }
            };
        }

    }

    private void showLocationConfirmPopup() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> {
                    callLocationUpdates();
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                startIntentSenderForResult(rae.getResolution().getIntentSender(),
                                        LOCATION_REQUEST_CODE, null, 0,
                                        0, 0, null);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                                break;
                            }
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                    }
                });

    }

    @SuppressLint("MissingPermission")
    private void callLocationUpdates() {
        if (getPermissionUtils().isPermissionGrantedForLocation()) {
            providerClient.requestLocationUpdates(mLocationRequest, mCallback, null);
        } else {
            getPermissionUtils().requestFragmentPermissionForLocation();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLocationUpdates();
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
                    // All required changes were successfully made
                    CommonMethods.setLogs(TAG, "On Location Permission ", "Granted");
                    callLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
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
        return Settings.System.getInt(getActivity().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0;
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


}
