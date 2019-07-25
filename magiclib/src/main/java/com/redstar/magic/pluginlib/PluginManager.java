package com.redstar.magic.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {

    private static final PluginManager ourInstance = new PluginManager();

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }

    private Context mContext;
    private PluginApk mPluginApk;
    private InstalledApk mInstalledApk;

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 加载插件apk
     *
     * @param apkPath
     */
    public void loadApk(String apkPath) {
        if (mPluginApk != null) {
            Toast.makeText(mContext, "已加载", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = mContext.getDir("dex", Context.MODE_PRIVATE);
        mInstalledApk = new InstalledApk(apkPath, file.getAbsolutePath(), apkPath);
        ClassLoader classLoader = createDexClassLoader(mInstalledApk);

        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_SERVICES);

        if (packageInfo == null) {
            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        packageInfo.applicationInfo.publicSourceDir = apkPath;
        packageInfo.applicationInfo.sourceDir = apkPath;
//        AssetManager am = createAssetManager(apkPath);
//        Resources resources = createResources(am);
        //优化：不用反射
        try {
            Resources resources;
            resources = packageManager.getResourcesForApplication(packageInfo.applicationInfo);
            mPluginApk = new PluginApk(packageInfo, resources, classLoader);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PluginApk getPluginApk() {
        return mPluginApk;
    }

    /**
     * 创建访问插件apk的DexClassLoader对象
     *
     * @param apk
     * @return
     */
    private ClassLoader createDexClassLoader(InstalledApk apk) {
//        File file = mContext.getDir("dex", Context.MODE_PRIVATE);
        return new DexClassLoader(apk.apkFilePath, apk.oDexPath, null, mContext.getClassLoader());
//        return new ApkClassLoader(apk, mContext.getClassLoader(), null, 1);
    }

    /**
     * 创建访问插件apk资源的AssetManager对象
     *
     * @param apkPath
     * @return
     */
    private AssetManager createAssetManager(String apkPath) {

        try {
            AssetManager am = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(am, apkPath);
            return am;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建访问插件apk资源的Resources对象
     *
     * @param am
     * @return
     */
    private Resources createResources(AssetManager am) {
        Resources res = mContext.getResources();
        return new Resources(am, res.getDisplayMetrics(), res.getConfiguration());
    }

}
