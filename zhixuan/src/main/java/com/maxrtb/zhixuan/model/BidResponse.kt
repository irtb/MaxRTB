package com.maxrtb.zhixuan.api.model

import com.google.gson.annotations.SerializedName

/**
 * BidResponse - 竞价响应
 */
data class BidResponse(
    @SerializedName("id")
    val id: String,  // 对应请求ID

    @SerializedName("seatbid")
    val seatbid: List<SeatBid>? = null,  // 竞价数组

    @SerializedName("bidid")
    val bidid: String? = null,  // 竞价ID

    @SerializedName("cur")
    val cur: String = "CNY",  // 货币单位

    @SerializedName("nbr")
    val nbr: Int? = null  // 无竞价原因码
)

/**
 * SeatBid - 席位竞价
 */
data class SeatBid(
    @SerializedName("bid")
    val bid: List<Bid>,  // 竞价对象数组

    @SerializedName("seat")
    val seat: String? = null  // 席位ID
)

/**
 * Bid - 竞价对象
 */
data class Bid(
    @SerializedName("id")
    val id: String,  // 竞价ID

    @SerializedName("impid")
    val impid: String,  // 对应展示ID

    @SerializedName("price")
    val price: Double,  // 竞价价格（CPM）

    @SerializedName("adid")
    val adid: String? = null,  // 广告创意ID

    @SerializedName("nurl")
    val nurl: String? = null,  // 竞价成功通知URL

    @SerializedName("burl")
    val burl: String? = null,  // 竞价失败通知URL

    @SerializedName("lurl")
    val lurl: String? = null,  // 丢失通知URL

    @SerializedName("adm")
    val adm: String? = null,  // 广告标记（HTML/JSON）

    @SerializedName("adomain")
    val adomain: List<String>? = null,  // 广告主域名

    @SerializedName("iurl")
    val iurl: String? = null,  // 图片URL

    @SerializedName("cid")
    val cid: String? = null,  // 广告系列ID

    @SerializedName("crid")
    val crid: String? = null,  // 创意ID

    @SerializedName("cat")
    val cat: List<String>? = null,  // 创意分类

    @SerializedName("w")
    val w: Int? = null,  // 宽度

    @SerializedName("h")
    val h: Int? = null,  // 高度

    @SerializedName("exp")
    val exp: Int? = null,  // 过期时间（秒）

    @SerializedName("ext")
    val ext: BidExt? = null  // 扩展字段
)

/**
 * Bid扩展字段
 */
data class BidExt(
    @SerializedName("ldp")
    val ldp: String? = null,  // 落地页URL

    @SerializedName("imptrackers")
    val imptrackers: List<String>? = null,  // 曝光监测URLs

    @SerializedName("clicktrackers")
    val clicktrackers: List<String>? = null,  // 点击监测URLs

    @SerializedName("video")
    val video: VideoExt? = null,  // 视频扩展

    @SerializedName("native")
    val nativeExt: NativeExt? = null  // 原生扩展
)

/**
 * 视频扩展
 */
data class VideoExt(
    @SerializedName("videourl")
    val videourl: String? = null,  // 视频URL

    @SerializedName("duration")
    val duration: Int? = null,  // 时长（秒）

    @SerializedName("starttrackers")
    val starttrackers: List<String>? = null,  // 开始播放监测

    @SerializedName("completetrackers")
    val completetrackers: List<String>? = null,  // 完成播放监测

    @SerializedName("closetrackers")
    val closetrackers: List<String>? = null  // 关闭监测
)

/**
 * 原生扩展
 */
data class NativeExt(
    @SerializedName("title")
    val title: String? = null,  // 标题

    @SerializedName("desc")
    val desc: String? = null,  // 描述

    @SerializedName("icon")
    val icon: String? = null,  // 图标URL

    @SerializedName("mainimg")
    val mainimg: String? = null,  // 主图URL

    @SerializedName("logo")
    val logo: String? = null  // Logo URL
)
