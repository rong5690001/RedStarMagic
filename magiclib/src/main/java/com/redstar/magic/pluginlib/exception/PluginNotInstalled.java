package com.redstar.magic.pluginlib.exception;

import java.util.Locale;

/**
 * 插件未安装异常
 *
 * @author chen.huarong on 2019-11-13
 */
public class PluginNotInstalled extends Exception {

    public PluginNotInstalled() {
    }

    public PluginNotInstalled(String packageName) {
        super(String.format(Locale.CHINESE, "plugin %s is not installed!", packageName));
    }

    public PluginNotInstalled(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginNotInstalled(Throwable cause) {
        super(cause);
    }
}
