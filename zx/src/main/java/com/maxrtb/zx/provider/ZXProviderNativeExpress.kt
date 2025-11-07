package com.maxrtb.zx.provider

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import com.bumptech.glide.Glide
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/**
 * ZX原生模板广告提供商
 */
abstract class ZXProviderNativeExpress : BaseAdProvider() {

    private val expressAdList = mutableListOf<Any>()

    override fun getNativeExpressAdList(
        activity: Activity,
        adProviderType: String,
        alias: String,
        adCount: Int,
        listener: NativeExpressListener
    ) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)

        try {
            Thread {
                Thread.sleep(2000)
                
                // 模拟创建原生模板广告
                expressAdList.clear()
                for (i in 0 until adCount) {
                    val adView = createExpressAdView(activity, adProviderType, listener)
                    expressAdList.add(adView)
                }
                
                callbackNativeExpressLoaded(adProviderType, alias, listener, expressAdList)
            }.start()
        } catch (e: Exception) {
            callbackNativeExpressFailed(adProviderType, alias, listener, -1, e.message)
        }
    }

    private fun createExpressAdView(
        activity: Activity,
        adProviderType: String,
        listener: NativeExpressListener
    ): LinearLayout {
        return LinearLayout(activity).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                300
            )
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            
            // 广告图片
            val imageView = ImageView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
                )
                Glide.with(activity)
                    .load("https://via.placeholder.com/400x200")
                    .into(this)
                
                setOnClickListener {
                    listener.onAdClicked(adProviderType, this)
                }
            }
            addView(imageView)
            
            // 广告标题
            val titleView = TextView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "原生模板广告"
                textSize = 16f
                setTextColor(Color.BLACK)
                setPadding(16, 8, 16, 8)
            }
            addView(titleView)
            
            // 广告描述
            val descView = TextView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "这是一个模拟的原生模板广告"
                textSize = 12f
                setTextColor(Color.GRAY)
                setPadding(16, 0, 16, 8)
            }
            addView(descView)
        }
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        if (adObject is LinearLayout) {
            adObject.removeAllViews()
        }
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is LinearLayout
    }
}
