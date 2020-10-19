package com.redstar.magic.pluginlib.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.exception.ActivityPoolException;
import com.redstar.magic.pluginlib.exception.PluginNotInstalled;
import com.redstar.magic.pluginlib.pm.PluginManager;
import com.redstar.magic.pluginlib.pool.ActivityPool;
import com.redstar.magic.pluginlib.pool.ServicePool;

import androidx.annotation.NonNull;

/**
 * Created by chen.huarong on 2019-11-11.
 * intent工具类，启动组件，转换intent
 */
public class IntentUtils {

    private static final String TAG = IntentUtils.class.getSimpleName();

    public static String KEY_PLUGIN_PACKAGE_NAME = "plugin_package_name";
    public static String KEY_CLASS_NAME = "class_name";

    /**
     * 根据转换后的intent获取插件的包名和类名
     *
     * @param proxyIntent 转换后的intent
     * @return 插件的包名和类名
     */
    public static Pair<String, String> getPluginComponentInfo(@NonNull Intent proxyIntent) {
        String packageName = proxyIntent.getStringExtra(KEY_PLUGIN_PACKAGE_NAME);
        String className = proxyIntent.getStringExtra(KEY_CLASS_NAME);
        return Pair.create(packageName, className);
    }

    /**
     * 根据intent获取包名
     *
     * @param intent
     * @return
     */
    public static String getPackageName(Intent intent) {
        if (intent.getComponent() != null) {
            return intent.getComponent().getPackageName();
        }
        return "";
    }

    /**
     * 宿主启动插件Activity
     *
     * @param context 宿主上下文
     * @param intent
     */
    public static boolean startActivity(Context context, Intent intent) {
        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, getPackageName(intent));
        try {
            context.startActivity(transformIntentActivity(context, intent));
            return true;
        } catch (ActivityPoolException | PluginNotInstalled e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 宿主启动插件Activity
     *
     * @param activity    代理activity
     * @param intent
     * @param requestCode
     */
    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode) {
        return startActivityForResult(activity, intent, requestCode, null);
    }

    /**
     * 宿主启动插件Activity
     *
     * @param activity    代理activity
     * @param intent
     * @param requestCode
     * @param option
     */
    public static boolean startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle option) {
        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, getPackageName(intent));
        try {
            activity.startActivityForResult(transformIntentActivity(activity.getBaseContext(), intent), requestCode, option);
            return true;
        } catch (ActivityPoolException | PluginNotInstalled e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 转换Intent为启动代理Activity
     *
     * @param context        宿主上下文
     * @param originalIntent
     * @return
     */
    public static Intent transformIntentActivity(Context context, Intent originalIntent) throws ActivityPoolException, PluginNotInstalled {
        Intent intentProxy = new Intent(originalIntent);
        ComponentName componentName = intentProxy.getComponent();
        if (componentName == null) {
            throw new NullPointerException("component is null!!!");
        }
        String packageName = componentName.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("packageName is null!!!");
        }
        intentProxy.setExtrasClassLoader(PluginManager.getInstance().getPluginApk(packageName).mClassLoader);
        intentProxy.putExtra(KEY_CLASS_NAME, componentName.getClassName());

        //获取占坑ActivityName
        String placeHolderActivity = "";
        PluginApk pluginApk = PluginManager.getInstance().getPluginApk(componentName.getPackageName());
        for (ActivityInfo activity : pluginApk.mPackageInfo.activities) {
            if (TextUtils.equals(componentName.getClassName(), activity.name)) {
                placeHolderActivity = ActivityPool.getActivity(activity.name, activity.launchMode);
            }
        }

        intentProxy.setComponent(new ComponentName(context, placeHolderActivity));
        return intentProxy;
    }

    /**
     * 宿主启动插件Activity
     *
     * @param context 宿主上下文
     * @param intent
     * @return
     */
    public static Pair<Boolean, ComponentName> startService(Context context, Intent intent) {
        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, getPackageName(intent));
        try {
            return new Pair<>(true, context.startService(transformIntentService(context, intent)));
        } catch (PluginNotInstalled pluginNotInstalled) {
            pluginNotInstalled.printStackTrace();
            return new Pair<>(false, null);
        }
    }

    /**
     * stop插件Service
     *
     * @param context
     * @param intent
     * @return
     */
    public static Pair<Boolean, Boolean> stopService(Context context, Intent intent) {
        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, getPackageName(intent));
        try {
            return new Pair<>(true, context.stopService(transformIntentService(context, intent)));
        } catch (PluginNotInstalled pluginNotInstalled) {
            pluginNotInstalled.printStackTrace();
            return new Pair<>(false, null);
        }
    }

    /**
     * 绑定插件Service
     *
     * @param context 宿主上下文
     * @param intent
     * @param conn
     * @param flags
     * @return
     */
    public static Pair<Boolean, Boolean> bindService(Context context, Intent intent, ServiceConnection conn, int flags) {
        intent.putExtra(KEY_PLUGIN_PACKAGE_NAME, getPackageName(intent));
        try {
            return new Pair<>(true, context.bindService(transformIntentService(context, intent), conn, flags));
        } catch (PluginNotInstalled pluginNotInstalled) {
            pluginNotInstalled.printStackTrace();
            return new Pair<>(false, null);
        }
    }

    /**
     * unbind插件Service
     *
     * @param context 宿主上下文
     * @param conn
     * @return
     */
    public static Pair<Boolean, Boolean> unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
        return new Pair<>(true, null);
    }

    /**
     * 转换Intent为启动代理Service
     *
     * @param context        宿主上下文
     * @param originalIntent
     * @return
     * @throws PluginNotInstalled
     */
    private static Intent transformIntentService(Context context, Intent originalIntent) throws PluginNotInstalled {
        Intent intentProxy = new Intent(originalIntent);
        ComponentName componentName = intentProxy.getComponent();
        if (componentName == null) {
            throw new NullPointerException("component is null!!!");
        }
        String packageName = componentName.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("packageName is null!!!");
        }
        intentProxy.setExtrasClassLoader(PluginManager.getInstance().getPluginApk(packageName).mClassLoader);
        intentProxy.putExtra(KEY_CLASS_NAME, componentName.getClassName());

        intentProxy.setComponent(new ComponentName(context, ServicePool.SERVICE_NAME));
        return intentProxy;
    }

//    public static Intent

//    /**
//     * 获取占坑ActivityName
//     * <p>
//     * 根据activity启动模式选择不同的占坑Activity
//     *
//    * @param componentName 真正要启动的activity的componentName
//     * @return 占坑ActivityName
//     */
//    @NotNull
//    private static String getPlaceHolderActivity(@NonNull ComponentName componentName) throws
//    ActivityPoolException {
//        PluginApk pluginApk =
//                PluginManager.getInstance().getPluginApk(componentName.getPackageName());
//        for (ActivityInfo activity : pluginApk.mPackageInfo.activities) {
//            if (TextUtils.equals(componentName.getClassName(), activity.name)) {
//                return ActivityPool.getActivity(activity.name, activity.launchMode);
//            }
//        }
//
//        throw new ActivityPoolException(" Unable to find explicit activity class " +
//        componentName.getClassName() + "; have you declared this activity in your
//        AndroidManifest.xml?");
//    }


}
