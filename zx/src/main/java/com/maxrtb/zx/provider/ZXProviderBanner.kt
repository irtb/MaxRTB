package com.maxrtb.zx.provider

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX Banner广告提供商
 */
abstract class ZXProviderBanner : BaseAdProvider() {

    private var mBannerView: FrameLayout? = null
    private var bannerImageView: ImageView? = null

    override fun showBannerAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: BannerListener
    ) {
        destroyBannerAd()
        
        callbackBannerStartRequest(adProviderType, alias, listener)

        try {
            createBannerView(activity, adProviderType, listener, container)
            
            Thread {
                Thread.sleep(1000)
                callbackBannerLoaded(adProviderType, alias, listener)
                Thread.sleep(300)
                callbackBannerExpose(adProviderType, listener)
            }.start()
        } catch (e: Exception) {
            callbackBannerFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    private fun createBannerView(
        activity: Activity,
        adProviderType: String,
        listener: BannerListener,
        container: ViewGroup
    ) {
        mBannerView = FrameLayout(activity).apply {
            layoutParams = ViewGroup.LayoutParams(320, 50)
            setBackgroundColor(android.graphics.Color.WHITE)

            bannerImageView = ImageView(activity).apply {
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
            
            addView(bannerImageView)
        }

        container.addView(mBannerView)
    }

    override fun destroyBannerAd() {
        bannerImageView = null
        mBannerView = null
    }
}
