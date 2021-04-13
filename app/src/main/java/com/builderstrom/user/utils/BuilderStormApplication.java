package com.builderstrom.user.utils;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.builderstrom.user.repository.Prefs;
import com.builderstrom.user.repository.database.DatabaseHelper;
import com.builderstrom.user.views.offlineToOnline.NetworkChangeReceiver;

public class BuilderStormApplication extends Application {
    public static Prefs mPrefs;
    public static DatabaseHelper mLocalDatabase;
    private static NetworkChangeReceiver mNetworkReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        if (null == mNetworkReceiver) {
            mNetworkReceiver = new NetworkChangeReceiver();
        }

        if (mPrefs == null) {
            mPrefs = Prefs.with(getApplicationContext());
        }
        if (null == mLocalDatabase) {
            mLocalDatabase = new DatabaseHelper(getApplicationContext());
        }

        /* for global receiver*/
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }



}
