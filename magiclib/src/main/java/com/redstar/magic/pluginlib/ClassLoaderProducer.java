package com.redstar.magic.pluginlib;

public interface ClassLoaderProducer {

    ClassLoader createClassLoader(String dexPath, String optimizedDirectory,
                                  String librarySearchPath, ClassLoader parent);

}
