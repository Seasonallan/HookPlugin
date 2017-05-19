// IPackageDataObserver.aidl
package com.season.pluginlib;

// Declare any non-default types here with import statements

interface IPackageDataObserver {
    void onRemoveCompleted(in String packageName, boolean succeeded);
}
