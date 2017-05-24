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

package com.season.hookplugin.hookcore;

import android.content.Context;


import com.season.hookplugin.hookcore.handle.BaseHookHandle;
import com.season.hookplugin.hookcore.handle.BaseHookMethodHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Andy Zhang(zhangyong232@gmail.com) on 2015/3/14.
 */
public abstract class BaseHookProxy extends BaseHook implements InvocationHandler {

    protected Object mOldObj;

    public BaseHookProxy(Context hostContext) {
        super(hostContext);
        mHookHandles = createHookHandle();
    }
    protected BaseHookHandle mHookHandles;

    public void setOldObj(Object oldObj) {
        this.mOldObj = oldObj;
    }


    protected abstract BaseHookHandle createHookHandle();

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        BaseHookMethodHandler hookedMethodHandler = mHookHandles.getHookedMethodHandler(method);
        if (hookedMethodHandler != null) {
            return hookedMethodHandler.doHookInner(mOldObj, method, args);
        }
        return method.invoke(mOldObj, args);
    }
}
