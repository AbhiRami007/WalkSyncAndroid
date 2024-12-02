package com.example.walksyncandroid.HomeScreens;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.walksyncandroid.R;

public class DailyReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent dashboardIntent = new Intent(context, Dashboard.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, dashboardIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setContentTitle("Daily Reminder")
                .setContentText("Stay active and track your steps!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}
