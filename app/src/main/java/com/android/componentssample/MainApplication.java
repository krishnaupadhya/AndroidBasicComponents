package com.android.componentssample;

import android.app.Application;

/**
 * Created by Krishna Upadhya on 28/10/20.
 */
public class MainApplication extends Application {
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MainApplication getApplicationInstance() {
        return instance;
    }
}
