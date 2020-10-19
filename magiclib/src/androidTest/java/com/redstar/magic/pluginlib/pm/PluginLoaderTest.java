package com.redstar.magic.pluginlib.pm;

import android.content.pm.PackageManager;

import com.redstar.magic.pluginlib.AppTest;
import com.redstar.magic.pluginlib.InstalledApk;
import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.pm.impl.PluginLoaderImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Created by chen.huarong on 2019-10-08.
 */
@RunWith(AndroidJUnit4.class)
public class PluginLoaderTest extends AppTest {
    @Test
    public void loadTest() throws PackageManager.NameNotFoundException {
        InstalledApk installedApk = new InstalledApk();
        installedApk.apkFilePath =
                new File(PathManager.getInstance().getPluginDirByPkg(pkg).getAbsolutePath(),
                        "base.apk").getAbsolutePath();
        installedApk.oDexPath = PathManager.getInstance().getDexDirByPkg(pkg).getAbsolutePath();
        PluginLoader pluginLoader = new PluginLoaderImpl();
        PluginApk pluginApk = pluginLoader.load(appContext, installedApk, true);
        Assert.assertEquals(pkg, pluginApk.mPackageInfo.packageName);
    }
}
