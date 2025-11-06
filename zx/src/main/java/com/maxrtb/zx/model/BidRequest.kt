// zx/src/main/java/com/maxrtb/zx/model/BidRequest.kt
package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

/**
 * 竞价请求
 */
data class BidRequest(
    @SerializedName("request_id")
    val requestId: String = "",

    @SerializedName("slot_id")
    val slotId: String = "",

    @SerializedName("ad_type")
    val adType: String = "",

    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @SerializedName("app")
    val app: App = App(),

    @SerializedName("device")
    val device: Device = Device(),

    @SerializedName("user")
    val user: User = User(),

    @SerializedName("impression")
    val impression: Impression = Impression(),
)
