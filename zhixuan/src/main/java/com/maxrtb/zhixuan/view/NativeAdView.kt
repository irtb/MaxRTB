package com.maxrtb.zhixuan.view

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class NativeAdView(context: Context) : FrameLayout(context) {
    
    private var titleView: TextView? = null
    private var descView: TextView? = null
    private var iconView: ImageView? = null
    private var mainImageView: ImageView? = null
    private var ctaButton: TextView? = null
    private var listener: AdListener? = null
    private var currentBid: Bid? = null
    
    interface AdListener {
        fun onAdShown()
        fun onAdClicked()
        fun onAdFailed(msg: String)
    }
    
    init {
        setupViews()
    }
    
    private fun setupViews() {
        val container = FrameLayout(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        mainImageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (200 * resources.displayMetrics.density).toInt()
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        
        titleView = TextView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = (210 * resources.displayMetrics.density).toInt()
            }
            textSize = 18f
            setTextColor(android.graphics.Color.BLACK)
        }
        
        descView = TextView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = (240 * resources.displayMetrics.density).toInt()
            }
            textSize = 14f
            setTextColor(android.graphics.Color.GRAY)
        }
        
        container.addView(mainImageView)
        container.addView(titleView)
        container.addView(descView)
        
        addView(container)
    }
    
    fun loadAd(bid: Bid, listener: AdListener) {
        this.currentBid = bid
        this.listener = listener
        
        try {
            val native = bid.ext?.nativeExt
            if (native == null) {
                listener.onAdFailed("无原生广告数据")
                return
            }
            
            titleView?.text = native.title ?: "广告"
            descView?.text = native.desc ?: ""
            
            native.mainimg?.let { url ->
                Glide.with(context)
                    .load(url)
                    .into(mainImageView!!)
            }
            
            setOnClickListener {
                listener.onAdClicked()
                handleClick(bid)
            }
            
            listener.onAdShown()
            ZhixuanHelper.logI("原生广告展示成功")
            
        } catch (e: Exception) {
            ZhixuanHelper.logE("原生广告加载失败", e)
            listener.onAdFailed(e.message ?: "加载失败")
        }
    }
    
    private fun handleClick(bid: Bid) {
        bid.ext?.ldp?.let { url ->
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                context.startActivity(intent)
            } catch (e: Exception) {
                ZhixuanHelper.logE("打开落地页失败", e)
            }
        }
    }
    
    fun destroy() {
        listener = null
        currentBid = null
    }
}
