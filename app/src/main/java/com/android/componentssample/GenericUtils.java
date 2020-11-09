package com.android.componentssample;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.android.componentssample.services.BackgroundWorker;

import java.util.concurrent.TimeUnit;

/**
 * Created by Krishna Upadhya on 28/10/20.
 */

public class GenericUtils {

    public static void startBackgroundWorker() {
        WorkManager workManager = WorkManager.getInstance(MainApplication.getApplicationInstance());
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                BackgroundWorker.class,
                16,
                TimeUnit.MINUTES)
                .setInitialDelay(16, TimeUnit.MINUTES)
                .build();

        workManager.enqueueUniquePeriodicWork(
                BackgroundWorker.UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );
    }
}
