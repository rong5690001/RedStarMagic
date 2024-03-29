package com.redstar.magic.pluginlib;

import android.content.pm.ActivityInfo;

import com.redstar.magic.pluginlib.exception.ActivityPoolException;
import com.redstar.magic.pluginlib.pool.ActivityPool;

import org.junit.Assert;
import org.junit.Test;

/**
 * activity占位池
 *
 * @author chen.huarong on 2019-11-13
 */
public class ActivityPoolTest {

    @Test
    public void getActivityTest_singleTop() throws ActivityPoolException {
        for (int i = 0; i < 50; i++) {
            String proxyActivity = ActivityPool.getActivity("pluginActivity" + i,
                    ActivityInfo.LAUNCH_SINGLE_TOP);
            System.out.println("proxyActivity" + proxyActivity);
        }
    }

    @Test
    public void getActivityTest_singleTask() throws ActivityPoolException {
        for (int i = 0; i < 50; i++) {
            String proxyActivity = ActivityPool.getActivity("pluginActivity" + i,
                    ActivityInfo.LAUNCH_SINGLE_TASK);
            System.out.println("proxyActivity" + proxyActivity);
        }
    }

    @Test
    public void getActivityTest_singleInstance() throws ActivityPoolException {
        for (int i = 0; i < 30; i++) {
            String proxyActivity = ActivityPool.getActivity("pluginActivity" + i,
                    ActivityInfo.LAUNCH_SINGLE_INSTANCE);
            System.out.println("proxyActivity" + proxyActivity);
        }
    }

    @Test
    public void releaseActivityTest_singleTop() throws ActivityPoolException {
        String pluginActivity = "pluginActivity";
        String proxyActivity = ActivityPool.getActivity(pluginActivity,
                ActivityInfo.LAUNCH_SINGLE_TOP);
        Assert.assertTrue(ActivityPool.releaseActivity(pluginActivity, proxyActivity));
    }

    @Test
    public void releaseActivityTest_singleTask() throws ActivityPoolException {
        String pluginActivity = "pluginActivity";
        String proxyActivity = ActivityPool.getActivity(pluginActivity,
                ActivityInfo.LAUNCH_SINGLE_TASK);
        Assert.assertTrue(ActivityPool.releaseActivity(pluginActivity, proxyActivity));
    }

    @Test
    public void releaseActivityTest_singleInstance() throws ActivityPoolException {
        String pluginActivity = "pluginActivity";
        String proxyActivity = ActivityPool.getActivity(pluginActivity,
                ActivityInfo.LAUNCH_SINGLE_INSTANCE);
        Assert.assertTrue(ActivityPool.releaseActivity(pluginActivity, proxyActivity));
    }
}
