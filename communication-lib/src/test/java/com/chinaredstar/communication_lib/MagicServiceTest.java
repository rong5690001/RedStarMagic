package com.chinaredstar.communication_lib;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MagicServiceTest {

    private final static String hostPkg = "com.host";
    private final static String hostVersionName = "1.0";
    private final static int hostVersionCode = 1;
    private final static String magicVersionName = "1.0";
    private final static int magicVersionCode = 1;

    @Before
    public void register() {
        MagicServiceManager.init(hostPkg, hostVersionName, hostVersionCode, magicVersionName,
                magicVersionCode);
        MagicServiceManager.register("HostService1", new HostService1());
    }

    @Test
    public void hostServiceMethodTest() throws MethodNotFoundException {
        final MagicService service = MagicServiceManager.getService("HostService1");
        Assert.assertEquals("HostService1_method", service.call("method"));
    }

    @RemoteService(
            pluginName = "plugin"
            , serviceName = "HostService1"
            , version = 1
    )
    public static class HostService1 implements MagicService {

        private String a;

        public String method() {
            return "HostService1_method";
        }

        @Override
        public Object call(String method, Object... args) throws MethodNotFoundException {
            switch (method) {
                case "method":
                    return method();
            }
            return "call";
        }
    }

}
