package com.maxrtb.zhixuan.model

import com.google.gson.annotations.SerializedName

data class BidResponse(
    val id: String? = null,
    val seatbid: List<SeatBid>? = null,
    val bidid: String? = null,
    val cur: String? = null,
    val nbr: Int? = null
)

data class SeatBid(
    val bid: List<Bid>? = null,
    val seat: String? = null
)

data class Bid(
    val id: String? = null,
    val impid: String? = null,
    val price: Double? = null,
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
    val exp: Int? = null,
    val ext: BidExt? = null
)

data class BidExt(
    @SerializedName("ldp")
    val ldp: String? = null,
    
    @SerializedName("imptrackers")
    val imptrackers: List<String>? = null,
    
    @SerializedName("clicktrackers")
    val clicktrackers: List<String>? = null,
    
    @SerializedName("video")
    val video: VideoExt? = null,
    
    @SerializedName("nativeExt")
    val nativeExt: NativeExt? = null
)

data class VideoExt(
    @SerializedName("videourl")
    val videourl: String? = null,
    
    val mimes: List<String>? = null,
    val duration: Int? = null,
    val w: Int? = null,
    val h: Int? = null,
    val linearity: Int? = null,
    val skip: Int? = null,
    val skipmin: Int? = null,
    val skipafter: Int? = null,
    val starttrackers: List<String>? = null,
    val completetrackers: List<String>? = null
)

data class NativeExt(
    @SerializedName("title")
    val title: String? = null,
    
    @SerializedName("desc")
    val desc: String? = null,
    
    @SerializedName("icon")
    val icon: String? = null,
    
    @SerializedName("mainimg")
    val mainimg: String? = null,
    
    @SerializedName("logo")
    val logo: String? = null
)
