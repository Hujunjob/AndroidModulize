package com.hujun.modulize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujun.modulize.annotation.ARouter

@ARouter(path = "/app/OrderActivity",group = "app")
class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

//        `OrderActivity$$ARouter`.findTargetClass()
//        `MainActivity$$ARouter`.findTargetClass()
    }
}
