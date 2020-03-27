package com.hujun.common.base

import android.app.Application
import android.util.Log

/**
 * Created by junhu on 2020/3/26
 */
open class BaseApplication: Application() {
    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()
    }
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }
}