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

package com.redstar.magic.pluginlib.components;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.redstar.magic.pluginlib.components.activity.MagicActivity;
import com.redstar.magic.pluginlib.components.activity.PluginActivity;
import com.redstar.magic.pluginlib.proxy.PluginProxyActivity;


public interface MagicActivityLifecycleCallbacks {

    void onActivityCreated(MagicActivity activity, Bundle savedInstanceState);

    void onActivityStarted(MagicActivity activity);

    void onActivityResumed(MagicActivity activity);

    void onActivityPaused(MagicActivity activity);

    void onActivityStopped(MagicActivity activity);

    void onActivitySaveInstanceState(MagicActivity activity, Bundle outState);

    void onActivityDestroyed(MagicActivity activity);

    class Wrapper implements Application.ActivityLifecycleCallbacks {

        final MagicActivityLifecycleCallbacks shadowActivityLifecycleCallbacks;
        final MagicApplication shadowApplication;

        public Wrapper(MagicActivityLifecycleCallbacks shadowActivityLifecycleCallbacks, MagicApplication shadowApplication) {
            this.shadowActivityLifecycleCallbacks = shadowActivityLifecycleCallbacks;
            this.shadowApplication = shadowApplication;
        }

        private MagicActivity getPluginActivity(Activity activity) {
            if (activity instanceof PluginProxyActivity) {
                return (MagicActivity) ((PluginProxyActivity) activity).getPluginActivity();
            } else {
                return null;
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityCreated(pluginActivity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityStarted(pluginActivity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityResumed(pluginActivity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityPaused(pluginActivity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityStopped(pluginActivity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivitySaveInstanceState(pluginActivity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            final MagicActivity pluginActivity = getPluginActivity(activity);
            if (checkOwnerActivity(pluginActivity) ) {
                shadowActivityLifecycleCallbacks.onActivityDestroyed(pluginActivity);
            }
        }

        /**
         * 检测Activity是否属于当前Application所在的插件
         *
         * @param activity 插件Activity
         * @return 是否属于当前Application所在的插件 true属于
         */
        private boolean checkOwnerActivity(PluginActivity activity) {
            return activity != null && activity.getPluginApplication() == shadowApplication;
        }
    }
}
