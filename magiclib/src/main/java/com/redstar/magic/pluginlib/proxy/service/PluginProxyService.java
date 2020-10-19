package com.redstar.magic.pluginlib.proxy.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Pair;

import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.components.service.MagicService;
import com.redstar.magic.pluginlib.exception.PluginNotInstalled;
import com.redstar.magic.pluginlib.pm.PluginManager;
import com.redstar.magic.pluginlib.utils.IntentUtils;

import androidx.annotation.Nullable;

/**
 * 插件代理Service
 * 负责代理插件Service中与运行环境(Context)相关的方法
 *
 * @author chen.huarong on 2019-11-12
 */
public class PluginProxyService extends Service {

    /**
     * 插件Service
     */
    private MagicService mPluginService;
    private ClassLoader mPluginClassLoader;

    public PluginProxyService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent proxyIntent) {
        //创建插件Service
        try {
            mPluginService = createPluginService(proxyIntent);
            //执行插件Service生命周期
            mPluginService.onCreate();
            return mPluginService.onBind(proxyIntent);
        } catch (PluginNotInstalled
                | ClassNotFoundException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRebind(Intent intent) {
        if (mPluginService != null) {
            mPluginService.onRebind(intent);
        } else {
            super.onRebind(intent);
        }
    }

    @Override
    public boolean onUnbind(Intent proxyIntent) {
        if (mPluginService != null) {
            return mPluginService.onUnbind(proxyIntent);
        }
        return super.onUnbind(proxyIntent);
    }

    @Override
    public int onStartCommand(Intent proxyIntent, int flags, int startId) {
        if (proxyIntent.getComponent() == null) {
            return START_NOT_STICKY;
        }

        //创建插件Service
        try {
            mPluginService = createPluginService(proxyIntent);
            //执行插件Service生命周期
            mPluginService.onCreate();
            return mPluginService.onStartCommand(proxyIntent, flags, startId);
        } catch (PluginNotInstalled
                | ClassNotFoundException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
            return super.onStartCommand(proxyIntent, flags, startId);
        }
    }

    @Override
    public void onDestroy() {
        if (mPluginService != null) {
            mPluginService.onDestroy();
        } else {
            super.onDestroy();
        }
    }

    @Override
    public boolean stopService(Intent name) {
        if (mPluginService != null) {
            return mPluginService.stopService(name);
        } else {
            return super.stopService(name);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mPluginService != null) {
            mPluginService.onConfigurationChanged(newConfig);
        } else {
            super.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onLowMemory() {
        if (mPluginService != null) {
            mPluginService.onLowMemory();
        } else {
            super.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if (mPluginService != null) {
            mPluginService.onTrimMemory(level);
        } else {
            super.onTrimMemory(level);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (mPluginService != null) {
            mPluginService.onTaskRemoved(rootIntent);
        } else {
            super.onTaskRemoved(rootIntent);
        }
    }

    /**
     * 创建插件Service
     *
     * @param proxyIntent 代理Intent
     */
    private MagicService createPluginService(Intent proxyIntent) throws PluginNotInstalled,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        Pair<String, String> pair = IntentUtils.getPluginComponentInfo(proxyIntent);
        String packageName = pair.first;
        String serviceClassName = pair.second;

        PluginApk pluginApk = PluginManager.getInstance().getPluginApk(packageName);
        mPluginClassLoader = pluginApk.mClassLoader;
        Class serviceClass = mPluginClassLoader.loadClass(serviceClassName);
        mPluginService = (MagicService) serviceClass.newInstance();

        //初始化插件Service
        mPluginService.setHostContextAsBase(this);
        mPluginService.setPluginResources(pluginApk.mResources);
        mPluginService.setPluginClassLoader(mPluginClassLoader);
        return mPluginService;
    }


}
