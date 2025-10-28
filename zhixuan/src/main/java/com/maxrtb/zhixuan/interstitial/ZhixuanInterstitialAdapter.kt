package com.maxrtb.zhixuan.interstitial

import android.app.Activity
import com.maxrtb.zhixuan.base.BaseAdAdapter
import com.maxrtb.zhixuan.helper.DeviceHelper
import com.maxrtb.zhixuan.model.*
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zhixuan.interstitial.InterstitialDialog
import java.util.UUID

class ZhixuanInterstitialAdapter(
    activity: Activity,
    slotId: String
) : BaseAdAdapter(activity, slotId) {
    
    private var interstitialDialog: InterstitialDialog? = null
    
    interface InterstitialAdListener {
        fun onAdLoaded()
        fun onAdShown()
        fun onAdClicked()
        fun onAdClosed()
        fun onAdFailed(msg: String)
    }
    
    override fun getAdType(): String = "interstitial"
    
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
                    banner = Banner(
                        w = width,
                        h = height,
                        pos = 7
                    ),
                    instl = 1,
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
    
    fun loadAd(listener: InterstitialAdListener) {
        loadAdWithRetry(
            onSuccess = { _ ->
                listener.onAdLoaded()
            },
            onFailed = { msg ->
                listener.onAdFailed(msg)
            }
        )
    }
    
    fun show(listener: InterstitialAdListener) {
        val bid = currentBid
        if (bid == null) {
            listener.onAdFailed("未加载广告数据")
            return
        }
        
        try {
            interstitialDialog = InterstitialDialog(activity, bid, object : InterstitialDialog.InterstitialListener {
                override fun onShown() {
                    trackImpression()
                    listener.onAdShown()
                }
                
                override fun onClicked() {
                    trackClick()
                    listener.onAdClicked()
                }
                
                override fun onClosed() {
                    listener.onAdClosed()
                }
            })
            
            interstitialDialog?.show()
            
        } catch (e: Exception) {
            listener.onAdFailed(e.message ?: "显示失败")
        }
    }
    
    override fun destroy() {
        super.destroy()
        interstitialDialog = null
    }
}
