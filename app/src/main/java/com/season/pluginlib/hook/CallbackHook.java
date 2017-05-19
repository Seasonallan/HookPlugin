package com.season.pluginlib.hook;

import android.content.Context;
import android.os.Handler;

import com.season.pluginlib.compat.ActivityThreadCompat;
import com.season.pluginlib.hookhandle.BaseHookHandle;
import com.season.pluginlib.hookhandle.PluginCallback;
import com.season.pluginlib.reflect.FieldUtils;
import com.season.pluginlib.util.LogPlugin;
import com.season.pluginlib.util.LogTool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-17 10:29
 */
public class CallbackHook  extends Hook {

    private static final String TAG = CallbackHook.class.getSimpleName();
    private List<PluginCallback> mCallbacks = new ArrayList<PluginCallback>(1);

    public CallbackHook(Context hostContext) {
        super(hostContext);
    }

    @Override
    protected BaseHookHandle createHookHandle() {
        return null;
    }

    @Override
    public void setEnable(boolean enable, boolean reinstallHook) {
        if (reinstallHook) {
            try {
                onInstall(null);
            } catch (Throwable throwable) {
                LogPlugin.i(TAG, "setEnable onInstall fail", throwable);
            }
        }

        for (PluginCallback callback : mCallbacks) {
            callback.setEnable(enable);
        }
        super.setEnable(enable,reinstallHook);
    }

    @Override
    public void onInstall(ClassLoader classLoader) throws Throwable {
        LogTool.log("callbackhook oninstall");
        Object target = ActivityThreadCompat.currentActivityThread();
        Class ActivityThreadClass = ActivityThreadCompat.activityThreadClass();

        /*替换ActivityThread.mH.mCallback，拦截组件调度消息*/
        Field mHField = FieldUtils.getField(ActivityThreadClass, "mH");
        Handler handler = (Handler) FieldUtils.readField(mHField, target);
        Field mCallbackField = FieldUtils.getField(Handler.class, "mCallback");
        //*这里读取出旧的callback并处理*/
        Object mCallback = FieldUtils.readField(mCallbackField, handler);
        if (!PluginCallback.class.isInstance(mCallback)) {
            PluginCallback value = mCallback != null ? new PluginCallback(mHostContext, handler, (Handler.Callback) mCallback) : new PluginCallback(mHostContext, handler, null);
            value.setEnable(isEnable());
            mCallbacks.add(value);
            FieldUtils.writeField(mCallbackField, handler, value);
            LogPlugin.i(TAG, "PluginCallbackHook has installed");
        } else {
            LogPlugin.i(TAG, "PluginCallbackHook has installed,skip");
        }
    }
}
