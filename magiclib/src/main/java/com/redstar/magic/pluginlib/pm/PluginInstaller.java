package com.redstar.magic.pluginlib.pm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;

import com.redstar.magic.pluginlib.exception.CopyNativeSoException;
import com.redstar.magic.pluginlib.exception.InstallException;
import com.redstar.magic.pluginlib.pm.bean.InstallResult;
import com.redstar.magic.pluginlib.tools.Logger;
import com.redstar.magic.pluginlib.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chen.huarong on 2019-09-16.
 * 负责安装/卸载插件
 */
public class PluginInstaller {

    private static final String TAG = PluginInstaller.class.getSimpleName();

    private static final String BASE_APK_FILE_NAME = "base.apk";

    /**
     * 安装下载的apk
     *
     * @param context
     * @param apkPath
     * @param checkVersion
     * @param checkSignatures
     * @return
     */
    public synchronized InstallResult install(Context context
            , String apkPath
            , boolean checkVersion
            , boolean checkSignatures) throws InstallException {
        try {
            install(context, checkSignatures, new File(apkPath));
        } catch (CopyNativeSoException
                | IOException e) {
            e.printStackTrace();
            return new InstallResult(false);
        }
        return new InstallResult(true);
    }

    /**
     * 安装 assets 中的插件
     * <p>
     * PS:先把assets中的apk复制到临时文件夹assets_plugins下
     *
     * @param assetsApkPath   位于 assets 中的安装包文件路径（相对于 assets 根目录，比如：
     *                        <code>"plugins/com.wlqq.phantom.plugin.test1_1.0.0.apk"</code>）
     * @param checkVersion    是否检查版本号，若为 true, 则仅支持升级安装
     * @param checkSignatures 是否校验签名，若为 true, 则插件与宿主签名一致才能安装
     * @return 安装结果
     */
    public synchronized InstallResult installFromAssets(Context context
            , String assetsApkPath
            , boolean checkVersion
            , boolean checkSignatures) throws InstallException {

        //临时文件
        File tmpFile = new File(context.getDir("assets_plugins", Context.MODE_PRIVATE),
                SystemClock.elapsedRealtime() + ".apk");

        try {
            InputStream assetApkInputStream = context.getAssets().open(assetsApkPath);
            FileUtils.copyInputStreamToFile(assetApkInputStream, tmpFile);

            return install(context, checkSignatures, tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new InstallResult(false);
        } catch (CopyNativeSoException e) {
            e.printStackTrace();
            return new InstallResult(false);
        }
    }

    @NotNull
    private InstallResult install(Context context, boolean checkSignatures, File tmpFile) throws InstallException, CopyNativeSoException, IOException {
        PackageManager packageManager = context.getPackageManager();
        int flag = PackageManager.GET_ACTIVITIES
                | PackageManager.GET_SERVICES;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (checkSignatures) {
                flag |= PackageManager.GET_SIGNING_CERTIFICATES;
            }
        }
        String apkFilePath = tmpFile.getAbsolutePath();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkFilePath, flag);
        if (packageInfo == null) {
            Logger.e(TAG, "插件加载失败,获取packageInfo为null");
            throw new InstallException(InstallException.ERROR_RESOLVE_APK);
        }
//            packageInfo.applicationInfo.publicSourceDir = apkFilePath;
//            packageInfo.applicationInfo.sourceDir = apkFilePath;
        //复制lib目录
        File libDir = PathManager.getInstance().getLibDirByPkg(packageInfo.packageName);
        NativeLibraryUtils.copyNativeBinaries(tmpFile, libDir);

        File baseApk =
                new File(PathManager.getInstance().getPluginDirByPkg(packageInfo.packageName)
                        , BASE_APK_FILE_NAME);
        FileUtils.copyFile(tmpFile, baseApk);

        return new InstallResult(true);
    }

    /**
     * 卸载插件
     *
     * @param pkg
     */
    public void uninstall(String pkg) {
        File pluginDir = PathManager.getInstance().getPluginDirByPkg(pkg);
        FileUtils.cleanDir(pluginDir);
    }

    /**
     * 根据包名判断插件是否已经安装
     *
     * @param pkg
     * @return
     */
    public boolean isInstall(String pkg) {
        return getBaseApk(pkg).exists();
    }

    /**
     * 获取安装成功标志文件
     *
     * @param pkg
     * @return
     */
    public File getBaseApk(String pkg) {
        return new File(PathManager.getInstance().getPluginDirByPkg(pkg), BASE_APK_FILE_NAME);
    }


}
