package com.peter.practiceproject;

import android.app.Application;

import com.peter.practiceproject.studyitem.Hook.ActivityThreadHacker;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ActivityThreadHacker.hackSysHandlerCallback();
        ActivityThreadHacker.hackSysLooperLogging();
    }
}
