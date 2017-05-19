// IPluginManager.aidl
package com.season.pluginlib;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import com.season.pluginlib.IPackageDataObserver;
import com.season.pluginlib.IApplicationCallback;


import java.util.List;
// Declare any non-default types here with import statements

interface IPluginManager {


     //for my api
     boolean waitForReady();

     PackageInfo getPackageInfo(in String packageName, int flags);

     boolean isPluginPackage(in String packageName);

     ActivityInfo getActivityInfo(in ComponentName className, int flags);

     ActivityInfo getReceiverInfo(in ComponentName className, int flags);

     ServiceInfo getServiceInfo(in ComponentName className, int flags);

     ProviderInfo getProviderInfo(in ComponentName className, int flags);

     ResolveInfo resolveIntent(in Intent intent, in String resolvedType, int flags);

     List<ResolveInfo> queryIntentActivities(in Intent intent,in  String resolvedType, int flags);

     List<ResolveInfo> queryIntentReceivers(in Intent intent, String resolvedType, int flags);

     ResolveInfo resolveService(in Intent intent, String resolvedType, int flags);

     List<ResolveInfo> queryIntentServices(in Intent intent, String resolvedType, int flags);

     List<ResolveInfo> queryIntentContentProviders(in Intent intent, String resolvedType, int flags);

     List<PackageInfo> getInstalledPackages(int flags);

     List<ApplicationInfo> getInstalledApplications(int flags);

     PermissionInfo getPermissionInfo(in String name, int flags);

     List<PermissionInfo> queryPermissionsByGroup(in String group, int flags);

     PermissionGroupInfo getPermissionGroupInfo(in String name, int flags);

     List<PermissionGroupInfo> getAllPermissionGroups(int flags);

     ProviderInfo resolveContentProvider(in String name, int flags);

     void deleteApplicationCacheFiles(in String packageName,in  IPackageDataObserver observer);

     void clearApplicationUserData(in String packageName,in  IPackageDataObserver observer);

     ApplicationInfo getApplicationInfo(in String packageName, int flags);

     int installPackage(in String filepath,int flags);

     int deletePackage(in String packageName ,int flags);

     List<ActivityInfo> getReceivers(in String packageName ,int flags);

     List<IntentFilter> getReceiverIntentFilter(in ActivityInfo info);

     int checkSignatures(in String pkg1, in String pkg2);


      //////////////////////////////////////
      //
      //  THIS API FOR ACTIVITY MANAGER
      //
      //////////////////////////////////////

      ActivityInfo selectStubActivityInfo(in ActivityInfo targetInfo);
      ActivityInfo selectStubActivityInfoByIntent(in Intent targetIntent);

      ServiceInfo selectStubServiceInfo(in ServiceInfo targetInfo);
      ServiceInfo selectStubServiceInfoByIntent(in Intent targetIntent);
      ServiceInfo getTargetServiceInfo(in ServiceInfo stubInfo);

      ProviderInfo selectStubProviderInfo(in String name);

      List<String> getPackageNameByPid(in int pid);

      String getProcessNameByPid(in int pid);

      boolean killBackgroundProcesses(in String packageName);

      boolean killApplicationProcess(in String pluginPackageName);

      boolean forceStopPackage(in String pluginPackageName);

      boolean registerApplicationCallback(in IApplicationCallback callback);

      boolean unregisterApplicationCallback(in IApplicationCallback callback);

      void onActivityCreated(in ActivityInfo stubInfo,in ActivityInfo targetInfo);

      void onActivityDestory(in ActivityInfo stubInfo,in ActivityInfo targetInfo);

      void onServiceCreated(in ServiceInfo stubInfo,in ServiceInfo targetInfo);

      void onServiceDestory(in ServiceInfo stubInfo,in ServiceInfo targetInfo);

      void onProviderCreated(in ProviderInfo stubInfo,in ProviderInfo targetInfo);

      void reportMyProcessName(in String stubProcessName,in String targetProcessName, String targetPkg);

      void onActivtyOnNewIntent(in ActivityInfo stubInfo,in ActivityInfo targetInfo, in Intent intent);

      int getMyPid();
}
