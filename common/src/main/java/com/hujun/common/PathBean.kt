package com.hujun.common

/**
 * Created by junhu on 2020/3/27
 * 路径对象，公共基础库中，所有子模块都可以调用
 * path : "order/Order_MainActivity"这样
 * clazz : "Order_MainActivity.class"
 */
data class PathBean(var path:String?=null,var clazz:Class<*>?=null)