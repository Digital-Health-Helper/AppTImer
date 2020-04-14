package com.example.readingcourse;

import android.app.Application;
import android.content.SharedPreferences;

public class AppTimer extends Application {
    public static SharedPreferences appTimerPreferences;
    public static SharedPreferences mostUsedAppsPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        appTimerPreferences = getSharedPreferences(getPackageName() + "_app_timer", MODE_PRIVATE);
        mostUsedAppsPreferences = getSharedPreferences(getPackageName()+"_most_used_app",MODE_PRIVATE);
    }
}
