// zx/src/main/java/com/maxrtb/zx/config/ZXConfig.kt
package com.maxrtb.zx.config

/**
 * ZX SDK 配置类
 */
data class ZXConfig(
    // ===== 基础配置 =====
    val appId: String = "",
    val appName: String = "",
    val versionName: String = "1.0.0",

    // ===== 调试配置 =====
    val debugMode: Boolean = false,
    val enableDebugLog: Boolean = false,

    // ===== 网络配置 =====
    val connectTimeout: Long = 10000,  // 10 秒
    val readTimeout: Long = 10000,
    val writeTimeout: Long = 10000,

    // ===== 缓存配置 =====
    val enableCache: Boolean = true,
    val cacheExpireTime: Long = 3600000,  // 1 小时

    // ===== 预加载配置 =====
    val enablePreload: Boolean = true,
    val preloadCount: Int = 3,

    // ===== 重试配置 =====
    val enableRetry: Boolean = true,
    val maxRetryCount: Int = 3,
    val retryDelayMillis: Long = 1000,

    // ===== 上报配置 =====
    val enableTracking: Boolean = true,
    val trackingUrl: String = "https://tracking.example.com",

    // ===== 用户信息 =====
    val userId: String = "",
    val userAge: Int = 0,
    val userGender: String = "",  // "M", "F", "U"
)
