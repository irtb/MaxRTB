package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

class ZXProviderReward : BaseZXProvider() {
    override suspend fun requestAd(
        activity: Activity,
        slotId: String,
        listener: ZXBaseListener,
    ): Result<AdData> {
        this.listener = listener
        return try {
            _adData = AdData(
                adId = "reward_${System.currentTimeMillis()}",
                adName = "Reward Video",
                adType = "reward",
                title = "激励视频",
                desc = "观看视频获得奖励",
                imageUrl = "https://via.placeholder.com/300x300",
                ctaText = "观看视频",
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
