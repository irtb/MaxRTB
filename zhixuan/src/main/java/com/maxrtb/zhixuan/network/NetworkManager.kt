package com.maxrtb.zhixuan.network

import com.maxrtb.zhixuan.model.BidRequest
import com.maxrtb.zhixuan.model.BidResponse
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    
    private val apiService: ApiService
    
    init {
        // 创建日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            ZhixuanHelper.logI("OkHttp: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://m1.apifoxmock.com/m1/7056903-6777091-6404548/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
    }
    
    fun requestBid(request: BidRequest): Call<BidResponse> {
        ZhixuanHelper.logI("========== 发送竞价请求 ==========")
        ZhixuanHelper.logI("请求URL: https://m1.apifoxmock.com/m1/7056903-6777091-6404548/api/v2/bid")
        ZhixuanHelper.logI("请求体: $request")
        ZhixuanHelper.logI("============================")
        
        return apiService.requestBid(request)
    }
}

interface ApiService {
    @retrofit2.http.POST("api/v2/bid")
    fun requestBid(@retrofit2.http.Body request: BidRequest): Call<BidResponse>
}
