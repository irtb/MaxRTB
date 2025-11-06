package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maxrtb.zx.ZX
import com.maxrtb.zx.config.ZXConfig
import com.maxrtb.zx.listener.ZXBannerListener
import com.maxrtb.zx.listener.ZXRewardListener
import com.maxrtb.zx.listener.ZXInterstitialListener
import com.maxrtb.zx.listener.ZXNativeListener
import com.maxrtb.zx.listener.ZXSplashListener
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView
    private lateinit var btnBanner: Button
    private lateinit var btnReward: Button
    private lateinit var btnInterstitial: Button
    private lateinit var btnNative: Button
    private lateinit var btnSplash: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化视图
        tvLog = findViewById(R.id.tv_log)
        btnBanner = findViewById(R.id.btn_banner)
        btnReward = findViewById(R.id.btn_reward)
        btnInterstitial = findViewById(R.id.btn_interstitial)
        btnNative = findViewById(R.id.btn_native)
        btnSplash = findViewById(R.id.btn_splash)

        // 初始化 ZX SDK
        val config = ZXConfig(
            appId = "demo_app_001",
            appName = "ZXDemo",
            debugMode = true,
            enableDebugLog = true,
        )
        ZX.init(this, config)
        addLog("✅ ZX SDK 初始化成功")

        // 设置按钮点击事件
        btnBanner.setOnClickListener {
            addLog("📌 请求 Banner 广告...")
            requestBannerAd()
        }

        btnReward.setOnClickListener {
            addLog("📌 请求激励视频...")
            requestRewardAd()
        }

        btnInterstitial.setOnClickListener {
            addLog("📌 请求插屏广告...")
            requestInterstitialAd()
        }

        btnNative.setOnClickListener {
            addLog("📌 请求原生广告...")
            requestNativeAd()
        }

        btnSplash.setOnClickListener {
            addLog("📌 请求启屏广告...")
            requestSplashAd()
        }
    }

    /**
     * 请求 Banner 广告
     */
    private fun requestBannerAd() {
        lifecycleScope.launch {
            try {
                ZX.requestBannerAd(
                    activity = this@MainActivity,
                    slotId = "banner_slot_001",
                    listener = object : ZXBannerListener {
                        override fun onAdLoaded() {
                            addLog("✅ Banner 广告加载成功")
                        }

                        override fun onAdShown() {
                            addLog("📱 Banner 广告已展示")
                        }

                        override fun onAdClicked() {
                            addLog("👆 Banner 广告被点击")
                        }

                        override fun onAdClosed() {
                            addLog("❌ Banner 广告已关闭")
                        }

                        override fun onAdFailed(msg: String) {
                            addLog("⚠️ Banner 广告加载失败: $msg")
                        }

                        override fun onAdImpression() {
                            addLog("👁️ Banner 广告曝光")
                        }
                    }
                )
            } catch (e: Exception) {
                addLog("❌ 异常: ${e.message}")
            }
        }
    }

    /**
     * 请求激励视频
     */
    private fun requestRewardAd() {
        lifecycleScope.launch {
            try {
                ZX.requestRewardAd(
                    activity = this@MainActivity,
                    slotId = "reward_slot_001",
                    listener = object : ZXRewardListener {
                        override fun onAdLoaded() {
                            addLog("✅ 激励视频加载成功")
                        }

                        override fun onAdShown() {
                            addLog("📱 激励视频已展示")
                        }

                        override fun onVideoStart() {
                            addLog("▶️ 视频开始播放")
                        }

                        override fun onVideoProgress(current: Int, duration: Int) {
                            addLog("⏱️ 播放进度: $current/$duration")
                        }

                        override fun onVideoComplete() {
                            addLog("✅ 视频播放完成")
                        }

                        override fun onRewarded() {
                            addLog("🎁 用户获得奖励")
                        }

                        override fun onAdClicked() {
                            addLog("👆 激励视频被点击")
                        }

                        override fun onAdClosed() {
                            addLog("❌ 激励视频已关闭")
                        }

                        override fun onAdFailed(msg: String) {
                            addLog("⚠️ 激励视频加载失败: $msg")
                        }
                    }
                )
            } catch (e: Exception) {
                addLog("❌ 异常: ${e.message}")
            }
        }
    }

    /**
     * 请求插屏广告
     */
    private fun requestInterstitialAd() {
        lifecycleScope.launch {
            try {
                ZX.requestInterstitialAd(
                    activity = this@MainActivity,
                    slotId = "interstitial_slot_001",
                    listener = object : ZXInterstitialListener {
                        override fun onAdLoaded() {
                            addLog("✅ 插屏广告加载成功")
                        }

                        override fun onAdShown() {
                            addLog("📱 插屏广告已展示")
                        }

                        override fun onAdImpression() {
                            addLog("👁️ 插屏广告曝光")
                        }

                        override fun onAdClicked() {
                            addLog("👆 插屏广告被点击")
                        }

                        override fun onAdDismissed() {
                            addLog("❌ 插屏广告已关闭")
                        }

                        override fun onAdClosed() {
                            addLog("❌ 插屏广告已关闭")
                        }

                        override fun onAdFailed(msg: String) {
                            addLog("⚠️ 插屏广告加载失败: $msg")
                        }
                    }
                )
            } catch (e: Exception) {
                addLog("❌ 异常: ${e.message}")
            }
        }
    }

    /**
     * 请求原生广告
     */
    private fun requestNativeAd() {
        lifecycleScope.launch {
            try {
                ZX.requestNativeAd(
                    activity = this@MainActivity,
                    slotId = "native_slot_001",
                    listener = object : ZXNativeListener {
                        override fun onAdLoaded() {
                            addLog("✅ 原生广告加载成功")
                        }

                        override fun onAdShown() {
                            addLog("📱 原生广告已展示")
                        }

                        override fun onAdImpression() {
                            addLog("👁️ 原生广告曝光")
                        }

                        override fun onCtaClicked() {
                            addLog("👆 原生广告CTA被点击")
                        }

                        override fun onAdClicked() {
                            addLog("👆 原生广告被点击")
                        }

                        override fun onAdClosed() {
                            addLog("❌ 原生广告已关闭")
                        }

                        override fun onAdFailed(msg: String) {
                            addLog("⚠️ 原生广告加载失败: $msg")
                        }
                    }
                )
            } catch (e: Exception) {
                addLog("❌ 异常: ${e.message}")
            }
        }
    }

    /**
     * 请求启屏广告
     */
    private fun requestSplashAd() {
        lifecycleScope.launch {
            try {
                ZX.requestSplashAd(
                    activity = this@MainActivity,
                    slotId = "splash_slot_001",
                    listener = object : ZXSplashListener {
                        override fun onAdLoaded() {
                            addLog("✅ 启屏广告加载成功")
                        }

                        override fun onAdShown() {
                            addLog("📱 启屏广告已展示")
                        }

                        override fun onAdImpression() {
                            addLog("👁️ 启屏广告曝光")
                        }

                        override fun onSkipClicked() {
                            addLog("⏭️ 用户点击跳过")
                        }

                        override fun onCountdown(remainTime: Long) {
                            addLog("⏰ 倒计时: ${remainTime / 1000}s")
                        }

                        override fun onAdClicked() {
                            addLog("👆 启屏广告被点击")
                        }

                        override fun onAdClosed() {
                            addLog("❌ 启屏广告已关闭")
                        }

                        override fun onAdFailed(msg: String) {
                            addLog("⚠️ 启屏广告加载失败: $msg")
                        }
                    }
                )
            } catch (e: Exception) {
                addLog("❌ 异常: ${e.message}")
            }
        }
    }

    /**
     * 添加日志
     */
    private fun addLog(message: String) {
        runOnUiThread {
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date())
            val currentLog = tvLog.text.toString()
            tvLog.text = "[$timestamp] $message\n$currentLog"
        }
    }
}
