package com.maxrtb.zhixuan.interstitial

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

/**
 * 完整的插屏广告Dialog
 */
class InterstitialDialog(
    private val activity: Activity,
    private val bid: Bid,
    private val listener: InterstitialListener
) {
    
    private var dialog: Dialog? = null
    
    interface InterstitialListener {
        fun onShown()
        fun onClicked()
        fun onClosed()
    }
    
    fun show() {
        try {
            dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#80000000")))
                setCancelable(false)
                
                // 根布局 - 半透明背景
                val rootLayout = FrameLayout(activity).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                
                // 广告容器 - 居中卡片
                val cardWidth = (activity.resources.displayMetrics.widthPixels * 0.85).toInt()
                val cardHeight = (activity.resources.displayMetrics.heightPixels * 0.7).toInt()
                
                val cardContainer = FrameLayout(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(cardWidth, cardHeight).apply {
                        gravity = Gravity.CENTER
                    }
                    setBackgroundColor(Color.WHITE)
                    elevation = 12f
                }
                
                // 广告图片
                val imageView = ImageView(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (cardHeight * 0.75).toInt()
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setBackgroundColor(Color.parseColor("#F0F0F0"))
                }
                
                // 点击区域（图片）
                imageView.setOnClickListener {
                    listener.onClicked()
                    handleClick(bid)
                }
                
                // 标题
                val titleView = TextView(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = (cardHeight * 0.75).toInt()
                        leftMargin = 12
                        rightMargin = 12
                    }
                    text = bid.ext?.nativeExt?.title ?: "推荐内容"
                    textSize = 16f
                    setTextColor(Color.parseColor("#333333"))
                    maxLines = 1
                }
                
                // 描述
                val descView = TextView(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        topMargin = (cardHeight * 0.75 + 35).toInt()
                        leftMargin = 12
                        rightMargin = 12
                    }
                    text = bid.ext?.nativeExt?.desc ?: "点击了解更多详情"
                    textSize = 13f
                    setTextColor(Color.parseColor("#666666"))
                    maxLines = 1
                }
                
                // 关闭按钮 - 右上角
                val closeSize = (44 * activity.resources.displayMetrics.density).toInt()
                val closeButton = Button(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(closeSize, closeSize).apply {
                        gravity = Gravity.TOP or Gravity.END
                        topMargin = 8
                        rightMargin = 8
                    }
                    text = "✕"
                    textSize = 20f
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#CC000000"))
                    setOnClickListener {
                        dismiss()
                    }
                }
                
                // CTA按钮
                val ctaButton = Button(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        (cardWidth - 24).toInt(),
                        (44 * activity.resources.displayMetrics.density).toInt()
                    ).apply {
                        gravity = Gravity.BOTTOM
                        bottomMargin = 12
                        leftMargin = 12
                        rightMargin = 12
                    }
                    text = "了解详情"
                    textSize = 14f
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#FF6B5B"))
                    setOnClickListener {
                        listener.onClicked()
                        handleClick(bid)
                        dismiss()
                    }
                }
                
                // 组装视图
                cardContainer.addView(imageView)
                cardContainer.addView(titleView)
                cardContainer.addView(descView)
                cardContainer.addView(closeButton)
                cardContainer.addView(ctaButton)
                
                rootLayout.addView(cardContainer)
                setContentView(rootLayout)
                
                // 加载图片
                val imageUrl = bid.iurl ?: bid.ext?.nativeExt?.mainimg
                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(activity)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
                    ZhixuanHelper.logI("插屏广告图片加载: $imageUrl")
                }
                
                setOnDismissListener {
                    listener.onClosed()
                }
                
                setCanceledOnTouchOutside(false)
            }
            
            dialog?.show()
            listener.onShown()
            ZhixuanHelper.logI("插屏广告展示成功")
            
        } catch (e: Exception) {
            ZhixuanHelper.logE("插屏广告显示失败", e)
        }
    }
    
    private fun handleClick(bid: Bid) {
        bid.ext?.ldp?.let { url ->
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                activity.startActivity(intent)
                ZhixuanHelper.logI("打开落地页: $url")
            } catch (e: Exception) {
                ZhixuanHelper.logE("打开落地页失败", e)
            }
        }
    }
    
    private fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }
}
