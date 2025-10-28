package com.maxrtb.zxdemo

import android.app.Application
import com.maxrtb.zhixuan.manager.ZhixuanAdManager

/**
 * Demo 应用的 Application 类
 * 负责 SDK 初始化
 */
class ZxDemoApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化智选广告 SDK
        ZhixuanAdManager.init(this)
        
        // 设置调试模式（开发时启用）
        ZhixuanAdManager.setDebugEnabled(BuildConfig.DEBUG)
    }
}
