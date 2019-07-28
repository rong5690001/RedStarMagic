/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
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

package com.redstar.magic.pluginlib;

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

import com.redstar.magic.pluginlib.container.HostActivityDelegator;
import com.redstar.magic.pluginlib.container.MixResources;


public class ShadowContext extends PluginDirContextThemeWrapper {
    IPluginComponentLauncher mPluginComponentLauncher;
    ClassLoader mPluginClassLoader;
    MagicApplication mShadowApplication;
    Resources mPluginResources;
    Resources mMixResources;
    LayoutInflater mLayoutInflater;
    String mLibrarySearchPath;
    String mDexPath;
    protected String mPartKey;
    private String mBusinessName;
    private String mPluginName;
//    private ShadowRemoteViewCreatorProvider mRemoteViewCreatorProvider;

    public ShadowContext() {
    }

//    public ShadowContext(Context base, int themeResId) {
//        super(base, themeResId);
//    }

    public final void setPluginResources(Resources resources) {
        mPluginResources = resources;
    }

    public final void setPluginClassLoader(ClassLoader classLoader) {
        mPluginClassLoader = classLoader;
    }

    public void setPluginComponentLauncher(IPluginComponentLauncher pluginComponentLauncher) {
        mPluginComponentLauncher = pluginComponentLauncher;
    }

    public void setShadowApplication(MagicApplication shadowApplication) {
        mShadowApplication = shadowApplication;
    }

    public void setLibrarySearchPath(String mLibrarySearchPath) {
        this.mLibrarySearchPath = mLibrarySearchPath;
    }

    public void setDexPath(String dexPath) {
        mDexPath = dexPath;
    }

    public void setBusinessName(String businessName) {
        if (TextUtils.isEmpty(businessName)) {
            businessName = null;
        }
        this.mBusinessName = businessName;
    }

    public void setPluginPartKey(String partKey) {
        this.mPartKey = partKey;
    }

//    public final void setRemoteViewCreatorProvider(ShadowRemoteViewCreatorProvider provider) {
//        mRemoteViewCreatorProvider = provider;
//    }
//
//    public final ShadowRemoteViewCreatorProvider getRemoteViewCreatorProvider() {
//        return mRemoteViewCreatorProvider;
//    }


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
            if (baseContext instanceof HostActivityDelegator) {
                hostResources = ((HostActivityDelegator) baseContext).superGetResources();
            } else {
                hostResources = baseContext.getResources();
            }
            mMixResources = new MixResources(hostResources, mPluginResources);
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
        getBaseContext().startActivity(intent);
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
        if (mBusinessName == null) {
            return null;
        } else {
            return "ShadowPlugin_" + mBusinessName;
        }
    }
}
