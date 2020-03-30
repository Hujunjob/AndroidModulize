package com.hujun.modulize.personal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hujun.common.base.BaseActivity
import com.hujun.modulize.annotation.ARouter
import kotlinx.android.synthetic.main.personal_activity_main.*

@ARouter(path = "/personal/PersonalMainActivity")
class PersonalMainActivity : BaseActivity() {
    companion object {
        private val TAG = this::class.java.name.replace("${'$'}Companion", "").split(".").last()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_activity_main)
        Log.d(TAG, "onCreate: ")

        btn_order.setOnClickListener { jumpToOrder() }
        btn_app.setOnClickListener { jumpToApp() }

    }

    fun jumpToOrder() {
        //用类加载方式跳转
        val clz = Class.forName("com.hujun.modulize.order.Order_MainActivity")
        val intent = Intent(this,clz)
        startActivity(intent)
    }

    fun jumpToApp() {
        //用类加载方式跳转
        val clz = Class.forName("com.hujun.modulize.MainActivity")
        val intent = Intent(this,clz)
        startActivity(intent)

    }
}
