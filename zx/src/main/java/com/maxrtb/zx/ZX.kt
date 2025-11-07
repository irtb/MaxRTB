package com.maxrtb.zx

import android.app.Activity
import android.content.Context
import com.maxrtb.zx.config.ZXConfig
import com.maxrtb.zx.config.ZXConfigManager
import com.maxrtb.zx.utils.ZXHelper
import com.maxrtb.zx.provider.ZXProvider

object ZX {
    private const val TAG = "ZX_SDK"
    private var isInitialized = false
    private val provider = ZXProvider()

    fun init(context: Context, config: ZXConfig = ZXConfig()) {
        if (isInitialized) {
            ZXHelper.logW(TAG, "ZX SDK already initialized")
            return
        }
        ZXConfigManager.init(context, config)
        ZXHelper.setDebugMode(config.debugMode)
        ZXHelper.logI(TAG, "ZX SDK initialized")
        isInitialized = true
    }

    fun getProvider(): ZXProvider {
        checkInitialized()
        return provider
    }

    fun getConfig(): ZXConfig {
        checkInitialized()
        return ZXConfigManager.getConfig()
    }

    fun isInitialized(): Boolean = isInitialized

    private fun checkInitialized() {
        require(isInitialized) { "ZX SDK not initialized. Call ZX.init() first." }
    }
}
