package com.redstar.magic.pluginlib;

import dalvik.system.DexClassLoader;

public class WhiteListClassLoaderProducer implements ClassLoaderProducer {
    @Override
    public ClassLoader createClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        return new WhiteListClassLoader(dexPath,optimizedDirectory,librarySearchPath,parent);
    }

    public class WhiteListClassLoader extends DexClassLoader{

        private ClassLoader orgin;

        public WhiteListClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
            super(dexPath, optimizedDirectory, librarySearchPath, parent.getParent());
            this.orgin=parent;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

            Class clazz=null;

            if(checkWhiteList(name)){

                try {

                    clazz=orgin.loadClass(name);

                }catch (Exception e){

                    e.printStackTrace();

                }

            }

            if(clazz==null){
                clazz=super.loadClass(name,resolve);
            }

            return clazz;
        }

        private boolean checkWhiteList(String name){
            return false;
        }

    }

}
