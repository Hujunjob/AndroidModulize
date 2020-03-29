package com.hujun.modulize.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hujun.common.RecordPathManager
import com.hujun.common.base.BaseActivity
import com.hujun.modulize.annotation.ARouter
import kotlinx.android.synthetic.main.order_activity_main.*

@ARouter(path = "/order/Order_MainActivity")
class Order_MainActivity : BaseActivity() {
    companion object{
        private val TAG = this::class.java.name.replace("${'$'}Companion","").split(".").last()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_activity_main)
        Log.d(TAG, "onCreate: ")

        btn_app.setOnClickListener { jumpToApp() }
        btn_personal.setOnClickListener { jumpToPersonal() }
    }

    fun jumpToApp(){
        //用类加载方式跳转
//        val clz = Class.forName("com.hujun.modulize.MainActivity")
        val clz = RecordPathManager.getTargetClass("app","MainActivity")
        if (clz==null){
            return
        }
        val intent = Intent(this,clz)
        startActivity(intent)
    }

    fun jumpToPersonal(){
        //用类加载方式跳转
//        val clz = Class.forName("com.hujun.modulize.personal.PersonalMainActivity")
        val clz = RecordPathManager.getTargetClass("personal","PersonalMainActivity")
        if (clz==null){
            return
        }
        val intent = Intent(this,clz)
        startActivity(intent)
    }
}
