package com.maxrtb.zx.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.maxrtb.zx.listener.ZXRewardListener
import com.maxrtb.zx.model.AdData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RewardedVideoDialog(
    context: Context,
    private val adData: AdData,
    private val listener: ZXRewardListener,
) : Dialog(context) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private var videoDuration = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleView = TextView(context).apply { text = adData.title }
        val progressBar = ProgressBar(context).apply { max = videoDuration }
        val timeView = TextView(context).apply { text = "0/$videoDuration" }
        val closeButton = Button(context).apply {
            text = "关闭"
            isEnabled = false
            setOnClickListener {
                listener.onAdClosed()
                dismiss()
            }
        }
        val container = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(titleView)
            addView(progressBar)
            addView(timeView)
            addView(closeButton)
        }
        setContentView(container)
        scope.launch {
            listener.onVideoStart()
            for (i in 0..videoDuration) {
                progressBar.progress = i
                timeView.text = "$i/$videoDuration"
                listener.onVideoProgress(i, videoDuration)
                delay(1000)
            }
            listener.onVideoComplete()
            listener.onRewarded()
            closeButton.isEnabled = true
            closeButton.text = "获得奖励"
        }
    }

    override fun dismiss() {
        job.cancel()
        super.dismiss()
    }
}
