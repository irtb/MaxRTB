package com.maxrtb.zhixuan.tracker

import com.maxrtb.zhixuan.api.model.Bid
import com.maxrtb.zhixuan.helper.MacroReplacer
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AdTracker(private val bid: Bid) {

    private val httpClient = OkHttpClient()

    fun notifyBidSuccess() {
        bid.nurl?.let { url ->
            val replacedUrl = MacroReplacer.replace(url, bid.price)
            sendTracking(replacedUrl, "BidSuccess")
        }
    }

    fun trackImpression() {
        bid.ext?.imptrackers?.forEach { url ->
            val replacedUrl = MacroReplacer.replace(url, bid.price)
            sendTracking(replacedUrl, "Impression")
        }
    }

    fun trackClick() {
        bid.ext?.clicktrackers?.forEach { url ->
            val replacedUrl = MacroReplacer.replace(url, bid.price)
            sendTracking(replacedUrl, "Click")
        }
    }

    private fun sendTracking(url: String, type: String) {
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                ZhixuanHelper.logD("$type 上报成功: $url")
            }

            override fun onFailure(call: Call, e: IOException) {
                ZhixuanHelper.logE("$type 上报失败: $url", e)
            }
        })
    }
}
