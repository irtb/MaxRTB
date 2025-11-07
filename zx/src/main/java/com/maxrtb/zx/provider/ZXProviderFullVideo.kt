package com.maxrtb.zx.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX全屏视频广告提供商
 */
abstract class ZXProviderFullVideo : BaseAdProvider() {

    private var mFullVideoListener: FullVideoListener? = null
    private var isFullVideoLoaded = false

    override fun requestFullVideoAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: FullVideoListener
    ) {
        mFullVideoListener = listener
        
        callbackFullVideoStartRequest(adProviderType, alias, listener)

        try {
            Thread {
                Thread.sleep(2000)
                isFullVideoLoaded = true
                callbackFullVideoLoaded(adProviderType, alias, listener)
                Thread.sleep(300)
                callbackFullVideoCached(adProviderType, listener)
            }.start()
        } catch (e: Exception) {
            callbackFullVideoFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        if (!isFullVideoLoaded || mFullVideoListener == null) {
            return false
        }

        try {
            mFullVideoListener?.let { listener ->
                callbackFullVideoShow("zx", listener)
                
                Thread {
                    Thread.sleep(5000)
                    callbackFullVideoComplete("zx", listener)
                    Thread.sleep(300)
                    callbackFullVideoClosed("zx", listener)
                }.start()
            }
            
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
