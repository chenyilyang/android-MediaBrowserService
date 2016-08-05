package com.example.android.mediabrowserservice;

import android.app.Application;

/**
 * Created by chenyilyang on 2016/08/04.
 */

public class MyApplication extends Application {

    public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
