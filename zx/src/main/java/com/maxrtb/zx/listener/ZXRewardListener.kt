package com.maxrtb.zx.listener

interface ZXRewardListener : ZXBaseListener {
    fun onVideoStart() {}
    fun onVideoProgress(current: Int, duration: Int) {}
    fun onVideoComplete()
    fun onRewarded()
    fun onVideoSkipped() {}
}
