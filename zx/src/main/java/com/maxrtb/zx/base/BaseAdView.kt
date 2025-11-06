package com.maxrtb.zx.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.maxrtb.zx.listener.ZXBaseListener
import com.maxrtb.zx.model.AdData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    protected var _listener: ZXBaseListener? = null
    protected var _adData: AdData? = null
    protected var isDestroyed = false
    protected val viewScope = CoroutineScope(Dispatchers.Main + Job())
    
    init { onViewInit() }
    
    protected open fun onViewInit() {}
    fun setListener(listener: ZXBaseListener) { this._listener = listener }
    fun setAdData(data: AdData) {
        if (!isDestroyed) {
            this._adData = data
            onAdDataReady(data)
        }
    }
    
    protected open fun onAdDataReady(data: AdData) {}
    open suspend fun loadAd(slotId: String) {}
    open fun showAd() {
        if (!isDestroyed) {
            visibility = View.VISIBLE
            onAdShown()
        }
    }
    
    open fun hideAd() {
        if (!isDestroyed) { visibility = View.GONE }
    }
    
    open fun destroyAd() {
        if (!isDestroyed) {
            isDestroyed = true
            onDestroy()
            viewScope.cancel()
        }
    }
    
    protected open fun onDestroy() {
        _listener = null
        _adData = null
        removeAllViews()
    }
    
    protected open fun onAdLoaded() { _listener?.onAdLoaded() }
    protected open fun onAdShown() { _listener?.onAdShown() }
    protected open fun onAdClicked() { _listener?.onAdClicked() }
    protected open fun onAdClosed() {
        _listener?.onAdClosed()
        hideAd()
    }
    protected open fun onAdFailed(msg: String) { _listener?.onAdFailed(msg) }
}
