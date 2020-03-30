package com.hujun.modulize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujun.modulize.annotation.ARouter
import com.hujun.modulize.apt.`ARouter$$Path$$personal`
import kotlinx.android.synthetic.main.activity_main.*

@ARouter(path = "/app/MainActivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_order.setOnClickListener { jumpToOrder() }
        btn_personal.setOnClickListener { jumpToPersonal() }

//        `MainActivity$$ARouter`.get1()

    }

    fun jumpToOrder() {
//        val intent = Intent(this, Order_MainActivity::class.java)
//        startActivity(intent)
//        val groupMap = `ARouter$$Group$$order`()
//        val map = groupMap.loadGroup()
//        if (map["order"] != null) {
//            val path = map["order"]?.newInstance()
//            val pathMap = path?.loadPath()
//            val routerBean = pathMap?.get("/order/OrderMainActivity")
//            if (routerBean != null) {
//                val intent = Intent(this, routerBean.clazz)
//                startActivity(intent)
//            }
//        }

    }

    fun jumpToPersonal() {
//        val intent = Intent(this, PersonalMainActivity::class.java)
//        startActivity(intent)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
