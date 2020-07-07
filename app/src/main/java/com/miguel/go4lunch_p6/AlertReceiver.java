package com.miguel.go4lunch_p6;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;


    }

    private void updateNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("mychannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(mContext, "mychannel")
                .setContentTitle("Almost Lunch time !")
                .setContentText("Your lunch : Blabla ")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
        notificationManager.notify(1, notification);
    }
}




