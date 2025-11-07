package com.maxrtb.zx.provider

import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Button
import android.graphics.Color
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX插屏广告提供商
 */
abstract class ZXProviderInterstitial : BaseAdProvider() {

    private var mInterstitialDialog: Dialog? = null
    private var mInterListener: InterListener? = null
    private var isInterLoaded = false

    override fun requestInterAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: InterListener
    ) {
        mInterListener = listener
        
        callbackInterStartRequest(adProviderType, alias, listener)

        try {
            Thread {
                Thread.sleep(1500)
                isInterLoaded = true
                callbackInterLoaded(adProviderType, alias, listener)
            }.start()
        } catch (e: Exception) {
            callbackInterFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    override fun showInterAd(activity: Activity) {
        if (!isInterLoaded || mInterListener == null) {
            return
        }

        try {
            mInterstitialDialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar).apply {
                val container = FrameLayout(activity).apply {
                    layoutParams = ViewGroup.LayoutParams(600, 800)
                    setBackgroundColor(Color.WHITE)

                    // 添加广告图片
                    val imageView = ImageView(activity).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        Glide.with(activity)
                            .load("https://via.placeholder.com/600x700")
                            .into(this)
                        
                        setOnClickListener {
                            mInterListener?.let { 
                                callbackInterClicked("zx", it) 
                            }
                        }
                    }
                    addView(imageView)

                    // 添加关闭按钮
                    val closeButton = Button(activity).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            100, 100,
                            android.view.Gravity.TOP or android.view.Gravity.RIGHT
                        )
                        text = "✕"
                        setOnClickListener {
                            dismiss()
                            mInterListener?.let { 
                                callbackInterClosed("zx", it) 
                            }
                        }
                    }
                    addView(closeButton)
                }

                setContentView(container)
                show()

                mInterListener?.let { listener ->
                    callbackInterExpose("zx", listener)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun destroyInterAd() {
        mInterstitialDialog?.dismiss()
        mInterstitialDialog = null
        mInterListener = null
        isInterLoaded = false
    }
}
