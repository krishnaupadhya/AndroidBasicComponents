package com.android.componentssample.services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Krishna Upadhya on 28/10/20.
 */
public class BackgroundWorker extends Worker {

    public static final String UNIQUE_WORK_NAME = "com.android.componentssample.services.BackgroundWorker";
    private final Context mContext;

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if(!ScannerForegroundService.serviceRunning){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(mContext, new Intent(mContext, ScannerForegroundService.class));
            } else {
                mContext.startService(new Intent(mContext, ScannerForegroundService.class));
            }
        }
        return Result.success();
    }
}
