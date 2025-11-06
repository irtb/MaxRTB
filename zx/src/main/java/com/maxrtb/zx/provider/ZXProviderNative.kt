package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class ZXProviderNative : BaseZXProvider() {
    override suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData> {
        this.listener = listener
        return try {
            _adData = AdData(
                adId = "native_${System.currentTimeMillis()}",
                adName = "Native Ad",
                adType = "native",
                title = "原生广告",
                desc = "这是一个原生广告示例",
                iconUrl = "https://via.placeholder.com/40x40",
                imageUrl = "https://via.placeholder.com/300x200",
                price = "¥99.99",
                rating = "★★★★★",
                ctaText = "查看详情",
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
