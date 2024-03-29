package com.redstar.magic.pluginlib.pm;

import com.redstar.magic.pluginlib.AppTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Created by chen.huarong on 2019-10-08.
 */
@RunWith(AndroidJUnit4.class)
public class PluginManagerTest extends AppTest {

    @Test
    public void loadApkTest() throws InterruptedException {
        PluginManager pluginManager = PluginManager.getInstance();
        pluginManager.init(appContext);
        pluginManager.loadApk("com", true);
        Thread.sleep(10000);
    }

}
