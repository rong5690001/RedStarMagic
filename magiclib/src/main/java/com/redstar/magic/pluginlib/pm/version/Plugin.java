package com.redstar.magic.pluginlib.pm.version;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by chen.huarong on 2019-09-17.
 * 插件表
 */
@Entity
public class Plugin {

    @PrimaryKey(autoGenerate = true)
    public int id;

    //插件名称
    public String pluginName;

    //插件版本号
    public int pluginVersionCode;

    //插件版本名称
    public String pluginVersionName;

    //要求宿主最低版本号
    public int minHostVersionCode;

    //要求宿主最低版本名称
    public String minHostVersionName;

    //插件前一个版本的版本号
    public int pluginPreVersionCode;

    //插件前一个版本的版本名称
    public String pluginPreVersionName;

}
