package com.maxrtb.zhixuan.bidding

import com.maxrtb.zhixuan.model.BidResponse
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


object AdBiddingManager {

    private val http = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()



    data class BiddingResult(
        val winner: BidResponse,
        val winnerName: String,
        val price: Double,
        val allResponses: List<ProviderResponse>
    )
    
    data class ProviderResponse(
        val providerName: String,
        val response: BidResponse?,
        val price: Double,
        val error: String? = null
    )
    
    /**
     * 执行竞价逻辑
     * @param responses 多个广告源的响应
     * @return 竞价获胜的结果
     */
    fun executeBidding(responses: List<ProviderResponse>): BiddingResult? {
        ZhixuanHelper.logI("开始执行竞价，参与方：${responses.size}个")
        
        // 过滤出有效响应
        val validResponses = responses.filter { 
            it.response != null && 
            it.response.seatbid?.isNotEmpty() == true &&
            it.price > 0
        }
        
        if (validResponses.isEmpty()) {
            ZhixuanHelper.logE("竞价失败：无有效响应")
            return null
        }
        
        // 按价格降序排序
        val sortedResponses = validResponses.sortedByDescending { it.price }
        
        // 获取最高出价者
        val winner = sortedResponses.first()
        
        ZhixuanHelper.logI("竞价结果：${winner.providerName} 获胜，价格：${winner.price}")
        
        // 记录所有竞价信息
        sortedResponses.forEachIndexed { index, response ->
            ZhixuanHelper.logD("竞价排名${index + 1}: ${response.providerName}, 价格：${response.price}")
        }
        
        return BiddingResult(
            winner = winner.response!!,
            winnerName = winner.providerName,
            price = winner.price,
            allResponses = responses
        )
    }
    
    /**
     * 计算二价
     * 获胜者支付第二高价格 + 0.01
     */
    fun calculateSecondPrice(responses: List<ProviderResponse>): Double {
        val validPrices = responses
            .filter { it.response != null && it.price > 0 }
            .map { it.price }
            .sortedDescending()
        
        return when {
            validPrices.size >= 2 -> validPrices[1] + 0.01
            validPrices.size == 1 -> validPrices[0] // 只有一个出价者，支付其出价
            else -> 0.0
        }
    }
    
    /**
     * 价格宏替换
     */
    fun replacePriceMacro(url: String, price: Double): String {
        return url.replace("\${AUCTION_PRICE}", price.toString())
    }
    
    /**
     * 发送竞价通知
     */
    fun sendWinNotice(nurl: String?) {
        if (nurl.isNullOrBlank()) return
        CoroutineScope(Dispatchers.IO).launch {
            com.maxrtb.zhixuan.retry.RetryManager.retry(
                times = 2, initialDelay = 300, factor = 2f, maxDelay = 1000
            ) {
                val req = Request.Builder().url(nurl).build()
                http.newCall(req).execute().use {
                    if (!it.isSuccessful) error("win 上报失败 code=${it.code}")
                }
            }
            ZhixuanHelper.logI("WIN 通知完成: $nurl")
        }
    }

    fun sendLossNotice(burl: String?) {
        if (burl.isNullOrBlank()) return
        CoroutineScope(Dispatchers.IO).launch {
            val req = Request.Builder().url(burl).build()
            runCatching { http.newCall(req).execute().close() }
        }
    }


}
