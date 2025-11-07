package com.maxrtb.zx.utils

/**
 * 宏替换工具类 - 用于替换tracking URL中的宏
 * 参考zhixuan的实现
 */
object MacroReplacer {
    
    // 支持的宏定义
    private const val AUCTION_ID = "\${AUCTION_ID}"
    private const val AUCTION_BID_ID = "\${AUCTION_BID_ID}"
    private const val AUCTION_IMP_ID = "\${AUCTION_IMP_ID}"
    private const val AUCTION_SEAT_ID = "\${AUCTION_SEAT_ID}"
    private const val AUCTION_AD_ID = "\${AUCTION_AD_ID}"
    private const val AUCTION_PRICE = "\${AUCTION_PRICE}"
    private const val AUCTION_CURRENCY = "\${AUCTION_CURRENCY}"
    private const val AUCTION_MEDIA_TYPE = "\${AUCTION_MEDIA_TYPE}"
    private const val CACHE_BUSTER = "\${CACHE_BUSTER}"
    private const val RANDOM = "\${RANDOM}"
    private const val TIMESTAMP = "\${TIMESTAMP}"

    /**
     * 替换tracking URL中的宏
     */
    fun replace(
        url: String?,
        auctionId: String? = null,
        bidId: String? = null,
        impId: String? = null,
        seatId: String? = null,
        adId: String? = null,
        price: Double? = null,
        currency: String? = null,
        mediaType: String? = null
    ): String {
        if (url.isNullOrEmpty()) {
            return url ?: ""
        }

        var result: String = url
        
        // 替换基础宏
        if (auctionId != null) {
            result = result.replace(AUCTION_ID, auctionId)
        }
        if (bidId != null) {
            result = result.replace(AUCTION_BID_ID, bidId)
        }
        if (impId != null) {
            result = result.replace(AUCTION_IMP_ID, impId)
        }
        if (seatId != null) {
            result = result.replace(AUCTION_SEAT_ID, seatId)
        }
        if (adId != null) {
            result = result.replace(AUCTION_AD_ID, adId)
        }
        if (price != null) {
            result = result.replace(AUCTION_PRICE, price.toString())
        }
        if (currency != null) {
            result = result.replace(AUCTION_CURRENCY, currency)
        }
        if (mediaType != null) {
            result = result.replace(AUCTION_MEDIA_TYPE, mediaType)
        }
        
        // 替换随机数宏
        result = result.replace(CACHE_BUSTER, System.currentTimeMillis().toString())
        result = result.replace(RANDOM, generateRandomString())
        result = result.replace(TIMESTAMP, System.currentTimeMillis().toString())
        
        return result
    }

    /**
     * 生成随机字符串
     */
    private fun generateRandomString(): String {
        return (Math.random() * 1000000).toLong().toString()
    }
}
