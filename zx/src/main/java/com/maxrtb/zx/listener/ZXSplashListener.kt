package com.maxrtb.zx.listener

interface ZXSplashListener : ZXBaseListener {
    fun onAdImpression() {}
    fun onSkipClicked() {}
    fun onCountdown(remainTime: Long) {}
}
