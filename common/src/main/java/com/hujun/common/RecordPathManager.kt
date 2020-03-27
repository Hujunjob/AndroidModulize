package com.hujun.common

/**
 * Created by junhu on 2020/3/27
 * 全局路径记录，根据子模块分组
 */
class RecordPathManager {
    companion object {
        //key : order分组
        //value ： order子模块下，对应所有的Activity路径信息
        private val groupMap = mutableMapOf<String, MutableList<PathBean>>()

        fun joinGroup(groupName: String, pathName: String, clazz: Class<*>) {
            var list = groupMap[groupName]
            if (list == null) {
                list = arrayListOf(PathBean(pathName, clazz))
                groupMap[groupName] = list
            } else {
                var hasPath = false
                list.forEach {
                    if (pathName.equals(it.path, true)) {
                        hasPath = true
                    }
                }
                if (!hasPath)
                    list.add(PathBean(pathName, clazz))
            }
        }

        fun getTargetClass(groupName: String, pathName: String): Class<*>? {
            val list = groupMap[groupName] ?: return null

            list.forEach {
                if (pathName.equals(it.path, true)) {
                    return it.clazz
                }
            }
            return null
        }
    }
}