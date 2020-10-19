package com.redstar.magic.pluginlib;

import android.content.Context;

import com.redstar.magic.pluginlib.pm.PluginManager;

/**
 * Created by chen.huarong on 2019-09-28.
 */
public class PluginApplication {

    public static Context sAppContext;

    public static void install(Context context) {
        sAppContext = context.getApplicationContext();
        PluginManager.getInstance().init(context);
    }

    public static Context getAppContext() {
        if (sAppContext == null) {
            throw new NullPointerException("请在application的onCreate方法里调用MagicPlugin.install(context)");
        }
        return sAppContext;
    }

}
