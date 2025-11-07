package com.maxrtb.zxdm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zx.ZX
import com.maxrtb.zx.config.ZXConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化ZX SDK
        val config = ZXConfig(
            appId = "zxdm_app_001",
            appName = "ZXDMDemo",
            debugMode = true,
            enableDebugLog = true
        )
        ZX.init(this, config)

        setupButtons()
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btn_splash).setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
        }
        
        findViewById<Button>(R.id.btn_banner).setOnClickListener {
            startActivity(Intent(this, BannerActivity::class.java))
        }
        
        findViewById<Button>(R.id.btn_inter).setOnClickListener {
            startActivity(Intent(this, InterstitialActivity::class.java))
        }
        
        findViewById<Button>(R.id.btn_reward).setOnClickListener {
            startActivity(Intent(this, RewardActivity::class.java))
        }
        
        findViewById<Button>(R.id.btn_native).setOnClickListener {
            startActivity(Intent(this, NativeActivity::class.java))
        }
    }
}
