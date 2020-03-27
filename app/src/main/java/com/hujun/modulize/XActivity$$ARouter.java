package com.hujun.modulize;
public class XActivity$$ARouter {
    public static Class<?> findTargetClass(String path){
        if (path.equalsIgnoreCase("/app/MainActivity")){
            return MainActivity.class;
        }
        return null;
    }
}
