package com.redstar.magic.pluginlib.pm.version;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by chen.huarong on 2019-09-17.
 * 插件表Dao
 */
@Dao
public interface PluginDao {

//    /**
//     * 根据PluginName查找插件的最新版本
//     *
//     * @param pluginName
//     * @return Plugin
//     */
//    @Query("select * from Plugin where versionCode == (select max(versionCode) from Plugin where " + "pluginName == :pluginName)")
//    Plugin findNewestPluginsByName(String pluginName);

    /**
     * 根据所有对应PluginName的数据
     *
     * @param pluginName
     * @return
     */
    @Query("select * from plugin where pluginName == :pluginName")
    List<Plugin> findPluginsByName(String pluginName);

    /**
     * 查找所有数据
     *
     * @return
     */
    @Query("select * from plugin")
    List<Plugin> findAllPlugin();

    /**
     * 插入
     *
     * @param plugins
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlugins(Plugin... plugins);

    /**
     * 更新
     *
     * @param plugins
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updatePlugins(Plugin... plugins);

    /**
     * 删除
     *
     * @param plugins
     */
    @Delete
    void deletePlugins(Plugin... plugins);

}
