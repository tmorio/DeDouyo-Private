package com.example.capture;

import androidx.annotation.Nullable;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.widget.ImageView;

public class TestService extends Service  {
    private static final int REQUEST_MEDIA_PROJECTION = 1001;

    private MediaProjectionManager mpManager;
    private MediaProjection mProjection;
    private int displayWidth, displayHeight;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;
    private int screenDensity;
    private ImageView imageView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("debug", "onStartCommand()");

        int requestCode = intent.getIntExtra("REQUEST_CODE",0);
        Context context = getApplicationContext();
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification　Channel 設定
        NotificationChannel channel = new NotificationChannel(
                channelId, title , NotificationManager.IMPORTANCE_DEFAULT);

        if(notificationManager != null){
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    // android標準アイコンから
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .setContentText("サービス動作中")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();

            // startForeground
            startForeground(1, notification);

        }

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("debug", "onDestroy()");

        // Service終了
        stopSelf();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
