package com.android.componentssample.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Created by Krishna Upadhya on 28/10/20.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            if (!ScannerForegroundService.serviceRunning) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(context, new Intent(context, ScannerForegroundService.class));
                } else {
                    context.startService(new Intent(context, ScannerForegroundService.class));
                }
            }
        }

    }
}
