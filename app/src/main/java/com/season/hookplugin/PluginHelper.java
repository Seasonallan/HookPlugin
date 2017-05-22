package com.season.hookplugin;

import android.content.Context;

import com.season.hookplugin.core.PluginManager;

/**
 * Disc: 插件对外开放功能
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-17 09:19
 */
public class PluginHelper {

    private static PluginHelper sInstance = null;

    private PluginHelper() {
    }

    public static final PluginHelper getInstance() {
        if (sInstance == null) {
            sInstance = new PluginHelper();
        }
        return sInstance;
    }

    /**
     * 启动插件功能
     *
     * @param context
     */
    public void startPlugin(Context context) {
        PluginManager.getInstance().bindPluginManagerToService(context);
    }


    /**
     * 插件功能是否可以使用
     *
     * @return
     */
    public boolean isPluginInstall() {
        return PluginManager.getInstance().isConnected();
    }


    /**
     * 检测插件是否安装
     *
     * @param packageName
     * @return
     */
    public boolean isPackageInstall(String packageName) {
        return PluginManager.getInstance().getPackageInfo(packageName, 0) != null;
    }


    /**
     * 安装文件路径下的插件
     *
     * @param apkFilePath
     * @return
     */
    public int installApkFile(String apkFilePath) {
        if (isPluginInstall()) {
            return PluginManager.getInstance().installPackage(apkFilePath, PluginCodeDefine.INSTALL);
        }
        return PluginCodeDefine.PLUGIN_DISABLED;
    }

    /**
     *  安装文件入插件
     * @param apkFilePath
     * @param forceUpdateIfExist 设置为true后，如果插件已经存在，会强制更新
     * @return
     */
    public int installApkFile(String apkFilePath, boolean forceUpdateIfExist) {
        if (isPluginInstall()) {
            int result = PluginManager.getInstance().installPackage(apkFilePath, PluginCodeDefine.INSTALL);
            if (forceUpdateIfExist && result == PluginCodeDefine.INSTALL_FAILED_ALREADY_EXISTS){
                return updateApkFile(apkFilePath);
            }
        }
        return PluginCodeDefine.PLUGIN_DISABLED;
    }

    /**
     * 更新插件
     *
     * @param apkFilePath
     * @return
     */
    public int updateApkFile(String apkFilePath) {
        if (isPluginInstall()) {
            return PluginManager.getInstance().installPackage(apkFilePath, PluginCodeDefine.INSTALL_UPDATE);
        }
        return PluginCodeDefine.PLUGIN_DISABLED;
    }


    /**
     * 删除卸载插件
     *
     * @param packageName
     * @return
     */
    public int deletePackage(String packageName) {
        if (isPluginInstall()) {
            return PluginManager.getInstance().deletePackage(packageName, PluginCodeDefine.DELETE);
        }
        return PluginCodeDefine.PLUGIN_DISABLED;
    }

}







