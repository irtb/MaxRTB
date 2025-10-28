package com.maxrtb.zhixuan.splashad

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.SplashListener
import com.maxrtb.zhixuan.model.*
import com.maxrtb.zhixuan.helper.DeviceHelper
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.network.NetworkManager
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zhixuan.tracker.AdTracker
import com.maxrtb.zhixuan.view.SplashAdView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ZhixuanSplashAdapter {

    private var splashAdView: SplashAdView? = null
    private var currentBid: Bid? = null
    private val mProviderType: String = "zhixuan"
    private var mAdSlotId: String? = null

    fun setAdSlotId(slotId: String) {
        this.mAdSlotId = slotId
    }

    fun show(activity: Activity, container: ViewGroup, listener: SplashListener) {
        val slotId = mAdSlotId
        if (slotId.isNullOrEmpty()) {
            ZhixuanHelper.logE("广告位ID为空")
            listener.onAdFailed(mProviderType, "广告位ID为空")
            return
        }

        ZhixuanHelper.logI("开始请求开屏广告: slotId=$slotId")
        listener.onAdStartRequest(mProviderType)

        try {
            val request = buildBidRequest(activity, slotId)

            NetworkManager.requestBid(request).enqueue(object : Callback<BidResponse> {
                override fun onResponse(call: Call<BidResponse>, response: Response<BidResponse>) {
                    handleBidResponse(activity, container, response, listener)
                }

                override fun onFailure(call: Call<BidResponse>, t: Throwable) {
                    ZhixuanHelper.logE("请求失败", t)
                    listener.onAdFailed(mProviderType, t.message ?: "网络错误")
                }
            })
        } catch (e: Exception) {
            ZhixuanHelper.logE("创建请求失败", e)
            listener.onAdFailed(mProviderType, e.message ?: "未知错误")
        }
    }

    private fun handleBidResponse(
        activity: Activity,
        container: ViewGroup,
        response: Response<BidResponse>,
        listener: SplashListener
    ) {
        if (!response.isSuccessful) {
            ZhixuanHelper.logE("请求失败: HTTP ${response.code()}")
            listener.onAdFailed(mProviderType, "请求失败: ${response.code()}")
            return
        }

        val bidResponse = response.body()
        if (bidResponse == null) {
            ZhixuanHelper.logE("响应体为空")
            listener.onAdFailed(mProviderType, "响应体为空")
            return
        }

        val bid = bidResponse.seatbid?.firstOrNull()?.bid?.firstOrNull()
        if (bid == null) {
            ZhixuanHelper.logE("无广告返回: nbr=${bidResponse.nbr}")
            listener.onAdFailed(mProviderType, "无广告返回")
            return
        }

        currentBid = bid
        
        bid.nurl?.let { nurl ->
            AdTracker.trackImpression(listOf(nurl))
        }

        ZhixuanHelper.logI("广告请求成功: bidId=${bid.id}, price=${bid.price}")
        listener.onAdLoaded(mProviderType)

        showAd(activity, container, bid, listener)
    }

    private fun buildBidRequest(activity: Activity, slotId: String): BidRequest {
        val deviceInfo = DeviceHelper.getDeviceInfo(activity)
        val screenWidth = deviceInfo.w ?: 1080
        val screenHeight = deviceInfo.h ?: 2217

        return BidRequest(
            id = UUID.randomUUID().toString(),
            imp = listOf(
                Impression(
                    id = "1",
                    tagid = slotId,
                    banner = Banner(
                        w = screenWidth,
                        h = screenHeight,
                        pos = 7
                    ),
                    instl = 0,
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

    private fun showAd(activity: Activity, container: ViewGroup, bid: Bid, listener: SplashListener) {
        try {
            splashAdView = SplashAdView(activity)
            splashAdView?.loadAd(bid, object : SplashAdView.AdListener {
                override fun onAdShown() {
                    ZhixuanHelper.logI("广告曝光")
                    listener.onAdExposure(mProviderType)
                    bid.ext?.imptrackers?.let { trackers ->
                        AdTracker.trackImpression(trackers)
                    }
                }

                override fun onAdClicked() {
                    ZhixuanHelper.logI("广告点击")
                    listener.onAdClicked(mProviderType)
                    bid.ext?.clicktrackers?.let { trackers ->
                        AdTracker.trackClick(trackers)
                    }
                }

                override fun onAdDismissed() {
                    ZhixuanHelper.logI("广告关闭")
                    listener.onAdDismissed(mProviderType)
                }

                override fun onAdSkip() {
                    ZhixuanHelper.logI("广告跳过")
                    listener.onAdDismissed(mProviderType)
                }

                override fun onAdFailed(msg: String) {
                    ZhixuanHelper.logE("广告展示失败: $msg")
                    listener.onAdFailed(mProviderType, msg)
                }
            })

            container.removeAllViews()
            container.addView(splashAdView)

        } catch (e: Exception) {
            ZhixuanHelper.logE("展示广告失败", e)
            listener.onAdFailed(mProviderType, e.message ?: "未知错误")
        }
    }

    fun destroy() {
        splashAdView?.destroy()
        splashAdView = null
        currentBid = null
    }
}
