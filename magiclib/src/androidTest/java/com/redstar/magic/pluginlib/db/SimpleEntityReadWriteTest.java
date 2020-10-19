package com.redstar.magic.pluginlib.db;

import com.redstar.magic.pluginlib.AppTest;
import com.redstar.magic.pluginlib.pm.version.Plugin;
import com.redstar.magic.pluginlib.pm.version.PluginDao;
import com.redstar.magic.pluginlib.pm.version.PluginDataBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by chen.huarong on 2019-09-27.
 */
@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest extends AppTest {
    private PluginDao mPluginDao;
    private PluginDataBase mDb;

    @Before
    public void createDb() {
        //在内存中创建db,进程kill掉后就被回收了
//        mDb = Room.inMemoryDatabaseBuilder(appContext, PluginDataBase.class).build();
        mDb = Room.databaseBuilder(appContext, PluginDataBase.class, "plugindb").build();
        mPluginDao = mDb.pluginDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void writeUserAndReadInList() {
        Plugin plugin = new Plugin();
        plugin.pluginName = "george";
        mPluginDao.insertPlugins(plugin);
        List<Plugin> byName = mPluginDao.findPluginsByName("george");
        assertThat(byName.get(0).pluginName, equalTo(plugin.pluginName));
    }
}
