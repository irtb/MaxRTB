package com.maxrtb.zhixuan.network

import com.maxrtb.zhixuan.BuildConfig
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.model.BidRequest
import com.maxrtb.zhixuan.model.BidResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(loggingInterceptor)
            .build()

        val baseUrl = BuildConfig.ZHIXUAN_BASE_URL

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        
        apiService = retrofit.create(ApiService::class.java)
    }
    
    fun requestBid(request: BidRequest): Call<BidResponse> {
        ZhixuanHelper.logI("========== 发送竞价请求 ==========")
        ZhixuanHelper.logI("请求URL: ${(BuildConfig.ZHIXUAN_BASE_URL ?: "mock")}/api/v2/bid")
        ZhixuanHelper.logI("请求体: $request")
        ZhixuanHelper.logI("============================")
        
        return apiService.requestBid(request)
    }
}

interface ApiService {
    @retrofit2.http.POST("api/v2/bid")
    fun requestBid(@retrofit2.http.Body request: BidRequest): Call<BidResponse>
}
