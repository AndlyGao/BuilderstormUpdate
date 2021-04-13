package com.builderstrom.user.views.offlineToOnline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.repository.retrofit.api.DataNames;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static boolean firstConnect = true;

    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isActiveInternet(context)) {
            Log.e("network", "connected");
            if (firstConnect) {
                new SyncCompleteDatabase(context).executeSyncing();
                firstConnect = false;
            }

        } else {
            firstConnect = true;
            Log.e("network", "Disconnected");
            LocalBroadcastManager.getInstance(context).sendBroadcast(
                    new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE).putExtra("KEY_FLAG", true));
        }

        callnetworkBroadcast(context);

    }


    private boolean isActiveInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void callnetworkBroadcast(Context context) {
        Intent intent = new Intent(DataNames.INTENT_ACTION_UPDATE_NETWORK);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
