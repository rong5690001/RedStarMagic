package com.redstar.magic.pluginlib.pm;

import java.io.File;
import java.util.concurrent.Future;

/**
 * Created by chen.huarong on 2019-09-27.
 * 插件升级接口
 */
public interface PluginUpgrade {

    /**
     * 更新插件
     *
     * @return 下载后的文件，或者最新的文件
     */
    Future<File> update();

    /**
     * 获取本地最新的文件
     *
     * @return 本地最新的文件
     */
    File getLatest();
}
