package com.maxrtb.zhixuan.view

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class NativeAdCardView(context: Context) : FrameLayout(context) {
    
    private var listener: NativeAdListener? = null
    private var currentBid: Bid? = null
    
    interface NativeAdListener {
        fun onShown()
        fun onClicked()
        fun onFailed(msg: String)
    }
    
    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setBackgroundColor(Color.WHITE)
    }
    
    fun loadAd(bid: Bid, listener: NativeAdListener) {
        this.currentBid = bid
        this.listener = listener
        
        try {
            val native = bid.ext?.nativeExt
            
            // ========== 详细的验证 ==========
            if (native == null) {
                ZhixuanHelper.logE("原生广告数据为空: nativeExt = null")
                listener.onFailed("无原生广告数据")
                return
            }
            
            ZhixuanHelper.logI("原生广告数据: title=${native.title}, desc=${native.desc}")
            
            if (native.title.isNullOrEmpty()) {
                ZhixuanHelper.logE("原生广告标题为空")
                listener.onFailed("标题为空")
                return
            }
            
            if (native.mainimg.isNullOrEmpty()) {
                ZhixuanHelper.logW("原生广告图片为空，使用默认图片")
            }
            // ==================================
            
            removeAllViews()
            
            val container = LinearLayout(context).apply {
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                setPadding(12, 12, 12, 12)
            }
            
            // 图片
            val imageView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (200 * resources.displayMetrics.density).toInt()
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
                setBackgroundColor(Color.parseColor("#F0F0F0"))
            }
            
            native.mainimg?.let { url ->
                Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }
            
            // 标题
            val titleView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 12
                }
                text = native.title ?: "推荐"
                textSize = 16f
                setTextColor(Color.parseColor("#333333"))
            }
            
            // 描述
            val descView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                }
                text = native.desc ?: "点击了解更多"
                textSize = 13f
                setTextColor(Color.parseColor("#666666"))
                maxLines = 2
            }
            
            // 底部标签
            val tagView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                }
                text = "赞助商"
                textSize = 11f
                setTextColor(Color.parseColor("#999999"))
                setBackgroundColor(Color.parseColor("#F0F0F0"))
                setPadding(8, 4, 8, 4)
            }
            
            // 组装
            container.addView(imageView)
            container.addView(titleView)
            container.addView(descView)
            container.addView(tagView)
            
            addView(container)
            
            // 设置点击事件
            setOnClickListener {
                listener.onClicked()
                handleClick(bid)
            }
            
            listener.onShown()
            ZhixuanHelper.logI("原生广告展示成功")
            
        } catch (e: Exception) {
            ZhixuanHelper.logE("原生广告加载失败", e)
            listener.onFailed(e.message ?: "加载失败")
        }
    }
    
    private fun handleClick(bid: Bid) {
        bid.ext?.ldp?.let { url ->
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                context.startActivity(intent)
                ZhixuanHelper.logI("打开原生广告落地页: $url")
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
