package com.redstar.magic.pluginlib;

import android.content.Context;

import org.junit.Before;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

/**
 * Created by chen.huarong on 2019-09-28.
 */
@RunWith(AndroidJUnit4.class)
public class AppTest {

    protected Context appContext;
    protected String pkg = "chajian.ne.com.pluginapk";

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PluginApplication.install(appContext);
    }

}
