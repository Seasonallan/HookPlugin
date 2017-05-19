/*
**        DroidPlugin Project
**
** Copyright(c) 2015 Andy Zhang <zhangyong232@gmail.com>
**
** This file is part of DroidPlugin.
**
** DroidPlugin is free software: you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation, either
** version 3 of the License, or (at your option) any later version.
**
** DroidPlugin is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with DroidPlugin.  If not, see <http://www.gnu.org/licenses/lgpl.txt>
**
**/

package com.season.pluginlib.hookhandle;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.IServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;


import com.season.pluginlib.PluginManager;
import com.season.pluginlib.PluginManagerService;
import com.season.pluginlib.PluginProcessManager;
import com.season.pluginlib.compat.ActivityManagerCompat;
import com.season.pluginlib.compat.Env;
import com.season.pluginlib.reflect.FieldUtils;
import com.season.pluginlib.reflect.MethodUtils;
import com.season.pluginlib.stub.MyFakeIBinder;
import com.season.pluginlib.stub.RunningActivities;
import com.season.pluginlib.stub.ServcesManager;
import com.season.pluginlib.stub.ShortcutProxyActivity;
import com.season.pluginlib.util.LogPlugin;
import com.season.pluginlib.util.LogTool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy Zhang(zhangyong232@gmail.com) on 2015/2/28.
 */
public class IActivityManagerHookHandle extends BaseHookHandle {

    private static final String TAG = IActivityManagerHookHandle.class.getSimpleName();

    public IActivityManagerHookHandle(Context hostContext) {
        super(hostContext);
    }

    @Override
    protected void init() {
        sHookedMethodHandlers.put("startActivity", new startActivity(mHostContext));
        sHookedMethodHandlers.put("startActivityAsUser", new startActivityAsUser(mHostContext));
        sHookedMethodHandlers.put("startActivityAsCaller", new startActivityAsCaller(mHostContext));
        sHookedMethodHandlers.put("startActivityAndWait", new startActivityAndWait(mHostContext));
        sHookedMethodHandlers.put("startActivityWithConfig", new startActivityWithConfig(mHostContext));
        sHookedMethodHandlers.put("startActivityIntentSender", new startActivityIntentSender(mHostContext));
        sHookedMethodHandlers.put("startVoiceActivity", new startVoiceActivity(mHostContext));
        sHookedMethodHandlers.put("startNextMatchingActivity", new startNextMatchingActivity(mHostContext));
        sHookedMethodHandlers.put("startActivityFromRecents", new startActivityFromRecents(mHostContext));
        sHookedMethodHandlers.put("finishActivity", new finishActivity(mHostContext));
        sHookedMethodHandlers.put("registerReceiver", new registerReceiver(mHostContext));
        sHookedMethodHandlers.put("broadcastIntent", new broadcastIntent(mHostContext));
        sHookedMethodHandlers.put("unbroadcastIntent", new unbroadcastIntent(mHostContext));
        sHookedMethodHandlers.put("getCallingPackage", new getCallingPackage(mHostContext));
        sHookedMethodHandlers.put("getCallingActivity", new getCallingActivity(mHostContext));
        sHookedMethodHandlers.put("getAppTasks", new getAppTasks(mHostContext));
        sHookedMethodHandlers.put("addAppTask", new addAppTask(mHostContext));
        sHookedMethodHandlers.put("getTasks", new getTasks(mHostContext));
        sHookedMethodHandlers.put("getServices", new getServices(mHostContext));
        sHookedMethodHandlers.put("getProcessesInErrorState", new getProcessesInErrorState(mHostContext));
    //    sHookedMethodHandlers.put("getContentProvider", new getContentProvider(mHostContext));
    //    sHookedMethodHandlers.put("getContentProviderExternal", new getContentProviderExternal(mHostContext));
        sHookedMethodHandlers.put("removeContentProviderExternal", new removeContentProviderExternal(mHostContext));
        sHookedMethodHandlers.put("publishContentProviders", new publishContentProviders(mHostContext));
        sHookedMethodHandlers.put("getRunningServiceControlPanel", new getRunningServiceControlPanel(mHostContext));
        sHookedMethodHandlers.put("startService", new startService(mHostContext));
        sHookedMethodHandlers.put("stopService", new stopService(mHostContext));
        sHookedMethodHandlers.put("stopServiceToken", new stopServiceToken(mHostContext));
        sHookedMethodHandlers.put("setServiceForeground", new setServiceForeground(mHostContext));
        sHookedMethodHandlers.put("bindService", new bindService(mHostContext));
        sHookedMethodHandlers.put("publishService", new publishService(mHostContext));
        sHookedMethodHandlers.put("unbindFinished", new unbindFinished(mHostContext));
        sHookedMethodHandlers.put("peekService", new peekService(mHostContext));
        sHookedMethodHandlers.put("bindBackupAgent", new bindBackupAgent(mHostContext));
        sHookedMethodHandlers.put("backupAgentCreated", new backupAgentCreated(mHostContext));
        sHookedMethodHandlers.put("unbindBackupAgent", new unbindBackupAgent(mHostContext));
        sHookedMethodHandlers.put("killApplicationProcess", new killApplicationProcess(mHostContext));
        sHookedMethodHandlers.put("startInstrumentation", new startInstrumentation(mHostContext));
        sHookedMethodHandlers.put("getActivityClassForToken", new getActivityClassForToken(mHostContext));
        sHookedMethodHandlers.put("getPackageForToken", new getPackageForToken(mHostContext));
        sHookedMethodHandlers.put("getIntentSender", new getIntentSender(mHostContext));
        sHookedMethodHandlers.put("clearApplicationUserData", new clearApplicationUserData(mHostContext));
        sHookedMethodHandlers.put("handleIncomingUser", new handleIncomingUser(mHostContext));
        sHookedMethodHandlers.put("grantUriPermission", new grantUriPermission(mHostContext));
        sHookedMethodHandlers.put("getPersistedUriPermissions", new getPersistedUriPermissions(mHostContext));
        sHookedMethodHandlers.put("killBackgroundProcesses", new killBackgroundProcesses(mHostContext));
        sHookedMethodHandlers.put("forceStopPackage", new forceStopPackage(mHostContext));
        sHookedMethodHandlers.put("getRunningAppProcesses", new getRunningAppProcesses(mHostContext));
        sHookedMethodHandlers.put("getRunningExternalApplications", new getRunningExternalApplications(mHostContext));
        sHookedMethodHandlers.put("getMyMemoryState", new getMyMemoryState(mHostContext));
        sHookedMethodHandlers.put("crashApplication", new crashApplication(mHostContext));
        sHookedMethodHandlers.put("grantUriPermissionFromOwner", new grantUriPermissionFromOwner(mHostContext));
        sHookedMethodHandlers.put("checkGrantUriPermission", new checkGrantUriPermission(mHostContext));
        sHookedMethodHandlers.put("startActivities", new startActivities(mHostContext));
        sHookedMethodHandlers.put("getPackageScreenCompatMode", new getPackageScreenCompatMode(mHostContext));
        sHookedMethodHandlers.put("setPackageScreenCompatMode", new setPackageScreenCompatMode(mHostContext));
        sHookedMethodHandlers.put("getPackageAskScreenCompat", new getPackageAskScreenCompat(mHostContext));
        sHookedMethodHandlers.put("setPackageAskScreenCompat", new setPackageAskScreenCompat(mHostContext));
        sHookedMethodHandlers.put("navigateUpTo", new navigateUpTo(mHostContext));
        sHookedMethodHandlers.put("serviceDoneExecuting", new serviceDoneExecuting(mHostContext));


    }

    public static class getIntentSender extends ReplaceCallingPackageHookedMethodHandler {

        public getIntentSender(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3
        /* public IIntentSender getIntentSender(int type,
            String packageName, IBinder token, String resultWho,
            int requestCode, Intent intent, String resolvedType, int flags) throws RemoteException;*/

            //API 15
        /*public IIntentSender getIntentSender(int type,
            String packageName, IBinder token, String resultWho,
            int requestCode, Intent[] intents, String[] resolvedTypes,
            int flags) throws RemoteException;*/

            //API 16
        /* public IIntentSender getIntentSender(int type,
            String packageName, IBinder token, String resultWho,
            int requestCode, Intent[] intents, String[] resolvedTypes,
            int flags, Bundle options) throws RemoteException;*/


            //API 17, 18 19, 21
        /*  public IIntentSender getIntentSender(int type,
            String packageName, IBinder token, String resultWho,
            int requestCode, Intent[] intents, String[] resolvedTypes,
            int flags, Bundle options, int userId) throws RemoteException;*/

            //这里添加包名是为了欺骗系统而已。
            final int index = 1;
            if (args != null && args.length > index && args[index] != null && args[index] instanceof String) {
                String callerPackage = (String) args[index];
                String originPackageName = mHostContext.getPackageName();
                if (!TextUtils.equals(callerPackage, originPackageName)) {
                    args[index] = originPackageName;
                }
            }

            //这里我们用新的逻辑，将原来的PendingIntent.getXXX(XXX,XXX, Intent, XXX)全部替换成
            //PendingIntent.getService(XXX,XXX,intetn,XXX)
            //这样系统在处理的时候，会先调用到我们的中转服务，我们的中转服务再来处理这个事情。
            final int index5 = 5;
            boolean hasRelacedIntent = false;
            if (args != null && args.length > index5 && args[index5] != null) {
                int type = (Integer) args[0];
                if (args[index5] instanceof Intent) {
                    Intent intent = (Intent) args[index5];
                    Intent replaced = replace(type, intent);
                    if (replaced != null) {
                        args[index5] = replaced;
                        hasRelacedIntent = true;
                    }

                } else if (args[index5] instanceof Intent[]) {
                    Intent[] intents = (Intent[]) args[index5];
                    if (intents != null && intents.length > 0) {
                        for (int i = 0; i < intents.length; i++) {
                            Intent replaced = replace(type, intents[i]);
                            if (replaced != null) {
                                intents[i] = replaced;
                                hasRelacedIntent = true;
                            }
                        }
                        args[index5] = intents;
                    }
                }
            }

            final int index7 = 7;
            if (hasRelacedIntent && args != null && args.length > index7) {
                if (args[index7] instanceof Integer) {
                    args[index7] = PendingIntent.FLAG_UPDATE_CURRENT;
                }
                args[0] = ActivityManagerCompat.INTENT_SENDER_SERVICE;
            }
            return super.beforeInvoke(receiver, method, args);
        }

        private Intent replace(int type, Intent intent) throws RemoteException {
            if (type == ActivityManagerCompat.INTENT_SENDER_SERVICE) {
                ServiceInfo a = resolveService(intent);
                if (a != null && isPackagePlugin(a.packageName)) {
                    Intent newIntent = new Intent(mHostContext, PluginManagerService.class);
                    newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                    newIntent.putExtra(Env.EXTRA_TYPE, type);
                    newIntent.putExtra(Env.EXTRA_ACTION, "PendingIntent");
                    return newIntent;
                }
            } else if (type == ActivityManagerCompat.INTENT_SENDER_ACTIVITY) {
                ActivityInfo a = resolveActivity(intent);
                if (a != null && isPackagePlugin(a.packageName)) {
                    Intent newIntent = new Intent(mHostContext, PluginManagerService.class);
                    newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                    newIntent.putExtra(Env.EXTRA_TYPE, type);
                    newIntent.putExtra(Env.EXTRA_ACTION, "PendingIntent");
                    return newIntent;
                }
            }
            return null;
        }

        public static void handlePendingIntent(final Context context, Intent intent) {
            try {
                if (intent != null && "PendingIntent".equals(intent.getStringExtra(Env.EXTRA_ACTION))) {
                    int type = intent.getIntExtra(Env.EXTRA_TYPE, -1);
                    final Intent actionIntent = intent.getParcelableExtra(Env.EXTRA_TARGET_INTENT);
                    final Handler handle = new Handler(Looper.getMainLooper());
                    if (type == ActivityManagerCompat.INTENT_SENDER_SERVICE && actionIntent != null) {


                        final Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    context.startService(actionIntent);
                                } catch (Throwable e) {
                                    LogPlugin.e(TAG, "startService for PendingIntent %s", e, actionIntent);
                                }
                            }
                        };


                        new Thread("") {
                            @Override
                            public void run() {
                                try {
                                    PluginManager.getInstance().waitForConnected();
                                    handle.post(r);
                                } catch (Exception e) {
                                    LogPlugin.e(TAG, "startService for PendingIntent %s", e, actionIntent);
                                }
                            }
                        }.start();


                    } else if (type == ActivityManagerCompat.INTENT_SENDER_ACTIVITY && actionIntent != null) {
                        actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        final Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    context.startActivity(actionIntent);
                                } catch (Throwable e) {
                                    LogPlugin.e(TAG, "startActivity for PendingIntent %s", e, actionIntent);
                                }
                            }
                        };
                        new Thread("") {
                            @Override
                            public void run() {
                                try {
                                    PluginManager.getInstance().waitForConnected();
                                    handle.post(r);
                                } catch (Exception e) {
                                    LogPlugin.e(TAG, "startActivity for PendingIntent %s", e, actionIntent);
                                }
                            }
                        }.start();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
    }


    private static class clearApplicationUserData extends ReplaceCallingPackageHookedMethodHandler {

        public clearApplicationUserData(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //2.3,15
       /* public boolean clearApplicationUserData(final String packageName,
            final IPackageDataObserver observer) throws RemoteException;*/
            //API 16,17,18,19,21
        /* public boolean clearApplicationUserData(final String packageName,
            final IPackageDataObserver observer, int userId) throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        Object observer = args.length > 1 ? args[1] : null;
                        clearPluginApplicationUserData(targetPkg, observer);
                        return true;
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static boolean clearPluginApplicationUserData(String packageName, final Object observer) throws RemoteException {
        if (observer == null) {
            PluginManager.getInstance().clearApplicationUserData(packageName, null);
        } else {
            PluginManager.getInstance().clearApplicationUserData(packageName, observer);
        }
        return true;
    }

    private static class bindService extends ReplaceCallingPackageHookedMethodHandler {

        public bindService(Context hostContext) {
            super(hostContext);
        }

        private ServiceInfo info = null;

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15
        /* public int bindService(IApplicationThread caller, IBinder token,
            Intent service, String resolvedType,
            IServiceConnection connection, int flags) throws RemoteException;*/

            //API 16, 17, 18, 19, 21
        /* public int bindService(IApplicationThread caller, IBinder token,
            Intent service, String resolvedType,
            IServiceConnection connection, int flags, int userId) throws RemoteException;*/
            info = replaceFirstServiceIntentOfArgs(args);
            int index = findIServiceConnectionIndex(method);
            if (info != null && index >= 0) {
                final Object oldIServiceConnection = args[index];
                args[index] = new MyIServiceConnection(info) {

                    public void connected(ComponentName name, IBinder service) {
                        try {
                            MethodUtils.invokeMethod(oldIServiceConnection, "connected", new ComponentName(mInfo.packageName, mInfo.name), service);
                        } catch (Exception e) {
                            Log.e(TAG, "invokeMethod connected", e);
                        }
                    }
                };
            }
            return super.beforeInvoke(receiver, method, args);
        }

        private int findIServiceConnectionIndex(Method method) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length > 0) {
                for (int index = 0; index < parameterTypes.length; index++) {
                    if (parameterTypes[index] != null && TextUtils.equals(parameterTypes[index].getSimpleName(), "IServiceConnection")) {
                        return index;
                    }
                }
            }
            return -1;
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            if (invokeResult instanceof ComponentName) {
                if (info != null) {
                    setFakedResult(new ComponentName(info.packageName, info.name));
                }
            }
            info = null;
            super.afterInvoke(receiver, method, args, invokeResult);
        }

        private abstract static class MyIServiceConnection extends IServiceConnection.Stub {
            protected final ServiceInfo mInfo;

            private MyIServiceConnection(ServiceInfo info) {
                mInfo = info;
            }
        }
    }

    private static class publishService extends ReplaceCallingPackageHookedMethodHandler {

        public publishService(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16, 17, 18, 19, 21
        /* public void publishService(IBinder token,
            Intent intent, IBinder service) throws RemoteException;*/
            replaceFirstServiceIntentOfArgs(args);
            int index = 0;
            if (args != null && args.length > index && args[index] instanceof MyFakeIBinder) {
                return true;
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class stopService extends ReplaceCallingPackageHookedMethodHandler {

        public stopService(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16
        /* public int stopService(IApplicationThread caller, Intent service,
            String resolvedType) throws RemoteException;*/

            //API 17, 18, 19, 21
        /* public int stopService(IApplicationThread caller, Intent service,
            String resolvedType, int userId) throws RemoteException;*/
            int index = 1;
            if (args != null && args.length > index && args[index] instanceof Intent) {
                Intent intent = (Intent) args[index];
                ServiceInfo info = resolveService(intent);
                if (info != null && isPackagePlugin(info.packageName)) {
                    int re = ServcesManager.getDefault().stopService(mHostContext, intent);
                    setFakedResult(re);
                    return true;
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class stopServiceToken extends ReplaceCallingPackageHookedMethodHandler {

        public stopServiceToken(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16, 17, 18, 19, 21
        /*public boolean stopServiceToken(ComponentName className, IBinder token,
            int startId) throws RemoteException;*/
            if (args != null && args.length > 2) {
                ComponentName componentName = (ComponentName) args[0];
                if (isComponentNamePlugin(componentName)) {
                    IBinder token = (IBinder) args[1];
                    Integer startId = (Integer) args[2];
                    boolean re = ServcesManager.getDefault().stopServiceToken(componentName, token, startId);
                    setFakedResult(re);
                    return true;
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private class serviceDoneExecuting extends ReplaceCallingPackageHookedMethodHandler {
        public serviceDoneExecuting(Context context) {
            super(context);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            int index = 0;
            if (args != null && args.length > index && args[index] instanceof MyFakeIBinder) {
                return true;
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class startActivity extends ReplaceCallingPackageHookedMethodHandler {

        public startActivity(Context hostContext) {
            super(hostContext);
        }

        protected boolean doReplaceIntentForStartActivityAPIHigh(Object[] args) throws RemoteException {
            LogTool.log("doReplaceIntentForStartActivityAPIHigh");
            int intentOfArgIndex = findFirstIntentIndexInArgs(args);
            if (args != null && args.length > 1 && intentOfArgIndex >= 0) {
                Intent intent = (Intent) args[intentOfArgIndex];

                ActivityInfo activityInfo = resolveActivity(intent);
//                LogTool.log("activityInfo " + activityInfo.packageName);
                if (activityInfo != null && isPackagePlugin(activityInfo.packageName)) {
                    ComponentName component = selectProxyActivity(intent);
                    if (component != null) {
                        Intent newIntent = new Intent();
                        try {
                            ClassLoader pluginClassLoader = PluginProcessManager.getPluginClassLoader(component.getPackageName());
                            setIntentClassLoader(newIntent, pluginClassLoader);
                        } catch (Exception e) {
                            LogPlugin.w(TAG, "Set Class Loader to new Intent fail", e);
                        }
                        newIntent.setComponent(component);
                        newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                        newIntent.setFlags(intent.getFlags());

                        String callingPackage = (String) args[1];
                        if (TextUtils.equals(mHostContext.getPackageName(), callingPackage)) {
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && activityInfo.launchMode == ActivityInfo.LAUNCH_MULTIPLE) {
//                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//                            }
                        }

//                        if (activityInfo.launchMode == ActivityInfo.LAUNCH_MULTIPLE) {
//
//                        } else if (activityInfo.launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
//
//                        } else if (activityInfo.launchMode == ActivityInfo.LAUNCH_SINGLE_TASK) {
//                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        } else if (activityInfo.launchMode == ActivityInfo.LAUNCH_SINGLE_TOP) {
//                            newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        }

                        args[intentOfArgIndex] = newIntent;
                        args[1] = mHostContext.getPackageName();
                    } else {
                        LogPlugin.w(TAG, "startActivity,replace selectProxyActivity fail");
                    }
                }
            }

            return true;
        }

        private void setIntentClassLoader(Intent intent, ClassLoader classLoader) {
            try {
                Bundle mExtras = (Bundle) FieldUtils.readField(intent, "mExtras");
                if (mExtras != null) {
                    mExtras.setClassLoader(classLoader);
                } else {
                    Bundle value = new Bundle();
                    value.setClassLoader(classLoader);
                    FieldUtils.writeField(intent, "mExtras", value);
                }
            } catch (Exception e) {
            } finally {
                intent.setExtrasClassLoader(classLoader);
            }
        }

        protected boolean doReplaceIntentForStartActivityAPILow(Object[] args) throws RemoteException {
            LogTool.log("doReplaceIntentForStartActivityAPILow");
            int intentOfArgIndex = findFirstIntentIndexInArgs(args);
            if (args != null && args.length > 1 && intentOfArgIndex >= 0) {
                Intent intent = (Intent) args[intentOfArgIndex];
                ActivityInfo activityInfo = resolveActivity(intent);
                if (activityInfo != null && isPackagePlugin(activityInfo.packageName)) {
                    ComponentName component = selectProxyActivity(intent);
                    if (component != null) {
                        Intent newIntent = new Intent();
                        newIntent.setComponent(component);
                        newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                        newIntent.setFlags(intent.getFlags());
                        if (TextUtils.equals(mHostContext.getPackageName(), activityInfo.packageName)) {
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        args[intentOfArgIndex] = newIntent;
                    } else {
                        LogPlugin.w(TAG, "startActivity,replace selectProxyActivity fail");
                    }
                }
            }
            return true;
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            LogTool.log("beforeInvoke >> " + method.getName());

            RunningActivities.beforeStartActivity();
            boolean bRet = true;
            if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR2) {
                //2.3
        /*public int startActivity(IApplicationThread caller,
            Intent intent, String resolvedType, Uri[] grantedUriPermissions,
            int grantedMode, IBinder resultTo, String resultWho, int requestCode,
            boolean onlyIfNeeded, boolean debug) throws RemoteException;*/

                //api 15
        /*public int startActivity(IApplicationThread caller,
            Intent intent, String resolvedType, Uri[] grantedUriPermissions,
            int grantedMode, IBinder resultTo, String resultWho, int requestCode,
            boolean onlyIfNeeded, boolean debug, String profileFile,
            ParcelFileDescriptor profileFd, boolean autoStopProfiler) throws RemoteException;*/

                //api 16,17
        /*  public int startActivity(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options) throws RemoteException;*/
                bRet = doReplaceIntentForStartActivityAPILow(args);
            } else {
                //api 18,19
         /*  public int startActivity(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options) throws RemoteException;*/

                //api 21
        /*   public int startActivity(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, ProfilerInfo profilerInfo,
            Bundle options) throws RemoteException;*/
                bRet = doReplaceIntentForStartActivityAPIHigh(args);
            }
            LogTool.log("result = "+ bRet);
            if (!bRet) {
                setFakedResult(Activity.RESULT_CANCELED);
                return true;
            }

            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class startActivityAsUser extends startActivity {

        public startActivityAsUser(Context hostContext) {
            super(hostContext);
        }

        //API 17
         /* public int startActivityAsUser(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options, int userId) throws RemoteException;*/
        //API 18,19
        /* public int startActivityAsUser(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options, int userId) throws RemoteException;*/

        //API 21
        /* public int startActivityAsUser(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, ProfilerInfo profilerInfo,
            Bundle options, int userId) throws RemoteException;*/
    }

    private static class startActivityAsCaller extends startActivity {

        public startActivityAsCaller(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 21
             /* public int startActivityAsCaller(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode,
            int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException;*/
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class startActivityAndWait extends startActivity {

        public startActivityAndWait(Context hostContext) {
            super(hostContext);
        }

        //API 2.3
        /*public WaitResult startActivityAndWait(IApplicationThread caller,
            Intent intent, String resolvedType, Uri[] grantedUriPermissions,
            int grantedMode, IBinder resultTo, String resultWho, int requestCode,
            boolean onlyIfNeeded, boolean debug) throws RemoteException;*/

        //API 15
        /* public WaitResult startActivityAndWait(IApplicationThread caller,
            Intent intent, String resolvedType, Uri[] grantedUriPermissions,
            int grantedMode, IBinder resultTo, String resultWho, int requestCode,
            boolean onlyIfNeeded, boolean debug, String profileFile,
            ParcelFileDescriptor profileFd, boolean autoStopProfiler) throws RemoteException;*/


        //API 16
        /*  public WaitResult startActivityAndWait(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options) throws RemoteException;*/

        //API 17
        /*  public WaitResult startActivityAndWait(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options, int userId) throws RemoteException;*/

        //API 18,19
        /*  public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, String profileFile,
            ParcelFileDescriptor profileFd, Bundle options, int userId) throws RemoteException;*/

        //API 21
        /* public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int flags, ProfilerInfo profilerInfo,
            Bundle options, int userId) throws RemoteException;*/
    }


    private static class startActivityWithConfig extends startActivity {

        public startActivityWithConfig(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15
        /*  public int startActivityWithConfig(IApplicationThread caller,
            Intent intent, String resolvedType, Uri[] grantedUriPermissions,
            int grantedMode, IBinder resultTo, String resultWho, int requestCode,
            boolean onlyIfNeeded, boolean debug, Configuration newConfig) throws RemoteException;*/


        //API 16
        /* public int startActivityWithConfig(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int startFlags, Configuration newConfig,
            Bundle options) throws RemoteException;*/

        //API 17
        /* public int startActivityWithConfig(IApplicationThread caller,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int startFlags, Configuration newConfig,
            Bundle options, int userId) throws RemoteException;*/
        //API 18,19,21
        /*  public int startActivityWithConfig(IApplicationThread caller, String callingPackage,
            Intent intent, String resolvedType, IBinder resultTo, String resultWho,
            int requestCode, int startFlags, Configuration newConfig,
            Bundle options, int userId) throws RemoteException;*/
    }

    private static class startActivityIntentSender extends ReplaceCallingPackageHookedMethodHandler {

        public startActivityIntentSender(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15
        /* public int startActivityIntentSender(IApplicationThread caller,
            IntentSender intent, Intent fillInIntent, String resolvedType,
            IBinder resultTo, String resultWho, int requestCode,
            int flagsMask, int flagsValues) throws RemoteException;*/

        //API 16,17,18,19,21
        /*  public int startActivityIntentSender(IApplicationThread caller,
            IntentSender intent, Intent fillInIntent, String resolvedType,
            IBinder resultTo, String resultWho, int requestCode,
            int flagsMask, int flagsValues, Bundle options) throws RemoteException;*/
        //DO NOTHING
    }

    private static class startVoiceActivity extends startActivity {

        public startVoiceActivity(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 21
        /*   public int startVoiceActivity(String callingPackage, int callingPid, int callingUid,
            Intent intent, String resolvedType, IVoiceInteractionSession session,
            IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options,
            int userId) throws RemoteException;*/
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                final int index = 0;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String targetPkg = (String) args[index];
                        if (isPackagePlugin(targetPkg)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
                doReplaceIntentForStartActivityAPIHigh(args);
            }
            return false;
        }
    }

    private static class startNextMatchingActivity extends startActivity {

        public startNextMatchingActivity(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15
        /*  public boolean startNextMatchingActivity(IBinder callingActivity,
            Intent intent) throws RemoteException;*/

            //API 16,17,17,19,21
        /* public boolean startNextMatchingActivity(IBinder callingActivity,
            Intent intent, Bundle options) throws RemoteException;*/
            doReplaceIntentForStartActivityAPILow(args);
            return false;
        }
    }

    private static class startActivityFromRecents extends ReplaceCallingPackageHookedMethodHandler {

        public startActivityFromRecents(Context hostContext) {
            super(hostContext);
        }

        //API 21
        /*public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException;*/
        //DO NOTHING
    }

    private static class finishActivity extends ReplaceCallingPackageHookedMethodHandler {

        public finishActivity(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15,16,17,18,19
        /* public boolean finishActivity(IBinder token, int code, Intent data)
            throws RemoteException;*/
        //API 21
        /*public boolean finishActivity(IBinder token, int code, Intent data, boolean finishTask)
            throws RemoteException;*/
        //FIXME 先不修改。
    }

    private static class registerReceiver extends ReplaceCallingPackageHookedMethodHandler {

        public registerReceiver(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3
        /* public Intent registerReceiver(IApplicationThread caller,
            IIntentReceiver receiver, IntentFilter filter,
            String requiredPermission) throws RemoteException;*/

            //API 15,16
       /* public Intent registerReceiver(IApplicationThread caller, String callerPackage,
            IIntentReceiver receiver, IntentFilter filter,
            String requiredPermission) throws RemoteException;*/

            //API 17,18,19,21
       /*  public Intent registerReceiver(IApplicationThread caller, String callerPackage,
                IIntentReceiver receiver, IntentFilter filter,
                String requiredPermission, int userId) throws RemoteException;*/
            if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (args != null && args.length > 0) {
                    for (int index = 0; index < args.length; index++) {
                        if (args[index] instanceof String) {
                            String callerPackage = (String) args[index];
                            if (isPackagePlugin(callerPackage)) {
                                args[index] = mHostContext.getPackageName();
                            }
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class broadcastIntent extends ReplaceCallingPackageHookedMethodHandler {

        public broadcastIntent(Context hostContext) {
            super(hostContext);
        }

        //api 2.3, 15
        /*    public int broadcastIntent(IApplicationThread caller, Intent intent,
            String resolvedType, IIntentReceiver resultTo, int resultCode,
            String resultData, Bundle map, String requiredPermission,
            boolean serialized, boolean sticky) throws RemoteExceptions
        //API 16,17
        /* public int broadcastIntent(IApplicationThread caller, Intent intent,
            String resolvedType, IIntentReceiver resultTo, int resultCode,
            String resultData, Bundle map, String requiredPermission,
            boolean serialized, boolean sticky, int userId) throws RemoteException;*/
        //API 18,19,21
        /*  public int broadcastIntent(IApplicationThread caller, Intent intent,
            String resolvedType, IIntentReceiver resultTo, int resultCode,
            String resultData, Bundle map, String requiredPermission,
            int appOp, boolean serialized, boolean sticky, int userId) throws RemoteException;*/
        //TODO 广播相关的，研究完了再修改。


        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            final int index = 1;
            if (args != null && args.length > index && args[index] instanceof Intent) {
                Intent intent = (Intent) args[index];
                checkAndProcessIntent(intent);
            }
            return super.beforeInvoke(receiver, method, args);
        }

        private boolean checkAndProcessIntent(Intent intent) throws RemoteException {
            if (Env.ACTION_INSTALL_SHORTCUT.equals(intent.getAction())) {
                //安装快捷方式的.我们都需要处理
                Intent shortcutIntent = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
                if (shortcutIntent != null) {
                    ComponentName componentName = shortcutIntent.resolveActivity(mHostContext.getPackageManager());
                    if (componentName != null && PluginManager.getInstance().isPluginPackage(componentName.getPackageName())) {
                        //如果是插件，就把快捷方式Intent换成插件自己的，然后我们再跳转
                        Intent newShortcutIntent = new Intent(PluginManager.ACTION_SHORTCUT_PROXY);
                        newShortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        newShortcutIntent.putExtra(Env.EXTRA_TARGET_INTENT, shortcutIntent);
                        newShortcutIntent.putExtra(Env.EXTRA_TARGET_INTENT_URI, shortcutIntent.toUri(0));
                        intent.removeExtra(Intent.EXTRA_SHORTCUT_INTENT);
                        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, newShortcutIntent);


                        //替换图标
                        Intent.ShortcutIconResource icon = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
                        if (icon != null && !TextUtils.equals(icon.packageName, mHostContext.getPackageName())) {
                            try {
                                Context context = PluginProcessManager.getPluginContext(icon.packageName);
                                int resId = context.getResources().getIdentifier(icon.resourceName, "drawable", context.getPackageName());
                                if (resId > 0) {
                                    Drawable iconDrawable = context.getResources().getDrawable(resId);
                                    Bitmap newIcon = drawableToBitMap(iconDrawable);
                                    if (newIcon != null) {
                                        intent.removeExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
                                        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, newIcon);
                                        return true;
                                    } else {
                                        throw new Resources.NotFoundException(String.format("Can not found the icon resource in plugin package:%s", icon));
                                    }
                                } else {
                                    throw new Resources.NotFoundException(String.format("Can not found the icon resource in plugin package:%s", icon));
                                }
                            } catch (Resources.NotFoundException e) {
                                throw e;
                            } catch (Throwable e) {
                                Resources.NotFoundException exception = new Resources.NotFoundException(String.format("Can not found the icon resource in plugin package:%s", icon));
                                exception.initCause(e);
                                throw exception;
                            }
                        }
                    }
                }
                return false;
            } else if (Env.ACTION_UNINSTALL_SHORTCUT.equals(intent.getAction())) {
                //卸载快捷方式的。我们都需要处理
                Intent shortcutIntent = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
                if (shortcutIntent != null) {
                    ComponentName componentName = shortcutIntent.resolveActivity(mHostContext.getPackageManager());
                    if (componentName != null && PluginManager.getInstance().isPluginPackage(componentName.getPackageName())) {
                        //如果是插件，就把快捷方式Intent换成插件自己的，然后我们再
                        Intent newShortcutIntent = new Intent(mHostContext, ShortcutProxyActivity.class);
                        newShortcutIntent.putExtra(Env.EXTRA_TARGET_INTENT, shortcutIntent);
                        intent.removeExtra(Intent.EXTRA_SHORTCUT_INTENT);
                        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, newShortcutIntent);
                    }
                    return true;
                }
            }
            return false;
        }

        private Bitmap drawableToBitMap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                return bitmapDrawable.getBitmap();
            } else {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
    }

    private static class unbroadcastIntent extends ReplaceCallingPackageHookedMethodHandler {

        public unbroadcastIntent(Context hostContext) {
            super(hostContext);
        }

        //api 2.3,15
        /*  public void unbroadcastIntent(IApplicationThread caller, Intent intent) throws RemoteException;*/
        //API 16,17,18,19,21
        /*public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException;*/
        //TODO 广播相关的，研究完了再修改。
    }

    private static class getCallingPackage extends ReplaceCallingPackageHookedMethodHandler {

        public getCallingPackage(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15,16,17,18,19,21
        /* public String getCallingPackage(IBinder token) throws RemoteException;*/
        //FIXME  I don't know what function of this,just hook it.
    }

    private static class getCallingActivity extends ReplaceCallingPackageHookedMethodHandler {

        public getCallingActivity(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
        /*  public ComponentName getCallingActivity(IBinder token) throws RemoteException;*/
        //FIXME I don't know what function of this,just hook it.
        //也不知道这个是干嘛的。是返回此Activity是由谁调起的么？
    }

    private static class getAppTasks extends ReplaceCallingPackageHookedMethodHandler {

        public getAppTasks(Context hostContext) {
            super(hostContext);
        }
        // API 21
        /* public List<IAppTask> getAppTasks(String callingPackage) throws RemoteException;*/
        //FIXME I don't know what function of this,just hook it.
    }

    private static class addAppTask extends ReplaceCallingPackageHookedMethodHandler {

        public addAppTask(Context hostContext) {
            super(hostContext);
        }

        //API 21
        /* public int addAppTask(IBinder activityToken, Intent intent,
            ActivityManager.TaskDescription description, Bitmap thumbnail) throws RemoteException;*/
        //FIXME api21的不知道干嘛的，先不修改吧。
    }

    private static class getTasks extends ReplaceCallingPackageHookedMethodHandler {

        public getTasks(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15,16,17,18
        /*  public List getTasks(int maxNum, int flags,
                         IThumbnailReceiver receiver) throws RemoteException;*/
        //API 19
        /*public List<RunningTaskInfo> getTasks(int maxNum, int flags,
                         IThumbnailReceiver receiver) throws RemoteException;*/
        //API 21
        /* public List<RunningTaskInfo> getTasks(int maxNum, int flags) throws RemoteException;*/
        //FIXME 这里需要把原来函数返回的 List<RunningTaskInfo>中关于代理activity修改成插件自己的。

//        @Override
//        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
//            if (invokeResult instanceof List) {
//                List runningTaskInfo = (List) invokeResult;
//                if (runningTaskInfo.size() > 0) {
//                    for (Object obj : runningTaskInfo) {
//                        RunningTaskInfo info = (RunningTaskInfo) obj;
//                        info.baseActivity =;
//                        info.topActivity =;
//                    }
//                }
//            }
//            super.afterInvoke(receiver, method, args, invokeResult);
//        }
    }

    private static class getServices extends ReplaceCallingPackageHookedMethodHandler {

        public getServices(Context hostContext) {
            super(hostContext);
        }


        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            //api  2.3,15,16,17,18
        /*public List getServices(int maxNum, int flags) throws RemoteException;*/
            //API 19,21
        /*public List<RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException;*/
            if (invokeResult != null && invokeResult instanceof List) {
                List<Object> objectList = (List<Object>) invokeResult;
                for (Object obj : objectList) {
                    if (obj instanceof ActivityManager.RunningServiceInfo) {
                        ActivityManager.RunningServiceInfo serviceInfo = (ActivityManager.RunningServiceInfo) obj;
                        tryfixServiceInfo(serviceInfo);
                    }
                }
            }
        }
    }

    private static class getProcessesInErrorState extends ReplaceCallingPackageHookedMethodHandler {

        public getProcessesInErrorState(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
        /* public List<ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState()
            throws RemoteException;*/
        //FIXME I don't know what function of this,just hook it.
    }


    private static class removeContentProviderExternal extends ReplaceCallingPackageHookedMethodHandler {

        public removeContentProviderExternal(Context hostContext) {
            super(hostContext);
        }

        //API   16,17,18,19, 21
        /*public void removeContentProviderExternal(String name, IBinder token) throws RemoteException;*/
        //TODO removeContentProviderExternal
    }

    private static class publishContentProviders extends ReplaceCallingPackageHookedMethodHandler {

        public publishContentProviders(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
        /*    public void publishContentProviders(IApplicationThread caller,
            List<ContentProviderHolder> providers) throws RemoteException;*/
        //TODO 发布ContentProvider
    }

    private static class getRunningServiceControlPanel extends ReplaceCallingPackageHookedMethodHandler {

        public getRunningServiceControlPanel(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
        /*    public PendingIntent getRunningServiceControlPanel(ComponentName service)
            throws RemoteException;*/
        //FIXME 这里需要把service替换成代理服务吧？maybe.
        //通过服务名称拿PendingIntent？搞不懂，不改。
    }

    private static class startService extends ReplaceCallingPackageHookedMethodHandler {

        public startService(Context hostContext) {
            super(hostContext);
        }

        private ServiceInfo info = null;

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16
        /*    public ComponentName startService(IApplicationThread caller, Intent service,
            String resolvedType) throws RemoteException;*/

            //API 17, 18, 19, 21
        /*public ComponentName startService(IApplicationThread caller, Intent service,
            String resolvedType, int userId) throws RemoteException;*/
            info = replaceFirstServiceIntentOfArgs(args);
            return super.beforeInvoke(receiver, method, args);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            if (invokeResult instanceof ComponentName) {
                if (info != null) {
                    setFakedResult(new ComponentName(info.packageName, info.name));
                }
            }
            info = null;
            super.afterInvoke(receiver, method, args, invokeResult);
        }
    }


    private static class setServiceForeground extends ReplaceCallingPackageHookedMethodHandler {

        public setServiceForeground(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16, 17, 18, 19, 21
        /* public void setServiceForeground(ComponentName className, IBinder token,
            int id, Notification notification, boolean keepNotification) throws RemoteException;*/
            if (args != null && args.length > 1 && args[0] instanceof ComponentName) {
                ComponentName componentName = (ComponentName) args[0];
                if (isComponentNamePlugin(componentName)) {
                    args[0] = selectProxyService(componentName);
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }


    private static class unbindFinished extends ReplaceCallingPackageHookedMethodHandler {

        public unbindFinished(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16, 17, 18, 19, 21
        /*public void unbindFinished(IBinder token, Intent service,
            boolean doRebind) throws RemoteException;*/
            replaceFirstServiceIntentOfArgs(args);
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class peekService extends ReplaceCallingPackageHookedMethodHandler {

        public peekService(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3, 15, 16, 17, 18, 19, 21
        /* public IBinder peekService(Intent service, String resolvedType) throws RemoteException;*/
            replaceFirstServiceIntentOfArgs(args);
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class bindBackupAgent extends ReplaceCallingPackageHookedMethodHandler {

        public bindBackupAgent(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16,17,18,19, 21
        /* public boolean bindBackupAgent(ApplicationInfo appInfo, int backupRestoreMode)
            throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof ApplicationInfo) {
                    ApplicationInfo appInfo = (ApplicationInfo) args[index];
                    if (isPackagePlugin(appInfo.packageName)) {
                        args[index] = mHostContext.getApplicationInfo();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class backupAgentCreated extends ReplaceCallingPackageHookedMethodHandler {

        public backupAgentCreated(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16,17,18,19, 21
        /* public void backupAgentCreated(String packageName, IBinder agent) throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String packageName = (String) args[index];
                    if (isPackagePlugin(packageName)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class unbindBackupAgent extends ReplaceCallingPackageHookedMethodHandler {

        public unbindBackupAgent(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16,17,18,19, 21
        /* public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof ApplicationInfo) {
                    ApplicationInfo appInfo = (ApplicationInfo) args[index];
                    if (isPackagePlugin(appInfo.packageName)) {
                        args[index] = mHostContext.getApplicationInfo();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class killApplicationProcess extends ReplaceCallingPackageHookedMethodHandler {

        public killApplicationProcess(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16,17,18,19, 21
        /* public void killApplicationProcess(String processName, int uid) throws RemoteException;*/

            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        PluginManager.getInstance().killApplicationProcess(targetPkg);
                        return true;
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class startInstrumentation extends ReplaceCallingPackageHookedMethodHandler {

        public startInstrumentation(Context hostContext) {
            super(hostContext);
        }

        //API 2.3,15,16
        /*    public boolean startInstrumentation(ComponentName className, String profileFile,
            int flags, Bundle arguments, IInstrumentationWatcher watcher)
            throws RemoteException;*/
        //API 17
       /*    public boolean startInstrumentation(ComponentName className, String profileFile,
            int flags, Bundle arguments, IInstrumentationWatcher watcher, int userId)
            throws RemoteException;*/
        //API 18,19
        /*    public boolean startInstrumentation(ComponentName className, String profileFile,
            int flags, Bundle arguments, IInstrumentationWatcher watcher,
            IUiAutomationConnection connection, int userId) throws RemoteException;*/
        //API 21
        /* public boolean startInstrumentation(ComponentName className, String profileFile,
            int flags, Bundle arguments, IInstrumentationWatcher watcher,
            IUiAutomationConnection connection, int userId,
            String abiOverride) throws RemoteException;*/

        //FIXME 单元测试用的。这个就不改了。
    }

    private static class getActivityClassForToken extends ReplaceCallingPackageHookedMethodHandler {

        public getActivityClassForToken(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
       /* public ComponentName getActivityClassForToken(IBinder token) throws RemoteException;*/
        //FIXME I don't know what function of this,just hook it.
        //通过token拿Activity？搞不懂，不改。
    }

    private static class getPackageForToken extends ReplaceCallingPackageHookedMethodHandler {

        public getPackageForToken(Context hostContext) {
            super(hostContext);
        }

        //API  2.3,15,16,17,18,19, 21
        /* public String getPackageForToken(IBinder token) throws RemoteException;*/
        //FIXME I don't know what function of this,just hook it.
        //通过token拿包名？搞不懂，不改。
    }


    private static class handleIncomingUser extends ReplaceCallingPackageHookedMethodHandler {

        public handleIncomingUser(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API  17,18,19, 21
             /*public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll,
            boolean requireFull, String name, String callerPackage) throws RemoteException;*/
            //这个函数不知道是干嘛的
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
                final int index = 6;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String targetPkg = (String) args[index];
                        if (isPackagePlugin(targetPkg)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class grantUriPermission extends ReplaceCallingPackageHookedMethodHandler {

        public grantUriPermission(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16,17,18,19
        /*    public void grantUriPermission(IApplicationThread caller, String targetPkg,
            Uri uri, int mode) throws RemoteException;*/
            //API 21
        /* public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri,
            int mode, int userId) throws RemoteException;*/
            //这个函数是用来给某个包授予访问某个URI的权限。
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            final int index = 1;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class getPersistedUriPermissions extends ReplaceCallingPackageHookedMethodHandler {

        public getPersistedUriPermissions(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            // 19,21
            /*    public ParceledListSlice<UriPermission> getPersistedUriPermissions(
            String packageName, boolean incoming) throws RemoteException;*/
            //这个函数是用来检测什么权限，没搞明白。
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class killBackgroundProcesses extends ReplaceCallingPackageHookedMethodHandler {

        public killBackgroundProcesses(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16
        /*public void killBackgroundProcesses(final String packageName) throws RemoteException;*/

            //API 17,18,19,21
        /* public void killBackgroundProcesses(final String packageName, int userId)
            throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        PluginManager.getInstance().killBackgroundProcesses(targetPkg);
                        return true;
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class forceStopPackage extends ReplaceCallingPackageHookedMethodHandler {

        public forceStopPackage(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 2.3,15,16
            /* public void forceStopPackage(final String packageName) throws RemoteException;*/
            //API 17,18,19,21
            /*public void forceStopPackage(final String packageName, int userId) throws RemoteException;*/
            final int index = 0;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        PluginManager.getInstance().forceStopPackage(targetPkg);
                        return true;
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class getRunningAppProcesses extends ReplaceCallingPackageHookedMethodHandler {

        public getRunningAppProcesses(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            //2.3,15,16,17,18,19,21
             /*    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses()
            throws RemoteException;*/
            //这个hook有点不同。一般插件调用这个函数是为了获取自己当前进程的名字。
            //所以要把原函数返回的List<ActivityManager.RunningAppProcessInfo>中关于插件的部分的进程名字给改了，用来欺骗插件。
            //不过目前这种修改方式可能有问题。

            if (invokeResult != null && invokeResult instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> infos = (List<Object>) invokeResult;
                if (infos.size() > 0) {
                    for (Object info : infos) {
                        if (info instanceof ActivityManager.RunningAppProcessInfo) {
                            ActivityManager.RunningAppProcessInfo myinfo = (ActivityManager.RunningAppProcessInfo) info;
                            if (myinfo.uid != android.os.Process.myUid()) {
                                continue;
                            }
                            List<String> pkgs = PluginManager.getInstance().getPackageNameByPid(myinfo.pid);
                            String processname = PluginManager.getInstance().getProcessNameByPid(myinfo.pid);
                            if (processname != null) {
                                myinfo.processName = processname;
                            }else{
                               // myinfo.processName = PluginProcessManager.getCurrentPackage(mHostContext);
                            }

                            if (pkgs != null && pkgs.size() > 0) {
                                ArrayList<String> ls = new ArrayList<String>();
                                if (myinfo.pkgList != null) {
                                    for (String s : myinfo.pkgList) {
                                        if (!ls.contains(s)) {
                                            ls.add(s);
                                        }
                                    }
                                }
                                for (String s : pkgs) {
                                    if (!ls.contains(s)) {
                                        ls.add(s);
                                    }
                                }
                                myinfo.pkgList = ls.toArray(new String[ls.size()]);
                            }
                        }
                    }
                }
            }

        }
    }

    private static class getRunningExternalApplications extends ReplaceCallingPackageHookedMethodHandler {

        public getRunningExternalApplications(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected void afterInvoke(Object receiver, Method method, Object[] args, Object invokeResult) throws Throwable {
            //2.3,15,16,17,18,19,21
             /* public List<ApplicationInfo> getRunningExternalApplications()
            throws RemoteException;*/
            //这个hook有点不同。
            //我们把原函数返回的List<ApplicationInfo>中关于插件的部分给改了，用来欺骗插件，让其以为自己已经被安装了。咩哈哈！！
            if (invokeResult != null && invokeResult instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> infos = (List<Object>) invokeResult;
                if (infos.size() > 0) {
                    List<ApplicationInfo> pluginInfos = new ArrayList<ApplicationInfo>(2);
                    for (Object info : infos) {
                        if (info instanceof ApplicationInfo) {
                            ApplicationInfo myinfo = (ApplicationInfo) info;
                            if (isPackagePlugin(myinfo.packageName)) {
                                pluginInfos.add(myinfo);
                            }
                        }
                    }
                    if (pluginInfos.size() > 0) {
                        for (ApplicationInfo pluginInfo : pluginInfos) {
                            int index = infos.indexOf(pluginInfo);
                            if (index >= 0) {
                                ApplicationInfo object = queryPluginApplicationInfo(pluginInfo.packageName);
                                if (object != null) {
                                    infos.set(index, object);
                                }
                            }
                        }
                    }
                }
            }
            setFakedResult(invokeResult);
        }
    }

    private static class getMyMemoryState extends ReplaceCallingPackageHookedMethodHandler {

        public getMyMemoryState(Context hostContext) {
            super(hostContext);
        }
        //API 16,17,18,19,21
            /* public void getMyMemoryState(ActivityManager.RunningAppProcessInfo outInfo)
            throws RemoteException;*/
        //DO NOTHING
    }

    private static class crashApplication extends ReplaceCallingPackageHookedMethodHandler {

        public crashApplication(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //2.3,15,16,17,18,19,21
             /*public void crashApplication(int uid, int initialPid, String packageName,
            String message) throws RemoteException;*/
            //这个函数不知道是用来干嘛的，也许是向系统报告自己崩溃了？神奇的函数。
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            final int index = 2;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class grantUriPermissionFromOwner extends ReplaceCallingPackageHookedMethodHandler {

        public grantUriPermissionFromOwner(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //2.3,15,16,17,18,19,21
        /* public void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg,
            Uri uri, int mode) throws RemoteException;*/
            //这个函数是用来给某个包授予访问某个URI的权限。
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            final int index = 2;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }

            return super.beforeInvoke(receiver, method, args);
        }
    }

    private static class checkGrantUriPermission extends ReplaceCallingPackageHookedMethodHandler {

        public checkGrantUriPermission(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API  15,16,17,18,19,21
           /* public int checkGrantUriPermission(int callingUid, String targetPkg,
            Uri uri, int modeFlags) throws RemoteException;*/
            //API  21
          /* public int checkGrantUriPermission(int callingUid, String targetPkg, Uri uri,
            int modeFlags, int userId) throws RemoteException;*/
            //这个函数是用来检测某个包是否具有访问某个URI的权限。
            //插件调用这个函数会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            final int index = 1;
            if (args != null && args.length > index) {
                if (args[index] != null && args[index] instanceof String) {
                    String targetPkg = (String) args[index];
                    if (isPackagePlugin(targetPkg)) {
                        args[index] = mHostContext.getPackageName();
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for  API 15 or later*/
    private static class startActivities extends ReplaceCallingPackageHookedMethodHandler {


        public startActivities(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //Api 15
        /* public int startActivities(IApplicationThread caller,
            Intent[] intents, String[] resolvedTypes, IBinder resultTo) throws RemoteException;*/
            //Api 16
        /* public int startActivities(IApplicationThread caller,
            Intent[] intents, String[] resolvedTypes, IBinder resultTo,
            Bundle options) throws RemoteException;*/
            //Api 17
        /* public int startActivities(IApplicationThread caller,
            Intent[] intents, String[] resolvedTypes, IBinder resultTo,
            Bundle options, int userId) throws RemoteException;*/
            //API 18, 19, 21
       /* public int startActivities(IApplicationThread caller, String callingPackage,
            Intent[] intents, String[] resolvedTypes, IBinder resultTo,
            Bundle options, int userId) throws RemoteException;*/
            //启动一坨Activity用的，用的比较少。不过为了保险起见也改改。
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
                int index = 1;
                String callingPackage = null;
                if (args != null && args.length > index && args[index] instanceof String) {
                    if (args[index] == null) {
                        args[index] = mHostContext.getPackageName();
                    } else {
                        callingPackage = (String) args[1];
                        if (!TextUtils.equals(callingPackage, mHostContext.getPackageName())) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                } else {
                    LogPlugin.w(TAG, "hook startActivities,replace callingPackage fail");
                }

                index = 2;
                if (args != null && args.length > index && args[index] != null && args[index] instanceof Intent[]) {
                    Intent[] intents = (Intent[]) args[index];
                    for (int i = 0; i < intents.length; i++) {
                        Intent intent = intents[i];
                        ComponentName component = selectProxyActivity(intent);
                        if (component != null) {
                            Intent newIntent = new Intent();
                            newIntent.setComponent(component);
                            newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                            ActivityInfo activityInfo = resolveActivity(intent);
                            if (activityInfo != null && TextUtils.equals(mHostContext.getPackageName(), callingPackage)) {
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            intents[i] = newIntent;
                        }
                    }
                } else {
                    LogPlugin.w(TAG, "hook startActivities,replace intents fail");
                }


            } else if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                int index = 1;
                if (args != null && args.length > index && args[index] != null && args[index] instanceof Intent[]) {
                    Intent[] intents = (Intent[]) args[index];
                    for (int i = 0; i < intents.length; i++) {
                        Intent intent = intents[i];
                        ComponentName component = selectProxyActivity(intent);
                        if (component != null) {
                            Intent newIntent = new Intent();
                            newIntent.setComponent(component);
                            newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                            ActivityInfo activityInfo = resolveActivity(intent);
//                            if (activityInfo != null && TextUtils.equals(mHostContext.getPackageName(), activityInfo.packageName)) {
//                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            }
                            intents[i] = newIntent;
                        }
                    }
                } else {
                    LogPlugin.w(TAG, "hook startActivities,replace intents fail");
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for   api 15 or later*/
    private static class getPackageScreenCompatMode extends ReplaceCallingPackageHookedMethodHandler {

        public getPackageScreenCompatMode(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
             /* public int getPackageScreenCompatMode(String packageName) throws RemoteException;*/
            //我也不知道这个函数是干嘛的，不过既然写了，我们就改一下。
            //因为如果万一插件调用了这个函数，则会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                final int index = 0;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String packageName = (String) args[index];
                        if (isPackagePlugin(packageName)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for   api 15 or later*/
    private static class setPackageScreenCompatMode extends ReplaceCallingPackageHookedMethodHandler {

        public setPackageScreenCompatMode(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
             /* public void setPackageScreenCompatMode(String packageName, int mode)
            throws RemoteException;*/
            //我也不知道这个函数是干嘛的，不过既然写了，我们就改一下。
            //因为如果万一插件调用了这个函数，则会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                final int index = 0;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String packageName = (String) args[index];
                        if (isPackagePlugin(packageName)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for   api 15 or later*/
    private static class getPackageAskScreenCompat extends ReplaceCallingPackageHookedMethodHandler {

        public getPackageAskScreenCompat(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 15, 16, 17, 18, 19, 21
             /* public boolean getPackageAskScreenCompat(String packageName) throws RemoteException;*/
            //我也不知道这个函数是干嘛的，不过既然写了，我们就改一下。
            //因为如果万一插件调用了这个函数，则会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                final int index = 0;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String packageName = (String) args[index];
                        if (isPackagePlugin(packageName)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for  api 15 or later*/
    private static class setPackageAskScreenCompat extends ReplaceCallingPackageHookedMethodHandler {
        public setPackageAskScreenCompat(Context hostContext) {
            super(hostContext);
        }

        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            //API 15, 16, 17, 18, 19, 21
            /*public void setPackageAskScreenCompat(String packageName, boolean ask)
            throws RemoteException;*/
            //我也不知道这个函数是干嘛的，不过既然写了，我们就改一下。
            //因为如果万一插件调用了这个函数，则会传插件自己的包名，而此插件并未被安装。就这样调用原来函数传给系统，是会出问题的。所以改成宿主程序的包名。
            if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                final int index = 0;
                if (args != null && args.length > index) {
                    if (args[index] != null && args[index] instanceof String) {
                        String packageName = (String) args[index];
                        if (isPackagePlugin(packageName)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
    }

    /*ONLY for API 16 or later*/
    private static class navigateUpTo extends ReplaceCallingPackageHookedMethodHandler {
        public navigateUpTo(Context hostContext) {
            super(hostContext);
        }

        //API 16, 17, 18, 19, 21
        /* public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData)
            throws RemoteException;*/
        //TODO replace target(Intent) to ProxyActivity
        //这里先不做，测试如果有问题再处理。
    }


    private static ServiceInfo replaceFirstServiceIntentOfArgs(Object[] args) throws RemoteException {
        int intentOfArgIndex = findFirstIntentIndexInArgs(args);
        if (args != null && args.length > 1 && intentOfArgIndex >= 0) {
            Intent intent = (Intent) args[intentOfArgIndex];
            ServiceInfo serviceInfo = resolveService(intent);
            if (serviceInfo != null && isPackagePlugin(serviceInfo.packageName)) {
                ServiceInfo proxyService = selectProxyService(intent);
                if (proxyService != null) {
                    Intent newIntent = new Intent();
                    //FIXBUG：https://github.com/Qihoo360/DroidPlugin/issues/122
                    //如果插件中有两个Service：ServiceA和ServiceB，在bind ServiceA的时候会调用ServiceA的onBind并返回其IBinder对象，
                    // 但是再次bind ServiceA的时候还是会返回ServiceA的IBinder对象，这是因为插件系统对多个Service使用了同一个StubService
                    // 来代理，而系统对StubService的IBinder做了缓存的问题。这里设置一个Action则会穿透这种缓存。
                    newIntent.setAction(proxyService.name + new Random().nextInt());

                    newIntent.setClassName(proxyService.packageName, proxyService.name);
                    newIntent.putExtra(Env.EXTRA_TARGET_INTENT, intent);
                    newIntent.setFlags(intent.getFlags());
                    args[intentOfArgIndex] = newIntent;
                    return serviceInfo;
                }
            }
        }
        return null;
    }


    private static int findFirstIntentIndexInArgs(Object[] args) {
        if (args != null && args.length > 0) {
            int i = 0;
            for (Object arg : args) {
                if (arg != null && arg instanceof Intent) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    private static ComponentName selectProxyActivity(Intent intent) {
        try {
            if (intent != null) {
                ActivityInfo proxyInfo = PluginManager.getInstance().selectStubActivityInfo(intent);
                if (proxyInfo != null) {
                    return new ComponentName(proxyInfo.packageName, proxyInfo.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ServiceInfo selectProxyService(Intent intent) {
        try {
            if (intent != null) {
                ServiceInfo proxyInfo = PluginManager.getInstance().selectStubServiceInfo(intent);
                if (proxyInfo != null) {
                    return proxyInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ComponentName selectProxyService(ComponentName componentName) {
        try {
            if (componentName != null) {
                PluginManager instance = PluginManager.getInstance();
                ServiceInfo info = instance.getServiceInfo(componentName, 0);
                if (info != null) {
                    ServiceInfo proxyInfo = instance.selectStubServiceInfo(info);
                    if (proxyInfo != null) {
                        return new ComponentName(proxyInfo.packageName, proxyInfo.name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ActivityInfo resolveActivity(Intent intent) throws RemoteException {
        return PluginManager.getInstance().resolveActivityInfo(intent, 0);
    }

    private static ServiceInfo resolveService(Intent intent) throws RemoteException {
        return PluginManager.getInstance().resolveServiceInfo(intent, 0);
    }


    private static boolean isPackagePlugin(String packageName) throws RemoteException {
        return PluginManager.getInstance().isPluginPackage(packageName);
    }

    private static boolean isComponentNamePlugin(ComponentName className) throws RemoteException {
        return PluginManager.getInstance().isPluginPackage(className);
    }

    private static ApplicationInfo queryPluginApplicationInfo(String packageName) throws RemoteException {
        return PluginManager.getInstance().getApplicationInfo(packageName, 0);
    }



    private static void tryfixServiceInfo(ActivityManager.RunningServiceInfo serviceInfo) {
        //传入的是代理服务，要改成插件自己服务的信息。
    }

}
