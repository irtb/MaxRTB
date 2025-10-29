package com.maxrtb.zhixuan.view

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.maxrtb.zhixuan.helper.ZhixuanHelper

class RewardedVideoDialog(
    private val activity: Activity,
    private val videoUrl: String,
    private val listener: RewardVideoListener
) {
    
    private var dialog: Dialog? = null
    private var mediaPlayer: MediaPlayer? = null
    private var countDownTimer: CountDownTimer? = null
    private var canSkip = false
    private var hasRewarded = false
    
    interface RewardVideoListener {
        fun onShown()
        fun onCompleted()
        fun onSkipped()
        fun onClosed()
    }
    
    fun show() {
        try {
            ZhixuanHelper.logI("创建激励视频Dialog，视频URL: $videoUrl")
            
            dialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
                setCancelable(false)
                
                val rootLayout = FrameLayout(activity).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.BLACK)
                }
                
                // ========== SurfaceView 用于视频播放 ==========
                val surfaceView = SurfaceView(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                
                // 控制栏
                val controlArea = FrameLayout(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (60 * activity.resources.displayMetrics.density).toInt()
                    ).apply {
                        gravity = Gravity.BOTTOM
                    }
                    setBackgroundColor(Color.parseColor("#CC000000"))
                }
                
                // 倒计时文本
                val countdownView = TextView(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                        leftMargin = (16 * activity.resources.displayMetrics.density).toInt()
                    }
                    text = "观看视频后可跳过"
                    textSize = 14f
                    setTextColor(Color.WHITE)
                }
                
                // 跳过按钮
                val skipButton = Button(activity).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        (80 * activity.resources.displayMetrics.density).toInt(),
                        (44 * activity.resources.displayMetrics.density).toInt()
                    ).apply {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
                        rightMargin = (16 * activity.resources.displayMetrics.density).toInt()
                    }
                    text = "跳过"
                    isEnabled = false
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#FF6B5B"))
                    setOnClickListener {
                        ZhixuanHelper.logI("用户点击跳过按钮")
                        listener.onSkipped()
                        dismiss()
                    }
                }
                
                controlArea.addView(countdownView)
                controlArea.addView(skipButton)
                
                rootLayout.addView(surfaceView)
                rootLayout.addView(controlArea)
                
                setContentView(rootLayout)
                
                // ========== 初始化 MediaPlayer ==========
                mediaPlayer = MediaPlayer().apply {
                    try {
                        setDataSource(videoUrl)
                        ZhixuanHelper.logI("设置数据源成功: $videoUrl")
                    } catch (e: Exception) {
                        ZhixuanHelper.logE("设置数据源失败", e)
                        listener.onClosed()
                        dismiss()
                        return@apply
                    }
                    
                    // 设置 SurfaceView 回调
                    surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            try {
                                ZhixuanHelper.logI("Surface创建，开始准备播放器")
                                setDisplay(holder)
                                prepareAsync()
                            } catch (e: Exception) {
                                ZhixuanHelper.logE("Surface回调异常", e)
                            }
                        }
                        
                        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                        override fun surfaceDestroyed(holder: SurfaceHolder) {}
                    })
                    
                    // 准备完成回调
                    setOnPreparedListener { mp ->
                        try {
                            ZhixuanHelper.logI("MediaPlayer准备完成，视频时长: ${mp.duration}ms")
                            mp.start()
                            ZhixuanHelper.logI("开始播放视频")
                            startCountdown(skipButton, countdownView)
                        } catch (e: Exception) {
                            ZhixuanHelper.logE("播放失败", e)
                        }
                    }
                    
                    // 播放完成回调
                    setOnCompletionListener {
                        try {
                            ZhixuanHelper.logI("视频播放完成")
                            if (!hasRewarded) {
                                hasRewarded = true
                                listener.onCompleted()
                                skipButton.text = "关闭"
                                skipButton.isEnabled = true
                            }
                        } catch (e: Exception) {
                            ZhixuanHelper.logE("播放完成处理异常", e)
                        }
                    }
                    
                    // 播放错误回调
                    setOnErrorListener { mp, what, extra ->
                        ZhixuanHelper.logE("视频播放错误: what=$what, extra=$extra")
                        try { dismiss() } catch (_: Throwable) {}
                        listener.onClosed()
                        true
                    }
                }
                
                setOnDismissListener {
                    ZhixuanHelper.logI("激励视频Dialog已关闭")
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    listener.onClosed()
                }
            }
            
            dialog?.show()
            listener.onShown()
            ZhixuanHelper.logI("激励视频Dialog已显示")
            
        } catch (e: Exception) {
            ZhixuanHelper.logE("创建激励视频失败", e)
            listener.onClosed()
        }
    }
    
    private fun startCountdown(skipButton: Button, countdownView: TextView) {
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                countdownView.text = "观看视频后可跳过 (${seconds}秒)"
                ZhixuanHelper.logI("倒计时: ${seconds}秒")
            }
            
            override fun onFinish() {
                canSkip = true
                countdownView.text = "可跳过"
                skipButton.isEnabled = true
                skipButton.text = "跳过"
                ZhixuanHelper.logI("倒计时结束，用户现在可以跳过")
            }
        }.start()
    }
    
    fun dismiss() {
        countDownTimer?.cancel()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        dialog?.dismiss()
        dialog = null
        ZhixuanHelper.logI("激励视频已关闭")
    }
}
