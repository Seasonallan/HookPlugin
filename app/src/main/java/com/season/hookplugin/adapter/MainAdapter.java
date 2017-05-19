package com.season.hookplugin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.season.hookplugin.SeasonApplication;
import com.season.hookplugin.R;
import com.season.pluginlib.PluginManager;

import java.util.List;

/**
 * Disc:
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-18 13:22
 */
public class MainAdapter extends BaseAdapter {

    Context mContext;
    List<ApkItem> mLists;
    LayoutInflater mInflater = null;

    public MainAdapter(Context context, List<ApkItem> assetsNames){
        this.mContext = context;
        this.mLists = assetsNames;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public ApkItem getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //如果缓存convertView为空，则需要创建View
        if(convertView == null)
        {
            holder = new ViewHolder();
            //根据自定义的Item布局加载布局
            convertView = mInflater.inflate(R.layout.item_main, null);
            holder.img = (ImageView)convertView.findViewById(R.id.imageView);
            holder.title = (TextView)convertView.findViewById(R.id.textView1);
            holder.info = (TextView)convertView.findViewById(R.id.textView2);

            holder.btnLeft = (Button)convertView.findViewById(R.id.button3);
            holder.btnRight = (Button)convertView.findViewById(R.id.button2);
            holder.btnLeft.setTag(position);
            holder.btnRight.setTag(position);

            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        ApkItem item = getItem(position);

        holder.img.setImageDrawable(item.icon);
        holder.title.setText(item.title);
        holder.info.setText(String.format("%s(%s)", item.versionName, item.versionCode));


        holder.btnLeft.setText("打开");
        holder.btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                open(getItem(position));
            }
        });

        try {
            if (item.installing) {
                holder.btnRight.setText("安装中ing");
            } else {
                holder.btnRight.setText(PluginManager.getInstance().getPackageInfo(item.packageInfo.packageName, 0) != null ? "卸载" : "安装");
            }
        } catch (Exception e) {
            holder.btnRight.setText("安装1");
        }
        holder.btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                ApkItem item = getItem(position);
                try {
                    if (PluginManager.getInstance().getPackageInfo(item.packageInfo.packageName, 0) != null){
                        doUninstall(item);
                    }else{
                        doInstall(item);
                    }
                } catch (Exception e) {

                }
                //onListItemClick(getListView(), view, position, getItemId(position));
            }
        });
        return convertView;
    }

    Activity getActivity(){
        return (Activity) mContext;
    }

    private void open(ApkItem item){
        PackageManager pm = getActivity().getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(item.packageInfo.packageName);
        if (intent == null){
            Toast.makeText(getActivity(), "插件未安装", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }


    private synchronized void doInstall(final ApkItem item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                item.installing = true;
                SeasonApplication.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
                try {
                    final int re = PluginManager.getInstance().installPackage(item.apkfile, 0);
                    item.installing = false;
                    SeasonApplication.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), re == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION ? "安装失败，文件请求的权限太多" : "安装完成 " + re, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doUninstall(final ApkItem item) {
        try {
            PluginManager.getInstance().deletePackage(item.packageInfo.packageName, 0);
            Toast.makeText(getActivity(), "删除完成", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;

        public Button btnLeft, btnRight;
    }

}
