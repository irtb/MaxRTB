package com.maxrtb.zhixuan.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maxrtb.zhixuan.api.ZhixuanApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkManager {
    private lateinit var apiService: ZhixuanApiService
    private lateinit var appContext: Context
    private var appId: String = ""

    fun init(context: Context, baseUrl: String, appId: String, isDebug: Boolean) {
        this.appContext = context.applicationContext
        this.appId = appId

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (isDebug) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(ZhixuanApiService::class.java)
    }

    fun getApiService(): ZhixuanApiService = apiService
    fun getContext(): Context = appContext
    fun getAppId(): String = appId
}
