package com.hujun.modulize

import com.hujun.modulize.api.core.ParameterLoad

/**
 * Created by junhu on 2020/3/30
 */
class `XActivity$$Parameter` :ParameterLoad{
    override fun loadParameter(target: Any) {
        val t = target as MainActivity

        t.name = t.intent.getStringExtra("name")!!
        t.age = t.intent.getIntExtra("age",t.age)
    }
}