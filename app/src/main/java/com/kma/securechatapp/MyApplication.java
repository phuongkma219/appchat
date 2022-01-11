package com.kma.securechatapp;

import android.app.Application;

import com.kma.securechatapp.repository.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
