package com.maxrtb.zhixuan.api

import com.maxrtb.zhixuan.api.model.BidRequest
import com.maxrtb.zhixuan.api.model.BidResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ZhixuanApiService {
    @POST("api/v1/bid")
    fun bid(@Body request: BidRequest): Call<BidResponse>
}
