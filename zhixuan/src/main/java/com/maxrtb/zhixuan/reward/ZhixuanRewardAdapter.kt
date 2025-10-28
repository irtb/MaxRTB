package com.maxrtb.zhixuan.reward

import android.app.Activity
import com.maxrtb.zhixuan.base.BaseAdAdapter
import com.maxrtb.zhixuan.helper.DeviceHelper
import com.maxrtb.zhixuan.model.*
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zhixuan.view.RewardedVideoDialog
import java.util.UUID

class ZhixuanRewardAdapter(
    activity: Activity,
    slotId: String
) : BaseAdAdapter(activity, slotId) {
    
    private var rewardedVideoAdView: RewardedVideoDialog? = null
    
    interface RewardAdListener {
        fun onAdLoaded()
        fun onAdShown()
        fun onAdClicked()
        fun onAdClosed()
        fun onRewarded()
        fun onAdFailed(msg: String)
    }
    
    override fun getAdType(): String = "reward"
    
    override fun createBidRequest(): BidRequest {
        val deviceInfo = DeviceHelper.getDeviceInfo(activity)
        val width = deviceInfo.w ?: 1080
        val height = deviceInfo.h ?: 1920
        
        return BidRequest(
            id = UUID.randomUUID().toString(),
            imp = listOf(
                Impression(
                    id = "1",
                    tagid = slotId,
                    video = Video(
                        w = width,
                        h = height,
                        mimes = listOf("video/mp4", "video/3gpp"),
                        minduration = 5,
                        maxduration = 30
                    ),
                    secure = 1,
                    bidfloor = 0.0,
                    bidfloorcur = "CNY"
                )
            ),
            app = App(
                id = ZhixuanProvider.APP_ID,
                name = activity.packageName,
                bundle = activity.packageName,
                ver = "1.0.0"
            ),
            device = deviceInfo,
            test = 0,
            tmax = 5000
        )
    }
    
    fun loadAd(listener: RewardAdListener) {
        loadAdWithRetry(
            onSuccess = { _ ->
                listener.onAdLoaded()
            },
            onFailed = { msg ->
                listener.onAdFailed(msg)
            }
        )
    }
    
    fun show(listener: RewardAdListener) {
        val bid = currentBid
        if (bid == null) {
            listener.onAdFailed("未加载广告数据")
            return
        }
        
        try {
            val videoUrl = bid.ext?.video?.videourl
            if (videoUrl.isNullOrEmpty()) {
                listener.onAdFailed("无视频素材")
                return
            }
            
            rewardedVideoAdView = RewardedVideoDialog(
                activity, 
                videoUrl, 
                object : RewardedVideoDialog.RewardVideoListener {
                    override fun onShown() {
                        trackImpression()
                        listener.onAdShown()
                    }
                    
                    override fun onCompleted() {
                        trackClick()
                        listener.onRewarded()
                    }
                    
                    override fun onSkipped() {
                        trackClick()
                    }
                    
                    override fun onClosed() {
                        listener.onAdClosed()
                    }
                }
            )
            
            rewardedVideoAdView?.show()
            
        } catch (e: Exception) {
            listener.onAdFailed(e.message ?: "显示失败")
        }
    }
    
    override fun destroy() {
        super.destroy()
        rewardedVideoAdView?.dismiss()
        rewardedVideoAdView = null
    }
}
