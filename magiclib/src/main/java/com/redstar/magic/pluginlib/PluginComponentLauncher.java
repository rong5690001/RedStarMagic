package com.redstar.magic.pluginlib;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import com.redstar.magic.pluginlib.components.MagicContext;
import com.redstar.magic.pluginlib.exception.PluginNotInstalled;
import com.redstar.magic.pluginlib.pm.PluginManager;
import com.redstar.magic.pluginlib.proxy.activity.ProxyActivity;
import com.redstar.magic.pluginlib.proxy.activity.PluginProxyActivity;

/**
 * Created by chen.huarong on 2019-07-28.
 * 插件启动系统组件
 */
@Deprecated
public class PluginComponentLauncher implements IPluginComponentLauncher {

    @Override
    public boolean startActivity(MagicContext magicContext, Intent intent) {
        magicContext.superStartActivity(intent);
        return true;
    }

    @Override
    public boolean startActivityForResult(ProxyActivity delegator, Intent intent, int requestCode,
                                          Bundle option, ComponentName callingActivity) {
        try {
            delegator.startActivityForResult(convertPluginActivityIntent(intent), requestCode,
                        option);
            return true;
        } catch (PluginNotInstalled pluginNotInstalled) {
            pluginNotInstalled.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<Boolean, ComponentName> startService(MagicContext context, Intent service) {
        return null;
    }

    @Override
    public Pair<Boolean, Boolean> stopService(MagicContext context, Intent name) {
        return null;
    }

    @Override
    public Pair<Boolean, Boolean> bindService(MagicContext context, Intent service, ServiceConnection conn,
                                              int flags) {
        return null;
    }

    @Override
    public Pair<Boolean, ?> unbindService(MagicContext context, ServiceConnection conn) {
        return null;
    }

    @Override
    public Intent convertPluginActivityIntent(Intent pluginIntent) throws PluginNotInstalled {
        Intent intentProxy = new Intent(pluginIntent);
        ComponentName componentName = pluginIntent.getComponent();
        if (componentName == null) {
            throw new NullPointerException("component is null!!!");
        }
        String packageName = componentName.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("packageName is null!!!");
        }
        intentProxy.setExtrasClassLoader(PluginManager.getInstance().getPluginApk(packageName).mClassLoader);
        intentProxy.putExtra(KEY_CLASS_NAME, componentName.getClassName());
        intentProxy.setComponent(new ComponentName("com.redstar.magic", PluginProxyActivity.class.getName()));
        return intentProxy;
    }

}
