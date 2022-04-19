package com.noor.mystore99.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.noor.mystore99.R;

import androidx.annotation.RequiresApi;

public class NotificationHelper extends ContextWrapper {
    private static final String CHANEL_ID="com.noor.mystore99.NOTI";
    private static final String CHANEL_NAME="SABZI TAZA";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O);
        createChannel();
    }


    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel edmtChannel=new NotificationChannel(CHANEL_ID,CHANEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            edmtChannel.enableLights(true);
            edmtChannel.enableVibration(true);
            edmtChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(edmtChannel);
        }
    }
    private NotificationManager getManager(){
        if(manager==null)
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getSabziTazaChannalNotification(String title, String body, PendingIntent contentIntent, Uri sounduri){
        return new Notification.Builder(getApplicationContext(),CHANEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(sounduri)
                .setAutoCancel(false);
    }
}
