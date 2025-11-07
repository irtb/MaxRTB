package com.maxrtb.zxdm

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.listener.SplashListener
import com.maxrtb.zx.ZX

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val container = findViewById<FrameLayout>(R.id.splash_container)
        
        val provider = ZX.getProvider()
        provider.loadAndShowSplashAd(
            this,
            "zx",
            "splash_test",
            container,
            object : SplashListener {
                override fun onAdStartRequest(providerType: String) {}
                override fun onAdLoaded(providerType: String) {}
                override fun onAdFailed(providerType: String, failedMsg: String?) {}
                override fun onAdClicked(providerType: String) {}
                override fun onAdExposure(providerType: String) {}
                override fun onAdDismissed(providerType: String) {
                    finish()
                }
            }
        )
    }
}
