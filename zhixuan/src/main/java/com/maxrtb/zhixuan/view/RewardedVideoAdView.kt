package com.maxrtb.zhixuan.view

import android.app.Activity
import android.app.Dialog
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.maxrtb.zhixuan.model.Bid
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class RewardedVideoAdView(private val activity: Activity) {
    
    private var dialog: Dialog? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentBid: Bid? = null
    private var listener: RewardAdListener? = null
    private var isRewarded = false
    
    interface RewardAdListener {
        fun onAdLoaded()
        fun onAdShown()
        fun onAdClicked()
        fun onAdClosed()
        fun onRewarded()
        fun onAdFailed(msg: String)
    }
    
    fun loadAd(bid: Bid, listener: RewardAdListener) {
        this.currentBid = bid
        this.listener = listener
        
        val videoUrl = bid.ext?.video?.videourl
        if (videoUrl.isNullOrEmpty()) {
            listener.onAdFailed("无视频素材")
            return
        }
        
        try {
            createVideoDialog(bid, videoUrl)
            listener.onAdLoaded()
            ZhixuanHelper.logI("激励视频加载成功")
        } catch (e: Exception) {
            ZhixuanHelper.logE("激励视频加载失败", e)
            listener.onAdFailed(e.message ?: "加载失败")
        }
    }
    
    private fun createVideoDialog(bid: Bid, videoUrl: String) {
        dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            val contentView = FrameLayout(activity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(android.graphics.Color.BLACK)
            }
            
            // 视频播放器
            val surfaceView = SurfaceView(activity).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            
            // 倒计时和跳过按钮
            val skipButton = Button(activity).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = android.view.Gravity.TOP or android.view.Gravity.END
                    topMargin = 50
                    rightMargin = 50
                }
                text = "观看5秒后可跳过"
                isEnabled = false
            }
            
            contentView.addView(surfaceView)
            contentView.addView(skipButton)
            setContentView(contentView)
            
            // 初始化播放器
            mediaPlayer = MediaPlayer().apply {
                setDataSource(videoUrl)
                
                surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        setDisplay(holder)
                        prepareAsync()
                    }
                    
                    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                    override fun surfaceDestroyed(holder: SurfaceHolder) {}
                })
                
                setOnPreparedListener {
                    start()
                    startCountdown(skipButton)
                }
                
                setOnCompletionListener {
                    isRewarded = true
                    listener?.onRewarded()
                    dismiss()
                }
                
                setOnErrorListener { _, what, extra ->
                    ZhixuanHelper.logE("视频播放错误: $what, $extra")
                    listener?.onAdFailed("视频播放失败")
                    true
                }
            }
            
            // 点击落地页
            surfaceView.setOnClickListener {
                listener?.onAdClicked()
                handleClick(bid)
            }
        }
    }
    
    private fun startCountdown(button: Button) {
        object : android.os.CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                button.text = "观看${millisUntilFinished / 1000}秒后可跳过"
            }
            
            override fun onFinish() {
                button.text = "跳过广告"
                button.isEnabled = true
                button.setOnClickListener {
                    if (mediaPlayer?.currentPosition ?: 0 > 5000) {
                        isRewarded = true
                        listener?.onRewarded()
                    }
                    dismiss()
                }
            }
        }.start()
    }
    
    fun show() {
        dialog?.show()
        listener?.onAdShown()
        ZhixuanHelper.logI("激励视频展示")
    }
    
    private fun handleClick(bid: Bid) {
        bid.ext?.ldp?.let { url ->
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                activity.startActivity(intent)
            } catch (e: Exception) {
                ZhixuanHelper.logE("打开落地页失败", e)
            }
        }
    }
    
    private fun dismiss() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        dialog?.dismiss()
        listener?.onAdClosed()
    }
    
    fun destroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        dialog?.dismiss()
        dialog = null
        currentBid = null
        listener = null
    }
}
