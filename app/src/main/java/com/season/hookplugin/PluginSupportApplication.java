package com.season.hookplugin;

import android.app.Application;

/**
 * Disc: 直接继承该Application或自行注册Hook
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-22 10:50
 */
public class PluginSupportApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        PluginHelper.getInstance().startPlugin(this);

    }
}
