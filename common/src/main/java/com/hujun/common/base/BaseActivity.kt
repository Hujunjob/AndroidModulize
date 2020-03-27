package com.hujun.common.base

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * Created by junhu on 2020/3/26
 */
open class BaseActivity: Activity() {
    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }
}