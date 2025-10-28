package com.maxrtb.zhixuan.native

import android.app.Activity
import com.maxrtb.zhixuan.base.BaseAdAdapter
import com.maxrtb.zhixuan.helper.DeviceHelper
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.model.*
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zhixuan.view.NativeAdCardView
import java.util.UUID

class ZhixuanNativeAdapter(
    activity: Activity,
    slotId: String
) : BaseAdAdapter(activity, slotId) {
    
    interface NativeAdListener {
        fun onAdsLoaded(ads: List<Any>)
        fun onAdFailed(msg: String)
    }
    
    override fun getAdType(): String = "native"
    
    override fun createBidRequest(): BidRequest {
        val deviceInfo = DeviceHelper.getDeviceInfo(activity)
        
        return BidRequest(
            id = UUID.randomUUID().toString(),
            imp = listOf(
                Impression(
                    id = "1",
                    tagid = slotId,
                    nativeAd = Native(
                        request = "{\"ver\":\"1.2\",\"assets\":[{\"id\":1,\"required\":1,\"title\":{\"len\":100}},{\"id\":2,\"required\":1,\"img\":{\"type\":3,\"wmin\":300,\"hmin\":250}},{\"id\":3,\"required\":1,\"data\":{\"type\":2,\"len\":200}}]}",
                        ver = "1.2"
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
    
    fun loadAds(listener: NativeAdListener) {
        loadAdWithRetry(
            onSuccess = { bid ->
                try {
                    val nativeExt = bid.ext?.nativeExt
                    if (nativeExt == null) {
                        ZhixuanHelper.logE("原生广告数据为空: nativeExt = null")
                        listener.onAdFailed("无原生广告数据")
                        return@loadAdWithRetry
                    }
                    
                    if (nativeExt.title.isNullOrEmpty()) {
                        ZhixuanHelper.logE("原生广告标题为空")
                        listener.onAdFailed("原生广告标题为空")
                        return@loadAdWithRetry
                    }
                    
                    ZhixuanHelper.logI("原生广告数据验证通过: title=${nativeExt.title}")
                    
                    val nativeAdView = NativeAdCardView(activity)
                    nativeAdView.loadAd(bid, object : NativeAdCardView.NativeAdListener {
                        override fun onShown() {
                            trackImpression()
                            ZhixuanHelper.logI("原生广告展示成功")
                        }
                        
                        override fun onClicked() {
                            trackClick()
                            ZhixuanHelper.logI("原生广告被点击")
                        }
                        
                        override fun onFailed(msg: String) {
                            ZhixuanHelper.logE("原生广告展示失败: $msg")
                            listener.onAdFailed(msg)
                        }
                    })
                    
                    listener.onAdsLoaded(listOf(nativeAdView))
                    ZhixuanHelper.logI("原生广告加载成功: 1个")
                    
                } catch (e: Exception) {
                    ZhixuanHelper.logE("原生广告加载异常", e)
                    listener.onAdFailed(e.message ?: "未知错误")
                }
            },
            onFailed = { msg ->
                ZhixuanHelper.logE("原生广告加载失败: $msg")
                listener.onAdFailed(msg)
            }
        )
    }
}
