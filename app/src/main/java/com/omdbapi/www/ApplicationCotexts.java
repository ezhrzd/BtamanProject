package com.omdbapi.www;

import android.app.Application;
import android.content.Context;

public class ApplicationCotexts extends Application {

    public static Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }


}
