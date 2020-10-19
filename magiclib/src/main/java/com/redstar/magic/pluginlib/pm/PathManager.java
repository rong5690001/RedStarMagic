package com.redstar.magic.pluginlib.pm;

import android.content.Context;

import com.redstar.magic.pluginlib.PluginApplication;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by chen.huarong on 2019-09-28.
 * 插件路径管理
 */
public class PathManager {

    /**
     * 插件根目录名称
     */
    private static final String DIR_PLUGIN = "plugins";

    /**
     * lib目录(so)
     */
    private static final String DIR_PLUGIN_LIB = "lib";

    /**
     * 优化后的dex目录
     */
    private static final String DIR_PLUGIN_DEX = "oat";

    /**
     * 插件下载目录
     */
    private static final String DIR_PLUGIN_DOWNLOAD = "download";

    private static PathManager sInstance = new PathManager();

    private File mPluginsDir;

    private File mDownloadDir;

    public static PathManager getInstance() {
        return sInstance;
    }

    public File getPluginsDir() {
        if (mPluginsDir == null) {
            mPluginsDir = PluginApplication.getAppContext().getDir(DIR_PLUGIN, Context.MODE_PRIVATE);
        }
        return mkdirsIfNeed(mPluginsDir);
    }

    /**
     * 根据插件包名获取插件目录
     *
     * @param pkg
     * @return
     */
    public File getPluginDirByPkg(String pkg) {
        File pluginDir = new File(getPluginsDir().getAbsolutePath(), pkg);
        return mkdirsIfNeed(pluginDir);
    }

    /**
     * 根据插件包名获取lib文件目录
     *
     * @param pkg
     * @return
     */
    public File getLibDirByPkg(String pkg) {
        File libDir = new File(getPluginDirByPkg(pkg), DIR_PLUGIN_LIB);
        return mkdirsIfNeed(libDir);
    }

    /**
     * 根据插件包名获取oat文件目录
     *
     * @param pkg
     * @return
     */
    public File getDexDirByPkg(String pkg) {
        File dexDir = new File(getPluginDirByPkg(pkg), DIR_PLUGIN_DEX);
        return mkdirsIfNeed(dexDir);
    }

    /**
     * 获取所有插件下载文件夹
     *
     * @return
     */
    public File getDownloadDir() {
        if (mDownloadDir == null) {
            mDownloadDir = PluginApplication.getAppContext().getDir(DIR_PLUGIN_DOWNLOAD, Context.MODE_PRIVATE);
        }
        return mkdirsIfNeed(mDownloadDir);
    }

    /**
     * 根据包名获取对应插件下载文件夹
     *
     * @param pkg 插件包名
     * @return
     */
    public File getDownloadTargetDirByPkg(String pkg) {
        File downloadDir = new File(getDownloadDir(), pkg);
        return mkdirsIfNeed(downloadDir);
    }

    /**
     * 获取已下载插件文件路径
     *
     * @param pkg
     * @return
     */
    public File getDownloadPluginFileByPkg(String pkg) {
        return new File(getDownloadTargetDirByPkg(pkg), pkg + ".apk");
    }

    /**
     * 创建文件夹
     *
     * @param downloadDir
     * @return
     */
    @NotNull
    private File mkdirsIfNeed(File downloadDir) {
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }
}
