package com.season.plugindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);


        Button btn1 = new Button(this);
        Button btn2 = new Button(this);
        Button btn3 = new Button(this);
        Button btn4 = new Button(this);
        Button btn5 = new Button(this);
        linearLayout.addView(btn1);
        linearLayout.addView(btn2);
        linearLayout.addView(btn3);
        linearLayout.addView(btn4);
        linearLayout.addView(btn5);


        btn1.setText("standard模式");
        btn2.setText("singleInstance模式");
        btn3.setText("singleTask模式");
        btn4.setText("singleTop模式");
        btn5.setText("资源加载测试");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivityStandard.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivitySingleInstance.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivitySingleTask.class));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivitySingleTop.class));
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResourceActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PluginApplication.level = 0;
    }
}
