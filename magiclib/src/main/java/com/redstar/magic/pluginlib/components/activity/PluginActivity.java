/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copyFromAssets of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.redstar.magic.pluginlib.components.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.redstar.magic.pluginlib.components.MagicApplication;
import com.redstar.magic.pluginlib.components.MagicContext;
import com.redstar.magic.pluginlib.proxy.activity.ProxyActivity;

import java.util.List;

/**
 * 插件activity的父类抽象类
 * <p>
 * 封装部分方法
 * 负责将插件activity的方法转交由{@link com.redstar.magic.pluginlib.proxy.activity.PluginProxyActivity}处理
 *
 * @author chen.huarong on 2019-11-12
 */
public abstract class PluginActivity extends MagicContext implements Window.Callback {

    ProxyActivity mHostActivityDelegator;

    MagicApplication mPluginApplication;

    public final void setHostContextAsBase(Context context) {
        attachBaseContext(context);
    }

    public void setHostActivityDelegator(ProxyActivity delegator) {
        mHostActivityDelegator = delegator;
    }

    public void setPluginApplication(MagicApplication pluginApplication) {
        mPluginApplication = pluginApplication;
    }

    public MagicApplication getPluginApplication() {
        return mPluginApplication;
    }

    @SuppressLint("MissingSuperCall")
    public void onCreate(Bundle savedInstanceState) {
        mHostActivityDelegator.superOnCreate(savedInstanceState);
    }

    @SuppressLint("MissingSuperCall")
    public void onResume() {
        mHostActivityDelegator.superOnResume();
    }

    public void onNewIntent(Intent intent) {
        mHostActivityDelegator.superOnNewIntent(intent);
    }

    @SuppressLint("MissingSuperCall")
    public void onSaveInstanceState(Bundle outState) {
        mHostActivityDelegator.superOnSaveInstanceState(outState);
    }

    @SuppressLint("MissingSuperCall")
    public void onPause() {
        mHostActivityDelegator.superOnPause();
    }

    @SuppressLint("MissingSuperCall")
    public void onStart() {
        mHostActivityDelegator.superOnStart();
    }

    @SuppressLint("MissingSuperCall")
    public void onStop() {
        mHostActivityDelegator.superOnStop();
    }

    @SuppressLint("MissingSuperCall")
    public void onDestroy() {
        mHostActivityDelegator.superOnDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mHostActivityDelegator.superOnConfigurationChanged(newConfig);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return mHostActivityDelegator.superDispatchKeyEvent(event);
    }

    public void finish() {
        mHostActivityDelegator.superFinish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHostActivityDelegator.superOnActivityResult(requestCode, resultCode, data);
    }

    public void onChildTitleChanged(Activity childActivity, CharSequence title) {
        mHostActivityDelegator.superOnChildTitleChanged(childActivity, title);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mHostActivityDelegator.superOnRestoreInstanceState(savedInstanceState);
    }

    @SuppressLint("MissingSuperCall")
    public void onPostCreate(Bundle savedInstanceState) {
        mHostActivityDelegator.superOnPostCreate(savedInstanceState);
    }

    @SuppressLint("MissingSuperCall")
    public void onRestart() {
        mHostActivityDelegator.superOnRestart();
    }

    public void onUserLeaveHint() {
        mHostActivityDelegator.superOnUserLeaveHint();
    }

    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return mHostActivityDelegator.superOnCreateThumbnail(outBitmap, canvas);
    }

    public CharSequence onCreateDescription() {
        return mHostActivityDelegator.superOnCreateDescription();
    }

    public Object onRetainNonConfigurationInstance() {
        return mHostActivityDelegator.superOnRetainNonConfigurationInstance();
    }

    public void onLowMemory() {
        mHostActivityDelegator.superOnLowMemory();
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return mHostActivityDelegator.superOnTrackballEvent(event);
    }

    public void onUserInteraction() {
        mHostActivityDelegator.superOnUserInteraction();
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        mHostActivityDelegator.superOnWindowAttributesChanged(params);
    }

    public void onContentChanged() {
        mHostActivityDelegator.superOnContentChanged();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        mHostActivityDelegator.superOnWindowFocusChanged(hasFocus);
    }

    public View onCreatePanelView(int featureId) {
        return mHostActivityDelegator.superOnCreatePanelView(featureId);
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return mHostActivityDelegator.superOnCreatePanelMenu(featureId, menu);
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return false;
    }

    public void onPanelClosed(int featureId, Menu menu) {
        mHostActivityDelegator.superOnPanelClosed(featureId, menu);
    }

    public Dialog onCreateDialog(int id) {
        return mHostActivityDelegator.superOnCreateDialog(id);
    }

    public void onPrepareDialog(int id, Dialog dialog) {
        mHostActivityDelegator.superOnPrepareDialog(id, dialog);
    }

    public void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {

    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return mHostActivityDelegator.superOnCreateView(name, context, attrs);
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return mHostActivityDelegator.superOnCreateView(parent, name, context, attrs);
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        mHostActivityDelegator.superStartActivityFromChild(child, intent, requestCode);
    }

    public LayoutInflater getLayoutInflater() {
        return mHostActivityDelegator.getWindow().getLayoutInflater();
//        LayoutInflater inflater = mHostActivityDelegator.getWindow().getLayoutInflater();
//        return ShadowLayoutInflater.build(inflater, this, mPartKey);
    }

    public void onBackPressed() {
        mHostActivityDelegator.superOnBackPressed();
    }

    public void onAttachedToWindow() {
        mHostActivityDelegator.superOnAttachedToWindow();
    }


    public void onDetachedFromWindow() {
        mHostActivityDelegator.superOnDetachedFromWindow();
    }

    public void onAttachFragment(Fragment fragment) {
        mHostActivityDelegator.superOnAttachFragment(fragment);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        mHostActivityDelegator.superOnRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mHostActivityDelegator.superOnKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mHostActivityDelegator.superOnOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mHostActivityDelegator.superDispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mHostActivityDelegator.superDispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return mHostActivityDelegator.superDispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return mHostActivityDelegator.superDispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return mHostActivityDelegator.superDispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return mHostActivityDelegator.superOnMenuOpened(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return mHostActivityDelegator.superOnMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onSearchRequested() {
        return mHostActivityDelegator.superOnSearchRequested();
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return mHostActivityDelegator.superOnSearchRequested(searchEvent);
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return mHostActivityDelegator.superOnWindowStartingActionMode(callback);
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        return mHostActivityDelegator.superOnWindowStartingActionMode(callback, type);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActionModeStarted(ActionMode mode) {
        mHostActivityDelegator.superOnActionModeStarted(mode);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        mHostActivityDelegator.superOnActionModeFinished(mode);
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu,
                                           int deviceId) {
        mHostActivityDelegator.superOnProvideKeyboardShortcuts(data, menu, deviceId);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        mHostActivityDelegator.superOnPointerCaptureChanged(hasCapture);
    }

    public void recreate() {
        mHostActivityDelegator.recreate();
    }

    @SuppressLint("MissingSuperCall")
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        mHostActivityDelegator.superOnMultiWindowModeChanged(isInMultiWindowMode);
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        mHostActivityDelegator.superOnMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    @Deprecated
    public android.app.FragmentManager getFragmentManager() {
        return mHostActivityDelegator.superGetFragmentManager();
    }
}
