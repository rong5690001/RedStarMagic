package com.chinaredstar.communication_lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen.huarong on 2019-10-20.
 * 管理宿主/插件间通信服务类，提供注册、获取、调用功能
 */

public class MagicServiceManager {

    private static final Map<String, Map<String, Object>> serviceMap = new HashMap<>();
    private static String sHostPkg;
    private static String sHostVersionName;
    private static int sHostVersionCode;
    private static String sMagicVersionName;
    private static int sMagicVersionCode;
    private static boolean isInited;

    /**
     * 初始化
     *
     * @param hostPkg          宿主包名
     * @param hostVersionName  宿主版本名
     * @param hostVersionCode  宿主版本号
     * @param magicVersionName 插件框架版本名
     * @param magicVersionCode 插件框架版本号
     */
    public static synchronized void init(String hostPkg
            , String hostVersionName
            , int hostVersionCode
            , String magicVersionName
            , int magicVersionCode) {
        if (isInited) {
            return;
        }
        sHostPkg = hostPkg;
        sHostVersionName = hostVersionName;
        sHostVersionCode = hostVersionCode;
        sMagicVersionName = magicVersionName;
        sMagicVersionCode = magicVersionCode;
        isInited = true;
    }

    /**
     * 注册宿主通信服务对象
     *
     * @param serviceName
     * @param service
     */
    public static void register(String serviceName, Object service) {
        register(sHostPkg, serviceName, service);
    }

    /**
     * 注册通信服务对象
     *
     * @param pluginName  宿主/插件包名
     * @param serviceName 服务类名
     * @param service     服务类对象
     */
    public static void register(String pluginName, String serviceName, Object service) {
        synchronized (serviceMap) {
            if (serviceMap.containsKey(pluginName)) {
                if (serviceMap.get(pluginName).containsKey(serviceName)) {
                    throw new IllegalStateException("service :  pluginName = " + pluginName + "; " +
                            "   serviceName:" + serviceName + " is registered!");
                }
            } else {
                serviceMap.put(pluginName, new HashMap<String, Object>());
            }
            serviceMap.get(pluginName).put(serviceName, service);
        }
    }

    /**
     * 获取宿主通信服务对象
     *
     * @param serviceName 服务类名
     * @param <T>         服务对象类型
     * @return 服务类对象
     */
    public static <T extends MagicService> T getService(String serviceName) {
        if (serviceMap.containsKey(sHostPkg)) {
            if (serviceMap.get(sHostPkg).containsKey(serviceName)) {
                return (T) serviceMap.get(sHostPkg).get(serviceName);
            }
        }

        throw new NullPointerException("service :  pluginName = " + sHostPkg + "; " +
                "   serviceName:" + serviceName + " is not registered!");
    }

    /**
     * 获取通信服务对象
     *
     * @param pluginName  宿主/插件包名
     * @param serviceName 服务类名
     * @param <T>         服务对象类型
     * @return 服务类对象
     */
    public static <T extends MagicService> T getService(String pluginName, String serviceName) {
        if (serviceMap.containsKey(pluginName)) {
            if (serviceMap.get(pluginName).containsKey(serviceName)) {
                return (T) serviceMap.get(pluginName).get(serviceName);
            }
        }

        throw new NullPointerException("service :  pluginName = " + pluginName + "; " +
                "   serviceName:" + serviceName + " is not registered!");
    }

    /**
     * 获取宿主包名
     *
     * @return 宿主包名
     */
    public static String getHostPkg() {
        return sHostPkg;
    }

    /**
     * 获取宿主版本名称
     *
     * @return 宿主版本名称
     */
    public static String getHostVersionName() {
        return sHostVersionName;
    }

    /**
     * 获取宿主版本号
     *
     * @return 宿主版本号
     */
    public static int getHostVersionCode() {
        return sHostVersionCode;
    }

    /**
     * 获取插件框架版本名称
     *
     * @return 插件框架版本名称
     */
    public static String getMagicVersionName() {
        return sMagicVersionName;
    }

    /**
     * 获取插件框架版本号
     *
     * @return 插件框架版本号
     */
    public static int getMagicVersionCode() {
        return sMagicVersionCode;
    }
}
