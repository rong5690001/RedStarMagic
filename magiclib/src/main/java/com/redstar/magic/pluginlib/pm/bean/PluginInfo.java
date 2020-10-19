package com.redstar.magic.pluginlib.pm.bean;

/**
 * Created by chen.huarong on 2019-09-16.
 * 记录插件安装前的基本信息
 */
public class PluginInfo {

    public String apkPath;

    public String name;

    public int versionCode;

    public String versionName;

    public boolean isInner;//是否在assets下

    public String fileName;//文件名
}
