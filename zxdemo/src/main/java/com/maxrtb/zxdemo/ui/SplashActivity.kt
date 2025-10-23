package com.maxrtb.zxdemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.SplashListener
import com.maxrtb.zhixuan.provider.ZhixuanProvider
import com.maxrtb.zxdemo.R

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private lateinit var adContainer: FrameLayout
    private val provider = ZhixuanProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        adContainer = findViewById(R.id.splash_container)

        // 请求开屏广告
        loadSplashAd()
    }

    private fun loadSplashAd() {
        Log.d(TAG, "开始加载开屏广告")

        provider.loadAndShowSplashAd(
            activity = this,
            adProviderType = "zhixuan",
            alias = "splash_test", // 广告位ID/slotId
            container = adContainer,
            listener = object : SplashListener {
                override fun onAdStartRequest(providerType: String) {
                    Log.d(TAG, "[$providerType] 开始请求")
                }

                override fun onAdLoaded(providerType: String) {
                    Log.d(TAG, "[$providerType] 加载成功")
                }

                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    Log.e(TAG, "[$providerType] 加载失败: $failedMsg")
                    // 失败后跳转主页
                    goToMain()
                }

                override fun onAdClicked(providerType: String) {
                    Log.d(TAG, "[$providerType] 广告点击")
                }

                override fun onAdExposure(providerType: String) {
                    Log.d(TAG, "[$providerType] 广告曝光")
                }

                override fun onAdDismissed(providerType: String) {
                    Log.d(TAG, "[$providerType] 广告关闭")
                    // 广告关闭后跳转主页
                    goToMain()
                }
            }
        )
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        provider.destroyBannerAd()
    }
}
