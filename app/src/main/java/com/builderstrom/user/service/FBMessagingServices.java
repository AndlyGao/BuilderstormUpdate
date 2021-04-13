package com.builderstrom.user.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.NotificationHelper;
import com.builderstrom.user.views.activities.PinLoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class FBMessagingServices extends FirebaseMessagingService {
    String TAG = FBMessagingServices.class.getName();
    NotificationHelper mNotificationHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Log.e(TAG, "Data_payload: " + new Gson().toJson(remoteMessage));
//        Log.e(TAG, "Data_payload_title: " + remoteMessage.getData().get("title"));
//        Log.e(TAG, "Data_payload_title: " + new Gson().toJson(remoteMessage.getNotification()));

        mNotificationHelper = new NotificationHelper(getApplication());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            mNotificationHelper.createNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"),remoteMessage.getData().get("pid"));
        }

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
////            sendNotification(remoteMessage.getNotification().getBody());
//            mNotificationHelper.createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
//        }
    }

    @Override public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        long REQUEST_CODE = System.currentTimeMillis();
        Intent intent = new Intent(this, PinLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("click_action", true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) REQUEST_CODE /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int drawable;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // only for KITKAT and newer versions
            drawable = R.mipmap.ic_launcher;   // transparent icon
        } else {
            drawable = R.mipmap.ic_launcher;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(drawable)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) REQUEST_CODE /* ID of notification */, notificationBuilder.build());
    }
}
