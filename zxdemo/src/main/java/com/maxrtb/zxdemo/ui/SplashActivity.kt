package com.maxrtb.zxdemo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.SplashListener
import com.maxrtb.zxdemo.R
import com.maxrtb.zhixuan.provider.ZhixuanProvider

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private val provider = ZhixuanProvider()
    private val handler = Handler(Looper.getMainLooper())
    private var isAdLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val container = findViewById<FrameLayout>(R.id.splash_container)
        loadSplashAd(container)
    }

    private fun loadSplashAd(container: FrameLayout) {
        Log.d(TAG, "🎨 开始加载开屏广告...")

        provider.loadAndShowSplashAd(
            activity = this,
            adProviderType = "zhixuan",
            alias = "splash_test",
            container = container,
            listener = object : SplashListener {
                override fun onAdStartRequest(providerType: String) {
                    Log.d(TAG, "📡 向 $providerType 请求广告...")
                }

                override fun onAdLoaded(providerType: String) {
                    Log.i(TAG, "✅ 广告加载成功")
                    isAdLoaded = true
                }

                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    Log.e(TAG, "❌ 广告加载失败: $failedMsg")
                    goToMain()
                }

                override fun onAdClicked(providerType: String) {
                    Log.i(TAG, "👆 广告被点击")
                }

                override fun onAdExposure(providerType: String) {
                    Log.i(TAG, "👁️ 广告曝光")
                }

                override fun onAdDismissed(providerType: String) {
                    Log.i(TAG, "❌ 广告关闭")
                    goToMain()
                }
            }
        )

        // 30秒超时
        handler.postDelayed({
            if (!isAdLoaded && !isFinishing) {
                Log.w(TAG, "⏱️ 广告加载超时")
                goToMain()
            }
        }, 30000)
    }

    private fun goToMain() {
        try {
            if (!isFinishing) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            Log.e(TAG, "跳转失败", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        // 禁用返回键
    }
}
