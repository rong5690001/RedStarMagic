package com.redstar.magic.pluginlib.proxy.service;import android.app.Service;import android.content.Intent;import android.os.IBinder;import com.redstar.magic.pluginlib.components.service.MagicService;import androidx.annotation.Nullable;/** * 插件代理Service * 负责代理插件Service中与运行环境(Context)相关的方法 * * @author chen.huarong on 2019-11-12 */public class PluginProxyService extends Service {    /**     * 插件Service     */    private MagicService pluginService;    public PluginProxyService() {    }    @Nullable    @Override    public IBinder onBind(Intent intent) {        return null;    }}