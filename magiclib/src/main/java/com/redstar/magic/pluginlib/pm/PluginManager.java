package com.redstar.magic.pluginlib.pm;

import android.content.Context;
import android.content.pm.PackageManager;

import com.redstar.magic.pluginlib.InstalledApk;
import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.download.Downloader;
import com.redstar.magic.pluginlib.download.OnDownloadListener;
import com.redstar.magic.pluginlib.exception.InstallException;
import com.redstar.magic.pluginlib.exception.PluginNotInstalled;
import com.redstar.magic.pluginlib.pm.bean.InstallResult;
import com.redstar.magic.pluginlib.pm.impl.PluginLoaderImpl;
import com.redstar.magic.pluginlib.tools.Logger;
import com.redstar.magic.pluginlib.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Call;

public class PluginManager {

    private static final String TAG = PluginManager.class.getSimpleName();

    /**
     * 插件根目录名称
     */
    private static final String DIR_PLUGIN = "plugins";

    /**
     * 优化后的dex目录
     */
    private static final String DIR_PLUGIN_DEX = "odex";
    /**
     * 插件根目录
     */
    private static File mPluginRootDir;

    private static final PluginManager sInstance = new PluginManager();

    public static PluginManager getInstance() {
        return sInstance;
    }

    private PluginManager() {
    }

    private Context mContext;

    private PluginInstaller mPluginInstaller;
    private PluginLoader mPluginLoader;

    private Map<String, PluginApk> mPlugins = new HashMap<>();
//    private IPluginComponentLauncher mComponentLauncher;

    public void init(Context context) {
        mContext = context.getApplicationContext();
//        mComponentLauncher = new PluginComponentLauncher();
        mPluginRootDir = context.getDir(DIR_PLUGIN, Context.MODE_PRIVATE);
        mPluginInstaller = new PluginInstaller();
        mPluginLoader = new PluginLoaderImpl();
    }

    /**
     * 加载插件apk
     *
     * @param pkg              插件名
     * @param veritySignatures 是否校验签名
     * @return 是否加载成功
     */
    public boolean loadApk(final String pkg, final boolean veritySignatures) {

        if (isLoaded(pkg)) {//已加载
            Logger.d(TAG, "插件已加载");
            return true;
        }

        if (!isInited()) {
            throw new IllegalStateException("call init method first!");
        }

        InstalledApk installedApk = new InstalledApk();
        installedApk.apkFilePath = mPluginInstaller.getBaseApk(pkg).getAbsolutePath();
        installedApk.oDexPath = PathManager.getInstance().getDexDirByPkg(pkg).getAbsolutePath();

//        if (mPluginInstaller.isInstall(pkg)) {//已安装
//
//        }

        InstallResult result;
        try {
            if (isDownloaded(pkg)) {//已下载
                //安装
                if (!mPluginInstaller.install(mContext,
                        PathManager.getInstance().getDownloadPluginFileByPkg(pkg).getAbsolutePath()
                        , true, true).result) {
                    Logger.e(TAG, "安装失败！");
                    return false;
                }

            } else if (FileUtils.assetsFileExist(mContext, pkg + ".apk")) {
                result = mPluginInstaller.installFromAssets(mContext, "plugins/" + pkg + ".apk",
                        true, true);
                if (result != null
                        && !result.result) {
                    return false;
                }
            } else {
                //TODO 去下载
                new Downloader.Builder(mContext)
                        .url("http://www.redstarhome.com/applist/android/5c8b3265d2e740a79a5b685c203e4017.apk")
                        .targetFile(PathManager.getInstance().getDownloadPluginFileByPkg(pkg))
                        .callback(new OnDownloadListener() {
                            @Override
                            public void onStart(String absolutePath, long total) {
                                System.out.println("下载开始！");
                            }

                            @Override
                            public void onProgress(long count, long total) {
                                System.out.println("apk :{" + pkg + "}下载进度：" + (count / total) + "%");
                            }

                            @Override
                            public void onFinished() {
                                System.out.println("下载成功！");
                                loadApk(pkg, veritySignatures);
                            }

                            @Override
                            public void onFailure(Call call, Exception e) {
                                Logger.v(TAG,"下载失败！");
                            }

                        }).build().download();
                return false;
            }
            PluginApk pluginApk = mPluginLoader.load(mContext, installedApk,
                    veritySignatures);
            if (pluginApk == null) {
                return false;
            }
            mPlugins.put(pkg, pluginApk);
        } catch (InstallException e) {
            e.printStackTrace();
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean isInited() {
        return mContext != null;
    }

    private boolean isDownloaded(String pkg) {
        return PathManager.getInstance().getDownloadPluginFileByPkg(pkg).exists();
    }

    /**
     * 根据插件名称获取
     *
     * @param pkg
     * @return
     */
    private String getPluginFileName(String pkg) {
        return pkg + ".apk";
    }

    /**
     * 根据插件包名获取插件信息
     *
     * @param pkg 插件包名
     * @return 插件信息
     */
    @NonNull
    public PluginApk getPluginApk(String pkg) throws PluginNotInstalled {
        PluginApk pluginApk = mPlugins.get(pkg);
        if (pluginApk == null) {
            throw new PluginNotInstalled(pkg);
        }
        return pluginApk;
    }

//    public IPluginComponentLauncher getComponentLauncher() {
//        return mComponentLauncher;
//    }

    /**
     * 插件是否已加载
     *
     * @return
     */
    public boolean isLoaded(String pkg) {
        return mPlugins.containsKey(pkg);
    }

}
