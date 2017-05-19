package com.season.pluginlib.util;

import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;

import com.season.hookplugin.SeasonApplication;
import com.season.pluginlib.reflect.BuildCompat;
import com.season.pluginlib.reflect.MethodUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author weishu
 * @date 16/3/29
 */
public class FileUtil {

    /**
     * 解压缩APK文件里面的classes.dex到cacheDir，本地sdcard目录不可
     */
    public static void upZipFile(File zipFile) {
        try {

            ZipFile zf = new ZipFile(zipFile);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String fileName = entry.getName();
                if (fileName.equals("classes.dex")){
                    LogTool.log("copy file classes.dex");
                    InputStream in = zf.getInputStream(entry);
                    File desFile = SeasonApplication.sApplicationContext.getFileStreamPath(entry.getName());
                    // str = new String(str.getBytes("8859_1"), "GB2312");
                   // File desFile = new File(str, java.net.URLEncoder.encode(
                    //        entry.getName(), "UTF-8"));

                    if (!desFile.exists()) {
                        File fileParentDir = desFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                    }

                    OutputStream out = new FileOutputStream(desFile);
                    byte buffer[] = new byte[1024 * 1024];
                    int realLength = in.read(buffer);
                    while (realLength != -1) {
                        out.write(buffer, 0, realLength);
                        realLength = in.read(buffer);
                    }

                    out.close();
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 文件复制到 /data/data/files 目录下
     *
     * @param sourceFile
     */
    public static void copy(File sourceFile) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(sourceFile);
            File extractFile = SeasonApplication.sApplicationContext.getFileStreamPath(sourceFile.getName());
            fos = new FileOutputStream(extractFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(fos);
        }

    }

    /**
     * 把Assets里面得文件复制到 /data/data/files 目录下
     *
     * @param sourceName
     */
    public static void copyAssets(String sourceName) {
        AssetManager am = SeasonApplication.sApplicationContext.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = am.open(sourceName);
            File extractFile = SeasonApplication.sApplicationContext.getFileStreamPath(sourceName);
            fos = new FileOutputStream(extractFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(fos);
        }

    }

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     */
    public static File getPluginOptDexDir(String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(packageName), "odex"));
    }

    /**
     * 插件得lib库路径, 这个demo里面没有用
     */
    public static File getPluginLibDir(String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(packageName), "lib"));
    }

    /**
     *
     * 插件得lib库路径, 这个demo里面没有用
     */
    public static File getPluginCacheDir(String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(packageName), "cache"));
    }

    // --------------------------------------------------------------------------
    private static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable e) {
            // ignore
        }
    }

    private static File sBaseDir;

    // 需要加载得插件得基本目录 /data/data/<package>/files/plugin/
    private static File getPluginBaseDir(String packageName) {
        if (sBaseDir == null) {
            sBaseDir = SeasonApplication.sApplicationContext.getFileStreamPath("plugin");
            enforceDirExists(sBaseDir);
        }
        return enforceDirExists(new File(sBaseDir, packageName));
    }

    private static synchronized File enforceDirExists(File sBaseDir) {
        if (!sBaseDir.exists()) {
            boolean ret = sBaseDir.mkdir();
            if (!ret) {
                throw new RuntimeException("create dir " + sBaseDir + "failed");
            }
        }
        return sBaseDir;
    }




    private String findSoPath(Set<String> soPaths, String soName) {
        if (soPaths != null && soPaths.size() > 0) {
            if (is64Bit()) {
                //在宿主程序运行在64位进程中的时候，插件的so也只拷贝64位，否则会出现不支持的情况。
                String[] supported64BitAbis = BuildCompat.SUPPORTED_64_BIT_ABIS;
                Arrays.sort(supported64BitAbis);
                for (String soPath : soPaths) {
                    String abi = soPath.replaceFirst("lib/", "");
                    abi = abi.replace("/" + soName, "");

                    if (!TextUtils.isEmpty(abi) && Arrays.binarySearch(supported64BitAbis, abi) >= 0) {
                        return soPath;
                    }
                }
            } else {
                //在宿主程序运行在32位进程中的时候，插件的so也只拷贝64位，否则会出现不支持的情况。
                String[] supported32BitAbis = BuildCompat.SUPPORTED_32_BIT_ABIS;
                Arrays.sort(supported32BitAbis);
                for (String soPath : soPaths) {
                    String abi = soPath.replaceFirst("lib/", "");
                    abi = abi.replace("/" + soName, "");
                    if (!TextUtils.isEmpty(abi) && Arrays.binarySearch(supported32BitAbis, abi) >= 0) {
                        return soPath;
                    }
                }
            }
        }
        return null;
    }

    public final static boolean is64Bit() {
        //  dalvik.system.VMRuntime.getRuntime().is64Bit();
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return false;
            }
            Class VMRuntime = Class.forName("dalvik.system.VMRuntime");
            Object VMRuntimeObj = MethodUtils.invokeStaticMethod(VMRuntime, "getRuntime");
            Object is64Bit = MethodUtils.invokeMethod(VMRuntimeObj, "is64Bit");
            if (is64Bit instanceof Boolean) {
                return ((Boolean) is64Bit);
            }
        } catch (Throwable e) {
          //  LogPlugin.w(TAG, "is64Bit", e);
        }
        return false;
    }
}
