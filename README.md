# HookPlugin
HookPlugin on Android
Android插件
基于DroidPlugin进行功能简化
只提供Activity和Service的插件化

使用方法：

1、添加依赖，将HookPluginLibrary工程引用到主项目中
2、使用插件，使用自定义Application继承PluginSupportApplication或者在Application中手动启动
 @Override
 public void onCreate() {
     super.onCreate();
     PluginHelper.getInstance().applicationOnCreate(getBaseContext());
 }
3、安装、更新、卸载插件,使用如下方法：
安装插件
int resultCode =  PluginHelper.getInstance().installApkFile(String apkFilePath);
更新插件
int resultCode =  PluginHelper.getInstance().updateApkFile(String apkFilePath);
强制安装插件，如果已经存在该插件，强制更新
int resultCode =  PluginHelper.getInstance().installApkFile(String apkFilePath, boolean forceUpdateIfExist)
删除卸载插件
int resultCode =  PluginHelper.getInstance().deletePackage(String packageName);

resultCode大于0则为成功，其他信息可以通过PluginCodeDefine.getCodeMessage(resultCode);查看

4、查看插件是否安装
PluginHelper.getInstance().isPackageInstall(String packageName)
