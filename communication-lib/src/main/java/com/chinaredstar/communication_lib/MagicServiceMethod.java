package com.chinaredstar.communication_lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chen.huarong on 2019-10-20.
 * 该注解用于将 {@link MagicService} 注解类中的方法标记为可以跨<b>宿主/插件</b>互相调用的方法
 *
 * @see MagicService
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MagicServiceMethod {
}
