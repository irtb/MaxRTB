package com.maxrtb.zhixuan.helper

object MacroReplacer {
    fun replace(url: String, price: Double): String {
        return url
            .replace("\${AUCTION_PRICE}", price.toString())
            .replace("\${AUCTION_PRICE:B64}", android.util.Base64.encodeToString(price.toString().toByteArray(), android.util.Base64.NO_WRAP))
            .replace("\${AUCTION_ID}", java.util.UUID.randomUUID().toString())
            .replace("\${AUCTION_BID_ID}", java.util.UUID.randomUUID().toString())
            .replace("\${AUCTION_IMP_ID}", "1")
            .replace("\${AUCTION_SEAT_ID}", "zhixuan")
            .replace("\${AUCTION_CURRENCY}", "CNY")
    }
}
