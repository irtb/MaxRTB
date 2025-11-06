package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class ZXProviderBanner : BaseZXProvider() {
    override suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData> {
        this.listener = listener
        return try {
            _adData = AdData(
                adId = "banner_${System.currentTimeMillis()}",
                adName = "Banner Ad",
                adType = "banner",
                title = "Banner 广告",
                desc = "这是一个 Banner 广告",
                imageUrl = "https://via.placeholder.com/320x50",
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
