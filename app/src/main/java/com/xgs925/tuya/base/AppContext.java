package com.xgs925.tuya.base;

import android.app.Application;
import android.content.Context;

import com.xgs925.tuya.BuildConfig;


public class AppContext extends Application {
    public static final boolean isDebug = BuildConfig.LOG_DEBUG;
    public static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mApplicationContext = getApplicationContext();
    }

}
