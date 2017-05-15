package com.season.hookplugin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.season.SeasonApplication;
import com.season.hookutil.HookUtil;
import com.season.hookutil.LogTool;
import com.season.hookutil.Utils;

import java.io.File;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        t.setComponent(new ComponentName("com.season.plugindemo",
                                "com.season.plugindemo.MainActivity"));
                    } else {
                        t.setComponent(new ComponentName("com.season.genglish",
                                "com.season.genglish.ui.SplashActivity"));
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
        LogTool.log(SeasonApplication.sApplicationContext.getPackageName());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        File apkFile = new File(android.os.Environment.getExternalStorageDirectory(), "plugindemo-debug.apk");

        //激进方案：Hook掉ClassLoader，使用多LoadedAPK
        //HookUtil.hookClassLoader(apkFile);

        //保守方案：委托系统，让系统帮忙加载 使用单LoadedAPK，出现SeasonApplication.sApplicationContext.getPackageName()包名异常
        Utils.copy(apkFile);
        Utils.upZipFile(apkFile);
        HookUtil.patchClassLoader(getClassLoader(), getFileStreamPath(apkFile.getName()), getFileStreamPath("classes.dex"));

        HookUtil.hookSystemHandler();
        HookUtil.hookAms();
    }
}
