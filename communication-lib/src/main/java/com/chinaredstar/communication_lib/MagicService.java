package com.chinaredstar.communication_lib;

/**
 * Created by chen.huarong on 2019-10-20.
 * 可以跨<b>宿主/插件</b>互相调用类的接口
 */
public interface MagicService {

    /**
     * 调用方法
     *
     * @param method 方法名称
     * @param args   方法参数
     * @return 返回值
     * @throws MethodNotFoundException
     */
    Object call(String method, Object... args) throws MethodNotFoundException;

}
