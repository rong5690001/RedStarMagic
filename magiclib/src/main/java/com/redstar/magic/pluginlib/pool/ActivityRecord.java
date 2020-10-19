package com.redstar.magic.pluginlib.pool;

/**
 * 记录已启动的activity信息
 *
 * @author chen.huarong on 2019-11-13
 */
public class ActivityRecord {
    /**
     * 被代理的插件activity名称
     */
    public String pluginActivity;
    /**
     * 代理activity名称(占位activity)
     */
    public String proxyActivity;
    /**
     * 代理activity被引用的个数（standard模式下，代理activity会被多次引用）
     */
//    private int refCount;

    public ActivityRecord(String pluginActivity, String proxyActivity) {
        this.pluginActivity = pluginActivity;
        this.proxyActivity = proxyActivity;
    }
}
