package com.redstar.magic.pluginlib;

import dalvik.system.DexClassLoader;

public class DefaultClassLoaderProducer extends ClassLoaderProducer {

    @Override
    public ClassLoader createClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        return new DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

}
