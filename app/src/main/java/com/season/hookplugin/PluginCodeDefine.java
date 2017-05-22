package com.season.hookplugin;

/**
 * Disc: 插件使用状态控制
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-22 13:34
 */
public class PluginCodeDefine {

    public static final int PLUGIN_DISABLED = -1;

    public static final int INSTALL = 201701;
    public static final int INSTALL_UPDATE = 201702;
    public static final int DELETE = 201703;


    public static final int DELETE_SUCCEEDED = 100;
    public static final int DELETE_FAILED_INTERNAL_ERROR = -100;

    public static final int INSTALL_SUCCEEDED = 101;
    public static final int INSTALL_FAILED_INTERNAL_ERROR = -101;
    public static final int INSTALL_FAILED_INVALID_APK = -102;
    public static final int INSTALL_FAILED_PERMISSION_REQUESTED = -103;
    public static final int INSTALL_FAILED_ALREADY_EXISTS = -104;

    /**
     * 获取操作结果描述
     *
     * @return
     */
    public static String getCodeMessage(int code) {
        switch (code){
            case INSTALL_SUCCEEDED:
                return "安装成功";
            case INSTALL_FAILED_INTERNAL_ERROR:
                return "安装失败，内部错误";
            case INSTALL_FAILED_INVALID_APK:
                return "安装失败，APK文件错误";
            case INSTALL_FAILED_PERMISSION_REQUESTED:
                return "安装失败，插件权限太多";
            case INSTALL_FAILED_ALREADY_EXISTS:
                return "安装失败，已经存在该插件，请使用更新或卸载操作";
            case DELETE_SUCCEEDED:
                return "删除成功";
            case DELETE_FAILED_INTERNAL_ERROR:
                return "删除失败，内部错误";
        }

        if (code > 0) {
            return "操作成功";
        } else {
            return "操作失败";
        }
    }

}
