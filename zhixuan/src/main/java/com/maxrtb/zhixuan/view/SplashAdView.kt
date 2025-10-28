package com.maxrtb.zhixuan.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class SplashAdView(context: Context) : FrameLayout(context) {
    
    private var imageView: ImageView
    private var skipButton: TextView
    private var countDownTimer: CountDownTimer? = null
    private var listener: AdListener? = null
    private var currentBid: Bid? = null
    
    interface AdListener {
        fun onAdShown()
        fun onAdClicked()
        fun onAdDismissed()
        fun onAdSkip()
        fun onAdFailed(msg: String)
    }
    
    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        
        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // 修改为 CENTER_CROP 避免压缩变形
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        addView(imageView)
        
        skipButton = TextView(context).apply {
            val dp50 = (50 * resources.displayMetrics.density).toInt()
            val dp20 = (20 * resources.displayMetrics.density).toInt()
            layoutParams = LayoutParams(dp50, dp50).apply {
                gravity = Gravity.TOP or Gravity.END
                topMargin = dp20
                rightMargin = dp20
            }
            text = "跳过 5"
            textSize = 14f
            setTextColor(0xFFFFFFFF.toInt())
            setBackgroundColor(0x80000000.toInt())
            gravity = Gravity.CENTER
        }
        addView(skipButton)
    }
    
    fun loadAd(bid: Bid, listener: AdListener) {
        this.listener = listener
        this.currentBid = bid
        
        val imageUrl = bid.iurl ?: bid.ext?.nativeExt?.mainimg
        if (imageUrl.isNullOrEmpty()) {
            ZhixuanHelper.logE("开屏广告素材URL为空")
            listener.onAdFailed("无广告素材")
            return
        }
        
        loadImage(imageUrl)
        setupSkipButton()
        setupClickListener()
        
        listener.onAdShown()
    }
    
    private fun loadImage(imageUrl: String) {
        try {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.color.transparent)
                .error(android.R.color.darker_gray)
                .override(
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels
                )
            
            Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView)
                
            ZhixuanHelper.logI("开屏广告加载成功: $imageUrl")
        } catch (e: Exception) {
            ZhixuanHelper.logE("加载开屏广告失败", e)
            listener?.onAdFailed(e.message ?: "加载失败")
        }
    }
    
    private fun setupSkipButton() {
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                skipButton.text = "跳过 $seconds"
            }
            
            override fun onFinish() {
                skipButton.text = "跳过"
                dismiss()
            }
        }.start()
        
        skipButton.setOnClickListener {
            countDownTimer?.cancel()
            listener?.onAdSkip()
            dismiss()
        }
    }
    
    private fun setupClickListener() {
        imageView.setOnClickListener {
            handleClick()
        }
    }
    
    private fun handleClick() {
        listener?.onAdClicked()
        
        currentBid?.ext?.ldp?.let { landingPage ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(landingPage))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                ZhixuanHelper.logI("打开落地页: $landingPage")
            } catch (e: Exception) {
                ZhixuanHelper.logE("打开落地页失败", e)
            }
        }
    }
    
    private fun dismiss() {
        listener?.onAdDismissed()
        visibility = View.GONE
        
        if (context is Activity) {
            (context as Activity).finish()
        }
    }
    
    fun destroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        imageView.setImageDrawable(null)
        listener = null
        currentBid = null
    }
}
