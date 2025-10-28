package com.maxrtb.zhixuan.bannerad

import android.app.Activity
import android.view.ViewGroup
import com.maxrtb.zhixuan.base.BaseAdAdapter
import com.maxrtb.zhixuan.helper.DeviceHelper
import com.maxrtb.zhixuan.model.*
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zhixuan.view.BannerAdView
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import java.util.UUID

class ZhixuanBannerAdapter(
    activity: Activity,
    slotId: String
) : BaseAdAdapter(activity, slotId) {
    
    private var bannerAdView: BannerAdView? = null
    
    interface BannerAdListener {
        fun onAdLoaded()
        fun onAdShown()
        fun onAdClicked()
        fun onAdClosed()
        fun onAdFailed(msg: String)
    }
    
    override fun getAdType(): String = "banner"
    
    override fun createBidRequest(): BidRequest {
        val deviceInfo = DeviceHelper.getDeviceInfo(activity)
        
        return BidRequest(
            id = UUID.randomUUID().toString(),
            imp = listOf(
                Impression(
                    id = "1",
                    tagid = slotId,
                    banner = Banner(
                        w = 320,
                        h = 50,
                        pos = 1
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
    
    fun loadAd(width: Int, height: Int, listener: BannerAdListener) {
        loadAdWithRetry(
            onSuccess = { bid ->
                try {
                    bannerAdView = BannerAdView(activity).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        loadAd(bid, width, height, object : BannerAdView.BannerListener {
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
                            
                            override fun onFailed(msg: String) {
                                listener.onAdFailed(msg)
                            }
                        })
                    }
                    
                    listener.onAdLoaded()
                    
                } catch (e: Exception) {
                    ZhixuanHelper.logE("Banner 加载异常", e)
                    listener.onAdFailed(e.message ?: "加载失败")
                }
            },
            onFailed = { msg ->
                listener.onAdFailed(msg)
            }
        )
    }
    
    fun showAd(container: ViewGroup, listener: BannerAdListener) {
        val view = bannerAdView
        if (view == null) {
            listener.onAdFailed("Banner 未加载")
            return
        }
        
        try {
            container.removeAllViews()
            container.addView(view)
            listener.onAdShown()
        } catch (e: Exception) {
            listener.onAdFailed(e.message ?: "显示失败")
        }
    }
    
    fun getView(): BannerAdView? = bannerAdView
    
    override fun destroy() {
        super.destroy()
        bannerAdView = null
    }
}
