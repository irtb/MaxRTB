package com.maxrtb.zhixuan.manager

import android.app.Activity
import android.content.Context
import com.maxrtb.zhixuan.cache.AdCacheManager
import com.maxrtb.zhixuan.helper.ZhixuanHelper

/**
 * 智选广告SDK管理器
 * 统一管理所有广告功能
 */
object ZhixuanAdManager {
    
    private lateinit var applicationContext: Context
    private var isInitialized = false
    
    /**
     * 初始化SDK
     */
    fun init(context: Context) {
        if (isInitialized) {
            ZhixuanHelper.logW("SDK已初始化，跳过重复初始化")
            return
        }
        
        applicationContext = context.applicationContext
        isInitialized = true
        
        ZhixuanHelper.logI("智选广告SDK初始化成功")
        
        // 清理过期缓存
        AdCacheManager.cleanExpiredCache()
    }
    
    /**
     * 预加载广告
     */
    fun preloadAds(activity: Activity, slotIds: Map<String, String>) {
        slotIds.forEach { (adType, slotId) ->
            when (adType) {
                "splash" -> preloadSplash(slotId)
                "banner" -> preloadBanner(slotId)
                "interstitial" -> preloadInterstitial(slotId)
                "reward" -> preloadReward(slotId)
                "native" -> preloadNative(slotId)
            }
        }
    }
    
    private fun preloadSplash(slotId: String) {
        // 实现开屏预加载
        ZhixuanHelper.logI("预加载开屏广告: $slotId")
    }
    
    private fun preloadBanner(slotId: String) {
        // 实现Banner预加载
        ZhixuanHelper.logI("预加载Banner广告: $slotId")
    }
    
    private fun preloadInterstitial(slotId: String) {
        // 实现插屏预加载
        ZhixuanHelper.logI("预加载插屏广告: $slotId")
    }
    
    private fun preloadReward(slotId: String) {
        // 实现激励视频预加载
        ZhixuanHelper.logI("预加载激励视频: $slotId")
    }
    
    private fun preloadNative(slotId: String) {
        // 实现原生广告预加载
        ZhixuanHelper.logI("预加载原生广告: $slotId")
    }
    
    /**
     * 获取缓存信息
     */
    fun getCacheInfo(): String {
        return AdCacheManager.getCacheInfo()
    }
    
    /**
     * 清理缓存
     */
    fun clearCache() {
        AdCacheManager.clearAll()
    }
    
    /**
     * 设置日志开关
     */
    fun setDebugEnabled(enabled: Boolean) {
        ZhixuanHelper.isDebug = enabled
    }
    
    /**
     * SDK版本
     */
    fun getVersion(): String {
        return "1.0.0"
    }
}
