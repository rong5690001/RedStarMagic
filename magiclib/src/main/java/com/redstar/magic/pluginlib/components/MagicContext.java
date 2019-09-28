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

package com.redstar.magic.pluginlib.components;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;

import com.redstar.magic.pluginlib.IPluginComponentLauncher;
import com.redstar.magic.pluginlib.proxy.IProxyActivity;
import com.redstar.magic.pluginlib.proxy.MixResources;

/**
 * 插件上下文：
 * 1、负责加载资源
 * 2、处理启动组件逻辑
 * author:chen.huarong
 */
public class MagicContext extends PluginDirContextThemeWrapper {

    protected IPluginComponentLauncher mPluginComponentLauncher;
    protected ClassLoader mPluginClassLoader;
    protected MagicApplication mShadowApplication;
    protected Resources mPluginResources;
    protected Resources mMixResources;
    protected String mLibrarySearchPath;
    protected String mDexPath;
    private String mPluginName;

    public MagicContext() {
    }

    public final void setPluginResources(Resources resources) {
        mPluginResources = resources;
    }

    public final void setPluginClassLoader(ClassLoader classLoader) {
        mPluginClassLoader = classLoader;
    }

    public void setPluginComponentLauncher(IPluginComponentLauncher pluginComponentLauncher) {
        mPluginComponentLauncher = pluginComponentLauncher;
    }

    public void setPluginName(String pluginName) {
        mPluginName = pluginName;
    }

    @Override
    public Context getApplicationContext() {
        return mShadowApplication;
    }

    @Override
    public Resources getResources() {
        if (mMixResources == null) {
            Context baseContext = getBaseContext();
            Resources hostResources;
            if (baseContext instanceof IProxyActivity) {
                hostResources = ((IProxyActivity) baseContext).superGetResources();
            } else {
                hostResources = baseContext.getResources();
            }
            mMixResources = new MixResources(hostResources, mPluginResources, getPackageName());
        }
        return mMixResources;
    }

    @Override
    public AssetManager getAssets() {
        return mPluginResources.getAssets();
    }

    @Override
    public Object getSystemService(String name) {
//        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
//            if (mLayoutInflater == null) {
//                LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
//                mLayoutInflater = ShadowLayoutInflater.build(inflater, this, mPartKey);
//            }
//            return mLayoutInflater;
//        }
        return super.getSystemService(name);
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginClassLoader;
    }

    @Override
    public void startActivity(Intent intent) {
        final Intent pluginIntent = new Intent(intent);
        pluginIntent.setExtrasClassLoader(mPluginClassLoader);
        final boolean success = mPluginComponentLauncher.startActivity(this, pluginIntent);
        if (!success) {
            super.startActivity(intent);
        }
    }

    public void superStartActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if (!mPluginComponentLauncher.unbindService(this, conn).first)
            super.unbindService(conn);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (service.getComponent() == null) {
            return super.bindService(service, conn, flags);
        }
        Pair<Boolean, Boolean> ret = mPluginComponentLauncher.bindService(this, service, conn,
                flags);
        if (!ret.first)
            return super.bindService(service, conn, flags);
        return ret.second;
    }

    @Override
    public boolean stopService(Intent name) {
        if (name.getComponent() == null) {
            return super.stopService(name);
        }
        Pair<Boolean, Boolean> ret = mPluginComponentLauncher.stopService(this, name);
        if (!ret.first)
            return super.stopService(name);
        return ret.second;
    }

    @Override
    public ComponentName startService(Intent service) {
        if (service.getComponent() == null) {
            return super.startService(service);
        }
        Pair<Boolean, ComponentName> ret = mPluginComponentLauncher.startService(this, service);
        if (!ret.first)
            return super.startService(service);
        return ret.second;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        final ApplicationInfo applicationInfo = super.getApplicationInfo();
        applicationInfo.nativeLibraryDir = mLibrarySearchPath;
        applicationInfo.sourceDir = mDexPath;
        return applicationInfo;
    }

    public IPluginComponentLauncher getPendingIntentConverter() {
        return mPluginComponentLauncher;
    }

    @Override
    String getSubDirName() {
        if (TextUtils.isEmpty(mPluginName)) {
            return null;
        } else {
            return "MagicPlugin_" + mPluginName;
        }
    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }
}
