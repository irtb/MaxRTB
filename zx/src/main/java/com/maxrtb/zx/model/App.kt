package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class App(
    @SerializedName("app_id")
    val appId: String = "",
    @SerializedName("app_name")
    val appName: String = "",
    @SerializedName("bundle_id")
    val bundleId: String = "",
    @SerializedName("version")
    val version: String = "",
    @SerializedName("category")
    val category: String = "",
)
