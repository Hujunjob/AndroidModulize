package com.hujun.compiler.utils;

/**
 * Created by junhu on 2020/3/30
 */
public class Constants {
    //注解处理器需要处理的类的全路径
    public static final String AROUTER_ANNOTATION_TYPES = "com.hujun.modulize.annotation.ARouter";

    public static final String PARAMETER_ANNOTATION_TYPES = "com.hujun.modulize.annotation.Parameter";

    //在gradle里需要传递给注解处理器的信息，这是key-value里的key
    public static final String MODULE_NAME = "moduleName";

    //在gradle里需要传递给注解处理器的信息，这是key-value里的key
    public static final String PKG_NAME_FOR_APT = "pkgNameForAPT";

    //Activity的全类名
    public static final String ACTIVITY_PKG = "android.app.Activity";

    public static final String API_PACKAGE = "com.hujun.modulize.api";

    public static final String AROUTER_PATH = Constants.API_PACKAGE + ".core.ARouterLoadPath";

    public static final String AROUTER_GROUP = Constants.API_PACKAGE + ".core.ARouterLoadGroup";

    /********************************** APT需要用的常量  ***********************************/
    public static final String PATH_METHOD_NAME = "loadPath";
    public static final String GROUP_METHOD_NAME = "loadGroup";

    //Path文件的文件名前缀
    public static final String PATH_FILE_NAME = "ARouter$$Path$$";
    public static final String GROUP_FILE_NAME = "ARouter$$Group$$";

}
