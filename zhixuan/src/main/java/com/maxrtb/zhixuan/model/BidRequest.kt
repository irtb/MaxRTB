package com.maxrtb.zhixuan.api.model

import com.google.gson.annotations.SerializedName

/**
 * BidRequest - 竞价请求
 * 基于OpenRTB 2.5规范
 */
data class BidRequest(
    @SerializedName("id")
    val id: String,  // 请求ID（UUID）

    @SerializedName("imp")
    val imp: List<Impression>,  // 展示对象数组

    @SerializedName("app")
    val app: App? = null,  // 应用信息

    @SerializedName("device")
    val device: Device? = null,  // 设备信息

    @SerializedName("user")
    val user: User? = null,  // 用户信息

    @SerializedName("test")
    val test: Int = 0,  // 测试模式 0=正式 1=测试

    @SerializedName("tmax")
    val tmax: Int = 5000  // 超时时间（毫秒）
)

/**
 * 展示对象
 */
data class Impression(
    @SerializedName("id")
    val id: String,  // 展示ID

    @SerializedName("tagid")
    val tagid: String,  // 广告位ID

    @SerializedName("bidfloor")
    val bidfloor: Double = 0.0,  // 底价（CPM）

    @SerializedName("bidfloorcur")
    val bidfloorcur: String = "CNY",  // 货币单位

    @SerializedName("banner")
    val banner: Banner? = null,  // Banner信息（开屏广告可选）

    @SerializedName("video")
    val video: Video? = null,  // 视频信息（视频广告）

    @SerializedName("native")
    val nativeAd: Native? = null,  // 原生广告信息

    @SerializedName("instl")
    val instl: Int = 0,  // 是否插屏 1=插屏 0=其他

    @SerializedName("secure")
    val secure: Int = 1  // 是否HTTPS 1=必须 0=可选
)

/**
 * Banner信息
 */
data class Banner(
    @SerializedName("w")
    val w: Int,  // 宽度

    @SerializedName("h")
    val h: Int,  // 高度

    @SerializedName("pos")
    val pos: Int = 0,  // 位置 0=未知 4=header 5=footer 7=fullscreen

    @SerializedName("api")
    val api: List<Int>? = null  // API框架 [3,5,6,7]
)

/**
 * 视频信息
 */
data class Video(
    @SerializedName("mimes")
    val mimes: List<String>,  // MIME类型 ["video/mp4"]

    @SerializedName("minduration")
    val minduration: Int = 5,  // 最小时长（秒）

    @SerializedName("maxduration")
    val maxduration: Int = 60,  // 最大时长（秒）

    @SerializedName("protocols")
    val protocols: List<Int> = listOf(2, 3, 5, 6),  // 协议

    @SerializedName("w")
    val w: Int,  // 宽度

    @SerializedName("h")
    val h: Int,  // 高度

    @SerializedName("linearity")
    val linearity: Int = 1,  // 1=线性 2=非线性

    @SerializedName("api")
    val api: List<Int>? = null
)

/**
 * 原生广告信息
 */
data class Native(
    @SerializedName("request")
    val request: String,  // Native请求JSON字符串

    @SerializedName("ver")
    val ver: String = "1.2",  // Native规范版本

    @SerializedName("api")
    val api: List<Int>? = null
)

/**
 * 应用信息
 */
data class App(
    @SerializedName("id")
    val id: String,  // 应用ID（AppId）

    @SerializedName("name")
    val name: String? = null,  // 应用名称

    @SerializedName("bundle")
    val bundle: String? = null,  // 包名

    @SerializedName("ver")
    val ver: String? = null,  // 应用版本

    @SerializedName("cat")
    val cat: List<String>? = null,  // 应用分类

    @SerializedName("storeurl")
    val storeurl: String? = null  // 应用商店URL
)

/**
 * 设备信息
 */
data class Device(
    @SerializedName("ua")
    val ua: String,  // UserAgent

    @SerializedName("ip")
    val ip: String? = null,  // IP地址

    @SerializedName("geo")
    val geo: Geo? = null,  // 地理位置

    @SerializedName("dnt")
    val dnt: Int = 0,  // Do Not Track

    @SerializedName("lmt")
    val lmt: Int = 0,  // Limit Ad Tracking

    @SerializedName("devicetype")
    val devicetype: Int = 4,  // 设备类型 4=手机 5=平板

    @SerializedName("make")
    val make: String? = null,  // 设备制造商

    @SerializedName("model")
    val model: String? = null,  // 设备型号

    @SerializedName("os")
    val os: String,  // 操作系统

    @SerializedName("osv")
    val osv: String,  // 系统版本

    @SerializedName("w")
    val w: Int,  // 屏幕宽度

    @SerializedName("h")
    val h: Int,  // 屏幕高度

    @SerializedName("ppi")
    val ppi: Int? = null,  // 像素密度

    @SerializedName("pxratio")
    val pxratio: Float? = null,  // 像素比

    @SerializedName("language")
    val language: String? = null,  // 语言

    @SerializedName("connectiontype")
    val connectiontype: Int? = null,  // 网络类型

    @SerializedName("ifa")
    val ifa: String? = null,  // 广告标识符（IDFA/GAID）

    @SerializedName("didsha1")
    val didsha1: String? = null,  // 设备ID SHA1

    @SerializedName("didmd5")
    val didmd5: String? = null,  // 设备ID MD5

    @SerializedName("dpidsha1")
    val dpidsha1: String? = null,  // 平台设备ID SHA1

    @SerializedName("dpidmd5")
    val dpidmd5: String? = null  // 平台设备ID MD5
)

/**
 * 地理位置
 */
data class Geo(
    @SerializedName("lat")
    val lat: Double? = null,  // 纬度

    @SerializedName("lon")
    val lon: Double? = null,  // 经度

    @SerializedName("type")
    val type: Int = 1,  // 位置来源 1=GPS 2=IP 3=用户提供

    @SerializedName("country")
    val country: String? = null,  // 国家代码

    @SerializedName("region")
    val region: String? = null,  // 地区代码

    @SerializedName("city")
    val city: String? = null  // 城市
)

/**
 * 用户信息
 */
data class User(
    @SerializedName("id")
    val id: String? = null,  // 用户ID

    @SerializedName("yob")
    val yob: Int? = null,  // 出生年份

    @SerializedName("gender")
    val gender: String? = null,  // 性别 M/F/O

    @SerializedName("keywords")
    val keywords: String? = null  // 关键词
)
