package com.maxrtb.zx.provider

import android.app.Activity
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import android.widget.ImageView
import android.widget.FrameLayout

/**
 * ZX开屏广告提供商
 */
abstract class ZXProviderSplash : BaseAdProvider() {

    private var mSplashView: FrameLayout? = null
    private var splashImageView: ImageView? = null

    override fun loadAndShowSplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)

        try {
            // 模拟创建开屏广告视图
            createSplashView(activity, adProviderType, listener, container)
            
            // 延迟2秒后回调加载成功
            Thread {
                Thread.sleep(1000)
                callbackSplashLoaded(adProviderType, alias, listener)
                Thread.sleep(500)
                callbackSplashExposure(adProviderType, listener)
            }.start()
        } catch (e: Exception) {
            callbackSplashFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    private fun createSplashView(
        activity: Activity,
        adProviderType: String,
        listener: SplashListener,
        container: ViewGroup
    ) {
        mSplashView = FrameLayout(activity).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(android.graphics.Color.WHITE)

            splashImageView = ImageView(activity).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                // 加载模拟开屏广告图片
                Glide.with(activity)
                    .load("https://via.placeholder.com/1080x1920")
                    .into(this)
                
                setOnClickListener {
                    callbackSplashClicked(adProviderType, listener)
                }
            }
            
            addView(splashImageView)
        }

        container.addView(mSplashView)
    }

    override fun loadOnlySplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        
        try {
            Thread {
                Thread.sleep(2000)
                callbackSplashLoaded(adProviderType, alias, listener)
            }.start()
        } catch (e: Exception) {
            callbackSplashFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        mSplashView?.let { container.addView(it) }
        return mSplashView != null
    }

    private fun destroySplashAd() {
        splashImageView = null
        mSplashView = null
    }
}
