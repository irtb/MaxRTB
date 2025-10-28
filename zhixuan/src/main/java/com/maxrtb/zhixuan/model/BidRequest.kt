package com.maxrtb.zhixuan.model

import com.google.gson.annotations.SerializedName

data class BidRequest(
    val id: String? = null,
    val imp: List<Impression>? = null,
    val app: App? = null,
    val device: Device? = null,
    val user: User? = null,
    val test: Int? = null,
    val tmax: Int? = null
)

data class Impression(
    val id: String? = null,
    val tagid: String? = null,
    val banner: Banner? = null,
    val video: Video? = null,
    val nativeAd: Native? = null,
    val instl: Int? = null,
    val secure: Int? = null,
    val bidfloor: Double? = null,
    val bidfloorcur: String? = null
)

data class Banner(
    val w: Int? = null,
    val h: Int? = null,
    val pos: Int? = null
)

data class Video(
    val w: Int? = null,
    val h: Int? = null,
    val mimes: List<String>? = null,
    val minduration: Int? = null,
    val maxduration: Int? = null,
    val startdelay: Int? = null,
    val skip: Int? = null,
    val skipmin: Int? = null,
    val skipafter: Int? = null,
    val linearity: Int? = null,
    val duration: Int? = null,
    
    @SerializedName("videourl")
    val videourl: String? = null,
    
    val starttrackers: List<String>? = null,
    val completetrackers: List<String>? = null
)

data class Native(
    val request: String? = null,
    val ver: String? = null,
    val api: List<Int>? = null
)

data class App(
    val id: String? = null,
    val name: String? = null,
    val bundle: String? = null,
    val ver: String? = null,
    val cat: List<String>? = null,
    val storeurl: String? = null
)

data class Device(
    val ua: String? = null,
    val ip: String? = null,
    val geo: Geo? = null,
    val dnt: Int? = null,
    val lmt: Int? = null,
    val devicetype: Int? = null,
    val make: String? = null,
    val model: String? = null,
    val os: String? = null,
    val osv: String? = null,
    val w: Int? = null,
    val h: Int? = null,
    val ppi: Int? = null,
    val pxratio: Double? = null,
    val language: String? = null,
    val connectiontype: Int? = null,
    val ifa: String? = null,
    val didsha1: String? = null,
    val didmd5: String? = null,
    val dpidsha1: String? = null,
    val dpidmd5: String? = null
)

data class Geo(
    val lat: Double? = null,
    val lon: Double? = null,
    val country: String? = null,
    val city: String? = null,
    val region: String? = null
)

data class User(
    val id: String? = null,
    val buyeruid: String? = null
)
