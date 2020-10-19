package com.redstar.magic.pluginlib.pm.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.redstar.magic.pluginlib.InstalledApk;
import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.pm.PluginLoader;
import com.redstar.magic.pluginlib.tools.Logger;

import dalvik.system.DexClassLoader;

/**
 * Created by chen.huarong on 2019-09-16.
 * 负责加载插件
 */
public class PluginLoaderImpl implements PluginLoader {

    private static final String TAG = PluginLoaderImpl.class.getSimpleName();

    @Override
    public PluginApk load(Context context, InstalledApk installedApk, boolean veritySignatures)
            throws PackageManager.NameNotFoundException {

        if (installedApk == null) {
            throw new NullPointerException("installedApk is null");
        }

        ClassLoader pluginClassLoader = createDexClassLoader(installedApk,
                context.getClassLoader());
        PackageManager packageManager = context.getPackageManager();
        int flag = PackageManager.GET_ACTIVITIES
                | PackageManager.GET_SERVICES;
        if (veritySignatures) {
            flag |= PackageManager.GET_SIGNING_CERTIFICATES;
        }
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(installedApk.apkFilePath,
                flag);
        if (packageInfo == null) {
            Logger.e(TAG, "插件加载失败,获取packageInfo为null");
            return null;
        }
        packageInfo.applicationInfo.publicSourceDir = installedApk.apkFilePath;
        packageInfo.applicationInfo.sourceDir = installedApk.apkFilePath;

        //优化：不用反射
        Resources resources =
                packageManager.getResourcesForApplication(packageInfo.applicationInfo);
        return new PluginApk(packageInfo, resources, pluginClassLoader);
    }

    /**
     * 创建访问插件apk的DexClassLoader对象
     *
     * @param apk
     * @return
     */
    private ClassLoader createDexClassLoader(InstalledApk apk, ClassLoader parent) {
        return new DexClassLoader(apk.apkFilePath, apk.oDexPath, null, parent);
    }
}
