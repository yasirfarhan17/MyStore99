package com.noor.mystore99;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String Notification_channel_id="MY_NOTIFICATION_ID";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        String notificationType=remoteMessage.getData().get("notificationType");
        if(notificationType.equals("NewOrder")){
            String buyerUid=remoteMessage.getData().get("buyerUid");
            String sellerUid=remoteMessage.getData().get("sellerUid");
            String orderId=remoteMessage.getData().get("orderId");
            String notificationTitle=remoteMessage.getData().get("notificationTitle");
            String notificationDescription=remoteMessage.getData().get("notificationMessage");

            if(firebaseAuth!=null && firebaseAuth.getUid().equals(sellerUid)){
                showNotification(orderId,sellerUid,buyerUid,notificationTitle,notificationDescription,notificationType);
            }
        }
    }
    private void  showNotification(String orderId,String sellerUid,String buyerUid,String notification,String notificationDescription,String notificationType){
        NotificationManager notificationManager=(NotificationManager)getSystemService((Context.NOTIFICATION_SERVICE));
        int notificationId=new Random().nextInt(3000);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            setupNoricationChannel(notificationManager);
        }

        Intent intent;
        if(notificationType.equals("NewOrder")){

        }
        Bitmap largeIcon= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Uri notificationSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,Notification_channel_id);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationDescription)
                .setSound(notificationSound)
                .setAutoCancel(true);

        notificationManager.notify(notificationId,notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNoricationChannel(NotificationManager notificationManager) {
        CharSequence channelName="Some Sample Text";
        String channelDescription="Channel Description here";
        NotificationChannel notificationChannel=new NotificationChannel(Notification_channel_id,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        if(notificationManager!=null){
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}
