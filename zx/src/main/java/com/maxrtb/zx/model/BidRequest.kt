package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class BidRequest(
    val id: String? = null,
    val version: String? = "2.5",
    val imp: List<Impression>? = null,
    val app: App? = null,
    val device: Device? = null,
    val user: User? = null,
    val test: Int? = 0,
    val tmax: Int? = 3000
)

data class Impression(
    val id: String? = null,
    val tagid: String? = null,
    val banner: Banner? = null,
    val video: Video? = null,
    val nativeAd: Native? = null,
    val instl: Int? = 0,
    val secure: Int? = 1,
    val bidfloor: Double? = 0.0,
    val bidfloorcur: String? = "CNY"
)

data class Banner(
    val w: Int? = null,
    val h: Int? = null,
    val pos: Int? = 0
)

data class Video(
    val w: Int? = 1080,
    val h: Int? = 1920,
    val mimes: List<String>? = listOf("video/mp4"),
    val minduration: Int? = 5,
    val maxduration: Int? = 30,
    val startdelay: Int? = 0,
    val skip: Int? = 1,
    val skipmin: Int? = 5,
    val skipafter: Int? = 5,
    val linearity: Int? = 1,
    val duration: Int? = 15,
    
    @SerializedName("videourl")
    val videourl: String? = null,
    
    val starttrackers: List<String>? = null,
    val completetrackers: List<String>? = null
)

data class Native(
    val request: String? = null,
    val ver: String? = "1.0",
    val api: List<Int>? = listOf(1, 2, 3, 4, 5, 6)
)

data class App(
    val id: String? = "com.maxrtb.zxdm",
    val name: String? = "ZX Demo",
    val bundle: String? = "com.maxrtb.zxdm",
    val ver: String? = "1.0.0",
    val cat: List<String>? = listOf("IAB1", "IAB2"),
    val storeurl: String? = "https://example.com"
)

data class Device(
    val ua: String? = null,
    val ip: String? = null,
    val geo: Geo? = null,
    val dnt: Int? = 0,
    val lmt: Int? = 0,
    val devicetype: Int? = 1,
    val make: String? = "Apple",
    val model: String? = "iPhone",
    val os: String? = "iOS",
    val osv: String? = "14.0",
    val w: Int? = 1080,
    val h: Int? = 1920,
    val ppi: Int? = 326,
    val pxratio: Double? = 2.0,
    val language: String? = "zh",
    val connectiontype: Int? = 4,
    val ifa: String? = null,
    val didsha1: String? = null,
    val didmd5: String? = null,
    val dpidsha1: String? = null,
    val dpidmd5: String? = null
)

data class Geo(
    val lat: Double? = 39.9042,
    val lon: Double? = 116.4074,
    val country: String? = "CN",
    val city: String? = "Beijing",
    val region: String? = "Beijing"
)

data class User(
    val id: String? = null,
    val buyeruid: String? = null
)
