package com.redstar.magic.pluginlib;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Pair;

import com.redstar.magic.pluginlib.components.MagicContext;
import com.redstar.magic.pluginlib.exception.PluginNotInstalled;
import com.redstar.magic.pluginlib.proxy.activity.ProxyActivity;

/**
 * Created by chen.huarong on 2019-07-28.
 * 插件内部启动
 */
public interface IPluginComponentLauncher {

    String KEY_PLUGIN_NAME = "PLUGIN_NAME";
    String KEY_CLASS_NAME = "CLASS_NAME";

    /**
     * 启动Activity
     *
     * @param magicContext 启动context
     * @param intent        插件内传来的Intent.
     * @return <code>true</code>表示该Intent是为了启动插件内Activity的,已经被正确消费了.
     * <code>false</code>表示该Intent不是插件内的Activity.
     */
    boolean startActivity(MagicContext magicContext, Intent intent);

    /**
     * 启动Activity
     *
     * @param delegator       发起启动的activity的delegator
     * @param intent          插件内传来的Intent.
     * @param callingActivity 调用者
     * @return <code>true</code>表示该Intent是为了启动插件内Activity的,已经被正确消费了.
     * <code>false</code>表示该Intent不是插件内的Activity.
     */
    boolean startActivityForResult(ProxyActivity delegator, Intent intent,
                                   int requestCode, Bundle option,
                                   ComponentName callingActivity);

    Pair<Boolean, ComponentName> startService(MagicContext context, Intent service);

    Pair<Boolean, Boolean> stopService(MagicContext context, Intent name);

    Pair<Boolean, Boolean> bindService(MagicContext context, Intent service,
                                       ServiceConnection conn, int flags);

    Pair<Boolean, ?> unbindService(MagicContext context, ServiceConnection conn);

    /**
     * 转换Intent为启动代理Activity
     *
     * @param pluginIntent
     * @return
     */
    Intent convertPluginActivityIntent(Intent pluginIntent) throws PluginNotInstalled;

}
