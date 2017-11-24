package com.itgoyo.retrofit;

import android.app.Application;
import android.content.Context;

import com.itgoyo.retrofit.utils.Utils;

/**
 * Created by itgoyo
 */

public class App extends Application {
    private static App app;
    public static Context getAppContext() {
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        app=this;
    }
}
