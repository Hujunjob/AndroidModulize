package com.hujun.common.utils

/**
 * Created by junhu on 2020/3/26
 */

class Cons {
    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()
    }
}