package com.redstar.magic.pluginlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.redstar.magic.pluginlib.pm.PluginManager;
import com.redstar.magic.pluginlib.tools.Logger;
import com.redstar.magic.pluginlib.utils.IntentUtils;

/**
 * Created by chen.huarong on 2019-06-27.
 * 插件框架对宿主暴露的接口
 */
public class MagicPlugin {

    private static final String TAG = MagicPlugin.class.getSimpleName();

    public static void install(Context context) {
        PluginApplication.install(context);
    }

    //    /**
//     * hook系统ClassLoader
//     *
//     * @param base
//     */
//    static void hook(Context base) {
//        try {
//            // 获取mBase.mPackageInfo
//            // 1. ApplicationContext - Android 2.1
//            // 2. ContextImpl - Android 2.2 and higher
//            // 3. AppContextImpl - Android 2.2 and higher
//            Object mPackInfo = ReflectUtils.readField(base, "mPackageInfo");
//            // 获取mPackageInfo.mClassLoader
//            ClassLoader mSystemClassLoader = (ClassLoader) ReflectUtils.readField(mPackInfo,
//                    "mClassLoader");
//
//            //创建替你系统的ClassLoader
//            ClassLoader npClassLoader = new NiceMainClassLoader(mSystemClassLoader);
//            // 将新的ClassLoader写入mPackageInfo.mClassLoader
//
//            ReflectUtils.writeField(mPackInfo, "mClassLoader", npClassLoader);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 启动插件Activity
//     *
//     * @param context
//     * @param pkg
//     * @param intent
//     */
//    @Deprecated
//    public static boolean startActivity(Context context, String pkg, Intent intent) {
//        //插件是否安装 || 安装成功 -> 是否安装?安装成功:true
//        ComponentName componentName = intent.getComponent();
//        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, pkg);
//        if (isLoaded(pkg) || loadPlugin(pkg)) {
//            context.startActivity(transformIntentActivity(context, intent));
//            return true;
//        }
//        Logger.e(TAG, "插件{ %s }的activity{ %s } 启动失败", pkg, componentName.getClassName());
//        return false;
//    }

    /**
     * 启动插件Activity
     *
     * @param context
     * @param intent
     */
    public static boolean startActivity2(Context context, Intent intent) {
        //插件是否安装 || 安装成功 -> 是否安装?安装成功:true
        ComponentName componentName = intent.getComponent();

        String pkg = IntentUtils.getPackageName(intent);
        boolean result = loadPlugin(pkg);
        if (!result) {
            Logger.e(TAG, "插件{ %s }的activity{ %s } 启动失败", pkg, componentName.getClassName());
            return false;
        }
        return IntentUtils.startActivity(context, intent);
    }

    /**
     * 安装插件
     *
     * @param pkg
     */
    protected static boolean loadPlugin(String pkg) {
        if (PluginManager.getInstance().loadApk(pkg, false)) {
            return true;
        } else {
            Logger.e(TAG, "插件{ %s }安装失败", pkg);
            return false;
        }
    }

}
