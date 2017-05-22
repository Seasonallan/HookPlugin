package com.season.plugindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivitySingleTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);

        Button btn1 = new Button(this);
        Button btn2 = new Button(this);
        Button btn3 = new Button(this);
        linearLayout.addView(btn1);
        linearLayout.addView(btn2);
        linearLayout.addView(btn3);

        btn1.setText("当前进程 ：" + Binder.getCallingPid());
        btn2.setText("当前级别 ：" + PluginApplication.level);
        btn3.setText("下一级");

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginApplication.level ++;
                startActivity(new Intent(MainActivitySingleTask.this, MainActivitySingleTask.class));
            }
        });
    }
}
