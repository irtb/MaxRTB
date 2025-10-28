package com.maxrtb.zhixuan.cache

import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import java.util.concurrent.ConcurrentHashMap

object AdCacheManager {
    
    private val adCache = ConcurrentHashMap<String, CachedAd>()
    private const val CACHE_EXPIRE_TIME = 30 * 60 * 1000L // 30分钟
    
    data class CachedAd(
        val bid: Bid,
        val timestamp: Long,
        val adType: String
    )
    
    fun putAd(key: String, bid: Bid, adType: String) {
        adCache[key] = CachedAd(bid, System.currentTimeMillis(), adType)
        ZhixuanHelper.logI("缓存广告: key=$key, type=$adType")
        
        // 清理过期缓存
        cleanExpiredCache()
    }
    
    fun getAd(key: String): Bid? {
        val cached = adCache[key] ?: return null
        
        // 检查是否过期
        if (System.currentTimeMillis() - cached.timestamp > CACHE_EXPIRE_TIME) {
            adCache.remove(key)
            ZhixuanHelper.logI("广告缓存已过期: key=$key")
            return null
        }
        
        ZhixuanHelper.logI("获取缓存广告: key=$key")
        return cached.bid
    }
    
    fun hasValidCache(key: String): Boolean {
        val cached = adCache[key] ?: return false
        return System.currentTimeMillis() - cached.timestamp <= CACHE_EXPIRE_TIME
    }
    
    fun removeAd(key: String) {
        adCache.remove(key)
        ZhixuanHelper.logI("移除缓存广告: key=$key")
    }
    
    fun clearAll() {
        adCache.clear()
        ZhixuanHelper.logI("清空所有广告缓存")
    }
    
    fun cleanExpiredCache() {
        val currentTime = System.currentTimeMillis()
        val expiredKeys = adCache.entries
            .filter { currentTime - it.value.timestamp > CACHE_EXPIRE_TIME }
            .map { it.key }
        
        expiredKeys.forEach { adCache.remove(it) }
        
        if (expiredKeys.isNotEmpty()) {
            ZhixuanHelper.logI("清理过期缓存: ${expiredKeys.size}个")
        }
    }
    
    fun getCacheInfo(): String {
        val sb = StringBuilder()
        sb.append("广告缓存信息:\n")
        adCache.forEach { (key, value) ->
            val remainTime = CACHE_EXPIRE_TIME - (System.currentTimeMillis() - value.timestamp)
            sb.append("$key: ${value.adType}, 剩余${remainTime / 1000}秒\n")
        }
        return sb.toString()
    }
}
