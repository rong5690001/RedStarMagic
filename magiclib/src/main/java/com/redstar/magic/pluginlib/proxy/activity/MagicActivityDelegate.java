package com.redstar.magic.pluginlib.proxy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.redstar.magic.pluginlib.PluginApk;
import com.redstar.magic.pluginlib.components.activity.PluginActivity;
import com.redstar.magic.pluginlib.pm.PluginManager;
import com.redstar.magic.pluginlib.proxy.MixResources;
import com.redstar.magic.pluginlib.tools.Logger;
import com.redstar.magic.pluginlib.utils.IntentUtils;


/**
 * Created by chen.huarong on 2019-07-11.
 */
public class MagicActivityDelegate implements HostActivityDelegate {

    private static final String TAG = MagicActivityDelegate.class.getSimpleName();
    private ClassLoader mPluginClassLoader;
    private Resources mPluginResources;
    //代理Activity
    private ProxyActivity mPluginProxyActivity;
    //插件Activity
    private PluginActivity mPluginActivity;

    private boolean mDependenciesInjected = false;
    private boolean mPluginActivityCreated = false;

    /**
     * 判断是否调用过OnWindowAttributesChanged，如果调用过就说明需要在onCreate之前调用
     */
    private boolean mCallOnWindowAttributesChanged = false;
    private WindowManager.LayoutParams mBeforeOnCreateOnWindowAttributesChangedCalledParams = null;
    private MixResources mMixResources;

    public MagicActivityDelegate() {

    }

    @Override
    public void setDelegator(ProxyActivity pluginProxy) {
        mPluginProxyActivity = pluginProxy;
    }

    @Override
    public PluginActivity getPluginActivity() {
        return mPluginActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        savedInstanceState = savedInstanceState == null ?
                mPluginProxyActivity.getIntent().getExtras() : savedInstanceState;
        String activityClassName =
                mPluginProxyActivity.getIntent().getStringExtra(IntentUtils.KEY_CLASS_NAME);
        String packageName =
                mPluginProxyActivity.getIntent().getStringExtra(IntentUtils.KEY_PLUGIN_PACKAGE_NAME);

        try {

            PluginApk pluginApk = PluginManager.getInstance().getPluginApk(packageName);
            mPluginClassLoader = pluginApk.mClassLoader;
            mPluginResources = pluginApk.mResources;

            mMixResources = new MixResources(mPluginProxyActivity.superGetResources(),
                    mPluginResources,
                    mPluginProxyActivity.getIntent().getComponent().getPackageName());

            mDependenciesInjected = true;

            //设置主题
            mPluginProxyActivity.setTheme(pluginApk.getActivityInfo(activityClassName).getThemeResource());

            Class clazz = mPluginClassLoader.loadClass(activityClassName);
            mPluginActivity = (PluginActivity) clazz.newInstance();
            injectPluginActivity(mPluginActivity, packageName);

            //使PluginActivity替代ContainerActivity接收Window的Callback
            mPluginProxyActivity.getWindow().setCallback(mPluginActivity);

            //设置插件AndroidManifest.xml 中注册的WindowSoftInputMode
//            mPluginProxyActivity.getWindow().setSoftInputMode(pluginActivityInfo.activityInfo
//            .softInputMode);

            //Activity.onCreate调用之前应该先收到onWindowAttributesChanged。
            if (mCallOnWindowAttributesChanged) {
                mPluginActivity.onWindowAttributesChanged(mBeforeOnCreateOnWindowAttributesChangedCalledParams);
                mBeforeOnCreateOnWindowAttributesChangedCalledParams = null;
            }
            mPluginActivity.onCreate(savedInstanceState);
            mPluginActivityCreated = true;
        } catch (Throwable e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    /**
     * 向插件Activity里注入必要参数
     *
     * @param pluginActivity
     * @param pluginName
     */
    private void injectPluginActivity(PluginActivity pluginActivity, String pluginName) {
        pluginActivity.setHostContextAsBase((Context) mPluginProxyActivity);
        pluginActivity.setPluginClassLoader(mPluginClassLoader);
        pluginActivity.setPluginResources(mPluginResources);
        pluginActivity.setPluginName(pluginName);
//        pluginActivity.setPluginComponentLauncher(PluginManager.getInstance()
//        .getComponentLauncher());
        pluginActivity.setHostActivityDelegator(mPluginProxyActivity);
    }

    @Override
    public void onResume() {
        mPluginActivity.onResume();
    }

    @Override
    public void onNewIntent(Intent intent) {
        mPluginActivity.onNewIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPluginActivity.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        mPluginActivity.onPause();
    }

    @Override
    public void onStop() {
        mPluginActivity.onStop();
    }

    @Override
    public void onDestroy() {
        mPluginActivity.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mPluginActivity.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mPluginActivity.dispatchKeyEvent(event);
    }

    @Override
    public void finish() {
        mPluginActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPluginActivity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChildTitleChanged(Activity childActivity, CharSequence title) {
        mPluginActivity.onChildTitleChanged(childActivity, title);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mPluginActivity.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        mPluginActivity.onPostCreate(savedInstanceState);
    }

    @Override
    public void onRestart() {
        mPluginActivity.onRestart();
    }

    @Override
    public void onUserLeaveHint() {
        mPluginActivity.onUserLeaveHint();
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return mPluginActivity.onCreateThumbnail(outBitmap, canvas);
    }

    @Override
    public CharSequence onCreateDescription() {
        return mPluginActivity.onCreateDescription();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mPluginActivity.onRetainNonConfigurationInstance();
    }

    @Override
    public void onLowMemory() {
        mPluginActivity.onLowMemory();
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return mPluginActivity.onTrackballEvent(event);
    }

    @Override
    public void onUserInteraction() {
        mPluginActivity.onUserInteraction();
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (mPluginActivityCreated) {
            mPluginActivity.onWindowAttributesChanged(params);
        } else {
            mBeforeOnCreateOnWindowAttributesChangedCalledParams = params;
        }
        mCallOnWindowAttributesChanged = true;
    }

    @Override
    public void onContentChanged() {
        mPluginActivity.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mPluginActivity.onWindowFocusChanged(hasFocus);
    }

    @Override
    public View onCreatePanelView(int featureId) {
        return mPluginActivity.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return mPluginActivity.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return mPluginActivity.onPreparePanel(featureId, view, menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        mPluginActivity.onPanelClosed(featureId, menu);
    }

    @Override
    public Dialog onCreateDialog(int id) {
        return mPluginActivity.onCreateDialog(id);
    }

    @Override
    public void onPrepareDialog(int id, Dialog dialog) {
        mPluginActivity.onPrepareDialog(id, dialog);
    }

    @Override
    public void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        mPluginProxyActivity.superOnApplyThemeResource(theme, resid, first);
        //TODO 此处不一定会执行，后面优化
        if (mPluginActivityCreated) {
            mPluginActivity.onApplyThemeResource(theme, resid, first);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return mPluginActivity.onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return mPluginActivity.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        mPluginActivity.startActivityFromChild(child, intent, requestCode);
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginClassLoader;
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return mPluginActivity.getLayoutInflater();
    }

    @Override
    public Resources getPluginResources() {
        if (mDependenciesInjected) {
            return mMixResources;
        } else {
            //预期只有android.view.Window.getDefaultFeatures会调用到这个分支，此时我们还无法确定插件资源
            //而getDefaultFeatures只需要访问系统资源
            return Resources.getSystem();
        }
    }

    @Override
    public void onBackPressed() {
        mPluginActivity.onBackPressed();
    }

    @Override
    public void onStart() {
        mPluginActivity.onStart();
    }

    @Override
    public void onAttachedToWindow() {
        mPluginActivity.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        mPluginActivity.onDetachedFromWindow();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        mPluginActivity.onAttachFragment(fragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        mPluginActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void recreate() {
        mPluginActivity.recreate();
    }

    @Override
    public ComponentName getCallingActivity() {
        return null;
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        mPluginActivity.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        mPluginActivity.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }


//    public void inject(Resources resources) {
//        mPluginResources = resources;
//    }

}
