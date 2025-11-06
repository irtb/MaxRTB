package com.maxrtb.zx

import android.app.Activity
import android.content.Context
import com.maxrtb.zx.config.ZXConfig
import com.maxrtb.zx.config.ZXConfigManager
import com.maxrtb.zx.utils.ZXHelper
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.provider.ZXProvider

object ZX {
    private const val TAG = "ZX_SDK"
    private var isInitialized = false

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

    suspend fun requestBannerAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getBannerProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestRewardAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getRewardProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestInterstitialAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getInterstitialProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestNativeAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getNativeProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestSplashAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getSplashProvider()
        provider.requestAd(activity, slotId, listener)
    }

    suspend fun requestAd(
        activity: Activity,
        adType: String,
        slotId: String,
        listener: ZXBaseListener,
    ) {
        checkInitialized()
        val provider = ZXProvider.getProvider(adType)
            ?: throw IllegalArgumentException("Unsupported ad type: $adType")
        provider.requestAd(activity, slotId, listener)
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
