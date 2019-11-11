package com.redstar.magic.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * 插件apk信息的实体对象
 */
public class PluginApk {

    public PackageInfo mPackageInfo;
    public Resources mResources;
    public AssetManager mAssetManager;
    public ClassLoader mClassLoader;

    public PluginApk(PackageInfo packageInfo, Resources resources, ClassLoader classLoader) {
        mPackageInfo = packageInfo;
        mResources = resources;
        mClassLoader = classLoader;
        mAssetManager = resources.getAssets();
    }
}
