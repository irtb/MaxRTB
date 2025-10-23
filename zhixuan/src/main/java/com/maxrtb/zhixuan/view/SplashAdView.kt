package com.maxrtb.zhixuan.view

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.maxrtb.zhixuan.api.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class SplashAdView(context: Context) : FrameLayout(context) {

    private var imageView: ImageView
    private var skipButton: TextView
    private var listener: AdListener? = null
    private var countDownTimer: android.os.CountDownTimer? = null

    interface AdListener {
        fun onAdShown()
        fun onAdClicked()
        fun onAdDismissed()
        fun onAdSkip()
        fun onAdFailed(msg: String)
    }

    init {
        setBackgroundColor(Color.WHITE)

        imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        addView(imageView)

        skipButton = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.TOP or Gravity.END
                topMargin = 50
                rightMargin = 50
            }
            setBackgroundColor(Color.parseColor("#80000000"))
            setTextColor(Color.WHITE)
            setPadding(30, 15, 30, 15)
            textSize = 14f
            text = "跳过 5s"
        }
        addView(skipButton)
    }

    fun loadAd(bid: Bid, listener: AdListener) {
        this.listener = listener

        val imageUrl = bid.iurl ?: bid.ext?.nativeExt?.mainimg
        if (imageUrl.isNullOrEmpty()) {
            ZhixuanHelper.logE("广告素材URL为空")
            listener.onAdFailed("无广告素材")
            return
        }

        try {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.color.white)
                .error(android.R.color.darker_gray)

            Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView)

            listener.onAdShown()

            // 倒计时
            startCountDown()

            // 点击事件
            imageView.setOnClickListener {
                listener.onAdClicked()
                // 打开落地页
                bid.ext?.ldp?.let { openUrl(it) }
            }

            skipButton.setOnClickListener {
                listener.onAdSkip()
                listener.onAdDismissed()
            }

        } catch (e: Exception) {
            ZhixuanHelper.logE("加载广告失败", e)
            listener.onAdFailed(e.message ?: "加载失败")
        }
    }

    private fun startCountDown() {
        countDownTimer = object : android.os.CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                skipButton.text = "跳过 ${seconds}s"
            }

            override fun onFinish() {
                skipButton.text = "跳过"
                listener?.onAdDismissed()
            }
        }.start()
    }

    private fun openUrl(url: String) {
        try {
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                android.net.Uri.parse(url)
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            ZhixuanHelper.logE("打开落地页失败", e)
        }
    }

    fun destroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        listener = null
    }
}
