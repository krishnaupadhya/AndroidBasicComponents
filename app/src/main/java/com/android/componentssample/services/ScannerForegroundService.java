package com.android.componentssample.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.componentssample.Logger;
import com.android.componentssample.MainActivity;
import com.android.componentssample.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Krishna Upadhya on 28/10/20.
 */
public class ScannerForegroundService extends Service {

    public static final String NOTIFICATION_DESC = " FG service ";
    public static final String STOP_SERVICE = "STOP_SERVICE";
    public static final String START_SERVICE = "START_SERVICE";
    private static final int NOTIF_ID = 1955;
    public static final String NOTIFICATION_CHANNEL = "scanned_notification_channel";
    public static boolean serviceRunning;
    public static long serviceRunningTime = -60000;
    public static String serviceStartTime;
    private CountDownTimer timer;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Notification notification = getNotification(NOTIFICATION_DESC);
        startForeground(NOTIF_ID, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (STOP_SERVICE.equalsIgnoreCase(intent.getAction())) {
            stopScannerService();
            Logger.INSTANCE.logMsg("onStartCommand service stop");
        } else {
            serviceRunning = true;
            startTimer();
            Logger.INSTANCE.logMsg("onStartCommand service started");
        }
        return START_STICKY;
    }

    private void startTimer() {
        if (TextUtils.isEmpty(serviceStartTime)) {
            serviceStartTime = getStartedDate();
        }
        timer = new CountDownTimer(3600000, 60000) {
            @Override
            public void onTick(long millisUntilFinished) {
                serviceRunningTime += 60000;
                configureNotification(getServiceRunningDisplayTime(serviceRunningTime));
            }

            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }

    private String getStartedDate() {
        String pattern = "MM/dd HH:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public String getServiceRunningDisplayTime(long ms) {
        StringBuilder text = new StringBuilder("");
        if (ms > DAY) {
            text.append(ms / DAY).append(" days ");
            ms %= DAY;
        }
        if (ms > HOUR) {
            text.append(ms / HOUR).append(" hours ");
            ms %= HOUR;
        }
        if (ms > MINUTE) {
            text.append(ms / MINUTE).append(" minutes ");
            ms %= MINUTE;
        }
        if (ms > SECOND) {
            text.append(ms / SECOND).append(" seconds ");
        }
        return text.toString();
    }

    private void configureNotification(String serviceRunningDisplayTime) {
        Notification notification = getNotification(serviceStartTime + NOTIFICATION_DESC + serviceRunningDisplayTime);
        startForeground(NOTIF_ID, notification);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
            channel.enableLights(false);
            channel.setSound(null, null);
            channel.setShowBadge(false);

            channel.setDescription(NOTIFICATION_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private Notification getNotification(String notificationDescText) {
        Intent notificationIntent = new Intent(this, ServiceDemoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{notificationIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? NOTIFICATION_CHANNEL : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(getResources().getString(R.string.app_name));
        bigTextStyle.bigText(notificationDescText);
        return notificationBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(notificationDescText)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSmallIcon(R.drawable.notification_icon)
                .build();
    }

    @Override
    public void onDestroy() {
        Logger.INSTANCE.logMsg("onDestroy");
        super.onDestroy();
        stopScannerService();
    }

    private void stopScannerService() {
        if (timer != null)
            timer.cancel();
        serviceRunning = false;
        serviceStartTime="";;
        serviceRunningTime = -60000;
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onLowMemory() {
        Logger.INSTANCE.logMsg("onLowMemory");
        super.onLowMemory();
        stopScannerService();
    }

}
