package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class Impression(
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("min_duration")
    val minDuration: Int = 0,
    @SerializedName("max_duration")
    val maxDuration: Int = 0,
    @SerializedName("skip_offset")
    val skipOffset: Int = 0,
)
