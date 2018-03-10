package ru.kvartira_omsk.realtorapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.view.ViewDebug;
import android.widget.Toast;

/**
 * Created by Иван on 15.01.2018.
 */
public class AlarmNotification extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Напоминание")
                .setContentText("Напоминание")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Info");*/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        //Toast.makeText( ,"IDNotification: " + Integer.toString(id), Toast.LENGTH_LONG).show();
        notificationManager.notify(id, notification);
        //notificationManager.notify(1, builder.build());
    }
}
