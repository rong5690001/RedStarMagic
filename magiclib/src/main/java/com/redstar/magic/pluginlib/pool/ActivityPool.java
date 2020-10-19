package com.redstar.magic.pluginlib.pool;

import android.content.pm.ActivityInfo;

import com.redstar.magic.pluginlib.components.activity.PlaceHolderActivitys;
import com.redstar.magic.pluginlib.exception.ActivityPoolException;
import com.redstar.magic.pluginlib.proxy.activity.PluginProxyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 占位activity管理类
 * <p>
 * 管理三种启动模式对应已用和未用的activity
 *
 * @author chen.huarong on 2019-11-12
 */
public class ActivityPool {

    //占位activity前缀
    private static final String PREFIX = PlaceHolderActivitys.class.getName();

    //占位activity后缀
    private static final String ACTIVITY_NAME_SINGLE_STANDARD = PluginProxyActivity.class.getName();
    private static final String ACTIVITY_NAME_SINGLE_TOP = PREFIX + "$ActivityProxySingleTop";
    private static final String ACTIVITY_NAME_SINGLE_TASK = PREFIX + "$ActivityProxySingleTask";
    private static final String ACTIVITY_NAME_SINGLE_INSTANCE = PREFIX +
            "$ActivityProxySingleInstance";

    //对应启动模式的占位activity个数
    private static final int SINGLE_TOP_COUNT = 50;
    private static final int SINGLE_TASK_COUNT = 50;
    private static final int SINGLE_INSTANCE_COUNT = 30;

    //对应启动模式占位activity池
    private static final ActivityLaunchTool mSingleTopActivityLaunchTool =
            new ActivityLaunchTool(ActivityInfo.LAUNCH_SINGLE_TOP);
    private static final ActivityLaunchTool mSingleTaskActivityLaunchTool =
            new ActivityLaunchTool(ActivityInfo.LAUNCH_SINGLE_TASK);
    private static final ActivityLaunchTool mSingleInstanceActivityLaunchTool =
            new ActivityLaunchTool(ActivityInfo.LAUNCH_SINGLE_INSTANCE);

    /**
     * 获取一个占位activity
     *
     * @param pluginActivity 占位activity
     * @param launchModel    启动模式
     * @return 占位activity
     */
    public static String getActivity(String pluginActivity, int launchModel) throws ActivityPoolException {
        switch (launchModel) {
            case ActivityInfo.LAUNCH_MULTIPLE:
                return ACTIVITY_NAME_SINGLE_STANDARD;
            case ActivityInfo.LAUNCH_SINGLE_TOP:
                return mSingleTopActivityLaunchTool.getActivity(pluginActivity);
            case ActivityInfo.LAUNCH_SINGLE_TASK:
                return mSingleTaskActivityLaunchTool.getActivity(pluginActivity);
            case ActivityInfo.LAUNCH_SINGLE_INSTANCE:
                return mSingleInstanceActivityLaunchTool.getActivity(pluginActivity);
        }
        throw new ActivityPoolException(ActivityPoolException.MESSAGE_NOT_FIND_ACTIVITY);
    }

    /**
     * 回收一个占位activity
     *
     * @param pluginActivity 插件activity
     * @param proxyActivity  占位activity
     * @return 占位activity
     */
    public static boolean releaseActivity(String pluginActivity, String proxyActivity) {
        if (proxyActivity.contains(ACTIVITY_NAME_SINGLE_TOP)) {
            return mSingleTopActivityLaunchTool.releaseActivity(pluginActivity);
        } else if (proxyActivity.contains(ACTIVITY_NAME_SINGLE_TASK)) {
            return mSingleTaskActivityLaunchTool.releaseActivity(pluginActivity);
        } else if (proxyActivity.contains(ACTIVITY_NAME_SINGLE_INSTANCE)) {
            return mSingleInstanceActivityLaunchTool.releaseActivity(pluginActivity);
        }
        return false;
    }


    /**
     * 占位activity池
     * <p>
     * 对应singleTop、singleTask、singleInstance三种启动模式
     */
    private static class ActivityLaunchTool {

        List<String> mUnused = new ArrayList<>();
        Map<String, ActivityRecord> mUsed = new HashMap<>();

        public ActivityLaunchTool(int launchModel) {
            mUnused.clear();
            mUsed.clear();
            String activityName = "";
            int count = 0;
            switch (launchModel) {
                case ActivityInfo.LAUNCH_SINGLE_TOP:
                    activityName = ACTIVITY_NAME_SINGLE_TOP;
                    count = SINGLE_TOP_COUNT;
                    break;
                case ActivityInfo.LAUNCH_SINGLE_TASK:
                    activityName = ACTIVITY_NAME_SINGLE_TASK;
                    count = SINGLE_TASK_COUNT;
                    break;
                case ActivityInfo.LAUNCH_SINGLE_INSTANCE:
                    activityName = ACTIVITY_NAME_SINGLE_INSTANCE;
                    count = SINGLE_INSTANCE_COUNT;
                    break;
            }
            for (int i = 0; i < count; i++) {
                mUnused.add(activityName + i);
            }
        }

        /**
         * 获取一个占位activity
         *
         * @return 占位activity
         */
        String getActivity(@NonNull String pluginActivity) throws ActivityPoolException {
            synchronized (this) {
                //插件activity已经启动过，则直接返回之前对应的代理activity
                if (mUsed.containsKey(pluginActivity)) {
                    return mUsed.get(pluginActivity).proxyActivity;
                }

                if (mUnused.size() > 0) {
                    String proxyActivity = mUnused.get(0);
                    mUnused.remove(proxyActivity);
                    mUsed.put(pluginActivity, new ActivityRecord(pluginActivity, proxyActivity));
                    return proxyActivity;
                } else {
                    throw new ActivityPoolException(ActivityPoolException.MESSAGE_ACTIVITY_USED_UP);
                }
            }
        }

        /**
         * 回收一个占位activity
         *
         * @param pluginActivity
         */
        boolean releaseActivity(String pluginActivity) {
            synchronized (this) {
                if (mUsed.containsKey(pluginActivity)) {
                    try {
                        String proxyActivity = mUsed.get(pluginActivity).proxyActivity;
                        mUnused.add(proxyActivity);
                        mUsed.remove(pluginActivity);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        }
    }
}
