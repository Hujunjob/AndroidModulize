package com.hujun.modulize.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by junhu on 2020/3/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ARouter {
    //详细路由路径，比如"/app/MainActivity"
    String path();

    //也可以不填，可以从path中拿
    String group() default "";
}
