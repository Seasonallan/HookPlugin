package com.season.plugindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout2 = new LinearLayout(this);
        layout2.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout2);
// 修复 Patch 中新增的 View 显示正常
        Button btn1 = new Button(this);
        Button btn2 = new Button(this);
        btn1.setText("Button1");
        btn2.setText("Button2");
        layout2.addView(btn1);
        layout2.addView(btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResourceActivity.class));
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResourceActivity.class));
            }
        });
//加载宿主 View 中的 layout， android >= 19 正常显示；android <= 18 显示白色，
// 使用调试工具 View Hierarchy 还可以看到宿主中的 View，就是显示不出来，看 Visible 也正常
//        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        //       layout2.addView(v);

    }
}
