package com.redstar.magic.pluginlib.pm;

import com.redstar.magic.pluginlib.AppTest;
import com.redstar.magic.pluginlib.MagicPlugin;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.LogUtil;

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;

/**
 * Created by chen.huarong on 2019-09-28.
 */
@RunWith(AndroidJUnit4.class)
public class PathManagerTest extends AppTest {

    private static final String TAG = PathManagerTest.class.getSimpleName();

    /**
     * 测试获取插件文件夹
     */
    @Test
    public void testGetPluginDir() {
        MagicPlugin.install(appContext);
        File plugin = PathManager.getInstance().getPluginDirByPkg("designer");
        plugin.mkdirs();
        LogUtil.logDebugWithProcess(TAG, "pluginPath:%s", plugin.getAbsolutePath());
        assertThat("/data/user/0/com.redstar.magic.pluginlib.test/app_plugins/designer_1"
                , hasToString(plugin.getAbsolutePath()));
    }

    /**
     * 测试获取插件文件夹
     */
    @Test
    public void isInstallTest() throws IOException {
        MagicPlugin.install(appContext);
        File installedFile = new File(PathManager.getInstance().getPluginDirByPkg("designer"),
                "installed.txt");
        Assert.assertTrue(installedFile.mkdirs() && installedFile.createNewFile());
    }
}
