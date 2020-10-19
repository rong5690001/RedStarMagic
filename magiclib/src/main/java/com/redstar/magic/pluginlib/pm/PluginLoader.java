package com.redstar.magic.pluginlib.pm;

import android.content.Context;
import android.content.pm.PackageManager;

import com.redstar.magic.pluginlib.InstalledApk;
import com.redstar.magic.pluginlib.PluginApk;

/**
 * Created by chen.huarong on 2019-09-16.
 * 提供加载插件接口
 */
public interface PluginLoader {

    /**
     * 加载插件
     *
     * @param context          宿主上下文
     * @param installedApk     已安装的apk信息
     * @param veritySignatures 是否需要检验插件包
     * @return 插件信息
     * @throws PackageManager.NameNotFoundException
     */
    PluginApk load(Context context, InstalledApk installedApk, boolean veritySignatures) throws PackageManager.NameNotFoundException;

}
