package com.redstar.magic.pluginlib.pm.version;

import android.content.Context;

import androidx.room.Room;

/**
 * Created by chen.huarong on 2019-09-16.
 * 负责管理插件版本，是否需要升级
 */
public class PluginVersioner {

    private static final String DB_NAME = "db_plugin";
    private static PluginVersioner sInstance;
    private static PluginDataBase db;

    private PluginVersioner(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext()
                , PluginDataBase.class
                , "db_plugin").build();
    }

    public static PluginVersioner getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginVersioner.class) {
                if (sInstance == null) {
                    sInstance = new PluginVersioner(context);
                }
            }
        }
        return sInstance;
    }

    public boolean canInstall(int pluginMinHostCode, int hostVersionCode) {
        return pluginMinHostCode <= hostVersionCode;
    }

    public boolean rollback() {
        return false;
    }

}
