package com.season.test;

import android.content.Context;
import android.os.Handler;

import com.season.hookplugin.PluginSupportApplication;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-15 13:33
 */
public class SeasonApplication extends PluginSupportApplication {

    public static Context sApplicationContext;
    public static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplicationContext = this;
        sHandler = new Handler();
    }

    public static void post(Runnable runnable){
        sHandler.post(runnable);
    }

}
