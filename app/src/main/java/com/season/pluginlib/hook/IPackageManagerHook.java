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

package com.season.pluginlib.hook;

import android.content.Context;
import android.content.pm.PackageManager;

import com.season.pluginlib.compat.ActivityThreadCompat;
import com.season.pluginlib.hookhandle.BaseHookHandle;
import com.season.pluginlib.hookhandle.IPackageManagerHookHandle;
import com.season.pluginlib.reflect.FieldUtils;
import com.season.pluginlib.reflect.Utils;
import com.season.pluginlib.util.MyProxy;

import java.util.List;


/**
 * Hook some function on IPackageManager
 * <p/>
 * Code by Andy Zhang (zhangyong232@gmail.com) on  2015/2/5.
 */
public class IPackageManagerHook extends ProxyHook {

    private static final String TAG = IPackageManagerHook.class.getSimpleName();

    public IPackageManagerHook(Context hostContext) {
        super(hostContext);
    }

    @Override
    protected BaseHookHandle createHookHandle() {
        return new IPackageManagerHookHandle(mHostContext);
    }

    @Override
    public void onInstall(ClassLoader classLoader) throws Throwable {
        Object currentActivityThread = ActivityThreadCompat.currentActivityThread();
        setOldObj(FieldUtils.readField(currentActivityThread, "sPackageManager"));
        Class<?> iPmClass = mOldObj.getClass();
        List<Class<?>> interfaces = Utils.getAllInterfaces(iPmClass);
        Class[] ifs = interfaces != null && interfaces.size() > 0 ? interfaces.toArray(new Class[interfaces.size()]) : new Class[0];
        Object newPm = MyProxy.newProxyInstance(iPmClass.getClassLoader(), ifs, this);
        FieldUtils.writeField(currentActivityThread, "sPackageManager", newPm);
        PackageManager pm = mHostContext.getPackageManager();
        Object mPM = FieldUtils.readField(pm, "mPM");
        if (mPM != newPm) {
            FieldUtils.writeField(pm, "mPM", newPm);
        }
    }


    public static void fixContextPackageManager(Context context) {
        try {
            Object currentActivityThread = ActivityThreadCompat.currentActivityThread();
            Object newPm = FieldUtils.readField(currentActivityThread, "sPackageManager");
            PackageManager pm = context.getPackageManager();
            Object mPM = FieldUtils.readField(pm, "mPM");
            if (mPM != newPm) {
                FieldUtils.writeField(pm, "mPM", newPm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}