package com.season.hookplugin;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.season.pluginlib.PluginHelper;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-15 13:33
 */
public class SeasonApplication extends Application {

    public static Context sApplicationContext;
    public static Handler sHandler;

    @Override
    public void onCreate() {
        super.onCreate();

    //    File apkFile = new File(android.os.Environment.getExternalStorageDirectory(), "EnglishV13.0.4.apk");
    //    FileUtil.extractAssets(this, apkFile);
        sApplicationContext = this;
        sHandler = new Handler();
     //   HookUtil.hookSystemHandler(getFileStreamPath(assetsName).getAbsolutePath());
     //   HookUtil.hookAms();

        PluginHelper.getInstance().installHook(this,getClassLoader());

    //    HookUtil.hookActivityThreadInstrumentation();
    }

    public static void post(Runnable runnable){
        sHandler.post(runnable);
    }

}
