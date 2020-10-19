package com.redstar.magic.pluginlib.pm;

import com.redstar.magic.pluginlib.AppTest;
import com.redstar.magic.pluginlib.pm.bean.PluginInfo;
import com.redstar.magic.pluginlib.pm.impl.PluginResolverImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Created by chen.huarong on 2019-09-28.
 */
@RunWith(AndroidJUnit4.class)
public class PluginResolverTest extends AppTest {

    @Test
    public void resolveTest() {
        PluginInfo pluginInfo = new PluginInfo();
        pluginInfo.isInner = true;
        pluginInfo.fileName = "chajian.ne.com.pluginapk.apk";
        PluginResolver pluginResolver = new PluginResolverImpl(pluginInfo);
        pluginResolver.resolve(appContext);
        Assert.assertEquals(1, pluginResolver.getVersionCode());
    }

}
