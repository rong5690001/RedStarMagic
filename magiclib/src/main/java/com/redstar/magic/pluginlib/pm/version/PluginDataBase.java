package com.redstar.magic.pluginlib.pm.version;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by chen.huarong on 2019-09-27.
 * 数据库类
 */
@Database(version = 1, entities = {Plugin.class})
public abstract class PluginDataBase extends RoomDatabase {

    public abstract PluginDao pluginDao();

}
