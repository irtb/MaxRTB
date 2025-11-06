package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class ZXProviderSplash : BaseZXProvider() {
    override suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData> {
        this.listener = listener
        return try {
            _adData = AdData(
                adId = "splash_${System.currentTimeMillis()}",
                adName = "Splash Ad",
                adType = "splash",
                title = "启屏广告",
                desc = "应用启动时的广告",
                imageUrl = "https://via.placeholder.com/1080x1920",
                ctaText = "进入",
                landingPageUrl = "https://example.com",
            )
            onAdLoaded()
            Result.success(_adData!!)
        } catch (e: Exception) {
            onAdLoadFailed(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }
    
    override suspend fun showAd(activity: Activity): Result<Unit> {
        return try {
            if (_adData == null) throw IllegalStateException("Ad data is null")
            onAdShown()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun destroyAd() {
        _adData = null
    }
}
