package com.maxrtb.zhixuan.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.maxrtb.zhixuan.helper.ZhixuanHelper
import com.maxrtb.zhixuan.network.NetworkManager
import com.maxrtb.zhixuan.splashad.ZhixuanSplashAdapter

class ZhixuanProvider : BaseAdProvider() {

    companion object {
        private var splashAdapter: ZhixuanSplashAdapter? = null
        private var isInitialized = false
        const val BASE_URL_DEV = "https://dev.sdk.maxrtb.com/"
        const val BASE_URL_PROD = "https://sdk.maxrtb.com/"
        const val APP_ID = "100010" // 智选AppId
    }

    override fun loadAndShowSplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {
        // 懒初始化SDK
        if (!isInitialized) {
            initSDK(activity)
        }

        splashAdapter = ZhixuanSplashAdapter()
        splashAdapter?.setAdSlotId(alias)
        callbackSplashStartRequest(adProviderType, alias, listener)
        splashAdapter?.show(activity, container, listener)
    }

    override fun loadOnlySplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
        callbackSplashFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun showSplashAd(container: ViewGroup): Boolean = false

    override fun showBannerAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: BannerListener
    ) {
        callbackBannerFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun destroyBannerAd() {}

    override fun requestInterAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: InterListener
    ) {
        callbackInterFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun showInterAd(activity: Activity) {}
    override fun destroyInterAd() {}

    override fun getNativeAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        maxCount: Int,
        listener: NativeListener
    ) {
        callbackNativeFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean = false
    override fun resumeNativeAd(adObject: Any) {}
    override fun pauseNativeAd(adObject: Any) {}
    override fun destroyNativeAd(adObject: Any) {}

    override fun getNativeExpressAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        adCount: Int,
        listener: NativeExpressListener
    ) {
        callbackNativeExpressFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun destroyNativeExpressAd(adObject: Any) {}
    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean = false

    override fun requestAndShowRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        callbackRewardFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun requestRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        callbackRewardFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun showRewardAd(activity: Activity): Boolean = false

    override fun requestFullVideoAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: FullVideoListener
    ) {
        callbackFullVideoFailed(adProviderType, alias, listener, null, "暂未实现")
    }

    override fun showFullVideoAd(activity: Activity): Boolean = false

    /**
     * 初始化智选SDK
     */
    private fun initSDK(activity: Activity) {
        if (isInitialized) return

        try {
            // 使用TogetherAd的debug开关
            val isDebug = TogetherAd.printLogEnable
            val baseUrl = if (isDebug) BASE_URL_DEV else BASE_URL_PROD

            ZhixuanHelper.isDebug = isDebug

            NetworkManager.init(
                context = activity.applicationContext,
                baseUrl = baseUrl,
                appId = APP_ID,
                isDebug = isDebug
            )

            isInitialized = true
            ZhixuanHelper.logI("智选SDK初始化成功 [AppId=$APP_ID, BaseUrl=$baseUrl]")

        } catch (e: Exception) {
            ZhixuanHelper.logE("SDK初始化失败", e)
        }
    }
}
