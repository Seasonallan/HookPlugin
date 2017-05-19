package com.season.pluginlib;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.season.pluginlib.binder.IWindowManagerBinderHook;
import com.season.pluginlib.hook.ActivityHook;
import com.season.pluginlib.hook.CallbackHook;
import com.season.pluginlib.hook.Hook;
import com.season.pluginlib.hook.IPackageManagerHook;
import com.season.pluginlib.hook.InstrumentationHook;
import com.season.pluginlib.util.LogPlugin;
import com.season.pluginlib.util.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-17 09:19
 */
public class PluginHelper  implements ServiceConnection {

    private static PluginHelper sInstance = null;

    private PluginHelper() {
    }

    public static final PluginHelper getInstance() {
        if (sInstance == null) {
            sInstance = new PluginHelper();
        }
        return sInstance;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        setHookEnable(true, true);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }


    public final void onCallApplicationOnCreate(Context context, Application app) {
      //  installHook(new SQLiteDatabaseHook(context), app.getClassLoader());
    }

    public void installHook(Context context, ClassLoader classLoader){

        installHook(new IPackageManagerHook(context), classLoader);
        installHook(new ActivityHook(context), classLoader);
        installHook(new CallbackHook(context), classLoader);


        installHook(new InstrumentationHook(context), classLoader);
        installHook(new IWindowManagerBinderHook(context), classLoader);


        try {
            if (PluginProcessManager.isPluginProcess(context)) {
                setHookEnable(true);
            } else {
                setHookEnable(false);
            }
        } catch (Throwable e) {
          //  Log.e(TAG, "setHookEnable has error", e);
        }

        try {
            PluginManager.getInstance().addServiceConnection(PluginHelper.this);
            PluginManager.getInstance().init(context);
        } catch (Throwable e) {
            Log.e(TAG, "installHook has error", e);
        }

        LogTool.logV3("installHook "+ Binder.getCallingPid());

    }

    private static final String TAG = PluginHelper.class.getSimpleName();


    private List<Hook> mHookList = new ArrayList<Hook>(3);

    public void setHookEnable(boolean enable) {
        synchronized (mHookList) {
            for (Hook hook : mHookList) {
                hook.setEnable(enable);
            }
        }
    }

    public void setHookEnable(boolean enable, boolean reinstallHook) {
        synchronized (mHookList) {
            for (Hook hook : mHookList) {
                hook.setEnable(enable, reinstallHook);
            }
        }
    }

    public void setHookEnable(Class hookClass, boolean enable) {
        synchronized (mHookList) {
            for (Hook hook : mHookList) {
                if (hookClass.isInstance(hook)) {
                    hook.setEnable(enable);
                }
            }
        }
    }

    public void installHook(Hook hook, ClassLoader cl) {
        try {
            hook.onInstall(cl);
            synchronized (mHookList) {
                mHookList.add(hook);
            }
        } catch (Throwable throwable) {
           // Log.e(TAG, "installHook %s error", throwable, hook);
        }
    }

}
