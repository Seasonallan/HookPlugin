package com.season.pluginlib.hook;

import android.content.Context;
import android.util.AndroidRuntimeException;

import com.season.pluginlib.compat.ActivityManagerNativeCompat;
import com.season.pluginlib.compat.IActivityManagerCompat;
import com.season.pluginlib.compat.SingletonCompat;
import com.season.pluginlib.hookhandle.BaseHookHandle;
import com.season.pluginlib.hookhandle.IActivityManagerHookHandle;
import com.season.pluginlib.reflect.FieldUtils;
import com.season.pluginlib.reflect.Utils;
import com.season.pluginlib.util.LogPlugin;
import com.season.pluginlib.util.LogTool;
import com.season.pluginlib.util.MyProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-17 10:07
 */
public class ActivityHook  extends ProxyHook {

    private static final String TAG = ActivityHook.class.getSimpleName();

    public ActivityHook(Context hostContext) {
        super(hostContext);
    }

    @Override
    public BaseHookHandle createHookHandle() {
        return new IActivityManagerHookHandle(mHostContext);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException e) {
            String msg = String.format("msg[%s],args[%s]", e.getMessage(), Arrays.toString(args));
            SecurityException e1 = new SecurityException(msg);
            e1.initCause(e);
            throw e1;
        }
    }

    @Override
    public void onInstall(ClassLoader classLoader) throws Throwable {
        Class cls = ActivityManagerNativeCompat.Class();
        Object obj = FieldUtils.readStaticField(cls, "gDefault");
        if (obj == null) {
            ActivityManagerNativeCompat.getDefault();
            obj = FieldUtils.readStaticField(cls, "gDefault");
        }

        if (IActivityManagerCompat.isIActivityManager(obj)) {
            setOldObj(obj);
            Class<?> objClass = mOldObj.getClass();
            List<Class<?>> interfaces = Utils.getAllInterfaces(objClass);
            Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
            Object proxiedActivityManager = MyProxy.newProxyInstance(objClass.getClassLoader(), ifs, this);
            FieldUtils.writeStaticField(cls, "gDefault", proxiedActivityManager);
            LogPlugin.i(TAG, "Install ActivityManager Hook 1 old=%s,new=%s", mOldObj, proxiedActivityManager);
        } else if (SingletonCompat.isSingleton(obj)) {
            Object obj1 = FieldUtils.readField(obj, "mInstance");
            if (obj1 == null) {
                SingletonCompat.get(obj);
                obj1 = FieldUtils.readField(obj, "mInstance");
            }
            setOldObj(obj1);
            List<Class<?>> interfaces = Utils.getAllInterfaces(mOldObj.getClass());
            Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
            final Object object = MyProxy.newProxyInstance(mOldObj.getClass().getClassLoader(), ifs, ActivityHook.this);
            Object iam1 = ActivityManagerNativeCompat.getDefault();

            //这里先写一次，防止后面找不到Singleton类导致的挂钩子失败的问题。
            FieldUtils.writeField(obj, "mInstance", object);

            //这里使用方式1，如果成功的话，会导致上面的写操作被覆盖。
            FieldUtils.writeStaticField(cls, "gDefault", new android.util.Singleton<Object>() {
                @Override
                protected Object create() {
                    LogPlugin.e(TAG, "Install ActivityManager 3 Hook  old=%s,new=%s", mOldObj, object);
                    return object;
                }
            });

            LogPlugin.i(TAG, "Install ActivityManager Hook 2 old=%s,new=%s", mOldObj.toString(), object);
            Object iam2 = ActivityManagerNativeCompat.getDefault();
            // 方式2
            if (iam1 == iam2) {
                //这段代码是废的，没啥用，写这里只是不想改而已。
                FieldUtils.writeField(obj, "mInstance", object);
            }
        } else {
            throw new AndroidRuntimeException("Can not install IActivityManagerNative hook");
        }
    }
}
