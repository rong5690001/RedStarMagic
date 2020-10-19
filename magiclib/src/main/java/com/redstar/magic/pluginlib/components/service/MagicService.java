package com.redstar.magic.pluginlib.components.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import com.redstar.magic.pluginlib.components.MagicApplication;
import com.redstar.magic.pluginlib.components.MagicContext;

/**
 * 插件Service的父类
 * <p>
 * 负责将插件service的方法转交由{@link com.redstar.magic.pluginlib.proxy.service.PluginProxyService}处理
 *
 * @author chen.huarong on 2019-11-12
 */
public abstract class MagicService extends MagicContext {

    public final void setHostContextAsBase(Context context) {
        attachBaseContext(context);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {

    }

    public void onConfigurationChanged(Configuration newConfig) {

    }

    public void onLowMemory() {

    }

    public void onTrimMemory(int level) {

    }

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void onTaskRemoved(Intent rootIntent) {

    }

    public void onCreate() {

    }

    public void onRebind(Intent intent) {

    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
    }

    @Deprecated
    public final void setForeground(boolean isForeground) {
        //todo #37 支持Service设置Foreground
    }

    public final void startForeground(int id, Notification notification) {
        //mHostServiceDelegator.startForeground(id, notification);
        //todo #37 支持Service设置Foreground
    }

    public final void stopForeground(boolean removeNotification) {
        //todo #37 支持Service设置Foreground
        //mHostServiceDelegator.stopForeground(removeNotification);
    }

    public final void stopForeground(int flags) {

    }

    public final void stopSelf() {
        stopService(new Intent(this, getClass()));
    }

    /**
     * 插件环境下Service不支持调用带参数的stopSelf
     */
    public final void stopSelf(int startId) {
        stopSelf();
    }

    /**
     * 插件环境下Service不支持调用带参数的stopSelf
     */
    public final boolean stopSelfResult(int startId) {
        stopSelf();
        return true;
    }

    public final MagicApplication getApplication() {
        return mMagicApplication;
    }


}
