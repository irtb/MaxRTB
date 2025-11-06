// zx/src/main/java/com/maxrtb/zx/model/BidResponse.kt
package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

/**
 * 竞价响应
 */
data class BidResponse(
    @SerializedName("request_id")
    val requestId: String = "",

    @SerializedName("bid_id")
    val bidId: String = "",

    @SerializedName("status")
    val status: Int = 0,  // 0: success, 1: fail

    @SerializedName("message")
    val message: String = "",

    @SerializedName("ads")
    val ads: List<AdData> = emptyList(),

    @SerializedName("expires_at")
    val expiresAt: Long = 0,
)
