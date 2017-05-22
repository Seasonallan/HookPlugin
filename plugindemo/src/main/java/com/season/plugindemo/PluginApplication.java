package com.season.plugindemo;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-16 10:07
 */
public class PluginApplication extends Application {

    public static int level;

    @Override
    public void onCreate() {
        super.onCreate();

        level = 0;

        Log.e("SeasonLog", "PluginApplication onCreate");

        File file = getFilesDir();
        Log.e("SeasonLog", "FileDir = " + file.getAbsolutePath());
        Log.e("SeasonLog", "FileDir exists = " + file.exists());

        Fresco.initialize(this);

    }
}
