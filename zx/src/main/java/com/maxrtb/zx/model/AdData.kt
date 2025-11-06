// zx/src/main/java/com/maxrtb/zx/model/AdData.kt
package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

/**
 * 广告数据
 */
data class AdData(
    @SerializedName("ad_id")
    val adId: String = "",

    @SerializedName("ad_name")
    val adName: String = "",

    @SerializedName("ad_type")
    val adType: String = "",  // banner, reward, interstitial, native, splash

    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val desc: String = "",

    @SerializedName("icon_url")
    val iconUrl: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("landing_page_url")
    val landingPageUrl: String = "",

    @SerializedName("cta_text")
    val ctaText: String = "查看详情",

    @SerializedName("price")
    val price: String = "",

    @SerializedName("rating")
    val rating: String = "★★★★★",

    @SerializedName("click_url")
    val clickUrl: String = "",

    @SerializedName("impression_url")
    val impressionUrl: String = "",

    @SerializedName("expires_at")
    val expiresAt: Long = 0,
)
