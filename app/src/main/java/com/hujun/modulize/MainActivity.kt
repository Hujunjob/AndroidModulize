package com.hujun.modulize

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujun.modulize.order.Order_MainActivity
import com.hujun.modulize.personal.PersonalMainActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_order.setOnClickListener { jumpToOrder() }
        btn_personal.setOnClickListener { jumpToPersonal() }
    }

    fun jumpToOrder() {
        val intent = Intent(this, Order_MainActivity::class.java)
        startActivity(intent)
    }

    fun jumpToPersonal() {
        val intent = Intent(this, PersonalMainActivity::class.java)
        startActivity(intent)
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
