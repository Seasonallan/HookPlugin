package com.season.pluginlib.util;

import android.util.Log;

/**
 * Disc: 日志输出
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-15 14:29
 */
public class LogTool {

    private static final String TAG = "SeasonLog";

    /**
     * 杂乱
     * @param log
     */
    public static void log(String log){
        //Log.e(TAG, log);
    }

    /**
     * 进程日志
     * @param log
     */
    public static void logV3(String log){
        Log.e(TAG, log);
    }
}
