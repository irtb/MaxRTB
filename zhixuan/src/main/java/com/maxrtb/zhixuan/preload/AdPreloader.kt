package com.maxrtb.zhixuan.preload

import com.maxrtb.zhixuan.cache.AdCacheManager
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.model.BidRequest
import com.maxrtb.zhixuan.model.BidResponse
import com.maxrtb.zhixuan.network.NetworkManager
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AdPreloader {
    
    private val preloadScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val preloadingJobs = mutableMapOf<String, Job>()
    
    interface PreloadListener {
        fun onPreloadSuccess(adType: String, slotId: String)
        fun onPreloadFailed(adType: String, slotId: String, error: String)
    }
    
    fun preloadAd(
        adType: String,
        slotId: String,
        bidRequest: BidRequest,
        listener: PreloadListener? = null
    ) {
        val cacheKey = "${adType}_${slotId}"
        
        // 如果已有缓存，直接返回
        if (AdCacheManager.hasValidCache(cacheKey)) {
            ZhixuanHelper.logI("已有有效缓存，跳过预加载: $cacheKey")
            listener?.onPreloadSuccess(adType, slotId)
            return
        }
        
        // 如果正在预加载，避免重复
        if (preloadingJobs.containsKey(cacheKey)) {
            ZhixuanHelper.logI("正在预加载中，跳过: $cacheKey")
            return
        }
        
        val job = preloadScope.launch {
            try {
                ZhixuanHelper.logI("开始预加载广告: $adType, $slotId")
                
                NetworkManager.requestBid(bidRequest).enqueue(object : Callback<BidResponse> {
                    override fun onResponse(call: Call<BidResponse>, response: Response<BidResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val bidResponse = response.body()!!
                            val bid = bidResponse.seatbid?.firstOrNull()?.bid?.firstOrNull()
                            
                            if (bid != null) {
                                AdCacheManager.putAd(cacheKey, bid, adType)
                                ZhixuanHelper.logI("预加载成功: $adType, $slotId")
                                listener?.onPreloadSuccess(adType, slotId)
                                
                                // 预下载图片资源
                                preloadImageResources(bid)
                            } else {
                                ZhixuanHelper.logE("预加载失败：无广告返回")
                                listener?.onPreloadFailed(adType, slotId, "无广告")
                            }
                        } else {
                            ZhixuanHelper.logE("预加载失败：${response.code()}")
                            listener?.onPreloadFailed(adType, slotId, "请求失败：${response.code()}")
                        }
                        preloadingJobs.remove(cacheKey)
                    }
                    
                    override fun onFailure(call: Call<BidResponse>, t: Throwable) {
                        ZhixuanHelper.logE("预加载失败", t)
                        listener?.onPreloadFailed(adType, slotId, t.message ?: "网络错误")
                        preloadingJobs.remove(cacheKey)
                    }
                })
                
            } catch (e: Exception) {
                ZhixuanHelper.logE("预加载异常", e)
                listener?.onPreloadFailed(adType, slotId, e.message ?: "未知错误")
                preloadingJobs.remove(cacheKey)
            }
        }
        
        preloadingJobs[cacheKey] = job
    }
    
    private fun preloadImageResources(bid: com.maxrtb.zhixuan.model.Bid) {
        preloadScope.launch {
            // 预加载图片到Glide缓存
            val imageUrls = mutableListOf<String>()
            
            bid.iurl?.let { imageUrls.add(it) }
            bid.ext?.nativeExt?.mainimg?.let { imageUrls.add(it) }
            bid.ext?.nativeExt?.icon?.let { imageUrls.add(it) }
            bid.ext?.nativeExt?.logo?.let { imageUrls.add(it) }
            
            imageUrls.forEach { url ->
                try {
                    ZhixuanHelper.logD("预加载图片: $url")
                    // 这里需要在主线程调用Glide
                    // 实际项目中可以使用 Glide.with(context).load(url).preload()
                } catch (e: Exception) {
                    ZhixuanHelper.logE("预加载图片失败: $url", e)
                }
            }
        }
    }
    
    fun cancelPreload(adType: String, slotId: String) {
        val cacheKey = "${adType}_${slotId}"
        preloadingJobs[cacheKey]?.cancel()
        preloadingJobs.remove(cacheKey)
        ZhixuanHelper.logI("取消预加载: $cacheKey")
    }
    
    fun cancelAllPreloads() {
        preloadingJobs.forEach { (key, job) ->
            job.cancel()
        }
        preloadingJobs.clear()
        ZhixuanHelper.logI("取消所有预加载任务")
    }
}
