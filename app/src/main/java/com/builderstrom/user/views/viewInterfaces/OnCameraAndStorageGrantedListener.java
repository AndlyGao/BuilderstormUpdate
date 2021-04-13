package com.builderstrom.user.views.viewInterfaces;

/**
 * Created by anil_singhania on 16-10-2018.
 */

public interface OnCameraAndStorageGrantedListener {
    void onPermissionsGranted();

    void onPermissionRefused(String whichOne);
}
