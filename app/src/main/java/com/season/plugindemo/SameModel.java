package com.season.plugindemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Disc:  用于测试同一进程同一个类在自定义View中的异常
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-21 18:39
 */
public class SameModel  extends View {
    public String result = "plugindemo";

    public SameModel(Context context) {
        super(context);
    }

    public SameModel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SameModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}