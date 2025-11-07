package com.maxrtb.zx.provider

import android.app.Activity
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX智选广告提供商 - 所有广告类型的实现
 */
open class ZXProvider : BaseAdProvider() {

    // ==================== Splash 开屏广告 ====================
    override fun loadAndShowSplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(1000)
                callbackSplashLoaded(adProviderType, alias, listener)
                Thread.sleep(500)
                callbackSplashExposure(adProviderType, listener)
            } catch (e: Exception) {
                callbackSplashFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun loadOnlySplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(2000)
                callbackSplashLoaded(adProviderType, alias, listener)
            } catch (e: Exception) {
                callbackSplashFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun showSplashAd(container: ViewGroup): Boolean = true

    // ==================== Banner 横幅广告 ====================
    override fun showBannerAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: BannerListener
    ) {
        destroyBannerAd()
        callbackBannerStartRequest(adProviderType, alias, listener)
        
        Thread {
            try {
                val frameLayout = FrameLayout(activity).apply {
                    layoutParams = ViewGroup.LayoutParams(320, 50)
                    setBackgroundColor(Color.WHITE)

                    val imageView = ImageView(activity).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        Glide.with(activity)
                            .load("https://via.placeholder.com/320x50")
                            .into(this)
                        
                        setOnClickListener {
                            callbackBannerClicked(adProviderType, listener)
                        }
                    }
                    addView(imageView)
                }
                container.addView(frameLayout)
                
                Thread.sleep(1000)
                callbackBannerLoaded(adProviderType, alias, listener)
                Thread.sleep(300)
                callbackBannerExpose(adProviderType, listener)
            } catch (e: Exception) {
                callbackBannerFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun destroyBannerAd() {}

    // ==================== Interstitial 插屏广告 ====================
    override fun requestInterAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: InterListener
    ) {
        callbackInterStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(1500)
                callbackInterLoaded(adProviderType, alias, listener)
            } catch (e: Exception) {
                callbackInterFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun showInterAd(activity: Activity) {
        // 显示插屏广告对话框
    }

    override fun destroyInterAd() {}

    // ==================== Reward 激励视频 ====================
    override fun requestAndShowRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        requestRewardAd(activity, adProviderType, alias, listener)
    }

    override fun requestRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        callbackRewardStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(2000)
                callbackRewardLoaded(adProviderType, alias, listener)
                Thread.sleep(300)
                callbackRewardVideoCached(adProviderType, listener)
            } catch (e: Exception) {
                callbackRewardFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun showRewardAd(activity: Activity): Boolean = true

    // ==================== Native 原生广告 ====================
    override fun getNativeAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        maxCount: Int,
        listener: NativeListener
    ) {
        callbackNativeStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(1500)
                val adList = mutableListOf<Any>()
                for (i in 0 until maxCount) {
                    adList.add("NativeAd_$i")
                }
                callbackNativeLoaded(adProviderType, alias, listener, adList)
            } catch (e: Exception) {
                callbackNativeFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean = true

    override fun resumeNativeAd(adObject: Any) {}

    override fun pauseNativeAd(adObject: Any) {}

    override fun destroyNativeAd(adObject: Any) {}

    // ==================== NativeExpress 原生模板广告 ====================
    override fun getNativeExpressAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        adCount: Int,
        listener: NativeExpressListener
    ) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(2000)
                val adList = mutableListOf<Any>()
                for (i in 0 until adCount) {
                    adList.add("ExpressAd_$i")
                }
                callbackNativeExpressLoaded(adProviderType, alias, listener, adList)
            } catch (e: Exception) {
                callbackNativeExpressFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun destroyNativeExpressAd(adObject: Any) {}

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean = true

    // ==================== FullVideo 全屏视频 ====================
    override fun requestFullVideoAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: FullVideoListener
    ) {
        callbackFullVideoStartRequest(adProviderType, alias, listener)
        Thread {
            try {
                Thread.sleep(2000)
                callbackFullVideoLoaded(adProviderType, alias, listener)
            } catch (e: Exception) {
                callbackFullVideoFailed(adProviderType, alias, listener, -1, e.message)
            }
        }.start()
    }

    override fun showFullVideoAd(activity: Activity): Boolean = true

    // ==================== 配置对象 ====================
    object Banner {
        var slideIntervalTime = 30 * 1000
    }

    object Reward {
        var userID: String? = null
        var rewardName: String = "金币"
        var rewardAmount: Int = 100
    }

    object Inter {
        var width = 600
        var height = 800
    }

    object Native {
        var imageWidth = 300
        var imageHeight = 200
    }

    object Splash {
        var maxFetchDelay = 3500
    }
}
