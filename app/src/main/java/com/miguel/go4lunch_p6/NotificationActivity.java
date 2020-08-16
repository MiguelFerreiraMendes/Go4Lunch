package com.miguel.go4lunch_p6;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    public static final String CHANNEL_1_ID = "channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationactivity);
        ToggleButton button = findViewById(R.id.toggleButton4);
        mSharedPreferences = this.getSharedPreferences("notifications", MODE_PRIVATE);

        if (mSharedPreferences.getString("notif", "def").equals("def")){
            button.setChecked(false);
        }

        if (mSharedPreferences.getString("notif", "def").equals("false")){
            button.setChecked(false);
        }
        if (mSharedPreferences.getString("notif", "def").equals("true")){
            button.setChecked(true);
        }
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startAlarm();
                    mSharedPreferences.edit().putString("notif", "true").apply();
                    createNotificationChannels();
                    Toast.makeText(NotificationActivity.this, R.string.notifON, Toast.LENGTH_SHORT).show();

                } else {
                    startAlarm();
                    mSharedPreferences.edit().putString("notif", "false").apply();
                    Toast.makeText(NotificationActivity.this, R.string.notifOFF, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void startAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel chanel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription("Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(chanel1);
        }
    }
}
