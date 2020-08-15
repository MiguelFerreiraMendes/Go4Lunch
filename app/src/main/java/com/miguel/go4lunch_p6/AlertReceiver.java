package com.miguel.go4lunch_p6;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.miguel.go4lunch_p6.api.UserHelper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private Context mContext;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        updateNotification();
    }

    private void updateNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("mychannel", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        UserHelper.getUsersCollection().document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String nameRestaurant = task.getResult().get("restaurantInteressed").toString();
                Notification notification = new NotificationCompat.Builder(mContext, "mychannel")
                        .setContentTitle("Almost Lunch time !")
                        .setContentText("Don't forget ! You're eating at : " + nameRestaurant + "")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .build();
                notificationManager.notify(1, notification);

                UserHelper.getUsersCollection().document(userID).update("restaurantInteressed", "false");

            }
        });
    }
}




