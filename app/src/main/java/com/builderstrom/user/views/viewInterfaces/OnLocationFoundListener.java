package com.builderstrom.user.views.viewInterfaces;

/**
 * Created by anil_singhania on 05-06-2017.
 */

public interface OnLocationFoundListener {
    void onLocationFound(double latitude, double longitude);

    void onLocationNotFound();

}
