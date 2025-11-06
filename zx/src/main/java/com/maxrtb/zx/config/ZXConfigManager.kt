// zx/src/main/java/com/maxrtb/zx/config/ZXConfigManager.kt
package com.maxrtb.zx.config

import android.content.Context

/**
 * ZX 配置管理器
 */
object ZXConfigManager {

    private lateinit var config: ZXConfig
    private lateinit var context: Context

    private val lock = Any()

    /**
     * 初始化配置管理器
     */
    fun init(ctx: Context, zxConfig: ZXConfig) {
        synchronized(lock) {
            context = ctx.applicationContext
            config = zxConfig
        }
    }

    /**
     * 获取当前配置
     */
    fun getConfig(): ZXConfig {
        check(::config.isInitialized) { "ZXConfigManager 未初始化" }
        return config
    }

    /**
     * 获取应用上下文
     */
    fun getContext(): Context {
        check(::context.isInitialized) { "ZXConfigManager 未初始化" }
        return context
    }

    /**
     * 更新配置
     */
    fun updateConfig(newConfig: ZXConfig) {
        synchronized(lock) {
            config = newConfig
        }
    }

    /**
     * 是否已初始化
     */
    fun isInitialized(): Boolean = ::config.isInitialized
}
