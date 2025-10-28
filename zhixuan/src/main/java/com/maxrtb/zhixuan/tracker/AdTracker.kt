package com.maxrtb.zhixuan.tracker

import com.maxrtb.zhixuan.helper.ZhixuanHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * 广告监测上报
 */
object AdTracker {
    
    private val client = OkHttpClient()
    
    /**
     * 曝光监测
     */
    fun trackImpression(urls: List<String>) {
        track(urls, "曝光")
    }
    
    /**
     * 点击监测
     */
    fun trackClick(urls: List<String>) {
        track(urls, "点击")
    }
    
    /**
     * 视频开始监测
     */
    fun trackVideoStart(urls: List<String>) {
        track(urls, "视频开始")
    }
    
    /**
     * 视频完成监测
     */
    fun trackVideoComplete(urls: List<String>) {
        track(urls, "视频完成")
    }
    
    /**
     * 视频关闭监测
     */
    fun trackVideoClose(urls: List<String>) {
        track(urls, "视频关闭")
    }
    
    /**
     * 执行上报
     */
    private fun track(urls: List<String>, type: String) {
        if (urls.isEmpty()) return
        
        CoroutineScope(Dispatchers.IO).launch {
            urls.forEach { url ->
                try {
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()
                    
                    if (response.isSuccessful) {
                        ZhixuanHelper.logI("${type}上报成功: $url")
                    } else {
                        ZhixuanHelper.logE("${type}上报失败: ${response.code}")
                    }
                    response.close()
                } catch (e: Exception) {
                    ZhixuanHelper.logE("${type}上报异常: $url", e)
                }
            }
        }
    }
}
