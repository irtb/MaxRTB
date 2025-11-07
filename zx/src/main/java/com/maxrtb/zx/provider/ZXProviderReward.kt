package com.maxrtb.zx.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX激励视频提供商
 */
abstract class ZXProviderReward : BaseAdProvider() {

    private var mRewardListener: RewardListener? = null
    private var isRewardLoaded = false

    override fun requestAndShowRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        requestRewardAd(activity, adProviderType, alias, listener)
        showRewardAd(activity)
    }

    override fun requestRewardAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: RewardListener
    ) {
        mRewardListener = listener
        
        callbackRewardStartRequest(adProviderType, alias, listener)

        try {
            Thread {
                Thread.sleep(2000)
                isRewardLoaded = true
                callbackRewardLoaded(adProviderType, alias, listener)
                Thread.sleep(300)
                callbackRewardVideoCached(adProviderType, listener)
            }.start()
        } catch (e: Exception) {
            callbackRewardFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    override fun showRewardAd(activity: Activity): Boolean {
        if (!isRewardLoaded || mRewardListener == null) {
            return false
        }

        try {
            mRewardListener?.let { listener ->
                callbackRewardShow("zx", listener)
                callbackRewardExpose("zx", listener)
                
                Thread {
                    Thread.sleep(3000)
                    callbackRewardVideoComplete("zx", listener)
                    Thread.sleep(500)
                    callbackRewardVerify("zx", listener)
                    Thread.sleep(300)
                    callbackRewardClosed("zx", listener)
                }.start()
            }
            
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
