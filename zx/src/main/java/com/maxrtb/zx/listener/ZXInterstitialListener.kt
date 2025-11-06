package com.maxrtb.zx.listener

interface ZXInterstitialListener : ZXBaseListener {
    fun onAdImpression() {}
    fun onAdDismissed() {}
}
