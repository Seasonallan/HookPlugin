// IApplicationCallback.aidl
package com.season.pluginlib;

// Declare any non-default types here with import statements

interface IApplicationCallback {

    /**
     * API for package data change related callbacks from the Package Manager.
     * Some usage scenarios include deletion of cache directory, generate
     * statistics related to code, data, cache usage
     * {@hide}
     */
    Bundle onCallback(in Bundle extra);
}
