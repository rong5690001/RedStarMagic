package com.redstar.magic.pluginlib.pm;

import android.content.Context;
import android.os.SystemClock;

import com.redstar.magic.pluginlib.AppTest;
import com.redstar.magic.pluginlib.exception.InstallException;
import com.redstar.magic.pluginlib.pm.bean.InstallResult;
import com.redstar.magic.pluginlib.utils.FileUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Created by chen.huarong on 2019-09-28.
 */
@RunWith(AndroidJUnit4.class)
public class PluginInstallerTest extends AppTest {

    @Test
    public void installFormAssetsTest() throws InstallException {
        PluginInstaller pluginInstaller = new PluginInstaller();
        InstallResult result = pluginInstaller.installFromAssets(appContext, pkg + ".apk", true, false);
        Assert.assertTrue(result.result);
    }

    @Test
    public void copyApkFromAssets() throws IOException {
        //临时文件
        File tmpFile = new File(appContext.getDir("assets_plugins", Context.MODE_PRIVATE),
                SystemClock.elapsedRealtime() + ".apk");
//        tmpFile.createNewFile();
        InputStream assetApkInputStream = appContext.getAssets().open(pkg + ".apk");
        FileUtils.copyInputStreamToFile(assetApkInputStream, tmpFile);
    }

    @Test
    public void copyTmpFile() throws IOException {
        File tmpFile = new File(appContext.getDir("assets_plugins", Context.MODE_PRIVATE),
                "3940678330.apk");
        File baseApk =
                new File(PathManager.getInstance().getPluginDirByPkg("com.redstar.magic")
                        , "base.apk");
        FileUtils.copyFile(tmpFile, baseApk);
    }
}
