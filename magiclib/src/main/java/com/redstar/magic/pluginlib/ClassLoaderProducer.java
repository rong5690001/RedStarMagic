package com.redstar.magic.pluginlib;

public abstract class ClassLoaderProducer {

    public abstract ClassLoader createClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent);

}
