package com.maxrtb.zx.view

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.maxrtb.zx.base.BaseAdView
import com.maxrtb.zx.listener.ZXSplashListener
import com.maxrtb.zx.model.AdData

class SplashAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseAdView(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var skipButton: Button? = null
    private var countdownTimer: CountDownTimer? = null
    private var remainTime = 5000L

    override fun onViewInit() {
        super.onViewInit()
        setBackgroundColor(android.graphics.Color.WHITE)
        titleView = TextView(context).apply { text = "启屏广告" }
        addView(titleView)
        skipButton = Button(context).apply {
            text = "跳过"
            setOnClickListener {
                (_listener as? ZXSplashListener)?.onSkipClicked()
                onAdClosed()
            }
        }
        addView(skipButton)
    }

    override fun onAdDataReady(data: AdData) {
        super.onAdDataReady(data)
        titleView?.text = data.title
    }

    override fun showAd() {
        super.showAd()
        startCountdown()
    }

    private fun startCountdown() {
        countdownTimer = object : CountDownTimer(remainTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainTime = millisUntilFinished
                (_listener as? ZXSplashListener)?.onCountdown(millisUntilFinished)
                skipButton?.text = "跳过(${millisUntilFinished / 1000})"
            }
            override fun onFinish() {
                skipButton?.text = "进入"
                onAdClosed()
            }
        }.start()
    }

    override fun onDestroy() {
        countdownTimer?.cancel()
        super.onDestroy()
    }
}
