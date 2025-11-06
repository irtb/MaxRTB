package com.maxrtb.zx.provider

import android.app.Activity
import com.maxrtb.zx.utils.ZXHelper
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData

abstract class BaseZXProvider : IZXProvider {
    protected var listener: ZXBaseListener? = null
    protected var _adData: AdData? = null
    protected var isInitialized = false
    protected val TAG = "ZXProvider"
    
    override suspend fun init() {
        isInitialized = true
        ZXHelper.logD(TAG, "Provider initialized")
    }
    
    override fun isReady(): Boolean = isInitialized && _adData != null
    override fun getAdData(): AdData? = _adData
    
    protected open suspend fun onAdLoadFailed(msg: String) {
        listener?.onAdFailed(msg)
        ZXHelper.logE(TAG, "Ad load failed: $msg")
    }
    
    protected open fun onAdLoaded() {
        listener?.onAdLoaded()
        ZXHelper.logD(TAG, "Ad loaded successfully")
    }
    
    protected open fun onAdShown() {
        listener?.onAdShown()
        ZXHelper.logD(TAG, "Ad shown")
    }
    
    protected open fun onAdClicked() {
        listener?.onAdClicked()
        ZXHelper.logD(TAG, "Ad clicked")
    }
    
    protected open fun onAdClosed() {
        listener?.onAdClosed()
        ZXHelper.logD(TAG, "Ad closed")
    }
}
