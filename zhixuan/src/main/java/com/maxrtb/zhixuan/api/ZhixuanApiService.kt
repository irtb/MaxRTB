package com.maxrtb.zhixuan.api

import com.maxrtb.zhixuan.model.BidRequest
import com.maxrtb.zhixuan.model.BidResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ZhixuanApiService {
    
    @POST("api/v2/bid")
    fun bid(@Body request: BidRequest): Call<BidResponse>
}
