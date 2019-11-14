package com.redstar.magic.pluginlib;

import android.content.ActivityNotFoundException;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 插件apk信息的实体对象
 */
public class PluginApk {

    public PackageInfo mPackageInfo;
    public Resources mResources;
    public AssetManager mAssetManager;
    public ClassLoader mClassLoader;

    @NonNull
    public final Map<String, ActivityInfo> mPluginActivities;

    public PluginApk(PackageInfo packageInfo, Resources resources, ClassLoader classLoader) {
        mPackageInfo = packageInfo;
        mResources = resources;
        mClassLoader = classLoader;
        mAssetManager = resources.getAssets();
        mPluginActivities = new HashMap<>();

        for (ActivityInfo activity : mPackageInfo.activities) {
            mPluginActivities.put(activity.name, activity);
        }
    }

    /**
     * 根据activityName获取activity信息
     *
     * @param pluginActivity 插件activityName
     * @return activity信息
     * @throws ActivityNotFoundException
     */
    public ActivityInfo getActivityInfo(String pluginActivity) throws ActivityNotFoundException {
        if (mPluginActivities.containsKey(pluginActivity)) {
            return mPluginActivities.get(pluginActivity);
        }

        throw new ActivityNotFoundException();
    }
}
