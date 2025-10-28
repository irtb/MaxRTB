package com.maxrtb.zhixuan.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class InterstitialAdView(private val activity: Activity) {
    
    private var dialog: Dialog? = null
    private var currentBid: Bid? = null
    private var listener: AdListener? = null
    
    interface AdListener {
        fun onAdShown()
        fun onAdClicked()
        fun onAdClosed()
        fun onAdFailed(msg: String)
    }
    
    fun loadAd(bid: Bid, listener: AdListener) {
        this.currentBid = bid
        this.listener = listener
        
        try {
            createDialog(bid)
            ZhixuanHelper.logI("插屏广告加载成功")
        } catch (e: Exception) {
            ZhixuanHelper.logE("插屏广告加载失败", e)
            listener.onAdFailed(e.message ?: "加载失败")
        }
    }
    
    private fun createDialog(bid: Bid) {
        dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            
            val contentView = FrameLayout(activity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.parseColor("#80000000"))
            }
            
            // 广告图片
            val imageView = ImageView(activity).apply {
                val width = (activity.resources.displayMetrics.widthPixels * 0.8).toInt()
                val height = (activity.resources.displayMetrics.heightPixels * 0.7).toInt()
                layoutParams = FrameLayout.LayoutParams(width, height).apply {
                    gravity = Gravity.CENTER
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
                setBackgroundColor(Color.WHITE)
                setPadding(20, 20, 20, 20)
            }
            
            // 关闭按钮
            val closeButton = ImageButton(activity).apply {
                val size = (50 * resources.displayMetrics.density).toInt()
                layoutParams = FrameLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.TOP or Gravity.END
                    topMargin = 100
                    rightMargin = 50
                }
                setBackgroundResource(android.R.drawable.ic_menu_close_clear_cancel)
                setOnClickListener {
                    dismiss()
                    listener?.onAdClosed()
                }
            }
            
            contentView.addView(imageView)
            contentView.addView(closeButton)
            
            setContentView(contentView)
            
            // 加载图片
            val imageUrl = bid.iurl ?: bid.ext?.nativeExt?.mainimg
            Glide.with(activity)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
            
            // 点击事件
            imageView.setOnClickListener {
                listener?.onAdClicked()
                handleClick(bid)
            }
        }
    }
    
    fun show() {
        dialog?.show()
        listener?.onAdShown()
        ZhixuanHelper.logI("插屏广告展示")
    }
    
    private fun handleClick(bid: Bid) {
        bid.ext?.ldp?.let { url ->
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                activity.startActivity(intent)
            } catch (e: Exception) {
                ZhixuanHelper.logE("打开落地页失败", e)
            }
        }
    }
    
    private fun dismiss() {
        dialog?.dismiss()
    }
    
    fun destroy() {
        dialog?.dismiss()
        dialog = null
        currentBid = null
        listener = null
    }
}
