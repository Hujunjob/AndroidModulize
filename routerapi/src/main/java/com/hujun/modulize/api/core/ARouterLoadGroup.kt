package com.hujun.modulize.api.core

/**
 *  路由组Group对外提供的加载数据接口
 */
interface ARouterLoadGroup {
    /**
     * 加载路由组group数据
     * 比如：app，返回ARouter$$Path$$app.class，其实现了ARouterLoadPath接口
     */
    fun <T> loadGroup():Map<String,Class<T>> where T: ARouterLoadPath
}
