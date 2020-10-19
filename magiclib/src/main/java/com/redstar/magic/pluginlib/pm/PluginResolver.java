package com.redstar.magic.pluginlib.pm;

import android.content.Context;

/**
 * Created by chen.huarong on 2019-09-17.
 * 负责分解插件，提取插件信息
 */
public interface PluginResolver {

    /**
     * 解析插件apk
     *
     * @return
     * @param context
     */
    boolean resolve(Context context);

    /**
     * 将插件复制到指定位置
     *
     * @return 文件目标位置
     */
    String copyFromAssets(Context context);

    /**
     * 获取插件包名
     *
     * @return
     */
    String getPackageName();

    /**
     * 获取插件版本号
     * PS:  12
     *
     * @return
     */
    int getVersionCode();

    /**
     * 获取插件版本名称
     * PS;  1.1.0
     *
     * @return
     */
    String getVersionName();

    /**
     * 插件支持的最小宿主版本
     *
     * @return
     */
    int getMinHostVersion();
}
