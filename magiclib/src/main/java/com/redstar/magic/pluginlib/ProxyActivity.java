package com.redstar.magic.pluginlib;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * 代理Activity，管理插件Activity的生命周期
 */
public class ProxyActivity extends Activity {

    private static final String TAG = ProxyActivity.class.getSimpleName();
    private String    mClassName;
    private PluginApk mPluginApk;
    private IPlugin   mIPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClassName = getIntent().getStringExtra("className");
        mPluginApk = PluginManager.getInstance().getPluginApk();

        launchPluginActivity();

    }

    /**
     * 初始化插件Activity
     */
    private void launchPluginActivity() {
        if (mPluginApk == null) {
            throw new RuntimeException("请先加载插件apk");
        }

        try {
            Class<?> clazz = mPluginApk.mClassLoader.loadClass(mClassName);
            //这就是Activity实例对象了，注意:没有生命周期,没有上下文环境
            Object object = clazz.newInstance();
            Logger.e(TAG, object.getClass().getSuperclass().getName());
            Logger.e(TAG, object.getClass().getSuperclass().getSuperclass().getName());
//            if (object instanceof IPlugin) {
                mIPlugin = (IPlugin) object;
                mIPlugin.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM", IPlugin.FROM_EXTERNAL);
                mIPlugin.onCreate(bundle);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return mPluginApk != null ? mPluginApk.mResources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPluginApk != null ? mPluginApk.mAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginApk != null ? mPluginApk.mClassLoader : super.getClassLoader();
    }
}
