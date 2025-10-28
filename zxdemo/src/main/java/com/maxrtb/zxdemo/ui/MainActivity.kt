package com.maxrtb.zxdemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maxrtb.zxdemo.R

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButtons()
    }

    private fun setupButtons() {
        // 开屏广告
        findViewById<Button>(R.id.btn_splash).setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
        }

        // Banner 广告
        findViewById<Button>(R.id.btn_banner).setOnClickListener {
            startActivity(Intent(this, BannerTestActivity::class.java))
        }

        // 插屏广告
        findViewById<Button>(R.id.btn_inter).setOnClickListener {
            startActivity(Intent(this, InterstitialTestActivity::class.java))
        }

        // 激励视频
        findViewById<Button>(R.id.btn_reward).setOnClickListener {
            startActivity(Intent(this, RewardVideoTestActivity::class.java))
        }

        // 原生广告
        findViewById<Button>(R.id.btn_native).setOnClickListener {
            startActivity(Intent(this, NativeAdTestActivity::class.java))
        }
    }
}
