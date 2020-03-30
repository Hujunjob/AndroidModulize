package com.hujun.modulize.api.core

import com.hujun.modulize.annotation.RouterBean

/**
 * Created by junhu on 2020/3/30
 * 路由组Group对应的详细path加载数据接口
 * 比如：app分组对应哪些类需要加载
 */
interface ARouterLoadPath {
    /**
     * 加载路由组group中的path详细数据
     * 比如"app"分组下有这些信息
     * @return key:"/app/MainActivity",value:MainActivity信息封装在RouterBean对象中
     */
    fun loadPath():Map<String, RouterBean>
}