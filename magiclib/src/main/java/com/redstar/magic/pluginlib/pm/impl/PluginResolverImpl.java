package com.redstar.magic.pluginlib.pm.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.redstar.magic.pluginlib.InstalledApk;
import com.redstar.magic.pluginlib.pm.PathManager;
import com.redstar.magic.pluginlib.pm.bean.PluginInfo;
import com.redstar.magic.pluginlib.pm.PluginResolver;
import com.redstar.magic.pluginlib.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;

/**
 * Created by chen.huarong on 2019-09-17.
 */
public class PluginResolverImpl implements PluginResolver {

    private PluginInfo mPluginInfo;
    private final InstalledApk mInstalledApk;

    public PluginResolverImpl(PluginInfo pluginInfo) {
        mPluginInfo = pluginInfo;
        mInstalledApk = new InstalledApk();
    }

    @Override
    public boolean resolve(Context context) {
        try {
//            InputStream inputStream;
//            if (mPluginInfo.isInner) {//内置apk
//                inputStream = context.getAssets().open(mPluginInfo.fileName);
//            } else {
//
//            }
            File apkFile = new File(mPluginInfo.apkPath);
            ZipFile zipFile = new ZipFile(apkFile);
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry("config.json"));
            StringBuilder jsonStr = new StringBuilder();
            BufferedReader br;
            String rs;
            br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            while ((rs = br.readLine()) != null) {
                jsonStr.append(rs);
            }
            br.close();
            mPluginInfo = new Gson().fromJson(jsonStr.toString(), PluginInfo.class);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String copyFromAssets(Context context) {
        String filePath = FileUtils.copyAssetToFile(context, mPluginInfo.fileName
                , PathManager.getInstance().getPluginDirByPkg(mPluginInfo.fileName));
        mInstalledApk.apkFilePath = filePath;
        return filePath;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public int getVersionCode() {
        return mPluginInfo.versionCode;
    }

    @Override
    public String getVersionName() {
        return null;
    }

    @Override
    public int getMinHostVersion() {
        return 0;
    }

}
