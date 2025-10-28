package com.maxrtb.zhixuan.base

import android.app.Activity
import com.google.gson.GsonBuilder
import com.maxrtb.zhixuan.cache.AdCacheManager
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.model.BidRequest
import com.maxrtb.zhixuan.model.BidResponse
import com.maxrtb.zhixuan.network.NetworkManager
import com.maxrtb.zhixuan.retry.RetryManager
import com.maxrtb.zhixuan.tracker.AdTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseAdAdapter(
    protected val activity: Activity,
    protected val slotId: String
) {
    protected var currentBid: Bid? = null
    
    companion object {
        var useCacheForTest = false  // 测试时禁用缓存
    }
    
    abstract fun getAdType(): String
    abstract fun createBidRequest(): BidRequest
    
    protected fun loadAdWithRetry(
        onSuccess: (Bid) -> Unit,
        onFailed: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            RetryManager.retry(times = 3) {
                loadAdInternal(onSuccess, onFailed)
            }
        }
    }
    
    private suspend fun loadAdInternal(
        onSuccess: (Bid) -> Unit,
        onFailed: (String) -> Unit
    ) {
        val cacheKey = "${getAdType()}_$slotId"
        
        if (useCacheForTest) {
            val cachedBid = AdCacheManager.getAd(cacheKey)
            if (cachedBid != null) {
                ZhixuanHelper.logI("使用缓存广告: $cacheKey")
                CoroutineScope(Dispatchers.Main).launch {
                    onSuccess(cachedBid)
                }
                return
            }
        } else {
            ZhixuanHelper.logI("缓存已禁用，直接请求网络")
        }
        
        val request = createBidRequest()
        
        NetworkManager.requestBid(request).enqueue(object : Callback<BidResponse> {
            override fun onResponse(call: Call<BidResponse>, response: Response<BidResponse>) {
                // ========== 打印完整的原始响应 ==========
                ZhixuanHelper.logI("========== HTTP 响应信息 ==========")
                ZhixuanHelper.logI("状态码: ${response.code()}")
                ZhixuanHelper.logI("响应头: ${response.headers()}")
                
                val rawBody = response.raw().body
                if (rawBody != null) {
                    try {
                        val rawString = rawBody.string()
                        ZhixuanHelper.logI("原始响应体:")
                        ZhixuanHelper.logI(rawString)
                    } catch (e: Exception) {
                        ZhixuanHelper.logE("打印原始响应失败", e)
                    }
                }
                ZhixuanHelper.logI("============================")
                // =====================================
                
                handleResponse(response, cacheKey, onSuccess, onFailed)
            }
            
            override fun onFailure(call: Call<BidResponse>, t: Throwable) {
                ZhixuanHelper.logE("广告请求失败", t)
                CoroutineScope(Dispatchers.Main).launch {
                    onFailed(t.message ?: "网络错误")
                }
            }
        })
    }
    
    private fun handleResponse(
        response: Response<BidResponse>,
        cacheKey: String,
        onSuccess: (Bid) -> Unit,
        onFailed: (String) -> Unit
    ) {
        if (!response.isSuccessful) {
            CoroutineScope(Dispatchers.Main).launch {
                onFailed("请求失败: ${response.code()}")
            }
            return
        }
        
        val bidResponse = response.body()
        
        // ========== 打印反序列化后的对象 ==========
        ZhixuanHelper.logI("========== 反序列化后的对象 ==========")
        ZhixuanHelper.logI("bidResponse: $bidResponse")
        ZhixuanHelper.logI("seatbid数量: ${bidResponse?.seatbid?.size}")
        
        bidResponse?.seatbid?.forEachIndexed { index, seat ->
            ZhixuanHelper.logI("seatbid[$index] bid数量: ${seat.bid?.size}")
            seat.bid?.forEachIndexed { bidIndex, bid ->
                ZhixuanHelper.logI("  bid[$bidIndex]:")
                ZhixuanHelper.logI("    id: ${bid.id}")
                ZhixuanHelper.logI("    price: ${bid.price}")
                ZhixuanHelper.logI("    ext: ${bid.ext}")
                ZhixuanHelper.logI("    ext.nativeExt: ${bid.ext?.nativeExt}")
                ZhixuanHelper.logI("    ext.video: ${bid.ext?.video}")
            }
        }
        ZhixuanHelper.logI("============================")
        // =======================================
        
        val bid = bidResponse?.seatbid?.firstOrNull()?.bid?.firstOrNull()
        
        if (bid == null) {
            CoroutineScope(Dispatchers.Main).launch {
                onFailed("无广告返回")
            }
            return
        }
        
        // ========== 打印完整的 bid 对象 ==========
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val bidJson = gson.toJson(bid)
            ZhixuanHelper.logI("========== 完整的 Bid 对象 ==========")
            ZhixuanHelper.logI("广告类型: ${getAdType()}")
            ZhixuanHelper.logI("广告位: $slotId")
            bidJson.split("\n").forEach { line ->
                ZhixuanHelper.logI(line)
            }
            ZhixuanHelper.logI("============================")
        } catch (e: Exception) {
            ZhixuanHelper.logE("打印 Bid 对象失败", e)
        }
        // =======================================
        
        currentBid = bid
        AdCacheManager.putAd(cacheKey, bid, getAdType())
        
        bid.nurl?.let { url ->
            AdTracker.trackImpression(listOf(url))
        }
        
        CoroutineScope(Dispatchers.Main).launch {
            onSuccess(bid)
        }
    }
    
    protected fun trackImpression() {
        currentBid?.ext?.imptrackers?.let { trackers ->
            AdTracker.trackImpression(trackers)
        }
    }
    
    protected fun trackClick() {
        currentBid?.ext?.clicktrackers?.let { trackers ->
            AdTracker.trackClick(trackers)
        }
    }
    
    open fun destroy() {
        currentBid = null
    }
}
