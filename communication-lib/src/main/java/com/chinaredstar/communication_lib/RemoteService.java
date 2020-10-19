package com.chinaredstar.communication_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chen.huarong on 2019-10-20.
 * 用于<b>宿主/插件</b>通讯的注解，使用该注解标注类可以跨越<b>宿主/插件</b>调用的类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteService {

    /**
     * 服务所属宿主/插件包名
     *
     * @return
     */
    String pluginName();

    /**
     * 服务类名称，一般使用类名
     *
     * @return
     */
    String serviceName();

    /**
     * 通信服务类版本
     *
     * @return
     */
    int version();

}
