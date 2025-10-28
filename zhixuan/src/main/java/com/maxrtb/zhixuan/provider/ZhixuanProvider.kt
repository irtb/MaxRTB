package com.maxrtb.zhixuan.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.maxrtb.zhixuan.bannerad.ZhixuanBannerAdapter
import com.maxrtb.zhixuan.splashad.ZhixuanSplashAdapter
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class ZhixuanProvider : BaseAdProvider() {
    
    companion object {
        const val BASE_URL_DEV = "https://m1.apifoxmock.com/m1/7056903-6777091-6404548/"
        const val BASE_URL_PROD = "https://api.zhixuan.com/"
        const val APP_ID = "100010"
    }
    
    // ==================== 开屏广告 ====================
    override fun loadAndShowSplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        
        val adapter = ZhixuanSplashAdapter()
        adapter.setAdSlotId(alias)
        adapter.show(activity, container, object : SplashListener {
            override fun onAdStartRequest(providerType: String) {}
            override fun onAdLoaded(providerType: String) {
                callbackSplashLoaded(adProviderType, alias, listener)
            }
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, -1, failedMsg)
            }
            override fun onAdClicked(providerType: String) {
                callbackSplashClicked(adProviderType, listener)
            }
            override fun onAdExposure(providerType: String) {
                callbackSplashExposure(adProviderType, listener)
            }
            override fun onAdDismissed(providerType: String) {
                callbackSplashDismiss(adProviderType, listener)
            }
        })
    }
    
    override fun loadOnlySplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
        ZhixuanHelper.logI("仅加载开屏广告")
    }
    
    override fun showSplashAd(container: ViewGroup): Boolean {
        return true
    }
    
    // ==================== Banner 广告 ====================
    override fun showBannerAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: BannerListener
    ) {
        callbackBannerStartRequest(adProviderType, alias, listener)
        
        val adapter = ZhixuanBannerAdapter(activity, alias)
        adapter.loadAd(320, 50, object : ZhixuanBannerAdapter.BannerAdListener {
            override fun onAdLoaded() {
                callbackBannerLoaded(adProviderType, alias, listener)
                adapter.showAd(container, this)
            }
            override fun onAdShown() {
                callbackBannerExpose(adProviderType, listener)
            }
            override fun onAdClicked() {
                callbackBannerClicked(adProviderType, listener)
            }
            override fun onAdClosed() {
                callbackBannerClosed(adProviderType, listener)
            }
            override fun onAdFailed(msg: String) {
                callbackBannerFailed(adProviderType, alias, listener, -1, msg)
            }
        })
    }
    
    override fun destroyBannerAd() {
        ZhixuanHelper.logI("销毁 Banner 广告")
    }
    
    // ==================== 插屏广告 ====================
    override fun requestInterAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: InterListener
    ) {
        callbackInterStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载插屏广告")
    }
    
    override fun showInterAd(activity: Activity) {
        ZhixuanHelper.logI("显示插屏广告")
    }
    
    override fun destroyInterAd() {
        ZhixuanHelper.logI("销毁插屏广告")
    }
    
    // ==================== 原生广告 ====================
    override fun getNativeAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        maxCount: Int,
        listener: NativeListener
    ) {
        callbackNativeStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载原生广告列表")
    }
    
    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return true
    }
    
    override fun resumeNativeAd(adObject: Any) {
        ZhixuanHelper.logI("恢复原生广告")
    }
    
    override fun pauseNativeAd(adObject: Any) {
        ZhixuanHelper.logI("暂停原生广告")
    }
    
    override fun destroyNativeAd(adObject: Any) {
        ZhixuanHelper.logI("销毁原生广告")
    }
    
    // ==================== 原生模板广告 ====================
    override fun getNativeExpressAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        adCount: Int,
        listener: NativeExpressListener
    ) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载原生模板广告列表")
    }
    
    override fun destroyNativeExpressAd(adObject: Any) {
        ZhixuanHelper.logI("销毁原生模板广告")
    }
    
    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return true
    }
    
    // ==================== 激励视频 ====================
    override fun requestAndShowRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        callbackRewardStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载并显示激励视频")
    }
    
    override fun requestRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        callbackRewardStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载激励视频")
    }
    
    override fun showRewardAd(activity: Activity): Boolean {
        ZhixuanHelper.logI("显示激励视频")
        return true
    }
    
    // ==================== 全屏视频 ====================
    override fun requestFullVideoAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: FullVideoListener
    ) {
        callbackFullVideoStartRequest(adProviderType, alias, listener)
        ZhixuanHelper.logI("加载全屏视频广告")
    }
    
    override fun showFullVideoAd(activity: Activity): Boolean {
        ZhixuanHelper.logI("显示全屏视频广告")
        return true
    }
}
