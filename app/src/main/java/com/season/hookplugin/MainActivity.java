package com.season.hookplugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.season.pluginlib.PluginManager;
import com.season.pluginlib.util.FileUtil;
import com.season.pluginlib.util.LogTool;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String assetsName = "plugindemo-debug.apk";
        assetsName = "EnglishV13.0.4.apk";
        FileUtil.copyAssets(assetsName);
        try {
            int result = PluginManager.getInstance().installPackage(getFileStreamPath(assetsName).getAbsolutePath(), 0);
            LogTool.log("installPackage>> "+result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        findViewById(R.id.main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HookUtil.hookActivityInstrumentation(MainActivity.this);
                // Intent intent = new Intent(MainActivity.this, PluginStubActivity.class);
                Intent intent = new Intent(MainActivity.this, UnRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);

                try {
                    Intent t = new Intent();
                    if (true) {
                        t.setComponent(new ComponentName("com.season.genglish",
                                "com.season.genglish.ui.SplashActivity"));
                    } else {
                        t.setComponent(new ComponentName("com.season.plugindemo",
                                "com.season.plugindemo.ResourceActivity"));
                        t.setComponent(new ComponentName("com.season.plugindemo",
                                "com.season.plugindemo.MainActivity"));
                        t.setComponent(new ComponentName("com.weishu.upf.ams_pms_hook.app",
                                "com.weishu.upf.ams_pms_hook.app.MainActivity"));
                    }
                    startActivity(t);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    //    LogTool.log("attachBaseContext");
        //保守方案：委托系统，让系统帮忙加载 使用单LoadedAPK，出现SeasonApplication.sApplicationContext.getPackageName()包名异常
        //sdcard apk文件
/*        String fileName = "plugindemo-debug.apk";
        File apkFile = new File(android.os.Environment.getExternalStorageDirectory(), fileName);
        FileUtil.copy(apkFile);
        FileUtil.upZipFile(apkFile);
        HookUtil.patchClassLoader(getClassLoader(), getFileStreamPath(fileName), getFileStreamPath("classes.dex"));*/

        //assets apk文件
/*        String assetsName = "plugindemo-debug.apk";
        FileUtil.copyAssets(assetsName);
        FileUtil.upZipFile(getFileStreamPath(assetsName));
        HookUtil.patchClassLoader(getClassLoader(), getFileStreamPath(assetsName), getFileStreamPath("classes.dex"));*/

    //    String assetsName = "plugindemo-debug.apk";
        // assetsName = "app-debug.apk";
     //   FileUtil.copyAssets(assetsName);
        //激进方案：Hook掉ClassLoader，使用多LoadedAPK
     //   HookUtil.hookClassLoader(getFileStreamPath(assetsName));

      //  HookUtil.hookResource(getFileStreamPath(assetsName).getAbsolutePath());
    }
}
