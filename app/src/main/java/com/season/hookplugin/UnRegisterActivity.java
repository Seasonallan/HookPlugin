package com.season.hookplugin;

import android.app.Activity;
import android.os.Bundle;

import com.season.pluginlib.util.LogTool;

public class UnRegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogTool.log("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.log("onPause");
    }
}
