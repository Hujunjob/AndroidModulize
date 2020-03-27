package com.hujun.modulize

import com.hujun.common.RecordPathManager
import com.hujun.common.base.BaseApplication
import com.hujun.modulize.order.Order_MainActivity
import com.hujun.modulize.personal.PersonalMainActivity

/**
 * Created by junhu on 2020/3/27
 */
class AppApplication: BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        RecordPathManager.joinGroup("app","MainActivity",MainActivity::class.java)
        RecordPathManager.joinGroup("order","Order_MainActivity",Order_MainActivity::class.java)
        RecordPathManager.joinGroup("personal","PersonalMainActivity",PersonalMainActivity::class.java)
    }
}