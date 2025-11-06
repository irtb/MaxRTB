package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("device_id")
    val deviceId: String = "",
    @SerializedName("udid")
    val udid: String = "",
    @SerializedName("imei")
    val imei: String = "",
    @SerializedName("android_id")
    val androidId: String = "",
    @SerializedName("model")
    val model: String = "",
    @SerializedName("brand")
    val brand: String = "",
    @SerializedName("os_version")
    val osVersion: String = "",
    @SerializedName("screen_width")
    val screenWidth: Int = 0,
    @SerializedName("screen_height")
    val screenHeight: Int = 0,
    @SerializedName("screen_dpi")
    val screenDpi: Int = 0,
    @SerializedName("network_type")
    val networkType: String = "",
    @SerializedName("carrier")
    val carrier: String = "",
    @SerializedName("ip")
    val ip: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("timezone")
    val timezone: String = "",
)
