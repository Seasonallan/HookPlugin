package com.season.plugindemo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

public class ResourceActivity extends Activity {

    TextView mTextView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mContext = this;
        mTextView = (TextView) findViewById(R.id.plugin_text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
                Log.e("SeasonLog", "success");
            }
        });

        SimpleDraweeView mImageView = (SimpleDraweeView) findViewById(R.id.image);
        mImageView.setImageURI(Uri.parse("http://wx2.sinaimg.cn/mw690/6f048449gy1ffmzhttppoj20ht0eo7b2.jpg"));

        findViewById(R.id.plugin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResourceActivity.this, "success", Toast.LENGTH_SHORT).show();
                mTextView.setText(R.string.plugin_desc);
                Toast.makeText(ResourceActivity.this, "success", Toast.LENGTH_SHORT).show();
                Log.e("SeasonLog", "success");
            }
        });
//加载宿主 View 中的 layout， android >= 19 正常显示；android <= 18 显示白色，
// 使用调试工具 View Hierarchy 还可以看到宿主中的 View，就是显示不出来，看 Visible 也正常
//        View v = getLayoutInflater().inflate(R.layout.activity_main, null);
        //       layout2.addView(v);

    }
}
