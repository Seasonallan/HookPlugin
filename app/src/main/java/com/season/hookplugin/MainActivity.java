package com.season.hookplugin;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.season.hookplugin.adapter.ApkItem;
import com.season.hookplugin.adapter.MainAdapter;
import com.season.pluginlib.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.id_listview);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);

        new LoadApkInfoAsyncTask().execute();
    }

    public class LoadApkInfoAsyncTask extends AsyncTask<Void, Void, List<ApkItem>>{

        @Override
        protected List<ApkItem> doInBackground(Void... params) {
            List<ApkItem> result = new ArrayList<>();
            String[] assetsNames = {"EnglishV13.0.4.apk","plugindemo-debug.apk","app-debug.apk"};
            PackageManager pm = getPackageManager();
            for (String assetsName : assetsNames) {
                FileUtil.copyAssets(assetsName);
                File apkFile = getFileStreamPath(assetsName);
                PackageInfo info = pm.getPackageArchiveInfo(apkFile.getPath(), 0);
                ApkItem apkItem = new ApkItem(MainActivity.this, info, apkFile.getPath());
                result.add(apkItem);
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<ApkItem> apkItems) {
            super.onPostExecute(apkItems);
            mSwipeLayout.setRefreshing(false);
            mAdapter = new MainAdapter(MainActivity.this, apkItems);
            mListView.setAdapter(mAdapter);
        }
    }


    @Override
    public void onRefresh() {
        new LoadApkInfoAsyncTask().execute();
    }
}
