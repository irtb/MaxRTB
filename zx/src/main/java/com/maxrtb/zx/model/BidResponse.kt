package com.maxrtb.zx.model

import com.google.gson.annotations.SerializedName

data class BidResponse(
    val id: String? = null,
    val seatbid: List<SeatBid>? = null,
    val bidid: String? = null,
    val cur: String? = "CNY",
    val nbr: Int? = null
)

data class SeatBid(
    val bid: List<Bid>? = null,
    val seat: String? = "zx"
)

data class Bid(
    val id: String? = null,
    val impid: String? = null,
    val price: Double? = 1.5,
    val adid: String? = null,
    val nurl: String? = null,
    val burl: String? = null,
    val lurl: String? = null,
    val adm: String? = null,
    val adomain: List<String>? = null,
    val iurl: String? = null,
    val cid: String? = null,
    val crid: String? = null,
    val cat: List<String>? = null,
    val w: Int? = null,
    val h: Int? = null,
    val exp: Int? = 3600,
    val ext: BidExt? = null
)

data class BidExt(
    @SerializedName("ldp")
    val ldp: String? = "https://example.com/",
    
    @SerializedName("imptrackers")
    val imptrackers: List<String>? = listOf(
        "https://tracker.example.com/imp?id=\${AUCTION_ID}"
    ),
    
    @SerializedName("clicktrackers")
    val clicktrackers: List<String>? = listOf(
        "https://tracker.example.com/click?id=\${AUCTION_ID}"
    ),
    
    @SerializedName("video")
    val video: VideoExt? = null,
    
    @SerializedName("nativeExt")
    val nativeExt: NativeExt? = null
)

data class VideoExt(
    @SerializedName("videourl")
    val videourl: String? = "https://example.com/video.mp4",
    
    val mimes: List<String>? = listOf("video/mp4"),
    val duration: Int? = 15,
    val w: Int? = 1080,
    val h: Int? = 1920,
    val linearity: Int? = 1,
    val skip: Int? = 1,
    val skipmin: Int? = 5,
    val skipafter: Int? = 5,
    val starttrackers: List<String>? = null,
    val completetrackers: List<String>? = null
)

data class NativeExt(
    @SerializedName("title")
    val title: String? = "ZX智选广告",
    
    @SerializedName("desc")
    val desc: String? = "这是一个高质量的原生广告",
    
    @SerializedName("icon")
    val icon: String? = "https://via.placeholder.com/50x50",
    
    @SerializedName("mainimg")
    val mainimg: String? = "https://via.placeholder.com/300x200",
    
    @SerializedName("logo")
    val logo: String? = "https://via.placeholder.com/100x100"
)
