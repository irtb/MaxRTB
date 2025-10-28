package com.maxrtb.zxdemo

import android.app.Application
import com.maxrtb.zhixuan.base.BaseAdAdapter
import com.maxrtb.zhixuan.manager.ZhixuanAdManager

class DemoApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化 SDK
        ZhixuanAdManager.init(this)
        ZhixuanAdManager.setDebugEnabled(true)
        
        // ========== 测试时禁用缓存 ==========
        BaseAdAdapter.useCacheForTest = false
        // ===================================
    }
}
