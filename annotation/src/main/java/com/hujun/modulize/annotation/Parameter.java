package com.hujun.modulize.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by junhu on 2020/3/30
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Parameter {
    //可以不填写，默认为""
    //如果不填写，则将属性的属性名作为key
    String name() default "";
}
