package com.maxrtb.zx.listener

interface ZXBaseListener {
    fun onAdLoaded()
    fun onAdShown()
    fun onAdClicked()
    fun onAdClosed()
    fun onAdFailed(msg: String)
}
