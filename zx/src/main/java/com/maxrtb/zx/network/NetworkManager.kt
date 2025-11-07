package com.maxrtb.zx.network

import com.google.gson.GsonBuilder
import com.maxrtb.zx.BuildConfig
import com.maxrtb.zx.model.BidRequest
import com.maxrtb.zx.model.BidResponse
import com.maxrtb.zx.utils.ZXHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * 网络请求管理器
 */
object NetworkManager {
    
    private val apiService: BidApiService
    
    init {
        // 创建日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            ZXHelper.logI("NetworkManager", "OkHttp: $message")
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

        val baseUrl = BuildConfig.ZX_BASE_URL

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                )
            )
            .client(okHttpClient)
            .build()
        
        apiService = retrofit.create(BidApiService::class.java)
    }
    
    fun requestBid(request: BidRequest): Call<BidResponse> {
        ZXHelper.logI("NetworkManager", "========== 发送竞价请求 ==========")
        ZXHelper.logI("NetworkManager", "请求URL: ${BuildConfig.ZX_BASE_URL}api/v2/bid")
        ZXHelper.logI("NetworkManager", "请求体: $request")
        ZXHelper.logI("NetworkManager", "============================")
        
        return apiService.requestBid(request)
    }
}

/**
 * 竞价API服务接口
 */
interface BidApiService {
    @POST("api/v2/bid")
    fun requestBid(@Body request: BidRequest): Call<BidResponse>
}
